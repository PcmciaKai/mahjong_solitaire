package com.vortessence.mahjong.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.hypot
import kotlin.math.max

/**
 * Where the lamp hangs, as a fraction of the board's width and height: just up and to the left
 * of the center, which matches the direction the tile shadows fall in. Its light is fixed to the
 * screen, not to the board, so it stays put while the board zooms and pans underneath it.
 */
object Lamp {
    const val X = 0.46f
    const val Y = 0.42f

    fun center(size: Size): Offset = Offset(size.width * X, size.height * Y)

    /** Distance from the lamp to the farthest corner. */
    fun reach(size: Size): Float {
        val c = center(size)
        return hypot(max(c.x, size.width - c.x), max(c.y, size.height - c.y))
    }
}

/**
 * The lamp's light, multiplied over the finished board in a single pass.
 *
 * Like a real lamp, its light has one colour everywhere and only its brightness falls off with
 * distance: a soft, barely warm white (white tiles stay white, just a touch cosy), full strength
 * under the lamp and fading gently toward the edges. Multiplying never brightens, so the tiles'
 * edges and shadows keep their contrast in the middle of the board.
 *
 * Create one per canvas size: the brush then reuses its shader on every frame.
 */
internal class BoardLighting(width: Float, height: Float) {

    private val falloff = run {
        val size = Size(width, height)
        Brush.radialGradient(
            // Brightness at each distance, roughly as light spreads from a lamp hanging above
            0f to LampLight,
            0.3f to LampLight.dimmed(0.98f),
            0.55f to LampLight.dimmed(0.7f),
            0.8f to LampLight.dimmed(0.6f),
            1f to LampLight.dimmed(0.4f),
            center = Lamp.center(size),
            radius = Lamp.reach(size),
        )
    }

    fun drawFalloff(scope: DrawScope) = scope.drawRect(brush = falloff, blendMode = BlendMode.Multiply)

    private companion object {
        /** A warm white, around 4000 K: warmer than daylight, far from the orange of a candle. */
        val LampLight = Color(0xFFFFF6EA)

        fun Color.dimmed(brightness: Float) = Color(red * brightness, green * brightness, blue * brightness)
    }
}
