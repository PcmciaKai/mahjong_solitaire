@file:Suppress("ObjectPropertyName", "UnusedReceiverParameter", "unused")

package com.vortessence.mahjong.ui.components.tileset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MahjongTiles.`02GreenDragon`: ImageVector
    get() {
        if (_02GreenDragon != null) {
            return _02GreenDragon!!
        }
        _02GreenDragon = ImageVector.Builder(
            name = "02GreenDragon",
            defaultWidth = 296.dp,
            defaultHeight = 420.dp,
            viewportWidth = 296f,
            viewportHeight = 420f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(19.5f, 83.1f)
                    horizontalLineToRelative(256.63f)
                    verticalLineToRelative(253.79f)
                    lineTo(19.5f, 336.9f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(182.81f, 83.11f)
                    curveToRelative(-1.74f, -0.07f, -3.71f, 0.49f, -5.51f, 0.76f)
                    curveToRelative(-2.85f, 0.44f, -3.62f, -0.49f, -5.27f, 0.12f)
                    curveToRelative(-1.13f, 0.41f, -1.6f, 2.33f, -2.8f, 2.28f)
                    curveToRelative(-1.44f, -0.05f, -1.68f, -3.01f, -3.13f, -2.99f)
                    curveToRelative(-1.66f, 0.03f, -2.16f, 2.57f, -3.5f, 3.56f)
                    curveToRelative(-1.94f, 1.43f, -5.38f, -0.29f, -6.43f, 3.32f)
                    curveToRelative(-1.05f, 3.61f, 6.42f, 5.25f, 9.38f, 9.14f)
                    curveToRelative(1.64f, 2.16f, 1.88f, 2.52f, -0.43f, 8.25f)
                    curveToRelative(-2.31f, 5.72f, -17.48f, 14.95f, -18.05f, 19.85f)
                    curveToRelative(-0.69f, 5.94f, 10.97f, 16.77f, 9.42f, 19.48f)
                    curveToRelative(-1.73f, 3.03f, -27.02f, 14.97f, -27.4f, 15.71f)
                    curveToRelative(-1.19f, 2.33f, -0.57f, 6.21f, 1.53f, 8.29f)
                    curveToRelative(2.63f, 2.6f, 17.38f, -2.91f, 21.48f, -4.59f)
                    curveToRelative(9.83f, -4.05f, 11.6f, -6.34f, 15.24f, -5.68f)
                    curveToRelative(9.65f, 1.75f, 39.25f, 53.78f, 53.61f, 57.43f)
                    curveToRelative(14.36f, 3.65f, 49.17f, -4.48f, 53.52f, -6.38f)
                    curveToRelative(4.34f, -1.9f, -1.25f, -7.76f, -1.25f, -7.76f)
                    reflectiveCurveToRelative(-30.27f, -8.35f, -42.23f, -16.08f)
                    curveToRelative(-11.96f, -7.73f, -44.12f, -34.08f, -43.4f, -38.22f)
                    curveToRelative(0.73f, -4.15f, 12.6f, -7.78f, 15.26f, -7.28f)
                    curveToRelative(2.66f, 0.5f, 13.04f, 13.62f, 16.86f, 10.93f)
                    curveToRelative(1.48f, -1.04f, 3.97f, -5.89f, 2.42f, -11.92f)
                    curveToRelative(-2.43f, -9.52f, -7.68f, -14.66f, -14.99f, -25.49f)
                    curveToRelative(-7.3f, -10.83f, -23.3f, -11.44f, -25.32f, -15.69f)
                    curveToRelative(-2.01f, -4.26f, 7.91f, -12.3f, 3.76f, -16.02f)
                    curveToRelative(-0.78f, -0.7f, -1.74f, -0.97f, -2.78f, -1.02f)
                    close()
                    moveTo(105.74f, 91.48f)
                    curveToRelative(-4.49f, -0.05f, 2.02f, 6.52f, -2.97f, 10.14f)
                    curveToRelative(-5.6f, 4.07f, -18.89f, 3.41f, -16.67f, 7.62f)
                    curveToRelative(4.29f, 8.18f, 23.13f, 9.19f, 24.44f, 13.02f)
                    curveToRelative(1.31f, 3.83f, -16.94f, 35.8f, -22.27f, 36.97f)
                    curveToRelative(-5.33f, 1.17f, -24.05f, -14.05f, -26.33f, -10.71f)
                    curveToRelative(-4.61f, 6.74f, 14.19f, 24.45f, 14.58f, 29.69f)
                    curveToRelative(0.39f, 5.25f, -56.09f, 77.54f, -56.09f, 77.54f)
                    reflectiveCurveToRelative(-3.14f, 2.65f, 2.17f, 4.88f)
                    curveToRelative(5.32f, 2.22f, 52f, -38.5f, 68.97f, -61.73f)
                    curveToRelative(16.97f, -23.23f, 37.28f, -51.44f, 44.24f, -68.61f)
                    curveToRelative(6.96f, -17.17f, -22.55f, -37.67f, -29.01f, -38.71f)
                    curveToRelative(-0.35f, -0.06f, -0.7f, -0.09f, -1.05f, -0.1f)
                    close()
                    moveTo(181.75f, 118.09f)
                    curveToRelative(5.4f, 0.03f, 12.13f, 6.01f, 12.06f, 8.3f)
                    curveToRelative(-0.04f, 1.36f, -17.35f, 11.61f, -18.47f, 11.38f)
                    curveToRelative(-1.12f, -0.23f, -6.66f, -6.76f, -6.39f, -8.68f)
                    curveToRelative(0.27f, -1.92f, 6.8f, -10.61f, 12.28f, -10.98f)
                    curveToRelative(0.17f, -0.01f, 0.34f, -0.02f, 0.52f, -0.02f)
                    close()
                    moveTo(143.44f, 177.99f)
                    arcToRelative(5.93f, 5.93f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.44f, 0.47f)
                    curveToRelative(-7.18f, 2.87f, 1.36f, 26.83f, -1.61f, 37.06f)
                    curveToRelative(-2.96f, 10.23f, -13.95f, 22.74f, -13.95f, 22.74f)
                    reflectiveCurveToRelative(-0.96f, 8.35f, 0.59f, 10.37f)
                    curveToRelative(1.56f, 2.02f, 19.48f, -10.37f, 25.29f, -18.79f)
                    curveToRelative(5.82f, -8.42f, 0.66f, -23.53f, 6.29f, -28.91f)
                    reflectiveCurveToRelative(7.85f, -6.76f, 10.77f, -3.27f)
                    curveToRelative(1.42f, 1.7f, -3.13f, 6.23f, -6.4f, 12.95f)
                    curveToRelative(-3.45f, 7.09f, -5.57f, 16.48f, -2.15f, 21.88f)
                    curveToRelative(6.66f, 10.52f, 33.04f, 13.63f, 38.48f, 9.93f)
                    curveToRelative(5.43f, -3.7f, 2.48f, -18.39f, -3.01f, -22.3f)
                    reflectiveCurveToRelative(-17.9f, 7.98f, -19.44f, 0.7f)
                    curveToRelative(-1.54f, -7.28f, 14.38f, -19f, 13.05f, -26.68f)
                    curveToRelative(-1.32f, -7.68f, -10.61f, -13.39f, -16.16f, -13.46f)
                    curveToRelative(-5.55f, -0.07f, -16.73f, 8.46f, -19.89f, 7.21f)
                    curveToRelative(-2.76f, -1.1f, -4.29f, -9.68f, -9.44f, -9.89f)
                    close()
                    moveTo(120.99f, 186.75f)
                    curveToRelative(-3.71f, 0.14f, -5.58f, 3.73f, -8.54f, 5.31f)
                    curveToRelative(-3.16f, 1.68f, -17.25f, 2.74f, -17.47f, 5.17f)
                    curveToRelative(-0.23f, 2.43f, 2.13f, 6.17f, 6.64f, 8.47f)
                    curveToRelative(4.45f, 2.26f, 11.41f, 0.92f, 11.41f, 1.45f)
                    curveToRelative(0f, 1.06f, -4.88f, 2.76f, -7.67f, 6.13f)
                    curveToRelative(-3.65f, 4.4f, -6.4f, 9.54f, -9.11f, 11.8f)
                    curveToRelative(-4.78f, 3.99f, -23.63f, 6.33f, -24.36f, 9.61f)
                    curveToRelative(-0.73f, 3.28f, 1.32f, 6.18f, 4.45f, 7.96f)
                    curveToRelative(4.61f, 2.63f, 10.98f, 0.52f, 12.04f, 2.18f)
                    curveToRelative(1.76f, 2.79f, -14.93f, 26.61f, -15.02f, 29.3f)
                    curveToRelative(-0.09f, 2.68f, 1.96f, 5.49f, 5.1f, 6f)
                    curveToRelative(3.13f, 0.52f, 13.81f, -8.06f, 18.4f, -9.29f)
                    curveToRelative(4.59f, -1.22f, 3.42f, 13.63f, 3.13f, 15.76f)
                    curveToRelative(-0.28f, 2.13f, -7.71f, 14.45f, -13.94f, 16.28f)
                    curveToRelative(-6.23f, 1.84f, -18.01f, -26.27f, -21.23f, -20.77f)
                    curveToRelative(-3.23f, 5.5f, 7.45f, 34.92f, 7.45f, 34.92f)
                    reflectiveCurveToRelative(3.67f, 11.33f, 11.26f, 12.02f)
                    curveToRelative(7.59f, 0.68f, 18.16f, -7.78f, 24.02f, -14.17f)
                    curveToRelative(5.86f, -6.39f, 13.49f, -20.05f, 15.5f, -26.4f)
                    curveToRelative(2f, -6.36f, 0.03f, -10.82f, -3.31f, -16.31f)
                    curveToRelative(-3.34f, -5.49f, -14.21f, -10.64f, -16.13f, -12.99f)
                    curveToRelative(-0.82f, -1f, 0.27f, -4.35f, 1.43f, -7.64f)
                    curveToRelative(1.55f, -4.42f, 3.77f, -9.48f, 6.79f, -15.86f)
                    curveToRelative(5.27f, -11.14f, 21.95f, -23.64f, 20.8f, -33.72f)
                    curveToRelative(-1.15f, -10.08f, -6.64f, -14.83f, -10.87f, -15.19f)
                    arcToRelative(6.79f, 6.79f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.77f, -0.02f)
                    close()
                    moveTo(165.35f, 243.01f)
                    curveToRelative(-9.58f, 0.44f, -41.36f, 4.87f, -42.13f, 9.16f)
                    curveToRelative(-0.77f, 4.29f, 8.6f, 14.16f, 14.85f, 15.35f)
                    curveToRelative(6.25f, 1.2f, 20.48f, -8.28f, 24.12f, -7.75f)
                    curveToRelative(3.65f, 0.53f, 2.8f, -0.85f, 2.34f, 2.88f)
                    curveToRelative(-0.46f, 3.73f, -3.54f, 12.56f, -7.72f, 12.61f)
                    curveToRelative(-4.18f, 0.06f, -27.13f, -11.42f, -30.8f, -4.91f)
                    curveToRelative(-3.67f, 6.5f, 19.36f, 15.31f, 17.78f, 20.66f)
                    curveToRelative(-1.58f, 5.36f, -36.89f, 34.64f, -36.89f, 34.64f)
                    reflectiveCurveToRelative(-1.75f, 5.49f, 1.35f, 7.49f)
                    curveToRelative(3.09f, 2.01f, 16.56f, -3.48f, 29.35f, -11.3f)
                    curveToRelative(12.59f, -7.7f, 21.4f, -19.72f, 23.37f, -19.71f)
                    curveToRelative(3.97f, 0.03f, 29.65f, 30.09f, 33.74f, 33.26f)
                    curveToRelative(4.09f, 3.18f, 10.78f, 1.08f, 13.57f, -4.45f)
                    curveToRelative(2.79f, -5.53f, 1.52f, -12.51f, -3.62f, -21.24f)
                    curveToRelative(-5.14f, -8.72f, -27.01f, -24.78f, -27.01f, -24.78f)
                    reflectiveCurveToRelative(8.06f, -9.61f, 16.71f, -16.77f)
                    curveToRelative(4.29f, -3.55f, -1.28f, -12.99f, -2.65f, -14.47f)
                    curveToRelative(-3.84f, -4.16f, -16.78f, -11.1f, -26.36f, -10.67f)
                    close()
                }
            }
        }.build()

        return _02GreenDragon!!
    }

@Suppress("ObjectPropertyName")
private var _02GreenDragon: ImageVector? = null
