@file:Suppress("ObjectPropertyName", "UnusedReceiverParameter", "unused")

package com.vortessence.mahjong.ui.components.tileset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MahjongTiles.`40Orchid`: ImageVector
    get() {
        if (_40Orchid != null) {
            return _40Orchid!!
        }
        _40Orchid = ImageVector.Builder(
            name = "40Orchid",
            defaultWidth = 300.dp,
            defaultHeight = 420.dp,
            viewportWidth = 300f,
            viewportHeight = 420f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(34.5f, 58.5f)
                    horizontalLineToRelative(231f)
                    verticalLineToRelative(303f)
                    lineTo(34.5f, 361.5f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(40.66f, 80.13f)
                    curveToRelative(0.08f, -0.86f, 6.27f, -11.76f, 13.46f, -15.55f)
                    curveToRelative(7.19f, -3.78f, 18.21f, -8.43f, 28.17f, -4.71f)
                    curveToRelative(9.96f, 3.71f, 21.19f, 14.33f, 19.93f, 31.18f)
                    curveToRelative(-1.26f, 16.86f, -45.96f, 45.8f, -41.58f, 45.84f)
                    curveToRelative(3.95f, 0.04f, 38.75f, 0.04f, 41.63f, 0.04f)
                    curveToRelative(1.45f, 0f, 3.24f, 1.61f, 3.21f, 10.34f)
                    curveToRelative(-0.02f, 7.53f, -2.05f, 9.35f, -3.39f, 9.35f)
                    lineToRelative(-62.74f, -0.1f)
                    curveToRelative(-1.78f, 0f, -4.11f, -5.06f, -3.92f, -9.8f)
                    curveToRelative(0.2f, -5.03f, 2.99f, -9.76f, 3.99f, -9.76f)
                    curveToRelative(0.86f, 0f, 6.59f, -7.57f, 15.35f, -15.4f)
                    curveToRelative(11.12f, -9.94f, 26.04f, -21.34f, 28.35f, -29.97f)
                    curveToRelative(3.82f, -14.27f, -5.84f, -17.04f, -13.77f, -16.56f)
                    curveToRelative(-7.93f, 0.48f, -14.7f, 10.43f, -14.7f, 10.43f)
                    reflectiveCurveToRelative(-4.34f, 5.84f, -10.13f, 3.51f)
                    curveToRelative(-5.78f, -2.33f, -3.95f, -7.99f, -3.87f, -8.85f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(137.98f, 83.44f)
                    arcToRelative(2.2f, 2.2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.6f, 0.33f)
                    curveToRelative(-3.24f, 2.34f, 21.89f, 46.14f, 18.49f, 79.3f)
                    curveToRelative(-2.97f, 28.94f, -13.21f, 57.17f, -17.07f, 76.96f)
                    curveToRelative(-1.69f, -6.94f, -2.89f, -20.54f, -5.81f, -26.27f)
                    curveToRelative(-8.52f, -16.68f, -37.05f, -35.62f, -35.75f, -28.25f)
                    curveToRelative(0.35f, 2f, 16.53f, 15.01f, 24.75f, 44.76f)
                    curveToRelative(2.15f, 7.77f, 4.64f, 27.17f, 5.31f, 40.04f)
                    curveToRelative(-8.19f, -22.59f, -21.71f, -54.59f, -36.04f, -58.89f)
                    curveToRelative(-26.29f, -7.88f, -55.03f, -1.76f, -55.74f, 2.08f)
                    curveToRelative(-0.59f, 3.14f, 41.98f, 7.08f, 54.9f, 25.75f)
                    curveToRelative(3.4f, 4.91f, 10.09f, 13.85f, 13.51f, 20.89f)
                    curveToRelative(-7.5f, -5.85f, -15.91f, -10.45f, -15.91f, -10.45f)
                    lineToRelative(-2.72f, 4.93f)
                    reflectiveCurveToRelative(6.14f, 3.18f, 14.33f, 10.56f)
                    curveToRelative(3.57f, 3.21f, 7.28f, 8.33f, 10.33f, 13.1f)
                    curveToRelative(3.8f, 9.13f, 6.17f, 15.4f, 7.46f, 18.86f)
                    curveToRelative(-6.1f, -6.93f, -16.27f, -17.59f, -23.29f, -20.25f)
                    curveToRelative(-10.58f, -4.01f, -30.64f, -6.94f, -29.58f, -4.24f)
                    curveToRelative(1.96f, 4.97f, 15.21f, 8.2f, 24.27f, 17.03f)
                    curveToRelative(5.7f, 7.36f, 11.95f, 12.12f, 19.29f, 17.35f)
                    curveToRelative(2.79f, 2.06f, 4.73f, 4.36f, 4.73f, 4.36f)
                    lineToRelative(-5.43f, -3.61f)
                    lineToRelative(-5.2f, -3.27f)
                    curveToRelative(-2.9f, -1.66f, -6.67f, -3.61f, -9.6f, -4.27f)
                    curveToRelative(-5.43f, -1.22f, -17.02f, -2.13f, -17.28f, 0.67f)
                    curveToRelative(-0.1f, 1.15f, 9.87f, 2.78f, 14.27f, 5.76f)
                    curveToRelative(4.41f, 2.98f, 12.02f, 10.98f, 12.02f, 10.98f)
                    lineToRelative(26.71f, -11.4f)
                    reflectiveCurveToRelative(24.4f, 0.72f, 29.09f, 2.28f)
                    curveToRelative(0f, 0f, 28.93f, -71.89f, 66.83f, -68.32f)
                    curveToRelative(37.89f, 3.57f, 31.87f, 65.02f, 34.22f, 63.56f)
                    curveToRelative(7.36f, -4.55f, 23.35f, -76.77f, -30.85f, -85.87f)
                    curveToRelative(-14.7f, -2.47f, -36.65f, 13.19f, -47.98f, 21.81f)
                    curveToRelative(6.7f, -7.36f, 21.33f, -25.2f, 28.28f, -34.07f)
                    curveToRelative(11.18f, -14.27f, 17.53f, -47.79f, 14.07f, -46.82f)
                    curveToRelative(-3.45f, 0.98f, -12.14f, 19.9f, -23.64f, 32.41f)
                    curveToRelative(-11.5f, 12.52f, -27.03f, 24.57f, -37.28f, 41.86f)
                    curveToRelative(-2.33f, 3.94f, -10.58f, 24.22f, -12.86f, 30.2f)
                    curveToRelative(0.45f, -1.84f, 5.69f, -18.75f, 6.18f, -20.18f)
                    curveToRelative(7.61f, -22.02f, 26.35f, -44.33f, 26.71f, -81.6f)
                    curveToRelative(0.33f, -34.94f, -32.51f, -76.94f, -42.13f, -78.08f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(178.76f, 282.6f)
                    curveToRelative(-1.88f, -0.13f, -12.48f, 8.91f, -16.16f, 16.09f)
                    curveToRelative(-3.14f, 6.12f, -4.76f, 19.3f, -5.24f, 23.77f)
                    lineToRelative(-1.34f, 1.06f)
                    lineToRelative(0.75f, -15.35f)
                    reflectiveCurveToRelative(-1.64f, -6.66f, -6.96f, -8.01f)
                    curveToRelative(-5.42f, -1.38f, -7.79f, 0.68f, -7.79f, 0.68f)
                    reflectiveCurveToRelative(-3.23f, 3.22f, -6.13f, 2.46f)
                    curveToRelative(-2.9f, -0.77f, -4.51f, -5.79f, -4.51f, -5.79f)
                    reflectiveCurveToRelative(-1.8f, -4.32f, -8.82f, -3.52f)
                    curveToRelative(-7.02f, 0.81f, -6.13f, 3.14f, -6.13f, 3.14f)
                    lineToRelative(-1.76f, -1.95f)
                    reflectiveCurveToRelative(-4.25f, -2.91f, -10f, 0.52f)
                    curveToRelative(-3.65f, 2.18f, -3.35f, 5.24f, -3.47f, 6.99f)
                    curveToRelative(3.51f, 2.77f, 7.51f, 5.24f, 10.64f, 8.69f)
                    lineToRelative(-5.44f, -3.61f)
                    lineToRelative(-1.11f, -0.7f)
                    curveToRelative(-6.8f, -0.03f, -7.21f, 8.73f, -5.59f, 9.9f)
                    lineToRelative(22.67f, 16.18f)
                    curveToRelative(-6.35f, -2.88f, -11.89f, -5.03f, -14.46f, -5.11f)
                    curveToRelative(-8.07f, -0.26f, -21.45f, 2.27f, -20.85f, 5.5f)
                    curveToRelative(0.35f, 1.95f, 12.56f, 0.1f, 21.86f, 1.74f)
                    curveToRelative(9.3f, 1.64f, 23.51f, 10.65f, 31.22f, 12.2f)
                    curveToRelative(4.23f, 0.86f, 7.72f, 1.18f, 10.04f, 1.29f)
                    lineToRelative(26.46f, 12.71f)
                    lineToRelative(1.1f, -10.27f)
                    lineToRelative(-17.49f, -19.7f)
                    curveToRelative(4.17f, -3.69f, 9.37f, -8.15f, 12.78f, -10.54f)
                    curveToRelative(6.44f, -4.51f, 19.24f, -10.22f, 19.24f, -10.22f)
                    lineToRelative(-4.14f, -6.75f)
                    reflectiveCurveToRelative(-12.5f, 5.49f, -19.1f, 9.74f)
                    curveToRelative(-0.39f, 0.25f, -0.81f, 0.53f, -1.25f, 0.83f)
                    curveToRelative(1.15f, -5.14f, 2.4f, -10.6f, 3.16f, -13.44f)
                    curveToRelative(1.68f, -6.28f, 10.52f, -17.09f, 7.97f, -18.52f)
                    arcToRelative(0.34f, 0.34f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.15f, -0.04f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFB93C3C))) {
                    moveTo(64.09f, 231.65f)
                    curveToRelative(-14.18f, 0f, -24.99f, 3.02f, -24.99f, 11.55f)
                    curveToRelative(0f, 8.52f, 10.03f, 16.67f, 24.2f, 16.67f)
                    curveToRelative(14.18f, 0f, 24.8f, -2.57f, 24.8f, -11.09f)
                    curveToRelative(0f, -8.53f, -9.84f, -17.13f, -24.02f, -17.13f)
                    close()
                    moveTo(85.4f, 249.2f)
                    curveToRelative(0f, 0.29f, -0.02f, 0.57f, -0.07f, 0.84f)
                    curveToRelative(-1.69f, -0.67f, -5.41f, -2.09f, -7.4f, -2.56f)
                    curveToRelative(-2.63f, -0.62f, -5.35f, -0.56f, -5.35f, -0.56f)
                    curveToRelative(-3.12f, 0.16f, -3.05f, 1.05f, -1.35f, 2.34f)
                    curveToRelative(0.39f, 0.3f, 3.03f, 1.58f, 5.21f, 2.49f)
                    curveToRelative(1.66f, 0.7f, 4.19f, 2.1f, 5.68f, 2.77f)
                    curveToRelative(-3.06f, 1.72f, -7.8f, 1.74f, -13.29f, 0.64f)
                    curveToRelative(-9.65f, -1.94f, -13.59f, -7.24f, -11.3f, -11.59f)
                    curveToRelative(2.92f, -5.55f, 11.68f, -4.42f, 13.91f, -4.2f)
                    curveToRelative(6.55f, 0.62f, 13.97f, 6.25f, 13.97f, 9.82f)
                    close()
                    moveTo(203.85f, 263.29f)
                    curveToRelative(-1.33f, 0.12f, -1.04f, 5.44f, -3.56f, 9.7f)
                    curveToRelative(-2.52f, 4.27f, -10.43f, 10.66f, -12.71f, 14.64f)
                    curveToRelative(-2.28f, 3.97f, -7.62f, 12.18f, -3.16f, 24.64f)
                    curveToRelative(4.45f, 12.46f, 21.8f, 13.01f, 26.25f, 13.26f)
                    curveToRelative(4.45f, 0.25f, 12.66f, -1.44f, 17.83f, -0.9f)
                    curveToRelative(5.18f, 0.53f, 13.82f, 6.28f, 14.76f, 4.52f)
                    curveToRelative(1.67f, -3.13f, -5.74f, -11.39f, -10.5f, -14.05f)
                    curveToRelative(-4.76f, -2.67f, -15.99f, -0.9f, -15.99f, -0.9f)
                    reflectiveCurveToRelative(11.24f, -5.54f, 14.79f, -11.06f)
                    curveToRelative(3.56f, -5.53f, 6.52f, -13.74f, 3.55f, -17.64f)
                    curveToRelative(-2.66f, -3.49f, -12.93f, -4.68f, -18.97f, -3.89f)
                    curveToRelative(-6.04f, 0.78f, -13.94f, 4.59f, -13.94f, 4.59f)
                    reflectiveCurveToRelative(6.1f, -4.39f, 6.79f, -9.71f)
                    curveToRelative(0.7f, -5.31f, -2.91f, -13.39f, -5.15f, -13.19f)
                    close()
                    moveTo(213.14f, 287.44f)
                    curveToRelative(6.51f, 0.03f, 14.45f, 1.82f, 15.3f, 3.23f)
                    curveToRelative(1.66f, 2.78f, -3.01f, 12.54f, -8.29f, 16.47f)
                    curveToRelative(-5.29f, 3.94f, -12.95f, 5.98f, -17.7f, 3.4f)
                    arcToRelative(12.82f, 12.82f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.92f, -2.14f)
                    curveToRelative(3.02f, -0.82f, 10.01f, -2.86f, 12.52f, -4.68f)
                    curveToRelative(3.23f, -2.33f, 5.59f, -5.14f, 4.69f, -6.59f)
                    curveToRelative(-0.65f, -1.04f, -4.68f, -1.42f, -8.53f, -0.29f)
                    curveToRelative(-3.01f, 0.89f, -8.98f, 4.62f, -11.51f, 6.25f)
                    arcToRelative(10.97f, 10.97f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.12f, -4.26f)
                    curveToRelative(1.04f, -4.77f, 4.51f, -8.92f, 10.63f, -10.77f)
                    curveToRelative(1.53f, -0.46f, 3.54f, -0.65f, 5.71f, -0.63f)
                    close()
                }
            }
        }.build()

        return _40Orchid!!
    }

@Suppress("ObjectPropertyName")
private var _40Orchid: ImageVector? = null
