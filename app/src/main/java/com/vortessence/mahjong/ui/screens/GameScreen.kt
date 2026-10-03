package com.vortessence.mahjong.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vortessence.mahjong.data.model.GameSettings
import com.vortessence.mahjong.data.model.GameState
import com.vortessence.mahjong.data.repository.ConstellationRepository
import com.vortessence.mahjong.engine.TutorialStep
import com.vortessence.mahjong.ui.components.GameHeader
import com.vortessence.mahjong.ui.components.GameWonDialog
import com.vortessence.mahjong.ui.components.MahjongBoard
import com.vortessence.mahjong.ui.components.StalemateDialog
import com.vortessence.mahjong.ui.components.TutorialCard

@Composable
fun GameScreen(
    gameState: GameState?,
    levelIndex: Int?,
    settings: GameSettings = GameSettings(),
    tutorialStep: TutorialStep? = null,
    totalLevels: Int = ConstellationRepository.totalLevels,
    onBack: () -> Unit,
    onTileClick: (Int) -> Unit,
    onTileMatch: () -> Unit = {},
    onHint: () -> Unit,
    onUndo: () -> Unit,
    onRestart: () -> Unit,
    onReshuffle: () -> Unit,
    onNextLevel: () -> Unit,
    onChooseConstellation: () -> Unit,
    onMainMenu: () -> Unit,
    onTutorialContinue: () -> Unit = {},
    onSettingsChanged: (GameSettings) -> Unit = {},
) {
    BackHandler {
        onBack()
    }

    if (gameState == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
        return
    }

    val hasNextLevel = (levelIndex != null) && (levelIndex < totalLevels)

    val tutorialHighlights = when {
        tutorialStep == null -> emptySet()
        tutorialStep.highlightFreeTiles -> gameState.tiles.filter { it.isFree }.map { it.id }.toSet()
        else -> tutorialStep.targetTileIds
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        GameHeader(
            gameState = gameState,
            levelIndex = levelIndex,
            onBackClick = onBack,
            onHintClick = onHint,
            onUndoClick = onUndo,
            onRestartClick = onRestart,
            hintAndUndoEnabled = tutorialStep == null
        )

        Box(modifier = Modifier.weight(1f)) {
            MahjongBoard(
                gameState = gameState,
                settings = settings,
                highlightedTileIds = tutorialHighlights,
                onTileClick = onTileClick,
                onTileMatch = onTileMatch,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (tutorialStep != null) {
            TutorialCard(
                step = tutorialStep,
                settings = settings,
                onContinue = onTutorialContinue,
                onSettingsChanged = onSettingsChanged
            )
        }
    }

    // Victory celebration dialog
    if (gameState.isWon) {
        GameWonDialog(
            gameState = gameState,
            levelIndex = levelIndex,
            hasNextLevel = hasNextLevel,
            onPlayAgain = onRestart,
            onNextLevel = onNextLevel,
            onChooseConstellation = onChooseConstellation,
            onMainMenu = onMainMenu
        )
    }

    // Stalemate / no more moves dialog
    if (gameState.isStalemate && !gameState.isWon) {
        StalemateDialog(
            canUndo = gameState.moveHistory.isNotEmpty(),
            onReshuffle = onReshuffle,
            onUndo = onUndo,
            onRestart = onRestart
        )
    }
}
