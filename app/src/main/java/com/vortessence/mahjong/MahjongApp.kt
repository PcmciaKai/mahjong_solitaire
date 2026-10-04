package com.vortessence.mahjong

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vortessence.mahjong.ui.screens.ConstellationSelectScreen
import com.vortessence.mahjong.ui.screens.GameScreen
import com.vortessence.mahjong.ui.screens.LevelSelectScreen
import com.vortessence.mahjong.ui.screens.MainMenuScreen
import com.vortessence.mahjong.ui.screens.SettingsScreen
import com.vortessence.mahjong.ui.screens.StatsScreen
import com.vortessence.mahjong.ui.theme.MahjongTheme
import com.vortessence.mahjong.ui.viewmodel.MahjongViewModel
import com.vortessence.mahjong.ui.viewmodel.Screen

@Composable
fun MahjongApp(viewModel: MahjongViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    val levelIndex by viewModel.currentLevelIndex.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val continueInfo by viewModel.continueInfo.collectAsStateWithLifecycle()
    val tutorialStep by viewModel.tutorialStep.collectAsStateWithLifecycle()

    MahjongTheme(tableSurface = settings.tableSurface) {
        Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
            when (screen) {
                is Screen.MainMenu -> {
                    MainMenuScreen(
                        continueInfo = continueInfo,
                        onContinue = { viewModel.continueGame() },
                        onPlayLevels = { viewModel.navigateTo(Screen.LevelSelect) },
                        onChooseConstellation = { viewModel.navigateTo(Screen.ConstellationSelect) },
                        onStats = { viewModel.navigateTo(Screen.Stats) },
                        onSettings = { viewModel.navigateTo(Screen.Settings) },
                    )
                }

                is Screen.LevelSelect -> {
                    LevelSelectScreen(
                        stats = stats,
                        levels = viewModel.currentLevelConstellations,
                        onLevelSelected = { level -> viewModel.startLevel(level) },
                        onBack = { viewModel.navigateTo(Screen.MainMenu) },
                    )
                }

                is Screen.ConstellationSelect -> {
                    ConstellationSelectScreen(
                        stats = stats,
                        onConstellationSelected = { constellation -> viewModel.startConstellation(constellation) },
                        onBack = { viewModel.navigateTo(Screen.MainMenu) },
                    )
                }

                is Screen.Game -> {
                    GameScreen(
                        gameState = gameState,
                        levelIndex = levelIndex,
                        settings = settings,
                        tutorialStep = tutorialStep,
                        totalLevels = viewModel.currentLevelConstellations.size,
                        onBack = { viewModel.navigateTo(Screen.MainMenu) },
                        onTileClick = { tileId -> viewModel.onTileTapped(tileId) },
                        onTileMatch = { viewModel.playTileMatch() },
                        onHint = { viewModel.requestHint() },
                        onUndo = { viewModel.undo() },
                        onRestart = { viewModel.restartGame() },
                        onReshuffle = { viewModel.reshuffle() },
                        onNextLevel = { viewModel.playNextLevel() },
                        onChooseConstellation = { viewModel.navigateTo(Screen.ConstellationSelect) },
                        onMainMenu = { viewModel.navigateTo(Screen.MainMenu) },
                        onTutorialContinue = { viewModel.advanceTutorial() },
                        onSettingsChanged = { updated -> viewModel.updateSettings(updated) },
                    )
                }

                is Screen.Settings -> {
                    SettingsScreen(
                        settings = settings,
                        onSettingsChanged = { updated -> viewModel.updateSettings(updated) },
                        onBack = { viewModel.navigateTo(Screen.MainMenu) },
                    )
                }

                is Screen.Stats -> {
                    StatsScreen(
                        stats = stats,
                        onBack = { viewModel.navigateTo(Screen.MainMenu) },
                    )
                }
            }
        }
    }
}
