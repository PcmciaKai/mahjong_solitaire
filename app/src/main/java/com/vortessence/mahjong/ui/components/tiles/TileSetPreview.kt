package com.vortessence.mahjong.ui.components.tiles

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import com.vortessence.mahjong.data.model.BoardTile
import com.vortessence.mahjong.data.model.TableSurface
import com.vortessence.mahjong.ui.components.TableSurfaceRenderer
import com.vortessence.mahjong.data.model.Tile
import com.vortessence.mahjong.data.model.TilePosition
import com.vortessence.mahjong.data.model.TileSet
import com.vortessence.mahjong.data.model.TileSuit
import com.vortessence.mahjong.ui.components.TileRenderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * The look of a tile set for drawing. Its textures are generated in the background the first
 * time; until then the tiles are drawn without them.
 *
 * [preview] uses small, single-variant textures, enough for the tiles in the settings screen.
 */
@Composable
fun rememberTileLook(set: TileSet, colorway: Int, worn: Boolean, preview: Boolean = false): TileLook {
    val width = if (preview) TileTextureCache.PREVIEW_WIDTH else TileTextureCache.BOARD_WIDTH
    val variants = if (preview) TileTextureCache.PREVIEW_VARIANTS else TileTextureCache.BOARD_VARIANTS
    val textures by produceState(TileTextureCache.cachedOrNull(set, worn, width), set, worn, width) {
        value = TileTextureCache.cachedOrNull(set, worn, width)
            ?: withContext(Dispatchers.Default) { TileTextureCache.get(set, worn, width, variants) }
    }
    return remember(set, colorway, worn, textures) {
        val material = TileMaterials.of(set)
        // TileLook ignores textures of another set that may still be around while switching
        TileLook(material, material.colorway(colorway), worn, textures)
    }
}

private val PreviewTiles = listOf(
    Tile(TileSuit.DRAGON, Tile.DRAGON_RED),
    Tile(TileSuit.CIRCLE, 5),
    Tile(TileSuit.CHARACTER, 8),
)

/** How much of the table is magnified behind the tiles, so its material shows rather than its border. */
private const val TABLE_ZOOM = 3f

/** Three tiles of a set on a bit of the chosen table, for picking a set in the settings. */
@Composable
fun TileSetPreview(
    set: TileSet,
    colorway: Int,
    worn: Boolean,
    surface: TableSurface,
    modifier: Modifier = Modifier,
) {
    val look = rememberTileLook(set, colorway, worn, preview = true)
    Canvas(modifier = modifier) {
        scale(TABLE_ZOOM, TABLE_ZOOM, center) {
            TableSurfaceRenderer.drawTableSurface(this, surface, size.width, size.height)
        }
        val gap = size.width * 0.04f
        val tileW = (size.width - (gap * 5.5f)) / PreviewTiles.size
        val tileH = tileW * 1.333f
        val top = (size.height - (tileH * 1.08f)) / 2f
        PreviewTiles.forEachIndexed { index, tile ->
            TileRenderer.drawTile(
                scope = this,
                boardTile = BoardTile(id = index + 1, tile = tile, position = TilePosition(0f, 0f, 0)),
                x = (gap * 1.5f) + (index * (tileW + gap)),
                y = top,
                width = tileW,
                height = tileH,
                look = look,
            )
        }
        // Keep the corners of the little table soft against the card
        drawRect(
            brush = Brush.radialGradient(
                0.6f to Color.Transparent,
                1f to Color.Black.copy(alpha = 0.25f),
                center = Offset(size.width / 2f, size.height / 2f),
                radius = size.maxDimension * 0.75f,
            )
        )
    }
}
