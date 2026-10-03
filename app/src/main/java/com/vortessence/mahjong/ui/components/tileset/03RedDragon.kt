@file:Suppress("ObjectPropertyName", "UnusedReceiverParameter", "unused")

package com.vortessence.mahjong.ui.components.tileset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MahjongTiles.`03RedDragon`: ImageVector
    get() {
        if (_03RedDragon != null) {
            return _03RedDragon!!
        }
        _03RedDragon = ImageVector.Builder(
            name = "03RedDragon",
            defaultWidth = 301.dp,
            defaultHeight = 420.dp,
            viewportWidth = 301f,
            viewportHeight = 420f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(43.12f, 38f)
                    horizontalLineToRelative(215f)
                    verticalLineToRelative(344f)
                    lineTo(43.12f, 382f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFFB93C3C))) {
                    moveTo(136.72f, 38.03f)
                    curveToRelative(-2.44f, -0.52f, -3.5f, 5.34f, -7.49f, 9.14f)
                    curveToRelative(-3.47f, 3.3f, -8.82f, 4.43f, -8.8f, 7.08f)
                    curveToRelative(0.04f, 5.7f, 14.13f, 8.29f, 14.13f, 8.29f)
                    reflectiveCurveToRelative(-0.68f, 26.62f, -1.31f, 63.25f)
                    arcToRelative(167.31f, 167.31f, 0f, isMoreThanHalf = false, isPositiveArc = false, -6.12f, 2.22f)
                    curveToRelative(-19.48f, 7.56f, -31.21f, 15.54f, -39.6f, 18.05f)
                    curveToRelative(-3.27f, 0.98f, -6.04f, -1.93f, -8.25f, -2.84f)
                    curveToRelative(-2.24f, -0.93f, -6.21f, -1.99f, -8.62f, -1.44f)
                    curveToRelative(-1.85f, 0.42f, -2.48f, 1.9f, -2.53f, 4.41f)
                    curveToRelative(0.86f, 5.31f, 4.15f, 9.02f, 8.28f, 15.71f)
                    curveToRelative(3.55f, 5.76f, 4.58f, 13.88f, 7.51f, 22.27f)
                    curveToRelative(2.06f, 5.91f, 5.32f, 13.21f, 8.46f, 18.63f)
                    curveToRelative(6.16f, 10.62f, 12.88f, 17.58f, 17.04f, 16.94f)
                    curveToRelative(6.29f, -0.97f, 1.95f, -6.33f, 6.49f, -12.92f)
                    curveToRelative(1.64f, -2.37f, 8.41f, -4.45f, 16.5f, -6.02f)
                    curveToRelative(-0.5f, 88.05f, 1.02f, 184.68f, 10.61f, 180.64f)
                    curveToRelative(9.84f, -4.14f, 14.25f, -100.02f, 17.55f, -183.61f)
                    curveToRelative(3.55f, 0f, 6.09f, 0.26f, 6.7f, 0.86f)
                    curveToRelative(2.9f, 2.83f, -0.66f, 8.7f, 1.54f, 12.08f)
                    curveToRelative(2.2f, 3.38f, 6.54f, 6.02f, 15.22f, 5.39f)
                    curveToRelative(4.19f, -0.3f, 22.05f, -9.68f, 35.08f, -23.39f)
                    curveToRelative(13.95f, -14.66f, 16.97f, -30.75f, 22.34f, -33.77f)
                    curveToRelative(5.38f, -3.03f, 14.71f, 0.15f, 16.5f, -6.9f)
                    curveToRelative(1.39f, -5.48f, -22.33f, -19.23f, -38.79f, -33.81f)
                    curveToRelative(-5.06f, -4.48f, -18.6f, -3.97f, -23.68f, -4.43f)
                    arcToRelative(33.61f, 33.61f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.56f, -0.12f)
                    curveToRelative(-6.71f, -0.09f, -17.29f, 1.32f, -28.83f, 3.71f)
                    curveToRelative(1.37f, -25.06f, 2.6f, -42.28f, 2.6f, -42.28f)
                    reflectiveCurveToRelative(15.26f, -1.86f, 16.61f, -9.56f)
                    curveToRelative(0.41f, -2.37f, -2.96f, -4.82f, -9.58f, -6.99f)
                    curveToRelative(-5.22f, -1.71f, -13.04f, -2.97f, -18.57f, -5.7f)
                    curveToRelative(-11.43f, -5.64f, -13.87f, -13.92f, -18.43f, -14.89f)
                    close()
                    moveTo(191.78f, 135.07f)
                    curveToRelative(9.12f, 0.07f, 15.64f, 1.38f, 18.44f, 3.31f)
                    curveToRelative(11.19f, 7.73f, -5.99f, 29.4f, -5.99f, 29.4f)
                    reflectiveCurveToRelative(-18.33f, 28.3f, -21.83f, 27.85f)
                    curveToRelative(-1.74f, -0.22f, -1.65f, -2.73f, -3.64f, -6.07f)
                    curveToRelative(-2.01f, -3.38f, -6.12f, -7.61f, -11.19f, -8.9f)
                    curveToRelative(-1.96f, -0.5f, -3.9f, -1.1f, -6.27f, -1.46f)
                    curveToRelative(0.26f, -6.71f, 0.57f, -14.13f, 0.82f, -20.49f)
                    curveToRelative(0.29f, -7.4f, 0.62f, -13.91f, 0.94f, -20.68f)
                    curveToRelative(1.1f, -0.21f, 2.19f, -0.43f, 3.27f, -0.62f)
                    curveToRelative(9.69f, -1.71f, 18.35f, -2.39f, 25.44f, -2.34f)
                    close()
                    moveTo(132.94f, 145.62f)
                    curveToRelative(-0.18f, 12.39f, -0.3f, 24.76f, -0.42f, 38.38f)
                    curveToRelative(-9.8f, 2.8f, -18.93f, 5.4f, -19.43f, 3.36f)
                    curveToRelative(-1.43f, -5.81f, -9.48f, -25.72f, -9.18f, -30.22f)
                    curveToRelative(0.1f, -1.51f, 12.88f, -6.63f, 29.03f, -11.53f)
                    close()
                }
            }
        }.build()

        return _03RedDragon!!
    }

@Suppress("ObjectPropertyName")
private var _03RedDragon: ImageVector? = null
