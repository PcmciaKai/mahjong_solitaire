package com.vortessence.mahjong.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Matrix
import android.graphics.Shader
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.vortessence.mahjong.R
import com.vortessence.mahjong.data.model.TableSurface
import kotlin.math.max
import kotlin.math.min

object TableSurfaceRenderer {

    // =========================================================================
    // TEXTURE ZOOM / SCALE SETTINGS (Easily adjustable in code)
    // - Lower values = zoom out (smaller pattern, more frequent seamless tiles)
    // - Higher values = zoom in (larger pattern, fewer tiles)
    // =========================================================================
    var WALNUT_TEXTURE_SCALE = 0.4f

    private var cachedWalnutBitmap: Bitmap? = null

    fun drawTableSurface(
        scope: DrawScope,
        surface: TableSurface,
        width: Float,
        height: Float,
        context: Context? = null,
    ) {
        when (surface) {
            TableSurface.GREEN_MAT -> drawGreenMat(scope, width, height)
            TableSurface.WALNUT -> drawWalnut(scope, context, width, height)
        }
    }

    private fun getWalnutBitmap(context: Context): Bitmap? {
        val cached = cachedWalnutBitmap
        if (cached != null && !cached.isRecycled) return cached
        return try {
            val b = BitmapFactory.decodeResource(context.resources, R.drawable.walnut)
            cachedWalnutBitmap = b
            b
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Dark green typical Mahjong table mat surface:
     * - Deep rich forest/emerald gradient with gentle vignette
     * - Woven fabric felt micro-structure
     * - Classic tournament Mahjong mat perimeter stitched border
     */
    private fun drawGreenMat(scope: DrawScope, width: Float, height: Float) {
        // 1. Smooth natural dark green gradient
        val center = Offset(width * 0.5f, height * 0.45f)
        val radius = max(width, height) * 0.85f
        scope.drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF135A48),
                    Color(0xFF0C4033),
                    Color(0xFF06261E),
                ),
                center = center,
                radius = radius,
            ),
        )

        // 2. Woven mat fabric micro-structure (diagonal weave)
        val spacing = 20f
        var d = -height
        while (d < (width + height)) {
            scope.drawLine(
                color = Color.White.copy(alpha = 0.02f),
                start = Offset(d, 0f),
                end = Offset(d + height, height),
                strokeWidth = 1f,
            )
            scope.drawLine(
                color = Color.Black.copy(alpha = 0.035f),
                start = Offset(d + height, 0f),
                end = Offset(d, height),
                strokeWidth = 1f,
            )
            d += spacing
        }

        // 3. Stitched perimeter border of a classic Mahjong table mat
        val inset = 14f
        val corner = 18f
        scope.drawRoundRect(
            color = Color(0xFF031913).copy(alpha = 0.6f),
            topLeft = Offset(inset, inset),
            size = Size(width - (inset * 2), height - (inset * 2)),
            cornerRadius = CornerRadius(corner),
            style = Stroke(width = 2.5f),
        )
        scope.drawRoundRect(
            color = Color(0xFFE5BE64).copy(alpha = 0.22f),
            topLeft = Offset(inset + 4f, inset + 4f),
            size = Size(width - (inset + 4f) * 2, height - (inset + 4f) * 2),
            cornerRadius = CornerRadius(corner - 2f),
            style = Stroke(
                width = 1.2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f),
            ),
        )
    }

    /**
     * Natural walnut wood table surface:
     * - Seamless walnut wood texture (scaled via WALNUT_TEXTURE_SCALE)
     * - Warm amber-chestnut to espresso color grading gradient overlay (via Multiply blend)
     * - Soft warm satin luster highlight
     */
    private fun drawWalnut(scope: DrawScope, context: Context?, width: Float, height: Float) {
        val bitmap = context?.let { getWalnutBitmap(it) }
        if (bitmap != null) {
            // 1. Draw tiled seamless walnut texture
            val matrix = Matrix().apply {
                setScale(WALNUT_TEXTURE_SCALE, WALNUT_TEXTURE_SCALE)
            }
            val shader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT).apply {
                setLocalMatrix(matrix)
            }
            scope.drawRect(brush = ShaderBrush(shader))

            // 2. Color grading gradient overlay (rich warm walnut tones with Multiply blend)
            val center = Offset(width * 0.45f, height * 0.4f)
            val radius = max(width, height) * 0.9f
            scope.drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFDC8A8), // Light warm amber-chestnut center
                        Color(0xFFD4A88F), // Lighter mid-tone walnut
                        Color(0xFF957C6E), // Muted dark vignette edge
                    ),
                    center = center,
                    radius = radius,
                ),
                blendMode = BlendMode.Multiply,
            )

            // 3. Subtle warm satin sheen highlight across the top-left
            scope.drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFD4A8).copy(alpha = 0.08f),
                        Color.Transparent,
                    ),
                    center = Offset(width * 0.35f, height * 0.25f),
                    radius = min(width, height) * 0.65f,
                ),
                center = Offset(width * 0.35f, height * 0.25f),
                radius = min(width, height) * 0.65f,
                blendMode = BlendMode.Screen,
            )
        } else {
            // Fallback gradient if context/bitmap is unavailable
            val center = Offset(width * 0.45f, height * 0.4f)
            val radius = max(width, height) * 0.9f
            scope.drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFAD7858), // Light warm amber-chestnut center
                        Color(0xFF84583F), // Lighter mid-tone walnut
                        Color(0xFF452C1E), // Muted dark vignette edge
                    ),
                    center = center,
                    radius = radius,
                ),
            )
        }
    }
}
