@file:Suppress("ObjectPropertyName", "UnusedReceiverParameter", "unused")

package com.vortessence.mahjong.ui.components.tileset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MahjongTiles.`18Circles2`: ImageVector
    get() {
        if (_18Circles2 != null) {
            return _18Circles2!!
        }
        _18Circles2 = ImageVector.Builder(
            name = "18Circles2",
            defaultWidth = 300.dp,
            defaultHeight = 420.dp,
            viewportWidth = 300f,
            viewportHeight = 420f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(87.5f, 47.5f)
                    horizontalLineToRelative(125f)
                    verticalLineToRelative(325f)
                    lineTo(87.5f, 372.5f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(150f, 47.5f)
                    arcToRelative(62.48f, 62.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -44.19f, 18.32f)
                    arcTo(62.56f, 62.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, 87.5f, 110.04f)
                    arcToRelative(62.58f, 62.58f, 0f, isMoreThanHalf = false, isPositiveArc = false, 18.3f, 44.22f)
                    arcToRelative(62.49f, 62.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, 44.19f, 18.32f)
                    arcToRelative(62.47f, 62.47f, 0f, isMoreThanHalf = false, isPositiveArc = false, 44.2f, -18.32f)
                    arcToRelative(62.55f, 62.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.55f, -20.29f)
                    arcToRelative(62.59f, 62.59f, 0f, isMoreThanHalf = false, isPositiveArc = false, 4.76f, -23.94f)
                    arcToRelative(62.57f, 62.57f, 0f, isMoreThanHalf = false, isPositiveArc = false, -18.31f, -44.22f)
                    arcToRelative(62.48f, 62.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -44.19f, -18.32f)
                    close()
                    moveTo(150f, 62.19f)
                    arcToRelative(47.81f, 47.81f, 0f, isMoreThanHalf = false, isPositiveArc = true, 33.82f, 14.02f)
                    arcToRelative(47.88f, 47.88f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 67.68f)
                    arcToRelative(47.81f, 47.81f, 0f, isMoreThanHalf = false, isPositiveArc = true, -67.64f, 0f)
                    arcToRelative(47.88f, 47.88f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -67.68f)
                    arcToRelative(47.81f, 47.81f, 0f, isMoreThanHalf = false, isPositiveArc = true, 33.82f, -14.02f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(150f, 68.96f)
                    arcToRelative(41.04f, 41.04f, 0f, isMoreThanHalf = false, isPositiveArc = false, -29.03f, 12.03f)
                    arcToRelative(41.1f, 41.1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -12.02f, 29.05f)
                    arcToRelative(41.1f, 41.1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12.02f, 29.05f)
                    arcToRelative(41.04f, 41.04f, 0f, isMoreThanHalf = false, isPositiveArc = false, 29.03f, 12.03f)
                    arcToRelative(41.04f, 41.04f, 0f, isMoreThanHalf = false, isPositiveArc = false, 29.03f, -12.03f)
                    arcToRelative(41.1f, 41.1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -58.1f)
                    arcToRelative(41.04f, 41.04f, 0f, isMoreThanHalf = false, isPositiveArc = false, -29.03f, -12.03f)
                    close()
                    moveTo(150f, 80.82f)
                    arcToRelative(29.19f, 29.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20.65f, 8.56f)
                    arcToRelative(29.23f, 29.23f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6.33f, 31.85f)
                    arcToRelative(29.22f, 29.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, -15.8f, 15.81f)
                    arcToRelative(29.18f, 29.18f, 0f, isMoreThanHalf = false, isPositiveArc = true, -31.83f, -6.33f)
                    arcToRelative(29.24f, 29.24f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -41.33f)
                    arcToRelative(29.19f, 29.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20.65f, -8.56f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(150f, 87.31f)
                    arcToRelative(22.71f, 22.71f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.06f, 6.66f)
                    arcToRelative(22.74f, 22.74f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 32.15f)
                    arcToRelative(22.71f, 22.71f, 0f, isMoreThanHalf = false, isPositiveArc = false, 32.12f, 0f)
                    arcToRelative(22.74f, 22.74f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0f, -32.14f)
                    arcToRelative(22.71f, 22.71f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.06f, -6.66f)
                    close()
                    moveTo(150f, 97.48f)
                    arcToRelative(12.54f, 12.54f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8.88f, 3.68f)
                    arcToRelative(12.55f, 12.55f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.68f, 8.88f)
                    arcToRelative(12.58f, 12.58f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.67f, 8.89f)
                    arcToRelative(12.56f, 12.56f, 0f, isMoreThanHalf = false, isPositiveArc = true, -8.88f, 3.68f)
                    arcToRelative(12.55f, 12.55f, 0f, isMoreThanHalf = false, isPositiveArc = true, -11.6f, -7.76f)
                    arcToRelative(12.56f, 12.56f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.95f, -4.81f)
                    arcToRelative(12.56f, 12.56f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7.75f, -11.61f)
                    arcToRelative(12.55f, 12.55f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.81f, -0.95f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(150f, 116.68f)
                    arcToRelative(6.63f, 6.63f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6.63f, -6.63f)
                    arcToRelative(6.63f, 6.63f, 0f, isMoreThanHalf = false, isPositiveArc = false, -6.63f, -6.63f)
                    arcToRelative(6.63f, 6.63f, 0f, isMoreThanHalf = false, isPositiveArc = false, -6.63f, 6.63f)
                    arcToRelative(6.63f, 6.63f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6.63f, 6.63f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(150f, 247.41f)
                    arcToRelative(62.47f, 62.47f, 0f, isMoreThanHalf = false, isPositiveArc = false, -44.19f, 18.32f)
                    arcTo(62.57f, 62.57f, 0f, isMoreThanHalf = false, isPositiveArc = false, 87.5f, 309.95f)
                    arcToRelative(62.57f, 62.57f, 0f, isMoreThanHalf = false, isPositiveArc = false, 18.3f, 44.22f)
                    arcToRelative(62.46f, 62.46f, 0f, isMoreThanHalf = false, isPositiveArc = false, 68.11f, 13.56f)
                    arcToRelative(62.5f, 62.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 33.83f, -33.85f)
                    arcToRelative(62.58f, 62.58f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.55f, -68.16f)
                    arcToRelative(62.48f, 62.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -44.19f, -18.32f)
                    close()
                    moveTo(150f, 262.1f)
                    arcToRelative(47.81f, 47.81f, 0f, isMoreThanHalf = false, isPositiveArc = true, 33.82f, 14.02f)
                    arcToRelative(47.88f, 47.88f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 67.68f)
                    arcToRelative(47.81f, 47.81f, 0f, isMoreThanHalf = false, isPositiveArc = true, -33.82f, 14.02f)
                    arcToRelative(47.81f, 47.81f, 0f, isMoreThanHalf = false, isPositiveArc = true, -33.82f, -14.02f)
                    arcToRelative(47.88f, 47.88f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -67.68f)
                    arcToRelative(47.81f, 47.81f, 0f, isMoreThanHalf = false, isPositiveArc = true, 33.82f, -14.02f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(150f, 268.87f)
                    arcToRelative(41.04f, 41.04f, 0f, isMoreThanHalf = false, isPositiveArc = false, -29.03f, 12.03f)
                    arcToRelative(41.1f, 41.1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -12.02f, 29.05f)
                    arcToRelative(41.1f, 41.1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12.02f, 29.05f)
                    arcToRelative(41.04f, 41.04f, 0f, isMoreThanHalf = false, isPositiveArc = false, 58.06f, 0f)
                    arcToRelative(41.1f, 41.1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -58.1f)
                    arcToRelative(41.04f, 41.04f, 0f, isMoreThanHalf = false, isPositiveArc = false, -29.03f, -12.03f)
                    close()
                    moveTo(150f, 280.73f)
                    arcToRelative(29.2f, 29.2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20.65f, 8.56f)
                    arcToRelative(29.22f, 29.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8.55f, 20.67f)
                    arcToRelative(29.24f, 29.24f, 0f, isMoreThanHalf = false, isPositiveArc = true, -8.55f, 20.66f)
                    arcToRelative(29.2f, 29.2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -20.65f, 8.56f)
                    arcToRelative(29.2f, 29.2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -20.65f, -8.56f)
                    arcToRelative(29.24f, 29.24f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -41.33f)
                    arcToRelative(29.2f, 29.2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20.65f, -8.56f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(150f, 287.22f)
                    arcToRelative(22.7f, 22.7f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.06f, 6.66f)
                    arcToRelative(22.74f, 22.74f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 32.15f)
                    arcToRelative(22.71f, 22.71f, 0f, isMoreThanHalf = false, isPositiveArc = false, 32.12f, 0f)
                    arcToRelative(22.74f, 22.74f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0f, -32.14f)
                    arcToRelative(22.71f, 22.71f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.06f, -6.66f)
                    close()
                    moveTo(150f, 297.39f)
                    arcToRelative(12.55f, 12.55f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11.6f, 7.76f)
                    arcToRelative(12.59f, 12.59f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 9.62f)
                    arcToRelative(12.56f, 12.56f, 0f, isMoreThanHalf = false, isPositiveArc = true, -16.41f, 6.8f)
                    arcToRelative(12.56f, 12.56f, 0f, isMoreThanHalf = false, isPositiveArc = true, -7.75f, -11.61f)
                    curveToRelative(0f, -1.65f, 0.32f, -3.29f, 0.96f, -4.81f)
                    arcToRelative(12.55f, 12.55f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11.6f, -7.75f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(150f, 316.59f)
                    arcToRelative(6.63f, 6.63f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6.63f, -6.63f)
                    arcToRelative(6.63f, 6.63f, 0f, isMoreThanHalf = false, isPositiveArc = false, -6.63f, -6.63f)
                    arcToRelative(6.63f, 6.63f, 0f, isMoreThanHalf = false, isPositiveArc = false, -6.63f, 6.63f)
                    arcToRelative(6.63f, 6.63f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6.63f, 6.63f)
                    close()
                }
            }
        }.build()

        return _18Circles2!!
    }

@Suppress("ObjectPropertyName")
private var _18Circles2: ImageVector? = null
