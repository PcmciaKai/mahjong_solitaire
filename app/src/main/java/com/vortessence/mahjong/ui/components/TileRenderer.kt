package com.vortessence.mahjong.ui.components

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorGroup
import androidx.compose.ui.graphics.vector.VectorPath
import androidx.compose.ui.graphics.vector.toPath
import java.util.concurrent.ConcurrentHashMap
import com.vortessence.mahjong.ui.components.tileset.MahjongTiles
import com.vortessence.mahjong.ui.components.tileset.*
import com.vortessence.mahjong.ui.components.tiles.TileLook
import com.vortessence.mahjong.ui.components.tiles.TileSpace
import com.vortessence.mahjong.ui.components.tiles.TileTextures
import com.vortessence.mahjong.data.model.BoardTile
import com.vortessence.mahjong.data.model.Tile
import com.vortessence.mahjong.data.model.TileSuit
import com.vortessence.mahjong.ui.theme.TutorialHighlight
import kotlin.math.ceil
import kotlin.math.hypot
import kotlin.math.roundToInt

object TileRenderer {

    // ==========================================
    // TILE SPACE (used by the face, texture and light passes)
    // A tile drawn at (x, y) with its real width and height is mapped onto a fixed
    // TileSpace.WIDTH x TileSpace.HEIGHT box, so brushes can be created once per tile look and
    // reuse their shaders, and textures stick to the tile while zooming.
    // ==========================================
    private const val LOCAL_BEVEL = TileSpace.WIDTH * 0.015f
    private val LocalFaceTopLeft = Offset(LOCAL_BEVEL, LOCAL_BEVEL)
    private val LocalFaceSize = Size(TileSpace.WIDTH - (LOCAL_BEVEL * 2f), TileSpace.HEIGHT - (LOCAL_BEVEL * 2f))
    private val LocalFaceCorner = CornerRadius(TileSpace.WIDTH * 0.085f * 0.9f)
    private val LocalFaceCenter = Offset(TileSpace.WIDTH / 2f, TileSpace.HEIGHT / 2f)
    private val LocalRimStroke = Stroke(width = TileSpace.WIDTH * 0.011f)

    /**
     * Distance in pixels between the stacked layers that make up a tile's sides. About one pixel
     * hides the steps, so small tiles need far fewer layers than zoomed-in ones.
     */
    private const val SIDE_STEP_PX = 1.2f
    private const val MIN_SIDE_STEPS = 3
    private const val MAX_SIDE_STEPS = 24


    /** Lower layers are further from the lamp and get a bit less light. */
    private fun layerShade(layerZ: Int): Float = (0.08f - (layerZ * 0.025f)).coerceAtLeast(0f)

    private val cornerPaint = Paint().apply {
        isAntiAlias = true
        // Without it glyphs snap to whole pixels, so a sub-pixel engraving offset would come out
        // as 0 or 1 pixel depending on where the tile happens to be
        isSubpixelText = true
        textAlign = Paint.Align.LEFT
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
    }

    // ==========================================
    // SOFT SHADOWS
    // Blurred rounded rectangles; Skia draws these analytically on the GPU, in a single pass.
    // ==========================================
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** Blur filters by radius in half pixels, so they are created once and then reused. */
    private val blurFilters = HashMap<Int, BlurMaskFilter>()

    private const val SHADOW_RGB = 0x1A120A

    private fun drawSoftShadow(
        scope: DrawScope,
        left: Float,
        top: Float,
        size: Size,
        cornerRadius: Float,
        blur: Float,
        alpha: Float,
    ) {
        val key = (blur * 2f).roundToInt()
        shadowPaint.maskFilter = if (key <= 0) {
            null
        } else {
            blurFilters.getOrPut(key) { BlurMaskFilter(key / 2f, BlurMaskFilter.Blur.NORMAL) }
        }
        shadowPaint.color = ((alpha.coerceIn(0f, 1f) * 255f).roundToInt() shl 24) or SHADOW_RGB
        scope.drawContext.canvas.nativeCanvas.drawRoundRect(
            left, top, left + size.width, top + size.height, cornerRadius, cornerRadius, shadowPaint
        )
    }

    /**
     * Draws a single Mahjong tile with physical 3D depth, in the material and wear state of [look].
     *
     * [layerShading] darkens lower layers slightly, as if they were further from the lamp.
     * [lift] raises the tile off the board by that many pixels, toward the viewer; its shadow
     * stays behind on the board, longer and softer. [dimmed] veils the face, for tiles that
     * can't be played while free tiles are highlighted.
     *
     * Selection and hints are drawn separately with [drawHighlight], on top of the board.
     */
    fun drawTile(
        scope: DrawScope,
        boardTile: BoardTile,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        showCornerIndex: Boolean = false,
        look: TileLook = TileLook.Default,
        layerShading: Boolean = false,
        lift: Float = 0f,
        dimmed: Boolean = false,
    ) {
        if (boardTile.isRemoved) return

        val depthX = width * 0.08f
        val depthY = height * 0.08f
        val cornerR = width * 0.085f
        val corner = CornerRadius(cornerR, cornerR)
        val tileSize = Size(width, height)
        val layerZ = boardTile.z

        // A raised tile's shadow falls further away and spreads out
        val liftShift = lift * 0.9f
        val liftBlur = lift * 0.8f

        // 1. Shadows, falling down and to the right, away from the lamp. They land on whatever
        // was drawn before: the table, or the tiles of the layer below.
        //
        // Further from the lamp, light comes in at a flatter angle and shadows grow longer
        val lamp = Lamp.center(scope.size)
        val lampDistance = hypot(x + (width / 2f) - lamp.x, y + (height / 2f) - lamp.y)
        val shadowLength = 1f + (0.45f * (lampDistance / Lamp.reach(scope.size)).coerceIn(0f, 1f))

        if (layerZ == 0) {
            // Ambient: the whole stack darkens the table around its foot
            drawSoftShadow(
                scope,
                left = x + (depthX * 1.2f * shadowLength), top = y + (depthY * 1.2f * shadowLength),
                size = tileSize, cornerRadius = cornerR * 1.5f,
                blur = depthX * 2.4f, alpha = 0.24f,
            )
        }
        // Cast shadow on the surface below
        drawSoftShadow(
            scope,
            left = x + (depthX * 1.45f * shadowLength) + liftShift,
            top = y + (depthY * 1.45f * shadowLength) + liftShift,
            size = tileSize, cornerRadius = cornerR,
            blur = (depthX * 1.1f) + liftBlur, alpha = if (lift > 0f) 0.5f else 0.42f,
        )
        // Contact shadow: the dark crease where the tile rests on the surface below; a raised
        // tile no longer touches it
        if (lift <= 0f) {
            drawSoftShadow(
                scope,
                left = x + depthX, top = y + depthY,
                size = tileSize, cornerRadius = cornerR,
                blur = depthX * 0.3f, alpha = 0.4f,
            )
        }

        // Everything from here on is the tile itself, raised by [lift]
        if (lift != 0f) scope.drawContext.transform.translate(-lift, -lift)

        // 2. Solid 3D Body Extrusion (Backing -> Sides -> Face)
        val steps = ceil(depthX / SIDE_STEP_PX).toInt().coerceIn(MIN_SIDE_STEPS, MAX_SIDE_STEPS)
        val backingStart = look.backingStart
        val backSteps = if (backingStart >= 1f) 0 else (steps * (1f - backingStart)).roundToInt().coerceAtLeast(1)
        val faceSteps = (steps - backSteps).coerceAtLeast(1)

        // A) Backing material, from the far side up to where the face material begins
        if (backSteps > 0) {
            for (i in backSteps downTo 0) {
                val t = i / backSteps.toFloat()
                val stepDepth = backingStart + ((1f - backingStart) * t)
                val topLeft = Offset(x + (depthX * stepDepth), y + (depthY * stepDepth))
                scope.drawRoundRect(
                    color = mix(look.back, look.backDeep, t),
                    topLeft = topLeft,
                    size = tileSize,
                    cornerRadius = corner,
                )
                if (i == backSteps) drawTileOutline(scope, topLeft, tileSize, corner, width)
            }
        }

        // B) Face material sides
        for (i in faceSteps downTo 0) {
            val t = i / faceSteps.toFloat()
            val stepDepth = t * backingStart
            val topLeft = Offset(x + (depthX * stepDepth), y + (depthY * stepDepth))
            scope.drawRoundRect(
                color = mix(look.sideLight, look.sideDeep, t),
                topLeft = topLeft,
                size = tileSize,
                cornerRadius = corner,
            )
            // One-tone tiles: the far edge is face material
            if ((backSteps == 0) && (i == faceSteps)) drawTileOutline(scope, topLeft, tileSize, corner, width)
        }

        // 3. Face, lit from the upper left
        scope.inTileSpace(x, y, width, height) {
            drawRoundRect(
                brush = look.faceBrush,
                topLeft = LocalFaceTopLeft,
                size = LocalFaceSize,
                cornerRadius = LocalFaceCorner,
            )
        }

        // 5. Classic Top-Left Corner Index
        val tile = boardTile.tile
        // Engraved symbols: the groove's upper-left wall is in shadow, which shows as a thin dark
        // edge where the paint is drawn slightly down and to the right of the groove's outline
        val engraveOffset = if (look.engraving > 0f) {
            (width * 0.011f * look.engraving).coerceIn(0.45f, 2.5f)
        } else {
            0f
        }

        if (showCornerIndex) {
            // The thin index strokes are cut more shallowly than the big symbols
            drawCornerIndex(scope, tile, x, y, width, height, look, engraveOffset * 0.55f)
        }

        // 6. Artwork, in the colours of the material's paint
        val cx = x + (width * 0.5f)
        val cy = y + (height * 0.5f)
        val innerW = width * 0.85f
        val innerH = height * 0.85f

        getTileVector(tile)?.let { vector ->
            if (engraveOffset > 0f) {
                drawVectorTile(scope, vector, cx, cy, innerW, innerH, look.engravingShadowFilter)
            }
            drawVectorTile(scope, vector, cx + engraveOffset, cy + engraveOffset, innerW, innerH, look.inkFilter)
        }

        // 7. Wear, grain and light, over the artwork so the paint ages along with the face
        scope.inTileSpace(x, y, width, height) {
            look.textures?.let { textures ->
                if (textures.variantCount > 0) drawTextures(look, textures, boardTile.id)
            }
            look.sheenBrush?.let { sheen ->
                drawRoundRect(
                    brush = sheen,
                    topLeft = LocalFaceTopLeft,
                    size = LocalFaceSize,
                    cornerRadius = LocalFaceCorner,
                )
            }
            drawRoundRect(
                brush = look.rimBrush,
                topLeft = LocalFaceTopLeft,
                size = LocalFaceSize,
                cornerRadius = LocalFaceCorner,
                style = LocalRimStroke,
            )
            val shade = if (layerShading) layerShade(layerZ) else 0f
            if (shade > 0f) {
                drawRoundRect(
                    color = Color.Black.copy(alpha = shade),
                    topLeft = LocalFaceTopLeft,
                    size = LocalFaceSize,
                    cornerRadius = LocalFaceCorner,
                )
            }
        }

        // 8. The lamp mirrored in the polished faces, one reflection shared by all tiles
        look.reflectionBrush(scope.size)?.let { reflection ->
            val bevel = width * 0.015f
            scope.drawRoundRect(
                brush = reflection,
                topLeft = Offset(x + bevel, y + bevel),
                size = Size(width - (bevel * 2f), height - (bevel * 2f)),
                cornerRadius = CornerRadius(cornerR * 0.9f),
            )
        }

        // 9. Tiles that can't be played step back when free tiles are highlighted
        if (dimmed) {
            scope.drawRoundRect(
                color = DimVeil,
                topLeft = Offset(x, y),
                size = Size(width + depthX, height + depthY),
                cornerRadius = corner,
            )
        }

        if (lift != 0f) scope.drawContext.transform.translate(lift, lift)
    }

    // ==========================================
    // HIGHLIGHTS
    // A glowing ring around the tile, drawn on top of the whole board so neighbours never cover
    // it. Its contrast comes from the ring and its dark edge, not from tinting the face, so it
    // reads the same on every material, clean or aged.
    // ==========================================

    enum class Highlight(val color: Color) {
        SELECTED(Color(0xFFFFC23D)),
        HINT(Color(0xFF4FE3C1)),
        BLOCKED(Color(0xFFFF4D4D)),
        TUTORIAL(TutorialHighlight),
    }

    private val DimVeil = Color.Black.copy(alpha = 0.34f)
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }

    /** How far the selected tile is raised off the board, for a tile of [width]. */
    fun selectionLift(width: Float): Float = width * 0.06f

    /**
     * Draws [highlight] around the tile at [x], [y], which was drawn with the same [lift].
     * [intensity] fades it, for pulsing hints.
     */
    fun drawHighlight(
        scope: DrawScope,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        highlight: Highlight,
        lift: Float = 0f,
        intensity: Float = 1f,
    ) {
        val left = x - lift
        val top = y - lift
        val depthX = width * 0.08f
        val depthY = height * 0.08f
        val cornerR = width * 0.085f
        val alpha = intensity.coerceIn(0f, 1f)

        // Soft glow around the whole tile, its sides included
        val glowWidth = width * 0.07f
        val pad = width * 0.015f
        glowPaint.strokeWidth = glowWidth
        glowPaint.maskFilter = blurFilter(width * 0.06f)
        glowPaint.color = highlight.color.copy(alpha = 0.85f * alpha).toArgb()
        scope.drawContext.canvas.nativeCanvas.drawRoundRect(
            left - pad, top - pad,
            left + width + depthX + pad, top + height + depthY + pad,
            cornerR * 1.3f, cornerR * 1.3f,
            glowPaint,
        )

        // Crisp ring along the face edge, on a dark hairline so it stands out on light faces too
        val ringTopLeft = Offset(left - (width * 0.012f), top - (width * 0.012f))
        val ringSize = Size(width + (width * 0.024f), height + (width * 0.024f))
        scope.drawRoundRect(
            color = Color.Black.copy(alpha = 0.55f * alpha),
            topLeft = ringTopLeft,
            size = ringSize,
            cornerRadius = CornerRadius(cornerR * 1.1f),
            style = Stroke(width = width * 0.05f),
        )
        scope.drawRoundRect(
            color = highlight.color.copy(alpha = alpha),
            topLeft = ringTopLeft,
            size = ringSize,
            cornerRadius = CornerRadius(cornerR * 1.1f),
            style = Stroke(width = width * 0.028f),
        )
        // A faint wash of the colour on the face
        scope.drawRoundRect(
            color = highlight.color.copy(alpha = 0.12f * alpha),
            topLeft = Offset(left, top),
            size = Size(width, height),
            cornerRadius = CornerRadius(cornerR),
        )
    }

    private fun blurFilter(radius: Float): BlurMaskFilter? {
        val key = (radius * 2f).roundToInt()
        if (key <= 0) return null
        return blurFilters.getOrPut(key) { BlurMaskFilter(key / 2f, BlurMaskFilter.Blur.NORMAL) }
    }

    /** Dark edge around the tile's outermost layer. */
    private fun drawTileOutline(scope: DrawScope, topLeft: Offset, size: Size, corner: CornerRadius, width: Float) {
        scope.drawRoundRect(
            color = Color.Black.copy(alpha = 0.22f),
            topLeft = topLeft,
            size = size,
            cornerRadius = corner,
            style = Stroke(width = width * 0.012f),
        )
    }

    /** Plain sRGB blend; cheaper than Compose's lerp, which converts to Oklab. */
    private fun mix(a: Color, b: Color, t: Float): Color = Color(
        red = a.red + ((b.red - a.red) * t),
        green = a.green + ((b.green - a.green) * t),
        blue = a.blue + ((b.blue - a.blue) * t),
        alpha = a.alpha + ((b.alpha - a.alpha) * t),
    )

    private inline fun DrawScope.inTileSpace(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        block: DrawScope.() -> Unit,
    ) {
        withTransform(
            transformBlock = {
                translate(x, y)
                scale(width / TileSpace.WIDTH, height / TileSpace.HEIGHT, Offset.Zero)
            },
            drawBlock = block,
        )
    }

    /**
     * Rubbed-off paint and the surface texture of the material. The tile id picks the variant and
     * whether it is mirrored, so a tile keeps the same marks for the whole game.
     */
    private fun DrawScope.drawTextures(look: TileLook, textures: TileTextures, tileId: Int) {
        val hash = tileId * -0x61c88647 // golden ratio hash spreads consecutive ids
        val variant = (hash ushr 16) % textures.variantCount
        val flipX = ((hash ushr 4) and 1) == 1
        val flipY = ((hash ushr 5) and 1) == 1
        withTransform(
            transformBlock = {
                if (flipX || flipY) {
                    scale(if (flipX) -1f else 1f, if (flipY) -1f else 1f, LocalFaceCenter)
                }
            },
            drawBlock = {
                // Where the paint is gone, the face colour shows again: "lighten" replaces the
                // darker ink but leaves the (already lighter) face untouched
                textures.rubOff?.getOrNull(variant)?.let { rubOff ->
                    drawRoundRect(
                        brush = rubOff,
                        topLeft = LocalFaceTopLeft,
                        size = LocalFaceSize,
                        cornerRadius = LocalFaceCorner,
                        colorFilter = look.rubOffFilter,
                        blendMode = BlendMode.Lighten,
                    )
                }
                textures.surface?.getOrNull(variant)?.let { surface ->
                    drawRoundRect(
                        brush = surface,
                        topLeft = LocalFaceTopLeft,
                        size = LocalFaceSize,
                        cornerRadius = LocalFaceCorner,
                        blendMode = BlendMode.Multiply,
                    )
                }
            },
        )
    }

    private fun getTileVector(tile: Tile): ImageVector? = when (tile.suit) {
        TileSuit.CHARACTER -> when (tile.value) {
            1 -> MahjongTiles.`08Characters1`
            2 -> MahjongTiles.`09Characters2`
            3 -> MahjongTiles.`10Characters3`
            4 -> MahjongTiles.`11Characters4`
            5 -> MahjongTiles.`12Characters5`
            6 -> MahjongTiles.`13Characters6`
            7 -> MahjongTiles.`14Characters7`
            8 -> MahjongTiles.`15Characters8`
            9 -> MahjongTiles.`16Characters9`
            else -> null
        }
        TileSuit.CIRCLE -> when (tile.value) {
            1 -> MahjongTiles.`17Circles1`
            2 -> MahjongTiles.`18Circles2`
            3 -> MahjongTiles.`19Circles3`
            4 -> MahjongTiles.`20Circles4`
            5 -> MahjongTiles.`21Circles5`
            6 -> MahjongTiles.`22Circles6`
            7 -> MahjongTiles.`23Circles7`
            8 -> MahjongTiles.`24Circles8`
            9 -> MahjongTiles.`25Circles9`
            else -> null
        }
        TileSuit.BAMBOO -> when (tile.value) {
            1 -> MahjongTiles.`26Bamboos1`
            2 -> MahjongTiles.`27Bamboos2`
            3 -> MahjongTiles.`28Bamboos3`
            4 -> MahjongTiles.`29Bamboos4`
            5 -> MahjongTiles.`30Bamboos5`
            6 -> MahjongTiles.`31Bamboos6`
            7 -> MahjongTiles.`32Bamboos7`
            8 -> MahjongTiles.`33Bamboos8`
            9 -> MahjongTiles.`34Bamboos9`
            else -> null
        }
        TileSuit.WIND -> when (tile.value) {
            1 -> MahjongTiles.`04EastWind`
            2 -> MahjongTiles.`05SouthWind`
            3 -> MahjongTiles.`06WestWind`
            4 -> MahjongTiles.`07NorthWind`
            else -> null
        }
        TileSuit.DRAGON -> when (tile.value) {
            1 -> MahjongTiles.`03RedDragon`
            2 -> MahjongTiles.`02GreenDragon`
            3 -> MahjongTiles.`01WhiteDragon`
            else -> null
        }
        TileSuit.SEASON -> when (tile.value) {
            1 -> MahjongTiles.`35Spring`
            2 -> MahjongTiles.`36Summer`
            3 -> MahjongTiles.`37Autumn`
            4 -> MahjongTiles.`38Winter`
            else -> null
        }
        TileSuit.FLOWER -> when (tile.value) {
            1 -> MahjongTiles.`39Plum`
            2 -> MahjongTiles.`40Orchid`
            3 -> MahjongTiles.`41Chrysanthemum`
            4 -> MahjongTiles.`42Bamboo`
            else -> null
        }
    }

    // ==========================================
    // CORNER INDEX (Top-Left Red Notation)
    // ==========================================
    private fun drawCornerIndex(
        scope: DrawScope,
        tile: Tile,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        look: TileLook,
        engraveOffset: Float,
    ) {
        val label = when (tile.suit) {
            TileSuit.CHARACTER, TileSuit.CIRCLE, TileSuit.BAMBOO -> tile.value.toString()
            TileSuit.WIND -> when (tile.value) {
                1 -> "E"
                2 -> "S"
                3 -> "W"
                4 -> "N"
                else -> ""
            }
            TileSuit.SEASON -> when (tile.value) {
                1 -> "SPR"
                2 -> "SUM"
                3 -> "AUT"
                4 -> "WIN"
                else -> ""
            }
            TileSuit.FLOWER -> ""
            TileSuit.DRAGON -> when (tile.value) {
                1 -> "C"
                2 -> "F"
                3 -> "P"
                else -> ""
            }
        }

        if (label.isEmpty()) return

        cornerPaint.textSize = height * 0.12f

        val cornerX = x + (width * 0.08f)
        val cornerY = y + (height * 0.155f)
        val canvas = scope.drawContext.canvas.nativeCanvas
        if (engraveOffset > 0f) {
            cornerPaint.color = look.engravingShadowColor.toArgb()
            canvas.drawText(label, cornerX, cornerY, cornerPaint)
        }
        cornerPaint.color = look.cornerIndexColor.toArgb()
        canvas.drawText(label, cornerX + engraveOffset, cornerY + engraveOffset, cornerPaint)
    }


    // ==========================================
    // VECTOR DRAWING RENDERER & CACHE
    // ==========================================
    private sealed interface CachedVectorNode

    private class CachedGroupNode(
        val clipPath: Path?,
        val translationX: Float,
        val translationY: Float,
        val rotation: Float,
        val pivotX: Float,
        val pivotY: Float,
        val scaleX: Float,
        val scaleY: Float,
        val children: List<CachedVectorNode>,
    ) : CachedVectorNode

    private class CachedPathNode(
        val path: Path,
        val fill: Brush?,
        val fillAlpha: Float,
        val stroke: Brush?,
        val strokeAlpha: Float,
        // Created once here instead of on every frame
        val strokeStyle: Stroke?,
    ) : CachedVectorNode

    private val vectorCache = ConcurrentHashMap<ImageVector, CachedGroupNode>()

    private fun getOrCreateCachedVector(imageVector: ImageVector): CachedGroupNode {
        return vectorCache.computeIfAbsent(imageVector) { vector ->
            vector.root.toCachedGroup()
        }
    }

    private fun VectorGroup.toCachedGroup(): CachedGroupNode {
        val clipPath = if (clipPathData.isNotEmpty()) {
            clipPathData.toPath()
        } else null
        val childrenNodes = map { node ->
            when (node) {
                is VectorGroup -> node.toCachedGroup()
                is VectorPath -> {
                    val path = node.pathData.toPath()
                    path.fillType = node.pathFillType
                    CachedPathNode(
                        path = path,
                        fill = node.fill,
                        fillAlpha = node.fillAlpha,
                        stroke = node.stroke,
                        strokeAlpha = node.strokeAlpha,
                        strokeStyle = node.stroke?.let {
                            Stroke(
                                width = node.strokeLineWidth,
                                cap = node.strokeLineCap,
                                join = node.strokeLineJoin,
                                miter = node.strokeLineMiter,
                            )
                        },
                    )
                }
            }
        }
        return CachedGroupNode(
            clipPath = clipPath,
            translationX = translationX,
            translationY = translationY,
            rotation = rotation,
            pivotX = pivotX,
            pivotY = pivotY,
            scaleX = scaleX,
            scaleY = scaleY,
            children = childrenNodes
        )
    }

    private fun drawCachedNode(scope: DrawScope, node: CachedVectorNode, colorFilter: ColorFilter?) {
        when (node) {
            is CachedGroupNode -> {
                scope.withTransform(
                    transformBlock = {
                        if ((node.translationX != 0f) || (node.translationY != 0f)) {
                            translate(node.translationX, node.translationY)
                        }
                        if (node.rotation != 0f) {
                            rotate(node.rotation, Offset(node.pivotX, node.pivotY))
                        }
                        if ((node.scaleX != 1f) || (node.scaleY != 1f)) {
                            scale(node.scaleX, node.scaleY, Offset(node.pivotX, node.pivotY))
                        }
                        if (node.clipPath != null) {
                            clipPath(node.clipPath)
                        }
                    },
                    drawBlock = {
                        for (child in node.children) {
                            drawCachedNode(this, child, colorFilter)
                        }
                    }
                )
            }
            is CachedPathNode -> {
                if (node.fill != null) {
                    scope.drawPath(
                        path = node.path,
                        brush = node.fill,
                        alpha = node.fillAlpha,
                        colorFilter = colorFilter,
                    )
                }
                if ((node.stroke != null) && (node.strokeStyle != null)) {
                    scope.drawPath(
                        path = node.path,
                        brush = node.stroke,
                        alpha = node.strokeAlpha,
                        style = node.strokeStyle,
                        colorFilter = colorFilter,
                    )
                }
            }
        }
    }

    private fun drawVectorTile(
        scope: DrawScope,
        imageVector: ImageVector,
        cx: Float,
        cy: Float,
        maxW: Float,
        maxH: Float,
        colorFilter: ColorFilter?,
    ) {
        val scale = minOf(maxW / imageVector.viewportWidth, maxH / imageVector.viewportHeight)
        val targetW = imageVector.viewportWidth * scale
        val targetH = imageVector.viewportHeight * scale
        val drawX = cx - (targetW * 0.5f)
        val drawY = cy - (targetH * 0.5f)

        val cached = getOrCreateCachedVector(imageVector)
        scope.withTransform(
            transformBlock = {
                translate(drawX, drawY)
                scale(scale, scale, Offset.Zero)
            },
            drawBlock = {
                drawCachedNode(this, cached, colorFilter)
            }
        )
    }
}
