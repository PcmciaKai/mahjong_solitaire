package com.vortessence.mahjong.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Matrix
import android.graphics.Shader
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.core.graphics.createBitmap
import com.vortessence.mahjong.data.model.TableSurface
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Draws the playing surfaces as vector drawings, laid out symmetrically around the centre of the
 * board.
 *
 * Sizes are given in units of `unit`: 1/400 of the shorter side, so a surface keeps its
 * proportions on any screen and in the small previews in the settings.
 */
object TableSurfaceRenderer {

    @Suppress("UNUSED_PARAMETER")
    fun drawTableSurface(
        scope: DrawScope,
        surface: TableSurface,
        width: Float,
        height: Float,
        context: Context? = null,
    ) {
        val unit = min(width, height) / 400f
        // Patterns are laid out from the centre and may run past the edges
        scope.clipRect(0f, 0f, width, height) {
            when (surface) {
                TableSurface.GREEN_MAT -> drawFeltMat(this, width, height, unit, GreenFelt)
                TableSurface.BLUE_MAT -> drawFeltMat(this, width, height, unit, BlueFelt)
                TableSurface.LEATHER_MAT -> drawLeatherMat(this, width, height, unit)
                TableSurface.CARD_TABLE -> drawCardTable(this, width, height, unit)
            }
        }
    }

    // =========================================================================
    // SHARED HELPERS
    // =========================================================================

    /** Base colour lit from the middle and falling off toward the edges. */
    private fun DrawScope.radialBase(width: Float, height: Float, center: Color, mid: Color, edge: Color) {
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(center, mid, edge),
                center = Offset(width * 0.5f, height * 0.45f),
                radius = max(width, height) * 0.85f,
            ),
        )
    }

    /** A rectangle [inset] from every edge. */
    private fun DrawScope.insetRect(inset: Float): Rect = Rect(inset, inset, size.width - inset, size.height - inset)

    private fun dashes(unit: Float, on: Float = 3.7f, off: Float = 2.2f) =
        PathEffect.dashPathEffect(floatArrayOf(on * unit, off * unit), 0f)

    /** A frame between two rounded rectangles, for borders and rails. */
    private fun framePath(outer: Rect, inner: Rect, outerRadius: Float, innerRadius: Float): Path = Path().apply {
        fillType = PathFillType.EvenOdd
        addRoundRect(RoundRect(outer, CornerRadius(outerRadius)))
        addRoundRect(RoundRect(inner, CornerRadius(innerRadius)))
    }

    /** A rounded rectangle outline [inset] from every edge. */
    private fun DrawScope.insetOutline(inset: Float, radius: Float, color: Color, width: Float, dashes: PathEffect? = null) {
        drawRoundRect(
            color = color,
            topLeft = Offset(inset, inset),
            size = Size(size.width - (inset * 2f), size.height - (inset * 2f)),
            cornerRadius = CornerRadius(max(0f, radius)),
            style = Stroke(width = width, pathEffect = dashes),
        )
    }

    /**
     * Runs [block] four times, mirrored into each corner. Draw for the top-left corner; the
     * others are reflections of it, which keeps every ornament perfectly symmetrical.
     */
    private inline fun DrawScope.inEachCorner(block: DrawScope.() -> Unit) {
        val center = Offset(size.width / 2f, size.height / 2f)
        block()
        scale(-1f, 1f, center) { block() }
        scale(1f, -1f, center) { block() }
        scale(-1f, -1f, center) { block() }
    }

    // =========================================================================
    // FELT
    // Felt has no weave: it is a mat of tangled fibres. A small, seamless noise texture gives
    // it that soft, mottled look; it is grey around 50 % and overlaid, so it keeps the colour.
    // =========================================================================

    private const val FELT_TEXTURE_SIZE = 256

    private val feltTexture: Bitmap by lazy { createFeltTexture() }

    private fun createFeltTexture(): Bitmap {
        val size = FELT_TEXTURE_SIZE
        val random = Random(0x46454C54)
        val mottle = tileableNoise(cells = 6, random)
        val tufts = tileableNoise(cells = 32, random)
        val pixels = IntArray(size * size) { i ->
            // Mostly soft, slow variation; only a hint of per-fibre speckle
            val v = 0.5f +
                (0.06f * (mottle[i] - 0.5f)) +
                (0.05f * (tufts[i] - 0.5f)) +
                (0.045f * (random.nextFloat() - 0.5f))
            val c = (v.coerceIn(0f, 1f) * 255f).toInt()
            android.graphics.Color.argb(255, c, c, c)
        }
        return createBitmap(size, size).apply { setPixels(pixels, 0, size, 0, 0, size, size) }
    }

    /** Smooth noise that wraps around at the edges, so the texture repeats without seams. */
    private fun tileableNoise(cells: Int, random: Random): FloatArray {
        val size = FELT_TEXTURE_SIZE
        val grid = FloatArray(cells * cells) { random.nextFloat() }
        val cell = size / cells.toFloat()
        fun smooth(t: Float) = t * t * (3f - (2f * t))
        return FloatArray(size * size) { i ->
            val fx = (i % size) / cell
            val fy = (i / size) / cell
            val x0 = fx.toInt()
            val y0 = fy.toInt()
            val tx = smooth(fx - x0)
            val ty = smooth(fy - y0)
            val x1 = (x0 + 1) % cells
            val y1 = (y0 + 1) % cells
            val top = grid[(y0 * cells) + x0] + ((grid[(y0 * cells) + x1] - grid[(y0 * cells) + x0]) * tx)
            val bottom = grid[(y1 * cells) + x0] + ((grid[(y1 * cells) + x1] - grid[(y1 * cells) + x0]) * tx)
            top + ((bottom - top) * ty)
        }
    }

    /** Overlays the felt fibres; [strength] 1 is the full texture. */
    private fun DrawScope.feltFibres(unit: Float, strength: Float) {
        val shader = BitmapShader(feltTexture, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT).apply {
            // A texture pixel is a bit larger than a screen pixel, so filtering softens the fibres;
            // tied to the unit, so they keep their size relative to the mat
            setLocalMatrix(Matrix().apply { setScale(unit * 0.65f, unit * 0.65f) })
        }
        drawRect(brush = ShaderBrush(shader), alpha = strength, blendMode = BlendMode.Overlay)
    }

    private class FeltColors(
        val center: Color,
        val mid: Color,
        val edge: Color,
        val binding: Color,
        val thread: Color,
    )

    private val GreenFelt = FeltColors(
        center = Color(0xFF16604D),
        mid = Color(0xFF0D4536),
        edge = Color(0xFF082C22),
        binding = Color(0xFF05231B),
        thread = Color(0xFF5FA88B),
    )

    private val BlueFelt = FeltColors(
        center = Color(0xFF2B62A3),
        mid = Color(0xFF1C487F),
        edge = Color(0xFF0F2A4E),
        binding = Color(0xFF0A1C36),
        thread = Color(0xFF7FA6D8),
    )

    /**
     * A felt mat: soft fibres and a slightly raised, bound edge with a tone-on-tone stitch, the
     * way tournament and home mats are finished.
     */
    private fun drawFeltMat(scope: DrawScope, width: Float, height: Float, unit: Float, colors: FeltColors) =
        with(scope) {
            radialBase(width, height, colors.center, colors.mid, colors.edge)

            // Binding tape around the edge
            val binding = 7f * unit
            val innerRadius = 4f * unit
            drawPath(framePath(insetRect(0f), insetRect(binding), 0f, innerRadius), colors.binding)
            // The tape is raised: it catches light on its inner edge and shades the felt next to it
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.28f),
                topLeft = Offset(binding + (0.8f * unit), binding + (0.8f * unit)),
                size = Size(width - (binding * 2f), height - (binding * 2f)),
                cornerRadius = CornerRadius(innerRadius),
                style = Stroke(width = 2f * unit),
            )
            insetOutline(binding - (0.6f * unit), innerRadius + (0.6f * unit), colors.thread.copy(alpha = 0.18f), 0.6f * unit)
            // Stitch running along the middle of the tape
            insetOutline(
                binding * 0.5f,
                innerRadius * 0.6f,
                colors.thread.copy(alpha = 0.5f),
                0.5f * unit,
                dashes(unit, 2.8f, 1.6f),
            )

            // Fibres over everything, the tape included
            feltFibres(unit, strength = 0.6f)
        }

    // =========================================================================
    // LEATHER MAT
    // =========================================================================

    private val LeatherGold = Color(0xFFC9A55A)

    /**
     * A dark leather mat with a satin sheen, burnished edges, a saddle stitch, a blind-tooled
     * frame and gilt ornaments in the corners, pressed into the leather like bookbinding.
     */
    private fun drawLeatherMat(scope: DrawScope, width: Float, height: Float, unit: Float) = with(scope) {
        // Dark brown, going toward black at the edges
        radialBase(width, height, Color(0xFF4A3222), Color(0xFF2C1C12), Color(0xFF120B07))
        // Satin sheen: a broad, soft reflection, as smooth leather has no gloss spot
        drawRect(
            brush = Brush.linearGradient(
                0f to Color(0xFFFFE2C0).copy(alpha = 0.07f),
                0.5f to Color(0xFFFFE2C0).copy(alpha = 0f),
                start = Offset.Zero,
                end = Offset(width * 0.8f, height * 0.8f),
            ),
        )

        // Burnished edge: the cut edge is dyed darker and polished
        val radius = 8f * unit
        val edge = 3f * unit
        drawPath(framePath(insetRect(0f), insetRect(edge), 0f, radius), Color(0xFF0B0604))
        insetOutline(edge, radius, Color(0xFFFFE2C0).copy(alpha = 0.1f), 0.6f * unit)

        // Saddle stitch
        insetOutline(
            7.5f * unit,
            radius - (4f * unit),
            Color(0xFFB8925E).copy(alpha = 0.75f),
            0.7f * unit,
            dashes(unit, 3f, 1.8f),
        )

        // Blind-tooled frame: a line pressed into the leather, dark in the groove with a light
        // lip on its lower-right side
        val frame = 16f * unit
        insetOutline(frame + (0.5f * unit), 0f, Color(0xFFFFE2C0).copy(alpha = 0.08f), 0.8f * unit)
        insetOutline(frame, 0f, Color.Black.copy(alpha = 0.45f), 0.8f * unit)
        // Gilt rule just inside it
        val rule = frame + (4f * unit)
        insetOutline(rule, 0f, LeatherGold.copy(alpha = 0.7f), 0.6f * unit)

        // Gilt corner ornaments, sitting on the corners of the gilt rule
        val ornament = cornerOrnament(Offset(rule, rule), unit)
        val ornamentStroke = Stroke(width = 0.9f * unit, cap = StrokeCap.Round, join = StrokeJoin.Round)
        inEachCorner {
            // Pressed in: a dark impression slightly down and to the right, then the gold
            translate(0.5f * unit, 0.6f * unit) {
                drawPath(ornament.lines, Color.Black.copy(alpha = 0.5f), style = ornamentStroke)
                drawPath(ornament.fills, Color.Black.copy(alpha = 0.5f), style = Fill)
            }
            drawPath(ornament.lines, LeatherGold, style = ornamentStroke)
            drawPath(ornament.fills, LeatherGold, style = Fill)
        }
    }

    private class Ornament(val lines: Path, val fills: Path)

    /**
     * The top-left corner ornament, at [corner]: a leaf pointing into the mat and two scrolls
     * running along the edges, curling inward, with dots and small leaves. Every element along
     * one edge is mirrored along the other, so the ornament is symmetrical around the diagonal.
     */
    private fun cornerOrnament(corner: Offset, unit: Float): Ornament {
        val lines = Path()
        val fills = Path()

        // a runs along the edge, b goes into the mat; [diagonal] swaps them for the other edge
        for (diagonal in listOf(false, true)) {
            fun p(a: Float, b: Float): Offset =
                if (diagonal) corner + Offset(b * unit, a * unit) else corner + Offset(a * unit, b * unit)
            fun moveTo(a: Float, b: Float) = p(a, b).let { lines.moveTo(it.x, it.y) }
            fun cubic(a1: Float, b1: Float, a2: Float, b2: Float, a3: Float, b3: Float) {
                val c1 = p(a1, b1)
                val c2 = p(a2, b2)
                val e = p(a3, b3)
                lines.cubicTo(c1.x, c1.y, c2.x, c2.y, e.x, e.y)
            }

            // Main scroll along the edge, ending in an inward curl
            moveTo(9f, 7f)
            cubic(16f, 1.5f, 27f, 1.5f, 35f, 6f)
            cubic(40f, 9f, 37f, 15f, 32f, 13f)
            cubic(28.5f, 11.5f, 30f, 7.5f, 33f, 8.5f)
            // Smaller counter-scroll beneath it
            moveTo(9f, 7f)
            cubic(13f, 13f, 21f, 15f, 24f, 11f)
            cubic(25.5f, 9f, 23f, 7.5f, 21.5f, 9f)
            // A short tendril out to a dot further along the edge
            moveTo(35f, 6f)
            cubic(40f, 3f, 45f, 3f, 48f, 4.5f)

            // Leaf sprouting from the main scroll, pointing along the edge
            leaf(fills, from = p(18f, 3.6f), to = p(25f, 0.8f), width = 1.6f * unit)
            // Dot at the end of the tendril
            p(50.5f, 5f).let { fills.addOval(Rect(it, 1.4f * unit)) }
        }

        // Leaf on the diagonal, pointing into the mat, and a dot beyond its tip
        val tip = corner + Offset(17f * unit, 17f * unit)
        val base = corner + Offset(3f * unit, 3f * unit)
        fills.moveTo(base.x, base.y)
        fills.quadraticTo(corner.x + (15f * unit), corner.y + (5f * unit), tip.x, tip.y)
        fills.quadraticTo(corner.x + (5f * unit), corner.y + (15f * unit), base.x, base.y)
        fills.close()
        (corner + Offset(21.5f * unit, 21.5f * unit)).let { fills.addOval(Rect(it, 1.6f * unit)) }

        return Ornament(lines, fills)
    }

    /** A pointed leaf from [from] to [to], [width] wide at its middle. */
    private fun leaf(path: Path, from: Offset, to: Offset, width: Float) {
        val direction = to - from
        val length = direction.getDistance()
        if (length == 0f) return
        // Perpendicular to the leaf; quadratic control points sit twice as far as the curve bulges
        val normal = Offset(-direction.y, direction.x) * (width / length)
        val mid = from + (direction * 0.5f)
        val side1 = mid + normal
        val side2 = mid - normal
        path.moveTo(from.x, from.y)
        path.quadraticTo(side1.x, side1.y, to.x, to.y)
        path.quadraticTo(side2.x, side2.y, from.x, from.y)
        path.close()
    }

    // =========================================================================
    // CARD TABLE
    // =========================================================================

    /** Burgundy felt in a padded, stitched leather rail with a brass bead along the felt. */
    private fun drawCardTable(scope: DrawScope, width: Float, height: Float, unit: Float) = with(scope) {
        radialBase(width, height, Color(0xFF842838), Color(0xFF5E1726), Color(0xFF380C16))
        feltFibres(unit, strength = 0.6f)

        val rail = 20f * unit
        val radius = 16f * unit
        val outer = insetRect(0f)
        val inner = insetRect(rail)

        // Leather, rounded like a padded roll: darker at both edges, lighter along the top
        drawPath(framePath(outer, inner, radius, radius - rail * 0.5f), Color(0xFF3E2618))
        val bands = listOf(
            0.5f to Color(0xFF5A3A26),
            0.36f to Color(0xFF6E4A32),
            0.22f to Color(0xFF7C5639),
        )
        for ((fraction, color) in bands) {
            val inset = rail * (0.5f - (fraction / 2f))
            drawRoundRect(
                color = color,
                topLeft = Offset(inset, inset),
                size = Size(width - (inset * 2f), height - (inset * 2f)),
                cornerRadius = CornerRadius(radius - inset * 0.5f),
                style = Stroke(width = rail * fraction),
            )
        }
        // Stitching along both sides of the roll
        for (inset in listOf(rail * 0.18f, rail * 0.82f)) {
            insetOutline(
                inset,
                max(1f, radius - inset * 0.5f),
                Color(0xFFD9B57C).copy(alpha = 0.6f),
                0.45f * unit,
                dashes(unit, 2.6f, 1.8f),
            )
        }
        // Brass bead between rail and felt, and the rail's shadow on the felt
        drawRoundRect(
            color = Color.Black.copy(alpha = 0.3f),
            topLeft = inner.topLeft + Offset(unit, unit),
            size = inner.size,
            cornerRadius = CornerRadius(radius - rail * 0.5f),
            style = Stroke(width = 2.5f * unit),
        )
        drawRoundRect(
            color = Color(0xFFC49A45),
            topLeft = inner.topLeft,
            size = inner.size,
            cornerRadius = CornerRadius(radius - rail * 0.5f),
            style = Stroke(width = 0.9f * unit),
        )
    }
}
