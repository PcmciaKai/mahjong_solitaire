@file:Suppress("ObjectPropertyName", "UnusedReceiverParameter", "unused")

package com.vortessence.mahjong.ui.components.tileset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MahjongTiles.`05SouthWind`: ImageVector
    get() {
        if (_05SouthWind != null) {
            return _05SouthWind!!
        }
        _05SouthWind = ImageVector.Builder(
            name = "05SouthWind",
            defaultWidth = 300.dp,
            defaultHeight = 420.dp,
            viewportWidth = 300f,
            viewportHeight = 420f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(45.9f, 72.64f)
                    horizontalLineToRelative(208.2f)
                    verticalLineToRelative(274.73f)
                    lineTo(45.9f, 347.36f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(132.74f, 72.64f)
                    arcToRelative(5.89f, 5.89f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.08f, 0.12f)
                    curveToRelative(-1.98f, 0.39f, -2.53f, 5.45f, -4.5f, 9.05f)
                    curveToRelative(-2.88f, 5.27f, -7.89f, 4.36f, -7.76f, 7.81f)
                    curveToRelative(0.13f, 3.45f, 8.32f, 3.79f, 15.11f, 8.94f)
                    curveToRelative(6.8f, 5.15f, -9.93f, 65.25f, -10.74f, 67.4f)
                    curveToRelative(-0.42f, 1.11f, -8.62f, 3.04f, -17.48f, 3.86f)
                    curveToRelative(-8.3f, 0.76f, -17.37f, -0.31f, -18.2f, 1.31f)
                    curveToRelative(-1.71f, 3.36f, 6.81f, 12.2f, 11.7f, 14.4f)
                    curveToRelative(4.9f, 2.2f, 18.56f, -0.79f, 19.85f, 0.35f)
                    curveToRelative(1.3f, 1.14f, -1.38f, 10.44f, -3.94f, 13.18f)
                    curveToRelative(-2.56f, 2.74f, -5.03f, 5.04f, -7.26f, 2.9f)
                    curveToRelative(-2.22f, -2.14f, -16.81f, -17.03f, -18.53f, -13.02f)
                    curveToRelative(-1.72f, 4.01f, 9.13f, 20.24f, 4.61f, 19.84f)
                    curveToRelative(-4.52f, -0.4f, -22.89f, 11.22f, -24.35f, 11.51f)
                    curveToRelative(-1.46f, 0.29f, -5.79f, -5.09f, -8.85f, -3.18f)
                    curveToRelative(-3.75f, 2.34f, -2.88f, 6.86f, -6.8f, 10.33f)
                    curveToRelative(-3.86f, 3.42f, -9.05f, 3.55f, -8.62f, 6.59f)
                    curveToRelative(0.85f, 6.12f, 6.38f, 6.8f, 6.49f, 10.41f)
                    curveToRelative(0.04f, 1.49f, 3.82f, 17.52f, 10.48f, 31.32f)
                    curveToRelative(9.48f, 19.65f, 23.37f, 38.25f, 28.34f, 33.65f)
                    curveToRelative(8.44f, -7.83f, -12.92f, -65.98f, -10.99f, -71.71f)
                    curveToRelative(1.92f, -5.73f, 19.09f, -16.21f, 22.07f, -14.87f)
                    curveToRelative(2.97f, 1.34f, 12.89f, 10.78f, 10.02f, 12.18f)
                    curveToRelative(-2.86f, 1.4f, -14.35f, 1.61f, -14.7f, 6.89f)
                    curveToRelative(-0.35f, 5.28f, 21.07f, 10.73f, 21.07f, 10.73f)
                    reflectiveCurveToRelative(1.87f, 6.28f, 0.04f, 6.85f)
                    curveToRelative(-1.84f, 0.57f, -25.46f, -0.12f, -26.08f, 3.46f)
                    curveToRelative(-0.63f, 3.58f, 6.87f, 10.13f, 11.51f, 12.33f)
                    curveToRelative(4.64f, 2.2f, 15.24f, -0.75f, 17.14f, 0.91f)
                    curveToRelative(1.9f, 1.67f, 2.88f, 48.92f, 8.49f, 48.44f)
                    curveToRelative(5.6f, -0.48f, 10.91f, -49.01f, 11.15f, -51.78f)
                    curveToRelative(0.23f, -2.76f, 28.79f, -0.72f, 31.27f, -3.04f)
                    curveToRelative(2.48f, -2.33f, -0.81f, -14.04f, -4.86f, -15.44f)
                    curveToRelative(-4.04f, -1.4f, -22.59f, 3.76f, -23.69f, 2.11f)
                    curveToRelative(-1.09f, -1.65f, -0.74f, -6.78f, -0.23f, -7.37f)
                    curveToRelative(0.51f, -0.6f, 8.28f, -2.21f, 6.51f, 1.44f)
                    curveToRelative(-2.18f, 4.49f, 13.11f, -2.85f, 14.22f, -3.81f)
                    curveToRelative(2.18f, -1.9f, 1.21f, -7.12f, -2.87f, -11.27f)
                    curveToRelative(-4.08f, -4.15f, -13.16f, 2.28f, -12.91f, 0.3f)
                    curveToRelative(0.12f, -0.92f, 5.08f, -4.76f, 9.31f, -10.05f)
                    curveToRelative(4.86f, -6.07f, 8.6f, -14.11f, 9.29f, -15.59f)
                    curveToRelative(1.29f, -2.76f, 38.01f, -0.58f, 40.77f, 10.41f)
                    curveToRelative(2.75f, 10.98f, -9.89f, 45.46f, -12.9f, 58.35f)
                    curveToRelative(-3.01f, 12.88f, -27.56f, 47.53f, -35.35f, 45.52f)
                    curveToRelative(-7.79f, -2.01f, -19.31f, -16.22f, -22.05f, -14.77f)
                    curveToRelative(-2.74f, 1.45f, 8.62f, 31.66f, 16.17f, 36.84f)
                    curveToRelative(7.55f, 5.17f, 33.29f, -12.84f, 49.15f, -29.03f)
                    curveToRelative(15.86f, -16.2f, 31.05f, -76.59f, 32.57f, -78.48f)
                    curveToRelative(1.52f, -1.89f, 19.3f, -0.38f, 18.76f, -4.98f)
                    curveToRelative(-0.71f, -6.05f, -9.23f, -7.91f, -18.88f, -15.78f)
                    curveToRelative(-9.84f, -8.04f, -20.58f, -18.32f, -28.25f, -21.77f)
                    curveToRelative(-15.18f, -6.84f, -37.36f, -2.73f, -39.44f, -4.4f)
                    curveToRelative(-2.08f, -1.66f, 1.1f, -3.64f, -1.82f, -7.94f)
                    curveToRelative(-2.32f, -3.43f, -8.83f, -7.5f, -8.59f, -8.9f)
                    curveToRelative(0.55f, -3.15f, 2.24f, -2.69f, 8.31f, -4.05f)
                    curveToRelative(4.26f, -0.96f, 11.63f, 1.08f, 16.28f, 0.42f)
                    curveToRelative(4.03f, -0.57f, 5.57f, -3.84f, 6.76f, -4.62f)
                    curveToRelative(4.55f, -2.97f, -1.44f, -14.09f, -10.43f, -17.69f)
                    curveToRelative(-8.99f, -3.6f, -36.14f, 17.96f, -34.08f, 14.24f)
                    curveToRelative(2.06f, -3.72f, 18f, -51.81f, 19.38f, -53.37f)
                    curveToRelative(1.38f, -1.57f, 18.96f, -4.86f, 15.65f, -11.44f)
                    curveToRelative(-4.17f, -8.29f, -20.23f, -7.05f, -30.91f, -12.82f)
                    curveToRelative(-10.37f, -5.61f, -10.32f, -13.27f, -15.33f, -13.2f)
                    lineToRelative(-0f, -0f)
                    close()
                    moveTo(145.52f, 177.44f)
                    curveToRelative(0.1f, 0f, 0.2f, 0f, 0.3f, 0.01f)
                    curveToRelative(1.59f, 0.14f, 2.21f, 5.08f, 3.5f, 9.28f)
                    curveToRelative(1.32f, 4.3f, 3.3f, 7.86f, 3f, 8.79f)
                    curveToRelative(-0.31f, 0.98f, -5.41f, -1.15f, -10.18f, -0.67f)
                    curveToRelative(-4.21f, 0.42f, -8.14f, 3.59f, -8.74f, 2.51f)
                    curveToRelative(-1.28f, -2.31f, 1.58f, -9.16f, 3.81f, -12.57f)
                    curveToRelative(2.16f, -3.3f, 5.19f, -7.36f, 8.31f, -7.36f)
                    close()
                    moveTo(145f, 212.48f)
                    curveToRelative(0.74f, 0f, 1.28f, 0.07f, 1.53f, 0.26f)
                    curveToRelative(1.28f, 0.97f, -3.29f, 5.88f, -5.6f, 11.13f)
                    curveToRelative(-2.15f, 4.88f, -1.97f, 10.16f, -3.15f, 9.9f)
                    curveToRelative(-1.06f, -0.23f, -2.96f, -1.39f, -5.22f, -0.47f)
                    curveToRelative(-2.99f, 1.22f, -6.19f, 4.28f, -4.8f, 0.33f)
                    curveToRelative(2.44f, -6.95f, -5.31f, -12.07f, -3.81f, -14.83f)
                    curveToRelative(0.68f, -1.25f, 5.72f, -4.5f, 7.28f, -5.31f)
                    curveToRelative(1.75f, -0.91f, 3.87f, 0.71f, 6.74f, 0.7f)
                    curveToRelative(2.4f, -0.01f, 5.57f, -1.71f, 7.04f, -1.71f)
                    verticalLineToRelative(-0f)
                    close()
                }
            }
        }.build()

        return _05SouthWind!!
    }

@Suppress("ObjectPropertyName")
private var _05SouthWind: ImageVector? = null
