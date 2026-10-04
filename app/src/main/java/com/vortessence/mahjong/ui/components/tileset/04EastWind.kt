@file:Suppress("ObjectPropertyName", "UnusedReceiverParameter", "unused")

package com.vortessence.mahjong.ui.components.tileset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MahjongTiles.`04EastWind`: ImageVector
    get() {
        if (_04EastWind != null) {
            return _04EastWind!!
        }
        _04EastWind = ImageVector.Builder(
            name = "04EastWind",
            defaultWidth = 300.dp,
            defaultHeight = 420.dp,
            viewportWidth = 300f,
            viewportHeight = 420f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(26.63f, 59.56f)
                    horizontalLineToRelative(246.75f)
                    verticalLineToRelative(300.88f)
                    lineTo(26.63f, 360.44f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(132.66f, 59.58f)
                    curveToRelative(-4.91f, 1.21f, -2.33f, 11.22f, -6.99f, 15.16f)
                    curveToRelative(-2.71f, 2.29f, -5.67f, -0.13f, -8.54f, 4.41f)
                    curveToRelative(-7.92f, 12.54f, 20.19f, 4.76f, 16.19f, 9.77f)
                    curveToRelative(-6.11f, 7.66f, 1.68f, 11.83f, -3.97f, 13.5f)
                    curveToRelative(-13.95f, 4.12f, -31.19f, 1.24f, -32.7f, 7.95f)
                    curveToRelative(-0.62f, 2.77f, 5.18f, 12.59f, 14.22f, 14.52f)
                    curveToRelative(5.9f, 1.27f, 15.22f, -5.89f, 18.01f, -2.35f)
                    curveToRelative(2.08f, 2.63f, 0.14f, 12.3f, 0.14f, 12.3f)
                    reflectiveCurveToRelative(-31.34f, 15.06f, -35.06f, 15.23f)
                    curveToRelative(-3.72f, 0.17f, -21.7f, -13.19f, -22.42f, -7.39f)
                    curveToRelative(-0.24f, 1.89f, 2.29f, 7.88f, 7.19f, 18.51f)
                    curveToRelative(3.95f, 8.57f, 7.22f, 29.57f, 12.53f, 38.16f)
                    curveToRelative(8.98f, 14.5f, 23.3f, 17.23f, 23.3f, 17.23f)
                    reflectiveCurveToRelative(-19.57f, 24.23f, -41.61f, 43.68f)
                    curveToRelative(-22.9f, 20.2f, -48.27f, 35.74f, -46.2f, 40.46f)
                    curveToRelative(2.14f, 4.88f, 34.79f, -8.2f, 60.96f, -25.7f)
                    curveToRelative(23.54f, -15.74f, 39.28f, -35.01f, 39.28f, -35.01f)
                    reflectiveCurveToRelative(-2.45f, 25.53f, -2.82f, 37.84f)
                    curveToRelative(-0.37f, 12.31f, 0.62f, 36.02f, 0.62f, 36.02f)
                    reflectiveCurveToRelative(-29.84f, -15.17f, -32.75f, -10.6f)
                    curveToRelative(-2.91f, 4.57f, 19.67f, 18.25f, 29.14f, 34.64f)
                    curveToRelative(6.94f, 12.02f, 3.55f, 23.96f, 7.21f, 22.4f)
                    curveToRelative(3f, -1.28f, 10.8f, -6.19f, 18.16f, -21.79f)
                    curveToRelative(4.15f, -8.79f, 5.45f, -30.92f, 5.38f, -50.64f)
                    curveToRelative(-0.1f, -26.99f, 0.65f, -55.39f, 0.65f, -55.39f)
                    reflectiveCurveToRelative(46.24f, 50.61f, 61.45f, 49.45f)
                    curveToRelative(17.31f, -1.32f, 56.18f, -14.84f, 59.21f, -18.55f)
                    curveToRelative(2.84f, -3.48f, -35.64f, -10.4f, -58.17f, -12.46f)
                    curveToRelative(-10.09f, -0.92f, -42.83f, -29.08f, -42.83f, -29.08f)
                    reflectiveCurveToRelative(18.55f, -11.47f, 26.56f, -21.53f)
                    curveToRelative(8.01f, -10.06f, 15.98f, -32.94f, 19.47f, -37.63f)
                    curveToRelative(3.49f, -4.7f, 16.8f, -5.2f, 15.65f, -11.64f)
                    curveToRelative(-0.37f, -2.1f, -7.36f, -8.12f, -18.84f, -13.92f)
                    curveToRelative(-7.04f, -3.55f, -14.11f, -12.86f, -22.35f, -14.8f)
                    curveToRelative(-18.77f, -4.4f, -34.21f, 5.29f, -36.03f, 3.52f)
                    curveToRelative(-1.81f, -1.77f, -1.63f, -10.53f, -0.21f, -13.95f)
                    curveToRelative(0.33f, -0.8f, 2.97f, -2.84f, 6.95f, -3.01f)
                    curveToRelative(3.82f, -0.17f, 9.89f, 0.92f, 13.78f, 1.79f)
                    curveToRelative(9.4f, 2.12f, 19.51f, 1.58f, 19.44f, -3.85f)
                    curveToRelative(-0.12f, -8.96f, -8.74f, -20.12f, -19.11f, -20.19f)
                    curveToRelative(-8.38f, -0.06f, -17.66f, 12f, -20.99f, 8.58f)
                    curveToRelative(-3.84f, -3.94f, 14.81f, -8.38f, 13.53f, -11.8f)
                    curveToRelative(-3.74f, -9.95f, -15.24f, -2.21f, -24.74f, -8.33f)
                    curveToRelative(-7.81f, -5.02f, -10.34f, -16.08f, -12.68f, -15.51f)
                    close()
                    moveTo(176.72f, 142.1f)
                    curveToRelative(3.95f, -0.07f, 7.81f, 0.56f, 10.98f, 2.31f)
                    curveToRelative(11.26f, 6.26f, -27.34f, 63.31f, -32.33f, 62.41f)
                    curveToRelative(-4.99f, -0.91f, -1.47f, -17.6f, -1.69f, -27.53f)
                    curveToRelative(-0.05f, -2.09f, 9.82f, -1.67f, 17.73f, -2.04f)
                    curveToRelative(5.46f, -0.25f, 7.24f, -0.99f, 8.32f, -7.7f)
                    curveToRelative(0.42f, -2.61f, -4.72f, -9.47f, -9.72f, -10.76f)
                    curveToRelative(-5.07f, -1.31f, -9.97f, 3.08f, -13.63f, 1.09f)
                    curveToRelative(-5.72f, -3.1f, -2.39f, -10.75f, -1.85f, -11.38f)
                    curveToRelative(1.38f, -1.6f, 12.1f, -6.25f, 22.2f, -6.41f)
                    close()
                    moveTo(123.86f, 157.67f)
                    curveToRelative(1.43f, -0.04f, 2.58f, 0.17f, 3.27f, 0.73f)
                    curveToRelative(0.62f, 0.51f, 4.09f, 7.25f, -0.24f, 7.34f)
                    curveToRelative(-9.36f, 0.21f, -16.93f, 4.11f, -18.7f, 7.96f)
                    curveToRelative(-0.7f, 1.52f, 3.09f, 7.26f, 4.04f, 8.27f)
                    curveToRelative(2.83f, 3f, 5.64f, 8.59f, 15.59f, 5.27f)
                    curveToRelative(6.4f, -2.14f, 1.81f, 12.71f, 1.43f, 13.37f)
                    curveToRelative(-1.26f, 2.19f, -15.1f, -1.05f, -19.62f, -11.01f)
                    curveToRelative(-4.53f, -9.96f, -6.22f, -20.89f, -4.66f, -24.14f)
                    curveToRelative(1.26f, -2.65f, 12.7f, -7.61f, 18.9f, -7.79f)
                    close()
                }
            }
        }.build()

        return _04EastWind!!
    }

@Suppress("ObjectPropertyName")
private var _04EastWind: ImageVector? = null
