@file:Suppress("ObjectPropertyName", "UnusedReceiverParameter", "unused")

package com.vortessence.mahjong.ui.components.tileset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MahjongTiles.`06WestWind`: ImageVector
    get() {
        if (_06WestWind != null) {
            return _06WestWind!!
        }
        _06WestWind = ImageVector.Builder(
            name = "06WestWind",
            defaultWidth = 300.dp,
            defaultHeight = 420.dp,
            viewportWidth = 300f,
            viewportHeight = 420f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(38.34f, 88.68f)
                    horizontalLineToRelative(223.32f)
                    verticalLineToRelative(242.63f)
                    lineTo(38.34f, 331.32f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(182.51f, 88.7f)
                    curveToRelative(-8.87f, 0.5f, -36.22f, 15.63f, -50.96f, 17.38f)
                    curveToRelative(-15.72f, 1.87f, -35.75f, -5.11f, -39.5f, -0.37f)
                    curveToRelative(-3.75f, 4.73f, 8.63f, 14.54f, 18.44f, 20.31f)
                    curveToRelative(9.81f, 5.78f, 36.08f, 2.54f, 39.69f, 8.59f)
                    curveToRelative(3.61f, 6.05f, -8.8f, 19.86f, -11.63f, 23.33f)
                    curveToRelative(-2.82f, 3.47f, -9.86f, 0.17f, -10.67f, 5.61f)
                    curveToRelative(-0.82f, 5.44f, 14.06f, 11.1f, 16.8f, 15.12f)
                    curveToRelative(2.74f, 4.01f, -1.17f, 20.44f, -3.85f, 23.67f)
                    curveToRelative(-2.68f, 3.23f, -9.23f, 3.76f, -11.23f, 2.14f)
                    curveToRelative(-1.99f, -1.61f, -5.03f, -18.17f, -9.92f, -19.76f)
                    curveToRelative(-4.88f, -1.59f, -3.46f, -12.73f, -9.14f, -13.72f)
                    curveToRelative(-5.68f, -0.99f, -9.99f, 15.72f, -11.87f, 16.71f)
                    curveToRelative(-1.88f, 0.99f, -12.97f, 2.97f, -13.82f, 7.06f)
                    curveToRelative(-0.85f, 4.09f, 13.57f, 7.13f, 15.55f, 8.12f)
                    curveToRelative(1.98f, 1f, 6.89f, 9.1f, 5.48f, 10.5f)
                    curveToRelative(-0.67f, 0.67f, -9.99f, 3.03f, -18.64f, 7.44f)
                    curveToRelative(-9.57f, 4.88f, -18.58f, 11.83f, -20.03f, 12f)
                    curveToRelative(-2.75f, 0.33f, -9.16f, -5.93f, -14.52f, -6.13f)
                    curveToRelative(-5.37f, -0.2f, -14.69f, -0.64f, -14.35f, 4.22f)
                    curveToRelative(0.34f, 4.86f, 8.27f, 8.84f, 11.31f, 15.31f)
                    curveToRelative(3.05f, 6.46f, 6.05f, 32.7f, 13.63f, 43.07f)
                    curveToRelative(7.58f, 10.37f, 20.87f, 33.48f, 27.45f, 31.55f)
                    curveToRelative(6.59f, -1.92f, 8.83f, -4.94f, 20.07f, -7.83f)
                    curveToRelative(11.24f, -2.89f, 43.93f, -4.61f, 47.5f, -4.93f)
                    curveToRelative(3.57f, -0.32f, -0.09f, 19.78f, 12.87f, 22.9f)
                    curveToRelative(12.96f, 3.12f, 31.89f, -15.88f, 44.95f, -30.41f)
                    curveToRelative(13.06f, -14.52f, 28.09f, -51.48f, 29.13f, -55.61f)
                    curveToRelative(1.04f, -4.13f, 20.56f, -5.95f, 15.57f, -14.19f)
                    curveToRelative(-4.99f, -8.24f, -31.77f, -11.19f, -37.73f, -22.43f)
                    curveToRelative(-5.96f, -11.24f, -49.66f, -14.26f, -57.08f, -10.74f)
                    curveToRelative(-3.61f, 1.71f, 0.32f, -4.03f, 6.67f, -9.36f)
                    curveToRelative(6.72f, -5.62f, 16.97f, -6.97f, 15.61f, -13.68f)
                    curveToRelative(-0.43f, -2.12f, -3.3f, -3.91f, -8.38f, -3.95f)
                    curveToRelative(-7.44f, -0.07f, -17.85f, 1.94f, -19.33f, -0.93f)
                    curveToRelative(-3.8f, -7.41f, 3.22f, -11.25f, 5.05f, -20.58f)
                    curveToRelative(1.27f, -6.51f, -0.36f, -14.62f, 0.49f, -16.96f)
                    curveToRelative(2.08f, -5.71f, 33.28f, -12.96f, 33.38f, -23.92f)
                    curveToRelative(0.1f, -10.96f, -8.84f, -17.73f, -15.51f, -19.41f)
                    arcToRelative(5.14f, 5.14f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.52f, -0.1f)
                    close()
                    moveTo(184.57f, 215.19f)
                    curveToRelative(3.18f, 0.08f, 6.08f, 0.34f, 8.21f, 0.84f)
                    curveToRelative(8.52f, 2.03f, 14.68f, 5.22f, 18.05f, 12.53f)
                    curveToRelative(3.37f, 7.3f, -9.89f, 44.47f, -9.89f, 44.47f)
                    reflectiveCurveToRelative(-22.69f, 39.43f, -24.42f, 35.31f)
                    curveToRelative(-1.73f, -4.13f, -7.47f, -16.57f, -12.27f, -19.29f)
                    curveToRelative(-4.8f, -2.71f, -14.52f, 0.09f, -36.74f, 1.46f)
                    curveToRelative(-22.23f, 1.38f, -25.71f, 9.05f, -30.33f, 5.96f)
                    curveToRelative(-4.62f, -3.08f, -9.58f, -12.34f, -12.92f, -20.34f)
                    curveToRelative(-3.34f, -8f, -7.35f, -14.01f, -7.13f, -16.79f)
                    curveToRelative(0.11f, -1.32f, 2.01f, -2.66f, 2.25f, -5.23f)
                    curveToRelative(0.27f, -2.85f, -1.11f, -6.95f, -0.57f, -8.07f)
                    curveToRelative(1.02f, -2.14f, 29f, -15.33f, 28.36f, -12.15f)
                    curveToRelative(-0.26f, 1.35f, -3.63f, 14.93f, -7.05f, 24.42f)
                    curveToRelative(-4.65f, 12.93f, -11.46f, 18.7f, -8.47f, 26.77f)
                    curveToRelative(2.42f, 6.54f, 24.68f, -21.69f, 28.89f, -28.25f)
                    curveToRelative(4.21f, -6.57f, 9.06f, -29.47f, 11.34f, -32f)
                    curveToRelative(2.28f, -2.53f, 5.22f, -1.84f, 5.22f, -1.84f)
                    reflectiveCurveToRelative(-4.19f, 21.53f, 1.95f, 29.65f)
                    curveToRelative(6.14f, 8.12f, 40.89f, 13.62f, 43.66f, 12.1f)
                    curveToRelative(2.78f, -1.52f, 8.56f, -8.4f, 7.82f, -13.8f)
                    curveToRelative(-0.73f, -5.4f, -7.19f, -9.31f, -10.28f, -10.9f)
                    curveToRelative(-3.09f, -1.59f, -19.58f, 5.73f, -21.58f, 0.98f)
                    curveToRelative(-0.64f, -1.52f, -1.51f, -5.16f, -0.5f, -8.8f)
                    curveToRelative(0.78f, -2.78f, 3.67f, -6.28f, 2.78f, -8.58f)
                    curveToRelative(-1.56f, -4.01f, -0.23f, -5.66f, 0.6f, -6.25f)
                    curveToRelative(1.43f, -1.02f, 13.49f, -2.41f, 23.02f, -2.18f)
                    verticalLineToRelative(-0f)
                    close()
                }
            }
        }.build()

        return _06WestWind!!
    }

@Suppress("ObjectPropertyName")
private var _06WestWind: ImageVector? = null
