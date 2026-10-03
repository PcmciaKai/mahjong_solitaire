@file:Suppress("ObjectPropertyName", "UnusedReceiverParameter", "unused")

package com.vortessence.mahjong.ui.components.tileset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MahjongTiles.`01WhiteDragon`: ImageVector
    get() {
        if (_01WhiteDragon != null) {
            return _01WhiteDragon!!
        }
        _01WhiteDragon = ImageVector.Builder(
            name = "01WhiteDragon",
            defaultWidth = 300.dp,
            defaultHeight = 420.dp,
            viewportWidth = 300f,
            viewportHeight = 420f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(38.74f, 51.02f)
                    horizontalLineToRelative(222.51f)
                    verticalLineToRelative(317.95f)
                    lineTo(38.74f, 368.98f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(76.02f, 51.03f)
                    arcToRelative(37.19f, 37.19f, 0f, isMoreThanHalf = false, isPositiveArc = false, -37.28f, 37.27f)
                    verticalLineToRelative(243.41f)
                    arcToRelative(37.19f, 37.19f, 0f, isMoreThanHalf = false, isPositiveArc = false, 37.28f, 37.27f)
                    horizontalLineToRelative(147.96f)
                    arcToRelative(37.19f, 37.19f, 0f, isMoreThanHalf = false, isPositiveArc = false, 26.38f, -10.89f)
                    arcToRelative(37.21f, 37.21f, 0f, isMoreThanHalf = false, isPositiveArc = false, 8.08f, -12.1f)
                    arcToRelative(37.2f, 37.2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.82f, -14.27f)
                    lineTo(261.26f, 88.29f)
                    arcToRelative(37.19f, 37.19f, 0f, isMoreThanHalf = false, isPositiveArc = false, -22.99f, -34.46f)
                    arcToRelative(37.19f, 37.19f, 0f, isMoreThanHalf = false, isPositiveArc = false, -14.27f, -2.82f)
                    lineToRelative(-147.96f, 0f)
                    close()
                    moveTo(72.35f, 69.94f)
                    horizontalLineToRelative(155.3f)
                    arcToRelative(12.39f, 12.39f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11.48f, 7.66f)
                    curveToRelative(0.62f, 1.51f, 0.94f, 3.13f, 0.94f, 4.76f)
                    verticalLineToRelative(255.27f)
                    arcToRelative(12.39f, 12.39f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.63f, 8.79f)
                    arcToRelative(12.4f, 12.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -8.79f, 3.63f)
                    lineTo(72.35f, 350.06f)
                    arcToRelative(12.39f, 12.39f, 0f, isMoreThanHalf = false, isPositiveArc = true, -8.79f, -3.63f)
                    arcToRelative(12.4f, 12.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.63f, -8.79f)
                    lineTo(59.93f, 82.37f)
                    arcToRelative(12.4f, 12.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7.66f, -11.48f)
                    arcToRelative(12.4f, 12.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.76f, -0.94f)
                    close()
                    moveTo(79.69f, 77.99f)
                    arcToRelative(12.68f, 12.68f, 0f, isMoreThanHalf = false, isPositiveArc = false, -12.71f, 12.7f)
                    lineTo(66.98f, 329.3f)
                    arcToRelative(12.68f, 12.68f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12.71f, 12.71f)
                    horizontalLineToRelative(140.62f)
                    arcToRelative(12.68f, 12.68f, 0f, isMoreThanHalf = false, isPositiveArc = false, 8.99f, -3.71f)
                    arcToRelative(12.68f, 12.68f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.71f, -8.99f)
                    lineTo(233.02f, 90.7f)
                    arcToRelative(12.7f, 12.7f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.71f, -8.99f)
                    arcToRelative(12.69f, 12.69f, 0f, isMoreThanHalf = false, isPositiveArc = false, -8.99f, -3.71f)
                    lineTo(79.69f, 77.99f)
                    close()
                    moveTo(82.51f, 87.45f)
                    horizontalLineToRelative(14.83f)
                    lineToRelative(-19.35f, 19.03f)
                    lineTo(78f, 91.97f)
                    arcToRelative(4.51f, 4.51f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.52f, -4.52f)
                    close()
                    moveTo(111.6f, 87.45f)
                    horizontalLineToRelative(75.6f)
                    lineToRelative(34.24f, 33.67f)
                    verticalLineToRelative(177.86f)
                    lineToRelative(-34.42f, 33.85f)
                    horizontalLineToRelative(-75.24f)
                    lineToRelative(-33.78f, -33.22f)
                    lineTo(78f, 120.49f)
                    lineTo(111.6f, 87.45f)
                    close()
                    moveTo(201.46f, 87.45f)
                    horizontalLineToRelative(15.47f)
                    arcToRelative(4.51f, 4.51f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.52f, 4.52f)
                    verticalLineToRelative(15.14f)
                    lineToRelative(-19.99f, -19.66f)
                    close()
                    moveTo(221.44f, 312.99f)
                    verticalLineToRelative(15.32f)
                    arcToRelative(4.51f, 4.51f, 0f, isMoreThanHalf = false, isPositiveArc = true, -4.52f, 4.52f)
                    horizontalLineToRelative(-15.65f)
                    lineToRelative(20.17f, -19.84f)
                    close()
                    moveTo(78f, 313.62f)
                    lineTo(97.53f, 332.83f)
                    lineTo(82.51f, 332.83f)
                    arcToRelative(4.52f, 4.52f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.73f, -0.34f)
                    arcToRelative(4.51f, 4.51f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.79f, -4.18f)
                    lineTo(78f, 313.62f)
                    close()
                }
            }
        }.build()

        return _01WhiteDragon!!
    }

@Suppress("ObjectPropertyName")
private var _01WhiteDragon: ImageVector? = null
