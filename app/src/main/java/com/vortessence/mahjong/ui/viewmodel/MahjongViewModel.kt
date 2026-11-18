package com.vortessence.mahjong.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vortessence.mahjong.audio.AudioManager
import com.vortessence.mahjong.audio.GameSounds
import com.vortessence.mahjong.data.model.Constellation
import com.vortessence.mahjong.data.model.ContinueInfo
import com.vortessence.mahjong.data.model.GameSettings
import com.vortessence.mahjong.data.model.GameState
import com.vortessence.mahjong.data.model.GameStats
import com.vortessence.mahjong.data.model.SavedGameState
import com.vortessence.mahjong.data.repository.ConstellationRepository
import com.vortessence.mahjong.data.repository.GameRepository
import com.vortessence.mahjong.data.repository.GameResult
import com.vortessence.mahjong.data.repository.PreferencesRepository
import com.vortessence.mahjong.engine.MahjongGameEngine
import com.vortessence.mahjong.engine.TileTapResult
import com.vortessence.mahjong.engine.TutorialScript
import com.vortessence.mahjong.engine.TutorialStep
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

sealed class Screen {
    data object MainMenu : Screen()
    data object LevelSelect : Screen()
    data object ConstellationSelect : Screen()
    data object Game : Screen()
    data object Settings : Screen()
    data object Stats : Screen()
}

class MahjongViewModel(
    private val prefsRepo: GameRepository,
    private val sounds: GameSounds,
    private val isTabletProvider: () -> Boolean,
    // Board generation, solver-backed hints and reshuffles run here, off the main thread
    private val computeDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    val isTablet: Boolean
        get() = isTabletProvider()

    val currentLevelConstellations: List<Constellation>
        get() = ConstellationRepository.getLevelConstellations(isTablet)

    private val _currentScreen = MutableStateFlow<Screen>(Screen.MainMenu)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    // Kept apart from the game state so the board does not recompose with every tick
    private val _timerSeconds = MutableStateFlow(0L)
    val timerSeconds: StateFlow<Long> = _timerSeconds.asStateFlow()

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
            ContinueInfo(
                levelIndex = levelIdx,
                constellationName = state.constellation.name,
                remainingTiles = state.remainingTilesCount,
            )
        } else if (saved != null && saved.tiles.any { !it.isRemoved }) {
            ContinueInfo(
                levelIndex = saved.levelIndex,
                constellationName = ConstellationRepository.getById(saved.constellationId).name,
                remainingTiles = saved.tiles.count { !it.isRemoved },
            )
        } else {
            null
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private var timerJob: Job? = null
    private var winRevealJob: Job? = null
    private var blockedClearJob: Job? = null
    // Deals a new board
    private var dealJob: Job? = null
    // Computes a hint or a reshuffle
    private var actionJob: Job? = null
    // Looks for a dead end on mid-sized boards
    private var deadEndJob: Job? = null

    init {
        viewModelScope.launch {
            prefsRepo.settingsFlow.distinctUntilChanged().collect { s ->
                _settings.value = s
                sounds.updateSettings(s)
            }
        }
        viewModelScope.launch {
            prefsRepo.statsFlow.collect { st ->
                _stats.value = st
            }
        }
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
                cancelPendingWork()
                _currentLevelIndex.value = saved.levelIndex
                _gameState.value = restored
                _timerSeconds.value = saved.timerSeconds
                _tutorialStepIndex.value = TutorialScript.resumeStepFor(restored)
                _currentScreen.value = Screen.Game
                startTimer()
                checkForDeadEnd(restored)
            }
        }
    }

    fun startLevel(levelIndex: Int, tablet: Boolean = isTablet) {
        _currentLevelIndex.value = levelIndex
        val constellation = ConstellationRepository.getLevel(levelIndex, tablet)
        initGame(constellation, countAsNewGame = true)
        _currentScreen.value = Screen.Game
    }

    fun startConstellation(constellation: Constellation) {
        _currentLevelIndex.value = null
        initGame(constellation, countAsNewGame = true)
        _currentScreen.value = Screen.Game
    }

    private fun cancelPendingWork() {
        // A pending win reveal or computation from the previous game must not overwrite the new one
        winRevealJob?.cancel()
        dealJob?.cancel()
        actionJob?.cancel()
        deadEndJob?.cancel()
        blockedClearJob?.cancel()
    }

    /**
     * Deals a new board for [constellation]. [countAsNewGame] is false when the player restarts
     * a game they have not finished, so that restarting does not count as another game played.
     */
    private fun initGame(constellation: Constellation, countAsNewGame: Boolean) {
        cancelPendingWork()
        pauseTimer()
        _gameState.value = null
        _tutorialStepIndex.value = null
        _timerSeconds.value = 0L

        dealJob = viewModelScope.launch {
            val state = withContext(computeDispatcher) { MahjongGameEngine.startNewGame(constellation) }
            val isTutorial = TutorialScript.isTutorial(state)
            _gameState.value = state
            _tutorialStepIndex.value = if (isTutorial) 0 else null
            startTimer()
            saveCurrentGame()
            if (countAsNewGame && !isTutorial) {
                prefsRepo.recordGameStarted()
            }
        }
    }

    private fun saveCurrentGame() {
        val current = _gameState.value ?: return
        if (current.isWon || current.remainingTilesCount == 0) return
        val saved = SavedGameState.fromGameState(current, _currentLevelIndex.value, _timerSeconds.value)
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
                sounds.playTileBlocked()
                blockedClearJob?.cancel()
                blockedClearJob = viewModelScope.launch {
                    delay(BLOCKED_FLASH_DURATION)
                    if (_gameState.value?.blockedTileId == result.tileId) {
                        _gameState.value = _gameState.value?.copy(blockedTileId = null)
                    }
                }
            }
            is TileTapResult.Selected -> {
                sounds.playTileSelect()
            }
            is TileTapResult.Deselected -> {
                sounds.playTileDeselect()
            }
            is TileTapResult.SelectionChanged -> {
                sounds.playTileSelect()
            }
            is TileTapResult.Matched -> {
                sounds.playTileSelect()
                if (!_settings.value.animationsEnabled) {
                    sounds.playTileMatch()
                }
                if (result.isWon) {
                    onGameWon(newState)
                } else {
                    saveCurrentGame()
                    checkForDeadEnd(newState)
                }
            }
        }
    }

    private fun onGameWon(wonState: GameState) {
        pauseTimer()
        clearSavedGame()
        val constellationId = wonState.constellation.id
        val gameResult = GameResult(
            constellationId = constellationId,
            levelIndex = _currentLevelIndex.value,
            isTabletLevel = ConstellationRepository.isTabletLevel(constellationId),
            timeSeconds = _timerSeconds.value,
            isAssisted = wonState.isAssisted,
            isTutorial = TutorialScript.isTutorial(wonState),
        )
        viewModelScope.launch {
            prefsRepo.recordGameWon(gameResult)
        }
        // Keep isWon false briefly so the final pair has time to animate smashing
        _gameState.value = wonState.copy(isWon = false)
        winRevealJob = viewModelScope.launch {
            delay(WIN_REVEAL_DELAY)
            _gameState.value = wonState
            sounds.playWin()
        }
    }

    fun playTileMatch() {
        sounds.playTileMatch()
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
            actionJob?.cancel()
            val updated = MahjongGameEngine.undo(current)
            _gameState.value = updated
            saveCurrentGame()
            sounds.playTileSelect()
            checkForDeadEnd(updated)
        }
    }

    /**
     * Takes back as many moves as needed to reach a position that can still be won. Offered when
     * the player hits a dead end, where undoing a single move often doesn't help.
     */
    fun undoToWinnable() {
        if (_tutorialStepIndex.value != null) return
        runAction { current ->
            MahjongGameEngine.undoToWinnable(current).takeIf { it !== current }
        }
    }

    /**
     * The engine only searches small boards for dead ends after every move. Mid-sized boards are
     * searched here in the background, so a dead end shows up closer to the move that caused it.
     */
    private fun checkForDeadEnd(state: GameState) {
        deadEndJob?.cancel()
        if (state.isStalemate || state.remainingTilesCount > BACKGROUND_DEAD_END_MAX_TILES) return
        deadEndJob = viewModelScope.launch {
            val isDeadEnd = withContext(computeDispatcher) { MahjongGameEngine.isDeadEnd(state) }
            if (!isDeadEnd) return@launch
            // Selecting tiles meanwhile is fine, but if the board itself changed the result is
            // outdated; the check started for the new board covers it
            val current = _gameState.value ?: return@launch
            if (current.tiles !== state.tiles) return@launch
            _gameState.value = current.copy(isStalemate = true, selectedTileId = null, hintPair = null)
        }
    }

    fun requestHint() {
        if (_tutorialStepIndex.value != null) return
        runAction { current ->
            MahjongGameEngine.requestHint(current).takeIf { it.hintPair != null }
        }
    }

    fun reshuffle() {
        runAction { current -> MahjongGameEngine.reshuffleRemaining(current) }
    }

    /**
     * Computes a new state off the main thread. The result is dropped if the game changed in the
     * meantime (e.g. the player tapped a tile), and further requests are ignored while one runs.
     */
    private fun runAction(compute: (GameState) -> GameState?) {
        val current = _gameState.value ?: return
        if (current.isWon || actionJob?.isActive == true) return
        actionJob = viewModelScope.launch {
            val updated = withContext(computeDispatcher) { compute(current) } ?: return@launch
            if (_gameState.value !== current) return@launch
            _gameState.value = updated
            saveCurrentGame()
            sounds.playTileSelect()
        }
    }

    fun restartGame() {
        val current = _gameState.value ?: return
        // Playing again after finishing is a new game; restarting an unfinished one is not
        val isFinished = current.remainingTilesCount == 0
        initGame(current.constellation, countAsNewGame = isFinished)
    }

    fun playNextLevel() {
        val currentLevel = _currentLevelIndex.value ?: return
        val current = _gameState.value ?: return
        // Stay in the level set of the game just played, even if the window size changed meanwhile
        val tablet = if (currentLevel == ConstellationRepository.TUTORIAL_LEVEL) {
            isTablet
        } else {
            ConstellationRepository.isTabletLevel(current.constellation.id)
        }
        if (currentLevel < ConstellationRepository.getTotalLevels(tablet)) {
            startLevel(currentLevel + 1, tablet)
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
                if (isTimerRunning()) {
                    _timerSeconds.value += 1
                }
            }
        }
    }

    /**
     * The clock only runs while the player can actually play: not in menus, not while the
     * stalemate dialog is open, not after winning, and never in the tutorial.
     */
    private fun isTimerRunning(): Boolean {
        val state = _gameState.value ?: return false
        return _currentScreen.value == Screen.Game &&
            state.isPlaying &&
            !state.isWon &&
            !state.isStalemate &&
            state.remainingTilesCount > 0 &&
            !TutorialScript.isTutorial(state)
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun onResume() {
        sounds.onResume()
        if (_currentScreen.value == Screen.Game && _gameState.value?.isWon == false) {
            startTimer()
        }
    }

    fun onPause() {
        pauseTimer()
        saveCurrentGame()
        sounds.onPause()
    }

    override fun onCleared() {
        super.onCleared()
        sounds.release()
    }

    companion object {
        private val BLOCKED_FLASH_DURATION = 350.milliseconds

        // Bigger boards are too expensive to prove dead within the solver budget
        private const val BACKGROUND_DEAD_END_MAX_TILES = 48
        // The board's match animation (600 ms) plus a short pause before the win dialog
        private val WIN_REVEAL_DELAY = 780.milliseconds

        val Factory = viewModelFactory {
            initializer {
                val application: Application = checkNotNull(this[APPLICATION_KEY])
                MahjongViewModel(
                    prefsRepo = PreferencesRepository(application),
                    sounds = AudioManager(application),
                    isTabletProvider = {
                        application.resources.configuration.smallestScreenWidthDp >= 600
                    },
                )
            }
        }
    }
}
