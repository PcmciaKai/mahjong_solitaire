@file:Suppress("ObjectPropertyName", "UnusedReceiverParameter", "unused")

package com.vortessence.mahjong.ui.components.tileset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MahjongTiles.`07NorthWind`: ImageVector
    get() {
        if (_07NorthWind != null) {
            return _07NorthWind!!
        }
        _07NorthWind = ImageVector.Builder(
            name = "07NorthWind",
            defaultWidth = 300.dp,
            defaultHeight = 420.dp,
            viewportWidth = 300f,
            viewportHeight = 420f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(51.72f, 98.83f)
                    horizontalLineToRelative(196.57f)
                    verticalLineToRelative(222.34f)
                    lineTo(51.72f, 321.17f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(168.96f, 98.84f)
                    curveToRelative(-3.7f, -0.06f, -3.77f, 0.21f, -4.91f, 1.79f)
                    curveToRelative(-1.91f, 4.25f, -4.7f, 10.74f, -5.99f, 11.27f)
                    curveToRelative(-1.76f, 0.73f, -6.85f, 6.63f, -6.9f, 7.75f)
                    curveToRelative(-0.38f, 9.14f, 9.64f, 8.21f, 12.92f, 9.84f)
                    curveToRelative(5.07f, 2.52f, 6.81f, 26.61f, 5.1f, 28.04f)
                    curveToRelative(-7.31f, 6.08f, -27.16f, 36.94f, -28.91f, 35f)
                    curveToRelative(-1.75f, -1.93f, 0.29f, -11.33f, -1.51f, -16.43f)
                    curveToRelative(-4.74f, -13.46f, 5.52f, -35.49f, -1.85f, -32.72f)
                    curveToRelative(-6.18f, 2.32f, -21.91f, 13.6f, -24.34f, 15.24f)
                    curveToRelative(-2.43f, 1.64f, -17.41f, 4.65f, -15.71f, 9.89f)
                    curveToRelative(1.71f, 5.24f, 10.26f, 5.86f, 9.26f, 10.56f)
                    curveToRelative(-1.01f, 4.7f, 6.84f, 45.44f, 3.98f, 51.45f)
                    curveToRelative(-1.25f, 2.65f, -8.51f, 15.62f, -15.57f, 25.12f)
                    curveToRelative(-8.96f, 12.06f, -15.86f, 25.21f, -17.67f, 23.67f)
                    curveToRelative(-3.23f, -2.75f, -17.83f, -23.42f, -23.6f, -19.74f)
                    curveToRelative(-5.77f, 3.68f, 6.36f, 37.49f, 7.92f, 43.25f)
                    curveToRelative(1.56f, 5.76f, -4.59f, 13.76f, -1.6f, 17.56f)
                    curveToRelative(3f, 3.79f, 23.59f, -7.45f, 27.94f, -11.85f)
                    curveToRelative(4.34f, -4.41f, 24.18f, -43.53f, 23.61f, -35.12f)
                    curveToRelative(-0.58f, 8.41f, -4.7f, 40.59f, 1.91f, 41.05f)
                    curveToRelative(2.56f, 0.17f, 7.66f, -3.77f, 11.22f, -10.88f)
                    curveToRelative(5.64f, -11.25f, 9.65f, -28.35f, 10.67f, -33.63f)
                    curveToRelative(3.25f, -9.12f, 3.98f, -41.43f, 9.28f, -48.45f)
                    curveToRelative(6.12f, -1.61f, 24.52f, -40.62f, 26.1f, -36.85f)
                    curveToRelative(1.59f, 3.77f, -10.94f, 89.97f, 0.83f, 113.79f)
                    curveToRelative(11.77f, 23.82f, 59.33f, 16.6f, 71.14f, 21.72f)
                    curveToRelative(11.82f, 5.11f, 2.5f, -10.57f, 2.42f, -17.43f)
                    curveToRelative(-0.07f, -6.87f, 3.15f, -13.81f, -6.07f, -16.85f)
                    curveToRelative(-9.22f, -3.04f, -40.88f, 8.75f, -42.37f, 5.39f)
                    curveToRelative(-1.57f, -3.53f, -5.26f, -31.01f, -2.28f, -34.16f)
                    curveToRelative(2.98f, -3.16f, 12.08f, -4.44f, 17.6f, -7.01f)
                    curveToRelative(5.51f, -2.57f, 11.03f, -6.32f, 15.68f, -9.05f)
                    curveToRelative(4.65f, -2.74f, 15.49f, -13.97f, 15.55f, -13.61f)
                    curveToRelative(0f, 0f, 9.71f, -4.26f, -16.33f, -14.14f)
                    curveToRelative(-9.35f, -3.55f, -14.08f, -20.96f, -19.09f, -18.59f)
                    curveToRelative(-1.92f, 0.9f, -2.58f, 6.35f, -3.73f, 8.7f)
                    curveToRelative(-1.15f, 2.35f, -1.33f, 2.65f, -1.97f, 5.15f)
                    curveToRelative(-0.64f, 2.51f, 2.42f, 15.99f, 1.31f, 18.86f)
                    curveToRelative(-1.1f, 2.87f, -9.75f, 22.13f, -9.24f, 17.86f)
                    curveToRelative(0.15f, -1.28f, 1.29f, -15.39f, 2.6f, -27.03f)
                    curveToRelative(0.26f, -25.64f, 9.39f, -54.1f, 8.08f, -78.22f)
                    curveToRelative(4.77f, -7.32f, 19.17f, -13.16f, 12.14f, -18.14f)
                    curveToRelative(-7.03f, -4.98f, -19.19f, -5.89f, -25.23f, -8.92f)
                    curveToRelative(-7.08f, -3.93f, -18.72f, -14.04f, -22.42f, -14.1f)
                    close()
                    moveTo(68.21f, 206.1f)
                    curveToRelative(-1.3f, 0.03f, -2.32f, 0.36f, -2.96f, 1.04f)
                    curveToRelative(-5.14f, 5.45f, 7.95f, 22.3f, 9.88f, 27.25f)
                    curveToRelative(1.93f, 4.95f, -5.71f, 22.1f, -2.12f, 27.39f)
                    curveToRelative(3.59f, 5.29f, 27.84f, -19.72f, 27.25f, -31.34f)
                    curveToRelative(-0.52f, -10.16f, -23f, -24.55f, -32.06f, -24.33f)
                    lineToRelative(0f, -0f)
                    close()
                }
            }
        }.build()

        return _07NorthWind!!
    }

@Suppress("ObjectPropertyName")
private var _07NorthWind: ImageVector? = null
