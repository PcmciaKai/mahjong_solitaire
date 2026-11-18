package com.vortessence.mahjong.ui.preview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vortessence.mahjong.data.model.BoardTile
import com.vortessence.mahjong.data.model.TableSurface
import com.vortessence.mahjong.data.model.Tile
import com.vortessence.mahjong.data.model.TilePosition
import com.vortessence.mahjong.data.model.TileSet
import com.vortessence.mahjong.data.model.TileSuit
import com.vortessence.mahjong.ui.components.BoardLighting
import com.vortessence.mahjong.ui.components.TableSurfaceRenderer
import com.vortessence.mahjong.ui.components.TileRenderer
import com.vortessence.mahjong.ui.components.tiles.TileLook
import com.vortessence.mahjong.ui.components.tiles.TileMaterials
import com.vortessence.mahjong.ui.components.tiles.TileTextureGenerator
import com.vortessence.mahjong.ui.theme.MahjongTheme

/** Builds a look with freshly generated textures; previews would otherwise keep a stale cache. */
private fun previewLook(set: TileSet, worn: Boolean, colorway: Int = 0): TileLook {
    val material = TileMaterials.of(set)
    val textures = TileTextureGenerator.generate(material, worn, TileTextureGenerator.BASE_WIDTH, 4)
    return TileLook(material, material.colorway(colorway), worn, textures)
}

@Preview(widthDp = 420, heightDp = 380)
@Composable
fun TileSelectionPreview() {
    // Per row: plain, selected, hint, blocked flash, tutorial, dimmed (not free)
    val rows = listOf(
        TileSet.BONE_BAMBOO to true,
        TileSet.CATALIN to true,
        TileSet.MELAMINE to false,
        TileSet.JADE to false,
    ).map { (set, worn) -> previewLook(set, worn) }
    val highlights = listOf(
        null,
        TileRenderer.Highlight.SELECTED,
        TileRenderer.Highlight.HINT,
        TileRenderer.Highlight.BLOCKED,
        TileRenderer.Highlight.TUTORIAL,
        null,
    )
    MahjongTheme {
        Canvas(modifier = Modifier.fillMaxSize()) {
            TableSurfaceRenderer.drawTableSurface(this, TableSurface.GREEN_MAT, size.width, size.height)
            val tileW = size.width / 8f
            val tileH = tileW * 1.333f
            rows.forEachIndexed { row, look ->
                highlights.forEachIndexed { col, highlight ->
                    val x = (tileW * 0.6f) + (col * tileW * 1.2f)
                    val y = (tileH * 0.3f) + (row * tileH * 1.25f)
                    val lift = if (highlight == TileRenderer.Highlight.SELECTED) TileRenderer.selectionLift(tileW) else 0f
                    TileRenderer.drawTile(
                        scope = this,
                        boardTile = BoardTile(
                            id = (row * 10) + col,
                            tile = Tile(TileSuit.CIRCLE, col + 2),
                            position = TilePosition(0f, 0f, 0)
                        ),
                        x = x,
                        y = y,
                        width = tileW,
                        height = tileH,
                        look = look,
                        lift = lift,
                        dimmed = col == highlights.lastIndex
                    )
                    if (highlight != null) {
                        TileRenderer.drawHighlight(this, x, y, tileW, tileH, highlight, lift)
                    }
                }
            }
        }
    }
}

/** Left half: no lamp, new tiles. Right half: lamp lighting and worn tiles. */
@Preview(widthDp = 720, heightDp = 600)
@Composable
fun LampLightingPreview() {
    val looks = listOf(previewLook(TileSet.Default, worn = false), previewLook(TileSet.Default, worn = false))
    val suits = listOf(TileSuit.CIRCLE, TileSuit.BAMBOO, TileSuit.CHARACTER)
    MahjongTheme {
        Row(modifier = Modifier.fillMaxSize()) {
            for (lit in listOf(false, true)) {
                Canvas(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    TableSurfaceRenderer.drawTableSurface(this, TableSurface.GREEN_MAT, size.width, size.height)
                    val lighting = BoardLighting(size.width, size.height)
                    val tileW = size.width / 8f
                    val tileH = tileW * 1.333f
                    var id = 0
                    for (row in 0 until 3) {
                        for (col in 0 until 6) {
                            TileRenderer.drawTile(
                                scope = this,
                                boardTile = BoardTile(
                                    id = id++,
                                    tile = Tile(suits[row], col + 1),
                                    position = TilePosition(0f, 0f, 0)
                                ),
                                x = tileW + (col * tileW),
                                y = (tileH * 3.5f) + (row * tileH),
                                width = tileW,
                                height = tileH,
                                look = looks[if (lit) 1 else 0],
                                layerShading = lit,
                            )
                        }
                    }
                    if (lit) lighting.drawFalloff(this)
                }
            }
        }
    }
}

@Preview(widthDp = 400, heightDp = 1500)
@Composable
fun SettingsTileSetPreview() {
    MahjongTheme {
        com.vortessence.mahjong.ui.screens.SettingsScreen(
            settings = com.vortessence.mahjong.data.model.GameSettings(tileSet = TileSet.RIICHI, tileColorway = 1),
            onSettingsChanged = {},
            onBack = {}
        )
    }
}

/** Every table surface at a phone board's proportions, with tiles and the lamp. */
@Preview(widthDp = 760, heightDp = 640)
@Composable
fun TableSurfacesOverviewPreview() {
    val look = previewLook(TileSet.Default, worn = false)
    MahjongTheme {
        Column(modifier = Modifier.fillMaxSize().padding(4.dp)) {
            for (row in TableSurface.entries.chunked(4)) {
                Row(modifier = Modifier.weight(1f)) {
                    for (surface in row) {
                        com.vortessence.mahjong.ui.components.TableSurfacePreview(
                            surface = surface,
                            tileLook = look,
                            lampLighting = true,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(4.dp)
                        )
                    }
                    repeat(4 - row.size) { Box(modifier = Modifier.weight(1f)) }
                }
            }
        }
    }
}

/** The top-left corner of the leather mat, magnified, to judge the ornament. */
@Preview(widthDp = 300, heightDp = 300)
@Composable
fun LeatherCornerPreview() {
    MahjongTheme {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // A phone-shaped mat, scaled up three times around its top-left corner
            scale(3f, 3f, Offset.Zero) {
                TableSurfaceRenderer.drawTableSurface(this, TableSurface.LEATHER_MAT, size.width, size.height)
            }
        }
    }
}

/** A small three-layer stack under the lamp, to judge shadows, depth and reflections. */
@Composable
private fun StackedBoard(set: TileSet, worn: Boolean) {
    val look = previewLook(set, worn)
    val tiles = buildList {
        var id = 0
        val suits = TileSuit.entries.take(3)
        // Layer 0: 7 x 5, layer 1: 5 x 3, layer 2: 3 x 1, each centred on the one below
        listOf(Triple(7, 5, 0), Triple(5, 3, 1), Triple(3, 1, 2)).forEach { (cols, rows, z) ->
            for (row in 0 until rows) for (col in 0 until cols) {
                add(
                    BoardTile(
                        id = id,
                        tile = Tile(suits[id % 3], (id % 9) + 1),
                        position = TilePosition((col + z) * 2f, (row + z) * 2f, z)
                    )
                )
                id++
            }
        }
    }.sortedWith(compareBy<BoardTile> { it.z }.thenBy { it.x + it.y }.thenBy { it.x })

    Canvas(modifier = Modifier.fillMaxSize()) {
        TableSurfaceRenderer.drawTableSurface(this, TableSurface.GREEN_MAT, size.width, size.height)
        val tileW = size.width / 9f
        val tileH = tileW * 1.333f
        val transform = com.vortessence.mahjong.ui.components.BoardTransform(
            unitX = tileW / 2f,
            tileWidth = tileW,
            tileHeight = tileH,
            startX = tileW,
            startY = (size.height - (tileH * 5f)) / 2f,
            layerShiftX = tileW * 0.08f,
            layerShiftY = tileH * 0.08f,
            depthX = tileW * 0.08f,
            depthY = tileH * 0.08f,
        )
        for (tile in tiles) {
            TileRenderer.drawTile(
                scope = this,
                boardTile = tile,
                x = transform.tilePixelX(tile.x, tile.z),
                y = transform.tilePixelY(tile.y, tile.z),
                width = tileW,
                height = tileH,
                look = look,
                layerShading = true,
            )
        }
        BoardLighting(size.width, size.height).drawFalloff(this)
    }
}

@Preview(widthDp = 400, heightDp = 420)
@Composable
fun StackedJadePreview() {
    MahjongTheme { StackedBoard(TileSet.JADE, worn = false) }
}

@Preview(widthDp = 400, heightDp = 420)
@Composable
fun StackedBonePreview() {
    MahjongTheme { StackedBoard(TileSet.BONE_BAMBOO, worn = true) }
}

@Preview(widthDp = 400, heightDp = 420)
@Composable
fun StackedMelaminePreview() {
    MahjongTheme { StackedBoard(TileSet.MELAMINE, worn = false) }
}

/** Zoomed in on engraved symbols: Riichi, Jade and Travel (printed, for comparison). */
@Preview(widthDp = 420, heightDp = 220)
@Composable
fun EngravingPreview() {
    val looks = listOf(TileSet.RIICHI, TileSet.JADE, TileSet.TRAVEL).map { previewLook(it, worn = false) }
    MahjongTheme {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(Color(0xFF0C4033))
            val tileW = size.width / 3.6f
            looks.forEachIndexed { index, look ->
                TileRenderer.drawTile(
                    scope = this,
                    boardTile = BoardTile(id = index, tile = Tile(TileSuit.CHARACTER, 5), position = TilePosition(0f, 0f, 0)),
                    x = (tileW * 0.25f) + (index * tileW * 1.15f),
                    y = tileW * 0.12f,
                    width = tileW,
                    height = tileW * 1.333f,
                    showCornerIndex = true,
                    look = look,
                )
            }
        }
    }
}

/** Every tile set: new on the left, worn on the right. */
@Preview(widthDp = 420, heightDp = 860)
@Composable
fun TileSetsPreview() {
    val tiles = listOf(
        Tile(TileSuit.DRAGON, 1),
        Tile(TileSuit.CIRCLE, 5),
        Tile(TileSuit.CHARACTER, 8),
    )
    val rows = TileSet.entries.map { set -> previewLook(set, worn = false) to previewLook(set, worn = true) }
    MahjongTheme {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(Color(0xFF0C4033))
            val rowH = size.height / rows.size
            val tileH = rowH * 0.8f
            val tileW = tileH / 1.333f
            rows.forEachIndexed { rowIndex, (newLook, wornLook) ->
                for (half in 0..1) {
                    tiles.forEachIndexed { index, tile ->
                        TileRenderer.drawTile(
                            scope = this,
                            boardTile = BoardTile(
                                id = (rowIndex * 10) + index + (half * 5),
                                tile = tile,
                                position = TilePosition(0f, 0f, 0)
                            ),
                            x = (half * size.width / 2f) + (tileW * 0.3f) + (index * tileW * 1.12f),
                            y = (rowIndex * rowH) + (rowH * 0.06f),
                            width = tileW,
                            height = tileH,
                            showCornerIndex = true,
                            look = if (half == 0) newLook else wornLook,
                        )
                    }
                }
            }
        }
    }
}
