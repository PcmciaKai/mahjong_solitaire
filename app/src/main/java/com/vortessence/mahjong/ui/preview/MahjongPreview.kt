package com.vortessence.mahjong.ui.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vortessence.mahjong.data.model.GameSettings
import com.vortessence.mahjong.data.model.TableSurface
import com.vortessence.mahjong.data.repository.ConstellationRepository
import com.vortessence.mahjong.engine.MahjongGameEngine
import com.vortessence.mahjong.ui.components.MahjongBoard
import com.vortessence.mahjong.ui.components.TableSurfaceRenderer
import com.vortessence.mahjong.ui.theme.MahjongTheme

@Preview(widthDp = 360, heightDp = 600, showBackground = true)
@Composable
fun TableSurfacesPreview() {
    MahjongTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TableSurfaceItem("Green Mat", TableSurface.GREEN_MAT)
            Spacer(modifier = Modifier.height(24.dp))
            TableSurfaceItem("Walnut Wood", TableSurface.WALNUT)
        }
    }
}

@Composable
private fun TableSurfaceItem(name: String, surface: TableSurface) {
    val context = LocalContext.current
    Column {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                TableSurfaceRenderer.drawTableSurface(
                    scope = this,
                    surface = surface,
                    width = size.width,
                    height = size.height,
                    context = context,
                )
            }
        }
    }
}

@Preview(widthDp = 400, heightDp = 650, showBackground = true)
@Composable
fun BoardWithWalnutPreview() {
    val miniTurtle = ConstellationRepository.getById("mini_turtle")
    val state = MahjongGameEngine.startNewGame(miniTurtle)

    MahjongTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            MahjongBoard(
                gameState = state,
                settings = GameSettings(
                    tableSurface = TableSurface.WALNUT,
                    highlightFreeTiles = false
                ),
                onTileClick = {}
            )
        }
    }
}

@Preview(widthDp = 400, heightDp = 650, showBackground = true)
@Composable
fun BoardPreview() {
    val miniTurtle = ConstellationRepository.getById("mini_turtle")
    val state = MahjongGameEngine.startNewGame(miniTurtle)

    MahjongTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            MahjongBoard(
                gameState = state,
                settings = GameSettings(highlightFreeTiles = false),
                onTileClick = {}
            )
        }
    }
}
