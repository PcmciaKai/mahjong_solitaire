package com.vortessence.mahjong.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.vortessence.mahjong.data.model.BoardTile
import com.vortessence.mahjong.data.model.TableSurface
import com.vortessence.mahjong.data.model.Tile
import com.vortessence.mahjong.data.model.TilePosition
import com.vortessence.mahjong.data.model.TileSuit
import com.vortessence.mahjong.ui.components.tiles.TileLook

private val PreviewTiles = listOf(
    BoardTile(id = 1, tile = Tile(TileSuit.BAMBOO, 1), position = TilePosition(0f, 0f, 0)),
    BoardTile(id = 2, tile = Tile(TileSuit.DRAGON, Tile.DRAGON_RED), position = TilePosition(0f, 0f, 0)),
    BoardTile(id = 3, tile = Tile(TileSuit.CIRCLE, 1), position = TilePosition(0f, 0f, 0)),
)

/**
 * A table surface in miniature, with a few tiles of the current set in the middle and the lamp
 * when it is switched on, for picking a surface in the settings.
 */
@Composable
fun TableSurfacePreview(
    surface: TableSurface,
    tileLook: TileLook,
    lampLighting: Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Canvas(modifier = modifier) {
        TableSurfaceRenderer.drawTableSurface(this, surface, size.width, size.height, context)

        val tileW = size.width * 0.2f
        val tileH = tileW * 1.333f
        val gap = tileW * 0.06f
        val rowWidth = (tileW * PreviewTiles.size) + (gap * (PreviewTiles.size - 1))
        val left = (size.width - rowWidth) / 2f
        val top = (size.height - tileH) / 2f
        PreviewTiles.forEachIndexed { index, tile ->
            TileRenderer.drawTile(
                scope = this,
                boardTile = tile,
                x = left + (index * (tileW + gap)),
                y = top,
                width = tileW,
                height = tileH,
                look = tileLook,
                layerShading = lampLighting,
            )
        }
        if (lampLighting) BoardLighting(size.width, size.height).drawFalloff(this)
    }
}
