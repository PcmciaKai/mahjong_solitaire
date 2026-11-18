package com.vortessence.mahjong

import com.vortessence.mahjong.audio.GameSounds
import com.vortessence.mahjong.data.model.GameSettings
import com.vortessence.mahjong.data.model.GameStats
import com.vortessence.mahjong.data.model.Move
import com.vortessence.mahjong.data.model.SavedGameState
import com.vortessence.mahjong.data.model.SavedTile
import com.vortessence.mahjong.data.model.TileSuit
import com.vortessence.mahjong.data.repository.ConstellationRepository
import com.vortessence.mahjong.data.repository.GameRepository
import com.vortessence.mahjong.data.repository.GameResult
import com.vortessence.mahjong.engine.MahjongGameEngine
import com.vortessence.mahjong.ui.viewmodel.MahjongViewModel
import com.vortessence.mahjong.ui.viewmodel.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MahjongViewModelTest {

    private class FakeRepository : GameRepository {
        override val settingsFlow = MutableStateFlow(GameSettings())
        override val statsFlow = MutableStateFlow(GameStats())
        override val savedGameFlow = MutableStateFlow<SavedGameState?>(null)
        var gamesStarted = 0
        val wins = mutableListOf<GameResult>()

        override suspend fun updateSettings(settings: GameSettings) {
            settingsFlow.value = settings
        }

        override suspend fun recordGameStarted() {
            gamesStarted++
        }

        override suspend fun recordGameWon(result: GameResult) {
            wins += result
        }

        override suspend fun saveGame(savedState: SavedGameState) {
            savedGameFlow.value = savedState
        }

        override suspend fun clearSavedGame() {
            savedGameFlow.value = null
        }
    }

    private class FakeSounds : GameSounds {
        var wins = 0
        override fun updateSettings(settings: GameSettings) = Unit
        override fun onResume() = Unit
        override fun onPause() = Unit
        override fun release() = Unit
        override fun playTileSelect() = Unit
        override fun playTileDeselect() = Unit
        override fun playTileMatch() = Unit
        override fun playTileBlocked() = Unit
        override fun playWin() {
            wins++
        }
    }

    private val dispatcher = StandardTestDispatcher()
    private val scheduler get() = dispatcher.scheduler
    private lateinit var repository: FakeRepository
    private lateinit var sounds: FakeSounds
    private lateinit var viewModel: MahjongViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = FakeRepository()
        sounds = FakeSounds()
        viewModel = MahjongViewModel(
            prefsRepo = repository,
            sounds = sounds,
            isTabletProvider = { false },
            computeDispatcher = dispatcher,
        )
        scheduler.runCurrent()
    }

    @After
    fun tearDown() {
        // Stops the timer loop
        viewModel.navigateTo(Screen.MainMenu)
        Dispatchers.resetMain()
    }

    private fun advanceSeconds(seconds: Int) {
        scheduler.advanceTimeBy(seconds * 1000L + 1)
        scheduler.runCurrent()
    }

    private fun savedTile(id: Int, suit: TileSuit, value: Int, x: Float, z: Int = 0, removed: Boolean = false) =
        SavedTile(id, suit.name, value, x, 0f, z, removed)

    /** Two free matching tiles left: one match wins the game. */
    private fun oneMatchFromWinning(constellationId: String, levelIndex: Int?) = SavedGameState(
        constellationId = constellationId,
        levelIndex = levelIndex,
        timerSeconds = 100L,
        movesCount = 0,
        tiles = listOf(
            savedTile(0, TileSuit.BAMBOO, 1, x = 0f),
            savedTile(1, TileSuit.BAMBOO, 1, x = 4f),
        ),
        moveHistory = emptyList(),
    )

    /** Two matching tiles stacked on top of each other: a stalemate. */
    private fun stalemateSave() = SavedGameState(
        constellationId = "jade_pillar",
        levelIndex = 1,
        timerSeconds = 10L,
        movesCount = 1,
        tiles = listOf(
            savedTile(0, TileSuit.WIND, 1, x = 0f, removed = true),
            savedTile(1, TileSuit.WIND, 1, x = 4f, removed = true),
            savedTile(2, TileSuit.BAMBOO, 3, x = 8f, z = 0),
            savedTile(3, TileSuit.BAMBOO, 3, x = 8f, z = 1),
        ),
        moveHistory = listOf(Move(0, 1)),
    )

    private fun continueFrom(saved: SavedGameState) {
        repository.savedGameFlow.value = saved
        scheduler.runCurrent()
        viewModel.continueGame()
        scheduler.runCurrent()
    }

    @Test
    fun newBoardIsDealtInTheBackground() {
        viewModel.startConstellation(ConstellationRepository.getById("jade_pillar"))
        assertNull("Board is not ready before the deal job ran", viewModel.gameState.value)
        scheduler.runCurrent()
        assertEquals(36, viewModel.gameState.value?.remainingTilesCount)
        assertEquals(1, repository.gamesStarted)
    }

    @Test
    fun restartingUnfinishedGameIsNotAnotherGamePlayed() {
        viewModel.startConstellation(ConstellationRepository.getById("jade_pillar"))
        scheduler.runCurrent()
        viewModel.restartGame()
        scheduler.runCurrent()
        assertNotNull(viewModel.gameState.value)
        assertEquals(1, repository.gamesStarted)
    }

    @Test
    fun tutorialIsNotCountedAndUntimed() {
        viewModel.startLevel(ConstellationRepository.TUTORIAL_LEVEL)
        scheduler.runCurrent()
        assertEquals(0, repository.gamesStarted)
        advanceSeconds(5)
        assertEquals(0L, viewModel.timerSeconds.value)
    }

    @Test
    fun timerRunsDuringPlayAndPausesInMenus() {
        viewModel.startConstellation(ConstellationRepository.getById("jade_pillar"))
        scheduler.runCurrent()
        advanceSeconds(3)
        assertEquals(3L, viewModel.timerSeconds.value)

        viewModel.navigateTo(Screen.MainMenu)
        advanceSeconds(3)
        assertEquals(3L, viewModel.timerSeconds.value)
    }

    @Test
    fun timerPausesWhileStalemateDialogIsShown() {
        continueFrom(stalemateSave())
        assertTrue(viewModel.gameState.value!!.isStalemate)
        assertEquals(10L, viewModel.timerSeconds.value)
        advanceSeconds(5)
        assertEquals(10L, viewModel.timerSeconds.value)
    }

    @Test
    fun winRecordsTimeLevelSetAndAssists() {
        continueFrom(oneMatchFromWinning("mini_turtle", levelIndex = 1))
        advanceSeconds(2)
        viewModel.onTileTapped(0)
        viewModel.onTileTapped(1)
        scheduler.runCurrent()

        val result = repository.wins.single()
        assertEquals("mini_turtle", result.constellationId)
        assertEquals(102L, result.timeSeconds)
        assertTrue("mini_turtle belongs to the tablet level set", result.isTabletLevel)
        assertFalse(result.isAssisted)
        assertFalse(result.isTutorial)
        assertNull("Finished games are not kept for Continue", repository.savedGameFlow.value)

        // The win is revealed after the final match animation
        assertFalse(viewModel.gameState.value!!.isWon)
        scheduler.advanceTimeBy(900)
        scheduler.runCurrent()
        assertTrue(viewModel.gameState.value!!.isWon)
        assertEquals(1, sounds.wins)
    }

    @Test
    fun hintIsComputedInBackgroundAndMarksWinAsAssisted() {
        continueFrom(oneMatchFromWinning("jade_pillar", levelIndex = 1))
        viewModel.requestHint()
        assertNull(viewModel.gameState.value!!.hintPair)
        scheduler.runCurrent()
        assertEquals(0 to 1, viewModel.gameState.value!!.hintPair)

        viewModel.onTileTapped(0)
        viewModel.onTileTapped(1)
        scheduler.runCurrent()
        val result = repository.wins.single()
        assertTrue(result.isAssisted)
        assertFalse(result.isTabletLevel)
    }

    @Test
    fun hintResultIsDroppedWhenBoardChangedMeanwhile() {
        continueFrom(oneMatchFromWinning("jade_pillar", levelIndex = 1))
        viewModel.requestHint()
        viewModel.onTileTapped(0)
        scheduler.runCurrent()
        val state = viewModel.gameState.value!!
        assertNull(state.hintPair)
        assertEquals(0, state.hintsUsed)
        assertEquals(0, state.selectedTileId)
    }

    @Test
    fun undoIsCountedAndSaved() {
        viewModel.startConstellation(ConstellationRepository.getById("jade_pillar"))
        scheduler.runCurrent()
        val pair = MahjongGameEngine.findAvailablePairs(viewModel.gameState.value!!.tiles).first()
        viewModel.onTileTapped(pair.first)
        viewModel.onTileTapped(pair.second)
        viewModel.undo()
        scheduler.runCurrent()
        assertEquals(1, viewModel.gameState.value!!.undosUsed)
        assertEquals(1, repository.savedGameFlow.value!!.undosUsed)
    }

    @Test
    fun blockedFlashIsClearedAfterAShortTime() {
        continueFrom(stalemateSave())
        viewModel.onTileTapped(2)
        assertEquals(2, viewModel.gameState.value!!.blockedTileId)
        scheduler.advanceTimeBy(400)
        scheduler.runCurrent()
        assertNull(viewModel.gameState.value!!.blockedTileId)
    }

    /**
     * 22 tiles like in the bug report: ten free pairs plus two Seasons stacked on top of each
     * other. Pairs are left, but the board can't be cleared; too big for the per-move check.
     */
    private fun deadEndWith22Tiles(): SavedGameState {
        val suits = listOf(TileSuit.BAMBOO, TileSuit.CHARACTER)
        val freePairs = (0 until 10).flatMap { pair ->
            val suit = suits[pair % 2]
            val value = pair / 2 + 1
            listOf(
                savedTile(pair * 2, suit, value, x = pair * 8f),
                savedTile(pair * 2 + 1, suit, value, x = pair * 8f + 4f, z = 0).copy(y = 4f),
            )
        }
        val stacked = listOf(
            savedTile(20, TileSuit.SEASON, 1, x = 100f, z = 0),
            savedTile(21, TileSuit.SEASON, 2, x = 100f, z = 1),
        )
        return SavedGameState(
            constellationId = "jade_pillar",
            levelIndex = 1,
            timerSeconds = 0L,
            movesCount = 0,
            tiles = freePairs + stacked,
            moveHistory = emptyList(),
        )
    }

    @Test
    fun deadEndOnMidSizedBoardIsFoundInBackground() {
        continueFrom(deadEndWith22Tiles())
        val state = viewModel.gameState.value!!
        assertEquals(22, state.remainingTilesCount)
        assertTrue("Pairs are still available", state.availablePairsCount > 0)
        assertTrue("The background check reports the dead end", state.isStalemate)
    }

    @Test
    fun undoToWinnableFromStalemate() {
        // Four Seasons, two of them stacked: matching the two loose ones was the losing move
        continueFrom(
            SavedGameState(
                constellationId = "jade_pillar",
                levelIndex = 1,
                timerSeconds = 0L,
                movesCount = 1,
                tiles = listOf(
                    savedTile(0, TileSuit.SEASON, 1, x = 0f, removed = true),
                    savedTile(1, TileSuit.SEASON, 2, x = 6f, removed = true),
                    savedTile(2, TileSuit.SEASON, 3, x = 12f, z = 1),
                    savedTile(3, TileSuit.SEASON, 4, x = 12f, z = 0),
                ),
                moveHistory = listOf(Move(0, 1)),
            )
        )
        assertTrue(viewModel.gameState.value!!.isStalemate)
        viewModel.undoToWinnable()
        scheduler.runCurrent()
        val state = viewModel.gameState.value!!
        assertFalse(state.isStalemate)
        assertTrue(state.moveHistory.isEmpty())
        assertEquals(1, repository.savedGameFlow.value!!.undosUsed)
    }

    @Test
    fun reshuffleResolvesStalemate() {
        continueFrom(stalemateSave())
        viewModel.reshuffle()
        scheduler.runCurrent()
        val state = viewModel.gameState.value!!
        assertFalse(state.isStalemate)
        assertEquals(1, state.reshufflesUsed)
    }
}
