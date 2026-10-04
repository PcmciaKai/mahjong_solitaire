package com.vortessence.mahjong.ui.preview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vortessence.mahjong.data.model.BoardTile
import com.vortessence.mahjong.data.model.Tile
import com.vortessence.mahjong.data.model.TilePosition
import com.vortessence.mahjong.data.model.TileSuit
import com.vortessence.mahjong.ui.components.TileRenderer
import com.vortessence.mahjong.ui.theme.MahjongTheme

@Preview(showBackground = true)
@Composable
fun TileSelectionPreview() {
    val tile = BoardTile(
        id = 1,
        tile = Tile(TileSuit.DRAGON, 1),
        position = TilePosition(0f, 0f, 0)
    )
    MahjongTheme {
        Box(modifier = Modifier.size(100.dp, 130.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                TileRenderer.drawTile(
                    scope = this,
                    boardTile = tile,
                    x = 10f,
                    y = 10f,
                    width = size.width - 20f,
                    height = size.height - 20f,
                    isSelected = false
                )
            }
        }
    }
}
