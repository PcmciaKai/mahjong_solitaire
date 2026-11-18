package com.vortessence.mahjong.ui.components.tiles

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.vortessence.mahjong.ui.components.Lamp
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.toArgb
import com.vortessence.mahjong.data.model.TileSet
import com.vortessence.mahjong.ui.theme.TileCornerIndexRed

/**
 * A tile set, colour variant and wear state resolved into what the renderer draws with. Create it
 * once per settings change (it builds brushes and colour filters), never per frame.
 */
class TileLook(
    val material: TileMaterial,
    colorway: TileColorway,
    val worn: Boolean,
    textures: TileTextures? = null,
) {
    private val wear = material.worn

    val faceLight: Color = colorway.faceLight.tinted(worn, wear.faceTint)
    val faceDark: Color = colorway.faceDark.tinted(worn, wear.faceTint)
    val sideLight: Color = colorway.sideLight.tinted(worn, wear.faceTint)
    val sideDeep: Color = colorway.sideDeep.tinted(worn, wear.faceTint)
    val back: Color = colorway.back.tinted(worn, wear.backTint)
    val backDeep: Color = colorway.backDeep.tinted(worn, wear.backTint)
    val backingStart: Float = colorway.backingStart.coerceIn(0f, 1f)

    /** Only textures made for this set and wear state; others may still be around while switching. */
    val textures: TileTextures? = textures?.takeIf { (it.set == material.set) && (it.worn == worn) }

    /** Face lit from the upper left, where the lamp hangs. In tile space. */
    val faceBrush: Brush = Brush.linearGradient(
        colors = listOf(faceLight, faceDark),
        start = Offset.Zero,
        end = Offset(TileSpace.WIDTH, TileSpace.HEIGHT),
    )

    private val gloss = if (worn) wear.gloss else material.gloss

    /**
     * A faint, even sheen across each face, so polished tiles still look polished away from the
     * lamp's reflection. In tile space.
     */
    val sheenBrush: Brush? = if (gloss < 0.01f) null else Brush.linearGradient(
        0f to Color.White.copy(alpha = gloss * 0.22f),
        0.6f to Color.White.copy(alpha = 0f),
        start = Offset.Zero,
        end = Offset(TileSpace.WIDTH, TileSpace.HEIGHT),
    )

    private var reflectionCanvas = Size.Unspecified
    private var reflectionBrush: Brush? = null

    /**
     * The lamp mirrored in the faces. All faces lie in the same plane, so like a real reflection
     * it is one shape in screen space, spread over the tiles near the lamp and fading across them,
     * rather than the same spot repeated on every tile. Broad and faint on waxy materials, smaller
     * and brighter on glassy ones.
     */
    fun reflectionBrush(canvas: Size): Brush? {
        if (gloss < 0.01f) return null
        if (canvas != reflectionCanvas) {
            val sharp = material.glossSharpness
            val strength = gloss * 0.75f
            reflectionCanvas = canvas
            reflectionBrush = Brush.radialGradient(
                0f to Color.White.copy(alpha = strength),
                0.3f to Color.White.copy(alpha = strength * (0.6f - (0.3f * sharp))),
                0.65f to Color.White.copy(alpha = strength * (0.18f * (1f - sharp))),
                1f to Color.White.copy(alpha = 0f),
                center = Lamp.center(canvas),
                radius = Lamp.reach(canvas) * (0.95f - (0.5f * sharp)),
            )
        }
        return reflectionBrush
    }

    /** Edge catching the light on the upper left and falling into shade on the lower right. */
    val rimBrush: Brush = Brush.linearGradient(
        0f to Color.White.copy(alpha = material.rim),
        0.45f to Color.White.copy(alpha = 0f),
        0.55f to Color.Black.copy(alpha = 0f),
        1f to Color.Black.copy(alpha = 0.16f),
        start = Offset.Zero,
        end = Offset(TileSpace.WIDTH, TileSpace.HEIGHT),
    )

    private val inkMatrix: InkMatrix = run {
        val ink = if (worn) wear.ink else material.ink
        val fade = if (worn) wear.inkFade else 0f
        val grime = if (worn) wear.inkGrime else 0f
        InkMatrix()
            .saturate(ink.saturation)
            .scale(ink.brightness)
            .mono(ink.monoTint, ink.monoAmount)
            .mix(faceDark, fade)
            .mix(GrooveDirt, grime)
    }

    /** Recolours the symbols for the material's paint: vibrancy, gilding, fading, dirt in grooves. */
    val inkFilter: ColorFilter? =
        if (inkMatrix.isIdentity()) null else ColorFilter.colorMatrix(ColorMatrix(inkMatrix.toColorMatrix()))

    /** The corner index is painted with the same paint as the symbols. */
    val cornerIndexColor: Color = inkMatrix.apply(TileCornerIndexRed)

    /** How deep the symbols are cut into the face; 0 for printed symbols. */
    val engraving: Float = material.engraving

    /**
     * Paints the symbols' shapes in shadow, for the upper-left wall of each engraved groove. The
     * paint is then drawn slightly down and to the right on top, leaving that wall visible.
     */
    val engravingShadowFilter: ColorFilter? = if (engraving <= 0f) null else ColorFilter.tint(
        GrooveShadow.copy(alpha = 0.35f + (0.45f * engraving)),
        BlendMode.SrcIn,
    )

    /** Shadow colour for the engraved corner index, matching [engravingShadowFilter]. */
    val engravingShadowColor: Color = GrooveShadow.copy(alpha = 0.35f + (0.45f * engraving))

    /** Colours the rub-off texture in the face colour, which is what shows where paint is gone. */
    val rubOffFilter: ColorFilter = ColorFilter.tint(faceDark, BlendMode.Modulate)

    companion object {
        private val GrooveDirt = Color(0xFF4A3A2A)
        private val GrooveShadow = Color(0xFF2A1C10)

        /** New tiles of the default set; for previews and any place without settings. */
        val Default: TileLook by lazy {
            val material = TileMaterials.of(TileSet.Default)
            TileLook(material, material.colorway(0), worn = false)
        }

        private fun Color.tinted(worn: Boolean, tint: Color): Color =
            if (!worn) this else Color(red * tint.red, green * tint.green, blue * tint.blue, alpha)
    }
}

/** The fixed box faces are drawn in, whatever the tile's size on screen. */
object TileSpace {
    const val WIDTH = TileTextureGenerator.BASE_WIDTH.toFloat()
    const val HEIGHT = TileTextureGenerator.BASE_HEIGHT.toFloat()
}

/**
 * An affine colour transform on RGB (0..1), built up step by step; each step applies to the
 * result of the previous ones.
 */
private class InkMatrix {
    // Rows r, g, b: [cr, cg, cb, offset]
    private val m = floatArrayOf(
        1f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f,
        0f, 0f, 1f, 0f,
    )

    /** Applies out = step * current, where step is given the same way. */
    private fun then(step: FloatArray): InkMatrix {
        val result = FloatArray(12)
        for (row in 0 until 3) {
            for (col in 0 until 4) {
                var v = if (col == 3) step[(row * 4) + 3] else 0f
                for (k in 0 until 3) v += step[(row * 4) + k] * m[(k * 4) + col]
                result[(row * 4) + col] = v
            }
        }
        result.copyInto(m)
        return this
    }

    fun saturate(s: Float): InkMatrix {
        if (s == 1f) return this
        val lr = 0.213f * (1f - s)
        val lg = 0.715f * (1f - s)
        val lb = 0.072f * (1f - s)
        return then(
            floatArrayOf(
                lr + s, lg, lb, 0f,
                lr, lg + s, lb, 0f,
                lr, lg, lb + s, 0f,
            )
        )
    }

    fun scale(b: Float): InkMatrix {
        if (b == 1f) return this
        return then(floatArrayOf(b, 0f, 0f, 0f, 0f, b, 0f, 0f, 0f, 0f, b, 0f))
    }

    /** Maps every colour onto [tint], keeping a little of its lightness for shading. */
    fun mono(tint: Color?, amount: Float): InkMatrix {
        if ((tint == null) || (amount <= 0f)) return this
        val keep = 1f - amount
        val rows = FloatArray(12)
        val channels = floatArrayOf(tint.red, tint.green, tint.blue)
        for (row in 0 until 3) {
            val c = channels[row] * amount
            // c * (0.85 + 0.3 * luminance) + keep * original: dark ink keeps nearly the full tint
            rows[(row * 4) + 0] = (c * 0.3f * 0.299f) + (if (row == 0) keep else 0f)
            rows[(row * 4) + 1] = (c * 0.3f * 0.587f) + (if (row == 1) keep else 0f)
            rows[(row * 4) + 2] = (c * 0.3f * 0.114f) + (if (row == 2) keep else 0f)
            rows[(row * 4) + 3] = c * 0.85f
        }
        return then(rows)
    }

    /** Blends toward a fixed [color] by [amount]. */
    fun mix(color: Color, amount: Float): InkMatrix {
        if (amount <= 0f) return this
        val k = 1f - amount
        return then(
            floatArrayOf(
                k, 0f, 0f, color.red * amount,
                0f, k, 0f, color.green * amount,
                0f, 0f, k, color.blue * amount,
            )
        )
    }

    /** This transform applied to a single colour. */
    fun apply(color: Color): Color = Color(
        red = ((m[0] * color.red) + (m[1] * color.green) + (m[2] * color.blue) + m[3]).coerceIn(0f, 1f),
        green = ((m[4] * color.red) + (m[5] * color.green) + (m[6] * color.blue) + m[7]).coerceIn(0f, 1f),
        blue = ((m[8] * color.red) + (m[9] * color.green) + (m[10] * color.blue) + m[11]).coerceIn(0f, 1f),
        alpha = color.alpha,
    )

    fun isIdentity(): Boolean = m.contentEquals(
        floatArrayOf(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f)
    )

    /** Android's 4x5 colour matrix, with offsets in 0..255. */
    fun toColorMatrix(): FloatArray = floatArrayOf(
        m[0], m[1], m[2], 0f, m[3] * 255f,
        m[4], m[5], m[6], 0f, m[7] * 255f,
        m[8], m[9], m[10], 0f, m[11] * 255f,
        0f, 0f, 0f, 1f, 0f,
    )
}
