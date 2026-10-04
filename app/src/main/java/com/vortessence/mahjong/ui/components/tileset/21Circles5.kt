@file:Suppress("ObjectPropertyName", "UnusedReceiverParameter", "unused")

package com.vortessence.mahjong.ui.components.tileset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MahjongTiles.`21Circles5`: ImageVector
    get() {
        if (_21Circles5 != null) {
            return _21Circles5!!
        }
        _21Circles5 = ImageVector.Builder(
            name = "21Circles5",
            defaultWidth = 300.dp,
            defaultHeight = 420.dp,
            viewportWidth = 300f,
            viewportHeight = 420f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(53f, 73.5f)
                    horizontalLineToRelative(194f)
                    verticalLineToRelative(273f)
                    lineTo(53f, 346.5f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(95.64f, 73.5f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, -30.15f, 12.48f)
                    arcTo(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, 53f, 116.1f)
                    arcToRelative(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12.49f, 30.12f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, 60.3f, 0f)
                    arcToRelative(42.58f, 42.58f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12.49f, -30.12f)
                    arcToRelative(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, -12.49f, -30.12f)
                    arcToRelative(42.64f, 42.64f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.83f, -9.23f)
                    arcTo(42.68f, 42.68f, 0f, isMoreThanHalf = false, isPositiveArc = false, 95.64f, 73.5f)
                    close()
                    moveTo(95.64f, 83.7f)
                    curveToRelative(8.6f, 0f, 16.85f, 3.41f, 22.93f, 9.49f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 45.82f)
                    arcToRelative(32.45f, 32.45f, 0f, isMoreThanHalf = false, isPositiveArc = true, -45.87f, 0f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, -22.91f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, -22.91f)
                    arcToRelative(32.45f, 32.45f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22.93f, -9.49f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(95.64f, 90.3f)
                    arcToRelative(25.84f, 25.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, -18.26f, 7.56f)
                    arcToRelative(25.79f, 25.79f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.56f, 18.24f)
                    arcToRelative(25.79f, 25.79f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.56f, 18.24f)
                    arcToRelative(25.84f, 25.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, 28.15f, 5.59f)
                    arcToRelative(25.82f, 25.82f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.98f, -13.96f)
                    arcToRelative(25.77f, 25.77f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -19.75f)
                    arcToRelative(25.8f, 25.8f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.98f, -13.96f)
                    arcToRelative(25.85f, 25.85f, 0f, isMoreThanHalf = false, isPositiveArc = false, -9.88f, -1.96f)
                    close()
                    moveTo(95.64f, 102.9f)
                    curveToRelative(3.5f, 0f, 6.86f, 1.39f, 9.34f, 3.87f)
                    arcToRelative(13.19f, 13.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 18.67f)
                    arcToRelative(13.22f, 13.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, -18.69f, 0f)
                    arcToRelative(13.19f, 13.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -18.67f)
                    arcToRelative(13.22f, 13.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.34f, -3.87f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(95.64f, 123.5f)
                    arcToRelative(7.41f, 7.41f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.41f, -7.4f)
                    curveToRelative(0f, -4.09f, -3.32f, -7.4f, -7.41f, -7.4f)
                    reflectiveCurveToRelative(-7.41f, 3.31f, -7.41f, 7.4f)
                    curveToRelative(0f, 4.09f, 3.32f, 7.4f, 7.41f, 7.4f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(204.36f, 73.5f)
                    arcToRelative(42.68f, 42.68f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.32f, 3.24f)
                    arcToRelative(42.64f, 42.64f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.83f, 9.23f)
                    arcToRelative(42.59f, 42.59f, 0f, isMoreThanHalf = false, isPositiveArc = false, -12.49f, 30.12f)
                    arcToRelative(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12.49f, 30.12f)
                    arcToRelative(42.64f, 42.64f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.83f, 9.23f)
                    arcToRelative(42.68f, 42.68f, 0f, isMoreThanHalf = false, isPositiveArc = false, 32.64f, 0f)
                    arcToRelative(42.64f, 42.64f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.83f, -9.23f)
                    arcTo(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, 247f, 116.1f)
                    arcToRelative(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, -12.49f, -30.12f)
                    arcTo(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, 204.36f, 73.5f)
                    close()
                    moveTo(204.36f, 83.7f)
                    curveToRelative(8.6f, 0f, 16.85f, 3.41f, 22.93f, 9.49f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, 22.91f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, 22.91f)
                    arcToRelative(32.45f, 32.45f, 0f, isMoreThanHalf = false, isPositiveArc = true, -45.86f, 0f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, -22.91f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, -22.91f)
                    arcToRelative(32.45f, 32.45f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22.93f, -9.49f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(204.36f, 90.3f)
                    arcToRelative(25.84f, 25.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, -18.26f, 7.56f)
                    arcToRelative(25.79f, 25.79f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.56f, 18.24f)
                    arcToRelative(25.79f, 25.79f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.56f, 18.24f)
                    arcToRelative(25.84f, 25.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, 28.15f, 5.59f)
                    arcToRelative(25.81f, 25.81f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.94f, -23.84f)
                    arcToRelative(25.8f, 25.8f, 0f, isMoreThanHalf = false, isPositiveArc = false, -15.94f, -23.84f)
                    arcToRelative(25.85f, 25.85f, 0f, isMoreThanHalf = false, isPositiveArc = false, -9.88f, -1.96f)
                    close()
                    moveTo(204.36f, 102.9f)
                    curveToRelative(3.5f, 0f, 6.86f, 1.39f, 9.34f, 3.87f)
                    arcToRelative(13.2f, 13.2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.87f, 9.33f)
                    curveToRelative(0f, 3.5f, -1.39f, 6.86f, -3.87f, 9.33f)
                    arcToRelative(13.22f, 13.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, -18.69f, 0f)
                    arcToRelative(13.19f, 13.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -18.67f)
                    arcToRelative(13.22f, 13.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.34f, -3.87f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(204.36f, 123.5f)
                    arcToRelative(7.41f, 7.41f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.41f, -7.4f)
                    curveToRelative(0f, -4.09f, -3.32f, -7.4f, -7.41f, -7.4f)
                    reflectiveCurveToRelative(-7.41f, 3.31f, -7.41f, 7.4f)
                    arcToRelative(7.41f, 7.41f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.41f, 7.4f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(204.36f, 261.3f)
                    arcToRelative(42.68f, 42.68f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.32f, 3.24f)
                    arcToRelative(42.64f, 42.64f, 0f, isMoreThanHalf = false, isPositiveArc = false, -23.08f, 23.06f)
                    arcToRelative(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 32.6f)
                    arcToRelative(42.6f, 42.6f, 0f, isMoreThanHalf = false, isPositiveArc = false, 23.08f, 23.06f)
                    arcToRelative(42.68f, 42.68f, 0f, isMoreThanHalf = false, isPositiveArc = false, 46.47f, -9.23f)
                    arcTo(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, 247f, 303.9f)
                    arcToRelative(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, -12.49f, -30.12f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, -30.15f, -12.48f)
                    close()
                    moveTo(204.36f, 271.5f)
                    curveToRelative(8.6f, 0f, 16.85f, 3.41f, 22.93f, 9.49f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, 22.91f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, 22.91f)
                    arcToRelative(32.45f, 32.45f, 0f, isMoreThanHalf = false, isPositiveArc = true, -22.93f, 9.49f)
                    arcToRelative(32.45f, 32.45f, 0f, isMoreThanHalf = false, isPositiveArc = true, -22.93f, -9.49f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, -22.91f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, -22.91f)
                    arcToRelative(32.45f, 32.45f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22.93f, -9.49f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(204.36f, 278.1f)
                    arcToRelative(25.84f, 25.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, -18.26f, 7.56f)
                    arcToRelative(25.79f, 25.79f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.56f, 18.24f)
                    arcToRelative(25.79f, 25.79f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.56f, 18.24f)
                    arcToRelative(25.84f, 25.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, 28.15f, 5.59f)
                    arcToRelative(25.81f, 25.81f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.94f, -23.84f)
                    arcToRelative(25.77f, 25.77f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.56f, -18.24f)
                    arcToRelative(25.84f, 25.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, -18.26f, -7.56f)
                    close()
                    moveTo(204.36f, 290.7f)
                    curveToRelative(3.5f, 0f, 6.86f, 1.39f, 9.34f, 3.87f)
                    arcToRelative(13.2f, 13.2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.87f, 9.33f)
                    curveToRelative(0f, 3.5f, -1.39f, 6.86f, -3.87f, 9.33f)
                    arcToRelative(13.22f, 13.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.34f, 3.87f)
                    curveToRelative(-3.51f, 0f, -6.86f, -1.39f, -9.34f, -3.87f)
                    arcToRelative(13.19f, 13.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -18.67f)
                    arcToRelative(13.22f, 13.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.34f, -3.87f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(204.36f, 311.3f)
                    curveToRelative(4.09f, 0f, 7.41f, -3.31f, 7.41f, -7.4f)
                    arcToRelative(7.41f, 7.41f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.41f, -7.4f)
                    arcToRelative(7.41f, 7.41f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.41f, 7.4f)
                    curveToRelative(0f, 4.09f, 3.32f, 7.4f, 7.41f, 7.4f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(95.64f, 261.3f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, -30.15f, 12.48f)
                    arcTo(42.59f, 42.59f, 0f, isMoreThanHalf = false, isPositiveArc = false, 53f, 303.9f)
                    arcToRelative(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.24f, 16.3f)
                    arcToRelative(42.6f, 42.6f, 0f, isMoreThanHalf = false, isPositiveArc = false, 23.08f, 23.06f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, 46.47f, -9.23f)
                    arcToRelative(42.58f, 42.58f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12.49f, -30.12f)
                    arcToRelative(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, -12.49f, -30.12f)
                    arcToRelative(42.64f, 42.64f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.83f, -9.23f)
                    arcToRelative(42.68f, 42.68f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.32f, -3.24f)
                    close()
                    moveTo(95.64f, 271.5f)
                    curveToRelative(8.6f, 0f, 16.85f, 3.41f, 22.93f, 9.49f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 45.82f)
                    arcToRelative(32.45f, 32.45f, 0f, isMoreThanHalf = false, isPositiveArc = true, -22.93f, 9.49f)
                    arcToRelative(32.45f, 32.45f, 0f, isMoreThanHalf = false, isPositiveArc = true, -22.93f, -9.49f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, -22.91f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, -22.91f)
                    arcToRelative(32.45f, 32.45f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22.93f, -9.49f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(95.64f, 278.1f)
                    arcToRelative(25.84f, 25.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, -18.26f, 7.56f)
                    arcToRelative(25.79f, 25.79f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.56f, 18.24f)
                    arcToRelative(25.79f, 25.79f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.56f, 18.24f)
                    arcToRelative(25.84f, 25.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, 28.15f, 5.59f)
                    arcToRelative(25.82f, 25.82f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.98f, -13.96f)
                    arcToRelative(25.77f, 25.77f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -19.75f)
                    arcToRelative(25.8f, 25.8f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.98f, -13.96f)
                    arcToRelative(25.84f, 25.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, -9.88f, -1.96f)
                    close()
                    moveTo(95.64f, 290.7f)
                    arcToRelative(13.22f, 13.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.34f, 3.87f)
                    arcToRelative(13.19f, 13.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 18.67f)
                    arcToRelative(13.22f, 13.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.34f, 3.87f)
                    curveToRelative(-3.5f, 0f, -6.86f, -1.39f, -9.34f, -3.87f)
                    arcToRelative(13.19f, 13.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -18.67f)
                    arcToRelative(13.22f, 13.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.34f, -3.87f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(95.64f, 311.3f)
                    curveToRelative(4.09f, 0f, 7.41f, -3.31f, 7.41f, -7.4f)
                    arcToRelative(7.41f, 7.41f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.41f, -7.4f)
                    curveToRelative(-4.09f, 0f, -7.41f, 3.31f, -7.41f, 7.4f)
                    curveToRelative(0f, 4.09f, 3.32f, 7.4f, 7.41f, 7.4f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFB93C3C))) {
                    moveTo(150f, 167.4f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, -30.15f, 12.48f)
                    arcTo(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, 107.36f, 210f)
                    arcToRelative(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12.49f, 30.12f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, 60.3f, 0f)
                    arcTo(42.59f, 42.59f, 0f, isMoreThanHalf = false, isPositiveArc = false, 192.64f, 210f)
                    arcToRelative(42.59f, 42.59f, 0f, isMoreThanHalf = false, isPositiveArc = false, -12.49f, -30.12f)
                    arcToRelative(42.64f, 42.64f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.83f, -9.23f)
                    arcToRelative(42.68f, 42.68f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.32f, -3.24f)
                    close()
                    moveTo(150f, 177.6f)
                    curveToRelative(8.6f, 0f, 16.85f, 3.41f, 22.93f, 9.49f)
                    arcTo(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, 182.43f, 210f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, 22.91f)
                    arcToRelative(32.45f, 32.45f, 0f, isMoreThanHalf = false, isPositiveArc = true, -22.93f, 9.49f)
                    arcToRelative(32.45f, 32.45f, 0f, isMoreThanHalf = false, isPositiveArc = true, -22.93f, -9.49f)
                    arcTo(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, 117.57f, 210f)
                    arcToRelative(32.38f, 32.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, -22.91f)
                    arcToRelative(32.45f, 32.45f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22.93f, -9.49f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFB93C3C))) {
                    moveTo(150f, 184.2f)
                    arcToRelative(25.84f, 25.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, -18.26f, 7.56f)
                    arcToRelative(25.78f, 25.78f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 36.49f)
                    arcToRelative(25.84f, 25.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, 42.12f, -8.37f)
                    arcToRelative(25.78f, 25.78f, 0f, isMoreThanHalf = false, isPositiveArc = false, -5.6f, -28.12f)
                    arcToRelative(25.85f, 25.85f, 0f, isMoreThanHalf = false, isPositiveArc = false, -18.26f, -7.56f)
                    close()
                    moveTo(150f, 196.8f)
                    arcToRelative(13.22f, 13.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.34f, 3.87f)
                    arcToRelative(13.19f, 13.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 18.67f)
                    arcToRelative(13.22f, 13.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, -18.69f, 0f)
                    arcToRelative(13.19f, 13.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -18.67f)
                    arcToRelative(13.22f, 13.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.34f, -3.87f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFB93C3C))) {
                    moveTo(150f, 217.4f)
                    arcToRelative(7.41f, 7.41f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.41f, -7.4f)
                    arcToRelative(7.41f, 7.41f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.41f, -7.4f)
                    arcToRelative(7.41f, 7.41f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.41f, 7.4f)
                    arcToRelative(7.41f, 7.41f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.41f, 7.4f)
                    close()
                }
            }
        }.build()

        return _21Circles5!!
    }

@Suppress("ObjectPropertyName")
private var _21Circles5: ImageVector? = null
