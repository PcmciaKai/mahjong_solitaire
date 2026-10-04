package com.vortessence.mahjong.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vortessence.mahjong.audio.AudioManager
import com.vortessence.mahjong.data.model.Constellation
import com.vortessence.mahjong.data.model.ContinueInfo
import com.vortessence.mahjong.data.model.GameSettings
import com.vortessence.mahjong.data.model.GameState
import com.vortessence.mahjong.data.model.GameStats
import com.vortessence.mahjong.data.model.SavedGameState
import com.vortessence.mahjong.data.repository.ConstellationRepository
import com.vortessence.mahjong.data.repository.PreferencesRepository
import com.vortessence.mahjong.engine.MahjongGameEngine
import com.vortessence.mahjong.engine.TileTapResult
import com.vortessence.mahjong.engine.TutorialScript
import com.vortessence.mahjong.engine.TutorialStep
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

sealed class Screen {
    data object MainMenu : Screen()
    data object LevelSelect : Screen()
    data object ConstellationSelect : Screen()
    data object Game : Screen()
    data object Settings : Screen()
    data object Stats : Screen()
}

class MahjongViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsRepo = PreferencesRepository(application)
    val audioManager = AudioManager(application)

    val isTablet: Boolean
        get() = getApplication<Application>().resources.configuration.smallestScreenWidthDp >= 600

    val currentLevelConstellations: List<Constellation>
        get() = ConstellationRepository.getLevelConstellations(isTablet)

    private val _currentScreen = MutableStateFlow<Screen>(Screen.MainMenu)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    private val _currentLevelIndex = MutableStateFlow<Int?>(null)
    val currentLevelIndex: StateFlow<Int?> = _currentLevelIndex.asStateFlow()

    // Index into TutorialScript.steps while the guided tutorial runs, null otherwise
    private val _tutorialStepIndex = MutableStateFlow<Int?>(null)
    val tutorialStep: StateFlow<TutorialStep?> = _tutorialStepIndex
        .map { index -> index?.let { TutorialScript.steps.getOrNull(it) } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val currentTutorialStep: TutorialStep?
        get() = _tutorialStepIndex.value?.let { TutorialScript.steps.getOrNull(it) }

    private val _settings = MutableStateFlow(GameSettings())
    val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    private val _stats = MutableStateFlow(GameStats())
    val stats: StateFlow<GameStats> = _stats.asStateFlow()

    private val _savedGame = MutableStateFlow<SavedGameState?>(null)
    @Suppress("unused")
    val savedGame: StateFlow<SavedGameState?> = _savedGame.asStateFlow()

    val continueInfo: StateFlow<ContinueInfo?> = combine(
        _gameState,
        _savedGame,
        _currentLevelIndex,
    ) { state, saved, levelIdx ->
        if (state != null && !state.isWon && (state.remainingTilesCount > 0)) {
            val title = if (levelIdx != null) "Level $levelIdx: ${state.constellation.name}" else state.constellation.name
            val subtitle = "${state.remainingTilesCount} tiles remaining"
            ContinueInfo(title = title, subtitle = subtitle)
        } else if (saved != null && saved.tiles.any { !it.isRemoved }) {
            val constellation = ConstellationRepository.getById(saved.constellationId)
            val title = if (saved.levelIndex != null) "Level ${saved.levelIndex}: ${constellation.name}" else constellation.name
            val remaining = saved.tiles.count { !it.isRemoved }
            val subtitle = "$remaining tiles remaining"
            ContinueInfo(title = title, subtitle = subtitle)
        } else {
            null
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private var timerJob: Job? = null
    private var winRevealJob: Job? = null

    init {
        // Collect settings
        viewModelScope.launch {
            prefsRepo.settingsFlow.distinctUntilChanged().collect { s ->
                _settings.value = s
                audioManager.updateSettings(s)
            }
        }
        // Collect stats
        viewModelScope.launch {
            prefsRepo.statsFlow.collect { st ->
                _stats.value = st
            }
        }
        // Collect saved game
        viewModelScope.launch {
            prefsRepo.savedGameFlow.collect { saved ->
                _savedGame.value = saved
            }
        }
    }

    fun navigateTo(screen: Screen) {
        if (screen != Screen.Game) {
            pauseTimer()
            saveCurrentGame()
        }
        _currentScreen.value = screen
    }

    fun continueGame() {
        val current = _gameState.value
        if (current != null && !current.isWon && current.remainingTilesCount > 0) {
            _currentScreen.value = Screen.Game
            startTimer()
            return
        }
        val saved = _savedGame.value
        if (saved != null) {
            val restored = saved.toGameState()
            if (!restored.isWon && restored.remainingTilesCount > 0) {
                _currentLevelIndex.value = saved.levelIndex
                _gameState.value = restored
                _tutorialStepIndex.value = TutorialScript.resumeStepFor(restored)
                _currentScreen.value = Screen.Game
                startTimer()
            }
        }
    }

    fun startLevel(levelIndex: Int) {
        _currentLevelIndex.value = levelIndex
        val constellation = ConstellationRepository.getLevel(levelIndex, isTablet)
        initGame(constellation)
        _currentScreen.value = Screen.Game
    }

    fun startConstellation(constellation: Constellation) {
        _currentLevelIndex.value = null
        initGame(constellation)
        _currentScreen.value = Screen.Game
    }

    private fun initGame(constellation: Constellation) {
        // A pending win reveal from the previous game must not overwrite the new one
        winRevealJob?.cancel()
        winRevealJob = null
        val state = MahjongGameEngine.startNewGame(constellation)
        _gameState.value = state
        _tutorialStepIndex.value = if (TutorialScript.isTutorial(state)) 0 else null
        startTimer()
        saveCurrentGame()

        viewModelScope.launch {
            prefsRepo.recordGameStarted()
        }
    }

    private fun saveCurrentGame() {
        val current = _gameState.value ?: return
        if (current.isWon || current.remainingTilesCount == 0) return
        val saved = SavedGameState.fromGameState(current, _currentLevelIndex.value)
        _savedGame.value = saved
        viewModelScope.launch {
            prefsRepo.saveGame(saved)
        }
    }

    private fun clearSavedGame() {
        _savedGame.value = null
        viewModelScope.launch {
            prefsRepo.clearSavedGame()
        }
    }

    fun onTileTapped(tileId: Int) {
        val current = _gameState.value ?: return
        if (current.isWon) return

        val step = currentTutorialStep
        if (step != null && !TutorialScript.isTapAllowed(step, tileId)) return

        val (newState, result) = MahjongGameEngine.handleTileTap(current, tileId)
        _gameState.value = newState

        if (step != null && TutorialScript.isStepComplete(step, result, newState)) {
            advanceTutorial()
        }

        when (result) {
            is TileTapResult.Blocked -> {
                audioManager.playTileBlocked()
                viewModelScope.launch {
                    delay(350.milliseconds)
                    if (_gameState.value?.blockedTileId == result.tileId) {
                        _gameState.value = _gameState.value?.copy(blockedTileId = null)
                    }
                }
            }
            is TileTapResult.Selected -> {
                audioManager.playTileSelect()
            }
            is TileTapResult.Deselected -> {
                audioManager.playTileDeselect()
            }
            is TileTapResult.SelectionChanged -> {
                audioManager.playTileSelect()
            }
            is TileTapResult.Matched -> {
                audioManager.playTileSelect()
                if (!_settings.value.animationsEnabled) {
                    audioManager.playTileMatch()
                }
                if (result.isWon) {
                    pauseTimer()
                    clearSavedGame()
                    val levelIndex = _currentLevelIndex.value
                    viewModelScope.launch {
                        prefsRepo.recordGameWon(
                            constellationId = current.constellation.id,
                            levelIndex = levelIndex,
                            timeSeconds = newState.timerSeconds,
                        )
                    }
                    // Keep isWon false briefly so the final pair has time to animate smashing
                    _gameState.value = newState.copy(isWon = false)
                    winRevealJob = viewModelScope.launch {
                        delay(600.milliseconds)
                        _gameState.value = newState
                        audioManager.playWin()
                    }
                } else {
                    _gameState.value = newState
                    saveCurrentGame()
                }
            }
        }
    }

    fun playTileMatch() {
        audioManager.playTileMatch()
    }

    fun advanceTutorial() {
        val index = _tutorialStepIndex.value ?: return
        _tutorialStepIndex.value = (index + 1).takeIf { it < TutorialScript.steps.size }
    }

    fun undo() {
        // Undo and hints would get in the way of the guided tutorial steps
        if (_tutorialStepIndex.value != null) return
        val current = _gameState.value ?: return
        if (current.moveHistory.isNotEmpty()) {
            val updated = MahjongGameEngine.undo(current)
            _gameState.value = updated
            saveCurrentGame()
            audioManager.playTileSelect()
        }
    }

    fun requestHint() {
        if (_tutorialStepIndex.value != null) return
        val current = _gameState.value ?: return
        val updated = MahjongGameEngine.requestHint(current)
        _gameState.value = updated
        audioManager.playTileSelect()
    }

    fun restartGame() {
        val current = _gameState.value ?: return
        initGame(current.constellation)
    }

    fun reshuffle() {
        val current = _gameState.value ?: return
        val updated = MahjongGameEngine.reshuffleRemaining(current)
        _gameState.value = updated
        saveCurrentGame()
        audioManager.playTileSelect()
    }

    fun playNextLevel() {
        val currentLevel = _currentLevelIndex.value ?: return
        if (currentLevel < ConstellationRepository.getTotalLevels(isTablet)) {
            startLevel(currentLevel + 1)
        }
    }

    fun updateSettings(newSettings: GameSettings) {
        viewModelScope.launch {
            prefsRepo.updateSettings(newSettings)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000.milliseconds)
                val current = _gameState.value
                if (current != null && current.isPlaying && !current.isWon) {
                    _gameState.value = current.copy(timerSeconds = current.timerSeconds + 1)
                }
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun onResume() {
        audioManager.onResume()
        if (_currentScreen.value == Screen.Game && _gameState.value?.isWon == false) {
            startTimer()
        }
    }

    fun onPause() {
        pauseTimer()
        saveCurrentGame()
        audioManager.onPause()
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.release()
    }
}
