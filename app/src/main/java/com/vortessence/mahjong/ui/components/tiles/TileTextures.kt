package com.vortessence.mahjong.ui.components.tiles

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.core.graphics.createBitmap
import com.vortessence.mahjong.data.model.TileSet
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

/**
 * Generated face textures of one tile set, in a few variants so neighbouring tiles differ.
 *
 * - [surface]: near-white, multiplied over the face: the material's grain plus, for worn tiles,
 *   grime, discolouring, scratches, cracks, dents and chips.
 * - [rubOff]: white with alpha where the paint has worn off; drawn with the face colour in
 *   "lighten" mode, so it removes ink but leaves the face itself untouched.
 */
class TileTextures internal constructor(
    val set: TileSet,
    val worn: Boolean,
    val surface: List<ShaderBrush>?,
    val rubOff: List<ShaderBrush>?,
) {
    val variantCount: Int = max(surface?.size ?: 0, rubOff?.size ?: 0)
}

/**
 * Keeps the textures of the sets in use. The board uses full-size textures of the selected set;
 * the settings screen small ones of every set.
 */
object TileTextureCache {

    /** Texture width used on the board; the tile renderer draws faces in this coordinate space. */
    const val BOARD_WIDTH = TileTextureGenerator.BASE_WIDTH
    const val BOARD_VARIANTS = 4
    const val PREVIEW_WIDTH = 96
    const val PREVIEW_VARIANTS = 1

    private data class Key(val set: TileSet, val worn: Boolean, val width: Int)

    private const val MAX_ENTRIES = 24
    private val cache = object : LinkedHashMap<Key, TileTextures>(16, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Key, TileTextures>?): Boolean =
            size > MAX_ENTRIES
    }

    fun cachedOrNull(set: TileSet, worn: Boolean, width: Int): TileTextures? =
        synchronized(cache) { cache[Key(set, worn, width)] }

    /** Generates the textures if needed; call this off the main thread. */
    fun get(set: TileSet, worn: Boolean, width: Int, variants: Int): TileTextures {
        val key = Key(set, worn, width)
        cachedOrNull(set, worn, width)?.let { return it }
        val textures = TileTextureGenerator.generate(TileMaterials.of(set), worn, width, variants)
        synchronized(cache) { cache[key] = textures }
        return textures
    }
}

object TileTextureGenerator {

    /** The renderer's tile space; textures of other widths are scaled to it. */
    const val BASE_WIDTH = 192
    const val BASE_HEIGHT = 256

    // A fixed seed keeps the look identical between launches
    private const val SEED = 0x4D4A4E47

    fun generate(material: TileMaterial, worn: Boolean, width: Int, variants: Int): TileTextures {
        val wear = material.worn
        // Worn tiles always have a surface texture, so rub-off never comes alone
        val needsSurface = (material.grain != GrainKind.NONE) || worn
        val needsRubOff = worn && (wear.rubOff > 0f)
        if (!needsSurface) return TileTextures(material.set, worn, null, null)

        val height = (width * BASE_HEIGHT) / BASE_WIDTH
        val random = Random(SEED + (material.set.ordinal * 7919) + (if (worn) 1 else 0))
        val surfaces = mutableListOf<ShaderBrush>()
        val rubOffs = mutableListOf<ShaderBrush>()
        repeat(variants) {
            surfaces += brushOf(surfaceBitmap(material, worn, width, height, random), width)
            if (needsRubOff) rubOffs += brushOf(rubOffBitmap(wear, width, height, random), width)
        }
        return TileTextures(
            set = material.set,
            worn = worn,
            surface = surfaces.takeIf { it.isNotEmpty() },
            rubOff = rubOffs.takeIf { it.isNotEmpty() },
        )
    }

    private fun brushOf(bitmap: Bitmap, width: Int): ShaderBrush {
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val scale = BASE_WIDTH / width.toFloat()
        if (scale != 1f) shader.setLocalMatrix(Matrix().apply { setScale(scale, scale) })
        return ShaderBrush(shader)
    }

    // =========================================================================
    // SURFACE: grain and wear, multiplied over the face
    // =========================================================================

    private fun surfaceBitmap(material: TileMaterial, worn: Boolean, w: Int, h: Int, random: Random): Bitmap {
        // All sizes below are in base tile pixels and scaled by s
        val s = w / BASE_WIDTH.toFloat()
        val wear = material.worn
        val wearLevel = 0.75f + (random.nextFloat() * 0.5f)

        val coarse = valueNoise(w, h, 32f * s, 32f * s, random)
        val fine = valueNoise(w, h, 7f * s, 7f * s, random)
        val blotches = if (worn) valueNoise(w, h, 56f * s, 56f * s, random) else null
        val grain = grainField(material.grain, w, h, s, coarse, fine, random)

        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                val i = (y * w) + x
                var grainDark = grain?.get(i) ?: 0f
                var dirt = 0f
                var yellow = 0f
                if (worn) {
                    val edgeX = min(x, w - 1 - x) / s
                    val edgeY = min(y, h - 1 - y) / s
                    dirt += wear.grime * wearLevel * exp(-min(edgeX, edgeY) / 6f)
                    dirt += wear.grime * 0.55f * wearLevel * exp(-(edgeX + edgeY) / 18f)
                    dirt += (0.02f * coarse[i]) + (0.012f * fine[i])
                    yellow = wear.yellowing * (0.02f + (0.06f * blotches!![i]))
                    grainDark *= 1.25f
                }
                if (worn || (grain != null)) grainDark += 0.012f * random.nextFloat()

                // Grain darkens neutrally, dirt is brownish, ageing yellows (blue drops fastest)
                pixels[i] = argb(
                    r = 1f - (grainDark * 0.92f) - (dirt * 0.55f),
                    g = 1f - grainDark - (dirt * 0.85f) - (yellow * 0.3f),
                    b = 1f - (grainDark * 1.08f) - (dirt * 1.4f) - yellow,
                )
            }
        }

        val bitmap = createBitmap(w, h)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        val canvas = Canvas(bitmap)
        if (material.grain == GrainKind.BONE_PORES) drawPores(canvas, random, w, h, s, worn)
        if (worn) {
            drawStains(canvas, random, w, h, s, wear.stains)
            drawDents(canvas, random, w, h, s, wear.dents)
            drawScratches(canvas, random, w, h, s, wear.scratches, wear.scratchStrength * wearLevel)
            drawCracks(canvas, random, w, h, s, wear.cracks)
            drawChips(canvas, random, w, h, s, wear.chips)
        }
        return bitmap
    }

    /** Darkness of the material's own pattern per pixel, or null for plain materials. */
    private fun grainField(
        kind: GrainKind,
        w: Int,
        h: Int,
        s: Float,
        coarse: FloatArray,
        fine: FloatArray,
        random: Random,
    ): FloatArray? = when (kind) {
        GrainKind.NONE -> null

        // Soft, slightly cloudy bone; the pores are drawn on top
        GrainKind.BONE_PORES -> FloatArray(w * h) { i -> (0.03f * coarse[i]) + (0.018f * fine[i]) }

        // Fine, wavy lines running down the tile, imitating ivory
        GrainKind.CELLULOID_LINES -> {
            val warp = valueNoise(w, h, 40f * s, 90f * s, random)
            FloatArray(w * h) { i ->
                val x = i % w
                val t = (x + (warp[i] * 14f * s)) / (4.5f * s)
                val stripe = (0.5f + (0.5f * cos(2f * PI.toFloat() * t))).pow(8)
                (0.045f * stripe * (0.5f + fine[i])) + (0.015f * coarse[i])
            }
        }

        // Growth rings with a dark latewood edge, plus long fibres
        GrainKind.WOOD_GRAIN -> {
            val warp = valueNoise(w, h, 60f * s, 140f * s, random)
            val fibres = valueNoise(w, h, 1.6f * s, 28f * s, random)
            FloatArray(w * h) { i ->
                val x = i % w
                val y = i / w
                val t = (x + (warp[i] * 34f * s) + (y * 0.06f)) / (13f * s)
                val ring = (t - floor(t)).pow(4)
                (0.16f * ring) + (0.07f * fibres[i]) + (0.05f * coarse[i])
            }
        }

        // Cloudy stone with thin, branching veins
        GrainKind.STONE_VEINS -> {
            val mottle = valueNoise(w, h, 48f * s, 48f * s, random)
            val veinA = valueNoise(w, h, 70f * s, 70f * s, random)
            val veinB = valueNoise(w, h, 22f * s, 22f * s, random)
            FloatArray(w * h) { i ->
                val v = (veinA[i] * 0.75f) + (veinB[i] * 0.25f)
                val vein = 1f - smoothstep(0f, 0.04f, abs(v - 0.5f))
                (0.08f * mottle[i]) + (0.03f * fine[i]) + (0.06f * vein)
            }
        }
    }

    /** Small, dark, slightly elongated pores of bone, darker as dirt settles in. */
    private fun drawPores(canvas: Canvas, random: Random, w: Int, h: Int, s: Float, worn: Boolean) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            maskFilter = BlurMaskFilter(max(0.3f, 0.4f * s), BlurMaskFilter.Blur.NORMAL)
        }
        val rect = RectF()
        repeat(170) {
            val cx = random.nextFloat() * w
            val cy = random.nextFloat() * h
            val rx = (0.4f + (random.nextFloat() * 0.5f)) * s
            val ry = (0.8f + (random.nextFloat() * 1.4f)) * s
            val alpha = if (worn) 45 + random.nextInt(45) else 22 + random.nextInt(30)
            paint.color = android.graphics.Color.argb(alpha, 95, 70, 40)
            rect.set(cx - rx, cy - ry, cx + rx, cy + ry)
            canvas.drawOval(rect, paint)
        }
    }

    /** Faint, tea-coloured blotches. */
    private fun drawStains(canvas: Canvas, random: Random, w: Int, h: Int, s: Float, count: Int) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        repeat(count) {
            if (random.nextFloat() < 0.35f) return@repeat
            val cx = random.nextFloat() * w
            val cy = random.nextFloat() * h
            val radius = (18f + (random.nextFloat() * 40f)) * s
            val alpha = 10 + random.nextInt(14)
            paint.shader = RadialGradient(
                cx, cy, radius,
                intArrayOf(android.graphics.Color.argb(alpha, 150, 110, 50), android.graphics.Color.argb(0, 150, 110, 50)),
                null,
                Shader.TileMode.CLAMP,
            )
            canvas.drawCircle(cx, cy, radius, paint)
        }
    }

    /** Soft dents, as in a soft material that has been knocked about. */
    private fun drawDents(canvas: Canvas, random: Random, w: Int, h: Int, s: Float, count: Int) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        repeat(count) {
            val cx = random.nextFloat() * w
            val cy = random.nextFloat() * h
            val radius = (2.5f + (random.nextFloat() * 5f)) * s
            val alpha = 25 + random.nextInt(25)
            paint.shader = RadialGradient(
                cx, cy, radius,
                intArrayOf(android.graphics.Color.argb(alpha, 80, 55, 30), android.graphics.Color.argb(0, 80, 55, 30)),
                null,
                Shader.TileMode.CLAMP,
            )
            canvas.drawCircle(cx, cy, radius, paint)
        }
    }

    /** Scuffs, blurred so they look worn in rather than cut. */
    private fun drawScratches(canvas: Canvas, random: Random, w: Int, h: Int, s: Float, count: Int, strength: Float) {
        if (count <= 0) return
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            maskFilter = BlurMaskFilter(max(0.4f, 1.4f * s), BlurMaskFilter.Blur.NORMAL)
        }
        val actual = ((count * 0.6f) + (random.nextFloat() * count * 0.8f)).roundToInt()
        repeat(actual) {
            val x = random.nextFloat() * w
            val y = random.nextFloat() * h
            val angle = random.nextFloat() * PI.toFloat()
            val length = (10f + (random.nextFloat() * random.nextFloat() * 80f)) * s
            paint.strokeWidth = (0.9f + (random.nextFloat() * 1.1f)) * s
            val alpha = ((12 + random.nextInt(16)) * strength / 0.6f).toInt().coerceIn(0, 255)
            paint.color = android.graphics.Color.argb(alpha, 115, 92, 62)
            canvas.drawLine(x, y, x + (cos(angle) * length), y + (sin(angle) * length), paint)
        }
    }

    /** Hairline cracks running in from an edge, as in old, shrinking celluloid. */
    private fun drawCracks(canvas: Canvas, random: Random, w: Int, h: Int, s: Float, count: Int) {
        if (count <= 0) return
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeWidth = 0.8f * s
            maskFilter = BlurMaskFilter(max(0.3f, 0.35f * s), BlurMaskFilter.Blur.NORMAL)
        }
        val path = Path()
        repeat(random.nextInt(count + 1)) {
            // Start on a random edge, heading inward
            val side = random.nextInt(4)
            var x = when (side) { 0 -> 0f; 1 -> w.toFloat(); else -> random.nextFloat() * w }
            var y = when (side) { 2 -> 0f; 3 -> h.toFloat(); 0, 1 -> random.nextFloat() * h; else -> 0f }
            var angle = when (side) { 0 -> 0f; 1 -> PI.toFloat(); 2 -> PI.toFloat() / 2f; else -> -PI.toFloat() / 2f }
            path.reset()
            path.moveTo(x, y)
            repeat(5 + random.nextInt(6)) {
                angle += (random.nextFloat() - 0.5f) * 1.2f
                val step = (5f + (random.nextFloat() * 9f)) * s
                x += cos(angle) * step
                y += sin(angle) * step
                path.lineTo(x, y)
            }
            paint.color = android.graphics.Color.argb(60 + random.nextInt(50), 85, 65, 40)
            canvas.drawPath(path, paint)
        }
    }

    /** Small dings along the edges, most of them at the corners. */
    private fun drawChips(canvas: Canvas, random: Random, w: Int, h: Int, s: Float, count: Int) {
        if (count <= 0) return
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            maskFilter = BlurMaskFilter(max(0.3f, 0.8f * s), BlurMaskFilter.Blur.NORMAL)
        }
        repeat(count) {
            val atCorner = random.nextFloat() < 0.5f
            val inset = (1f + (random.nextFloat() * 3f)) * s
            val (cx, cy) = if (atCorner) {
                val cornerX = if (random.nextBoolean()) inset * 2.5f else w - (inset * 2.5f)
                val cornerY = if (random.nextBoolean()) inset * 2.5f else h - (inset * 2.5f)
                cornerX to cornerY
            } else when (random.nextInt(4)) {
                0 -> inset to (random.nextFloat() * h)
                1 -> (w - inset) to (random.nextFloat() * h)
                2 -> (random.nextFloat() * w) to inset
                else -> (random.nextFloat() * w) to (h - inset)
            }
            paint.color = android.graphics.Color.argb(35 + random.nextInt(40), 95, 78, 60)
            canvas.drawCircle(cx, cy, (1.2f + (random.nextFloat() * 2.8f)) * s, paint)
        }
    }

    // =========================================================================
    // RUB-OFF: where the paint is gone
    // =========================================================================

    private fun rubOffBitmap(wear: WearStyle, w: Int, h: Int, random: Random): Bitmap {
        val s = w / BASE_WIDTH.toFloat()
        val cell = 22f * s * wear.rubSpotScale
        val patches = valueNoise(w, h, cell, cell, random)
        val detail = valueNoise(w, h, cell / 3f, cell / 3f, random)
        // Small drop-outs only where the noise peaks; large patches cover more of the face
        val threshold = 0.66f - (0.08f * wear.rubSpotScale)
        val pixels = IntArray(w * h)
        for (i in pixels.indices) {
            val n = (patches[i] * 0.8f) + (detail[i] * 0.2f)
            val a = smoothstep(threshold, threshold + 0.1f, n) * wear.rubOff
            pixels[i] = android.graphics.Color.argb((a.coerceIn(0f, 1f) * 255f).toInt(), 255, 255, 255)
        }
        val bitmap = createBitmap(w, h)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        return bitmap
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    /** Smoothly interpolated random values in 0..1 on a grid of cells of the given size. */
    private fun valueNoise(w: Int, h: Int, cellW: Float, cellH: Float, random: Random): FloatArray {
        val cw = max(1.5f, cellW)
        val ch = max(1.5f, cellH)
        val gridW = (w / cw).toInt() + 2
        val gridH = (h / ch).toInt() + 2
        val grid = FloatArray(gridW * gridH) { random.nextFloat() }
        val out = FloatArray(w * h)
        for (y in 0 until h) {
            val fy = y / ch
            val gy = fy.toInt()
            val ty = smooth(fy - gy)
            for (x in 0 until w) {
                val fx = x / cw
                val gx = fx.toInt()
                val tx = smooth(fx - gx)
                val a = grid[(gy * gridW) + gx]
                val b = grid[(gy * gridW) + gx + 1]
                val c = grid[((gy + 1) * gridW) + gx]
                val d = grid[((gy + 1) * gridW) + gx + 1]
                val top = a + ((b - a) * tx)
                val bottom = c + ((d - c) * tx)
                out[(y * w) + x] = top + ((bottom - top) * ty)
            }
        }
        return out
    }

    private fun smooth(t: Float): Float = t * t * (3f - (2f * t))

    private fun smoothstep(edge0: Float, edge1: Float, x: Float): Float =
        smooth(((x - edge0) / (edge1 - edge0)).coerceIn(0f, 1f))

    private fun argb(r: Float, g: Float, b: Float): Int = android.graphics.Color.argb(
        255,
        (r.coerceIn(0f, 1f) * 255f).toInt(),
        (g.coerceIn(0f, 1f) * 255f).toInt(),
        (b.coerceIn(0f, 1f) * 255f).toInt(),
    )
}
