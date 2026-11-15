package com.vortessence.mahjong.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vortessence.mahjong.R
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
    // Read lazily so the clock ticking does not recompose the whole screen
    timerSeconds: () -> Long = { 0L },
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
    onUndoToWinnable: () -> Unit = onUndo,
    onNextLevel: () -> Unit,
    onChooseConstellation: () -> Unit,
    onMainMenu: () -> Unit,
    onTutorialContinue: () -> Unit = {},
    onSettingsChanged: (GameSettings) -> Unit = {},
) {
    BackHandler {
        onBack()
    }

    var showRestartConfirmation by rememberSaveable { mutableStateOf(false) }

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
            timerSeconds = timerSeconds,
            levelIndex = levelIndex,
            onBackClick = onBack,
            onHintClick = onHint,
            onUndoClick = onUndo,
            onRestartClick = {
                // Nothing to lose before the first match, so skip the confirmation
                if (gameState.moveHistory.isEmpty()) onRestart() else showRestartConfirmation = true
            },
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
            timerSeconds = timerSeconds(),
            levelIndex = levelIndex,
            hasNextLevel = hasNextLevel,
            onPlayAgain = onRestart,
            onNextLevel = onNextLevel,
            onChooseConstellation = onChooseConstellation,
            onMainMenu = onMainMenu
        )
    }

    if (showRestartConfirmation) {
        AlertDialog(
            onDismissRequest = { showRestartConfirmation = false },
            title = { Text(stringResource(R.string.restart_confirm_title)) },
            text = { Text(stringResource(R.string.restart_confirm_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRestartConfirmation = false
                        onRestart()
                    }
                ) {
                    Text(stringResource(R.string.restart))
                }
            },
            dismissButton = {
                TextButton(onClick = { showRestartConfirmation = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // Stalemate / no more moves dialog
    if (gameState.isStalemate && !gameState.isWon) {
        StalemateDialog(
            canUndo = gameState.moveHistory.isNotEmpty(),
            hasFreePairs = gameState.availablePairsCount > 0,
            onReshuffle = onReshuffle,
            onUndoToWinnable = onUndoToWinnable,
            onRestart = onRestart
        )
    }
}
