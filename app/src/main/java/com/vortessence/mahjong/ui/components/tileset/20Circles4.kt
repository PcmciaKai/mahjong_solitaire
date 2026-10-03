@file:Suppress("ObjectPropertyName", "UnusedReceiverParameter", "unused")

package com.vortessence.mahjong.ui.components.tileset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MahjongTiles.`20Circles4`: ImageVector
    get() {
        if (_20Circles4 != null) {
            return _20Circles4!!
        }
        _20Circles4 = ImageVector.Builder(
            name = "20Circles4",
            defaultWidth = 300.dp,
            defaultHeight = 420.dp,
            viewportWidth = 300f,
            viewportHeight = 420f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(53f, 73f)
                    horizontalLineToRelative(194f)
                    verticalLineToRelative(274f)
                    lineTo(53f, 347f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(95.65f, 73f)
                    arcToRelative(42.74f, 42.74f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.32f, 3.24f)
                    arcToRelative(42.66f, 42.66f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.84f, 9.23f)
                    arcToRelative(42.55f, 42.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, -9.25f, 13.81f)
                    arcToRelative(42.47f, 42.47f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 32.57f)
                    arcToRelative(42.55f, 42.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9.25f, 13.81f)
                    arcToRelative(42.66f, 42.66f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.84f, 9.23f)
                    arcToRelative(42.75f, 42.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 32.65f, 0f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.84f, -9.23f)
                    arcToRelative(42.55f, 42.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9.25f, -13.81f)
                    arcToRelative(42.47f, 42.47f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.24f, -16.29f)
                    arcToRelative(42.47f, 42.47f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.24f, -16.29f)
                    arcToRelative(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, -9.25f, -13.81f)
                    arcToRelative(42.66f, 42.66f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.84f, -9.23f)
                    arcTo(42.74f, 42.74f, 0f, isMoreThanHalf = false, isPositiveArc = false, 95.65f, 73f)
                    close()
                    moveTo(95.65f, 83.19f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22.94f, 9.48f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, 22.89f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, 22.89f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, -22.94f, 9.48f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, -22.94f, -9.48f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, -22.89f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, -22.89f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22.94f, -9.48f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(95.65f, 89.78f)
                    arcToRelative(25.86f, 25.86f, 0f, isMoreThanHalf = false, isPositiveArc = false, -18.27f, 7.55f)
                    arcToRelative(25.75f, 25.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.57f, 18.23f)
                    arcToRelative(25.75f, 25.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.57f, 18.23f)
                    arcToRelative(25.86f, 25.86f, 0f, isMoreThanHalf = false, isPositiveArc = false, 18.27f, 7.55f)
                    arcToRelative(25.88f, 25.88f, 0f, isMoreThanHalf = false, isPositiveArc = false, 18.27f, -7.55f)
                    arcToRelative(25.77f, 25.77f, 0f, isMoreThanHalf = false, isPositiveArc = false, 5.6f, -8.36f)
                    arcToRelative(25.72f, 25.72f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -19.73f)
                    arcToRelative(25.76f, 25.76f, 0f, isMoreThanHalf = false, isPositiveArc = false, -5.6f, -8.36f)
                    arcToRelative(25.84f, 25.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, -8.38f, -5.59f)
                    arcToRelative(25.89f, 25.89f, 0f, isMoreThanHalf = false, isPositiveArc = false, -9.89f, -1.96f)
                    close()
                    moveTo(95.65f, 102.37f)
                    curveToRelative(3.51f, 0f, 6.87f, 1.39f, 9.35f, 3.86f)
                    arcToRelative(13.17f, 13.17f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 18.65f)
                    arcToRelative(13.23f, 13.23f, 0f, isMoreThanHalf = false, isPositiveArc = true, -18.69f, 0f)
                    arcToRelative(13.17f, 13.17f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.87f, -9.32f)
                    curveToRelative(0f, -3.5f, 1.39f, -6.85f, 3.87f, -9.32f)
                    arcToRelative(13.23f, 13.23f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.35f, -3.86f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(95.65f, 122.95f)
                    curveToRelative(4.09f, 0f, 7.41f, -3.31f, 7.41f, -7.39f)
                    curveToRelative(0f, -4.08f, -3.32f, -7.39f, -7.41f, -7.39f)
                    reflectiveCurveToRelative(-7.41f, 3.31f, -7.41f, 7.39f)
                    curveToRelative(0f, 4.08f, 3.32f, 7.39f, 7.41f, 7.39f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(204.35f, 73f)
                    arcToRelative(42.74f, 42.74f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.32f, 3.24f)
                    arcToRelative(42.66f, 42.66f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.84f, 9.23f)
                    arcToRelative(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, -9.25f, 13.81f)
                    arcToRelative(42.47f, 42.47f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.24f, 16.29f)
                    arcToRelative(42.47f, 42.47f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.24f, 16.29f)
                    arcToRelative(42.55f, 42.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9.25f, 13.81f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.84f, 9.23f)
                    arcToRelative(42.75f, 42.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 32.65f, 0f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.84f, -9.23f)
                    arcToRelative(42.55f, 42.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9.24f, -13.81f)
                    arcToRelative(42.45f, 42.45f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.25f, -16.29f)
                    arcToRelative(42.45f, 42.45f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.25f, -16.29f)
                    arcToRelative(42.55f, 42.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, -9.24f, -13.81f)
                    arcToRelative(42.66f, 42.66f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.84f, -9.23f)
                    arcTo(42.74f, 42.74f, 0f, isMoreThanHalf = false, isPositiveArc = false, 204.35f, 73f)
                    close()
                    moveTo(204.35f, 83.19f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22.94f, 9.48f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, 22.89f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, 22.89f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, -22.94f, 9.48f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, -22.94f, -9.48f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, -22.89f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, -22.89f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22.94f, -9.48f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(204.35f, 89.78f)
                    arcToRelative(25.86f, 25.86f, 0f, isMoreThanHalf = false, isPositiveArc = false, -18.27f, 7.55f)
                    arcToRelative(25.75f, 25.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.57f, 18.23f)
                    arcToRelative(25.75f, 25.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.57f, 18.23f)
                    arcToRelative(25.86f, 25.86f, 0f, isMoreThanHalf = false, isPositiveArc = false, 18.27f, 7.55f)
                    arcToRelative(25.88f, 25.88f, 0f, isMoreThanHalf = false, isPositiveArc = false, 18.27f, -7.55f)
                    arcToRelative(25.77f, 25.77f, 0f, isMoreThanHalf = false, isPositiveArc = false, 5.6f, -8.36f)
                    arcToRelative(25.72f, 25.72f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -19.73f)
                    arcToRelative(25.76f, 25.76f, 0f, isMoreThanHalf = false, isPositiveArc = false, -5.6f, -8.36f)
                    arcToRelative(25.84f, 25.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, -8.38f, -5.59f)
                    arcToRelative(25.89f, 25.89f, 0f, isMoreThanHalf = false, isPositiveArc = false, -9.89f, -1.96f)
                    close()
                    moveTo(204.35f, 102.37f)
                    curveToRelative(3.51f, 0f, 6.87f, 1.39f, 9.35f, 3.86f)
                    arcToRelative(13.17f, 13.17f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 18.65f)
                    arcToRelative(13.23f, 13.23f, 0f, isMoreThanHalf = false, isPositiveArc = true, -18.69f, 0f)
                    arcToRelative(13.17f, 13.17f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -18.65f)
                    arcToRelative(13.23f, 13.23f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.35f, -3.86f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(204.35f, 122.95f)
                    curveToRelative(4.09f, 0f, 7.41f, -3.31f, 7.41f, -7.39f)
                    curveToRelative(0f, -4.08f, -3.32f, -7.39f, -7.41f, -7.39f)
                    reflectiveCurveToRelative(-7.41f, 3.31f, -7.41f, 7.39f)
                    curveToRelative(0f, 4.08f, 3.32f, 7.39f, 7.41f, 7.39f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(204.35f, 261.88f)
                    arcToRelative(42.75f, 42.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.32f, 3.24f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.84f, 9.23f)
                    arcToRelative(42.55f, 42.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, -9.25f, 13.81f)
                    arcToRelative(42.47f, 42.47f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.24f, 16.29f)
                    arcToRelative(42.47f, 42.47f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.24f, 16.29f)
                    arcToRelative(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9.25f, 13.81f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.84f, 9.23f)
                    arcToRelative(42.74f, 42.74f, 0f, isMoreThanHalf = false, isPositiveArc = false, 32.65f, 0f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.84f, -9.23f)
                    arcToRelative(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9.24f, -13.81f)
                    arcToRelative(42.45f, 42.45f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.25f, -16.29f)
                    arcToRelative(42.45f, 42.45f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.25f, -16.29f)
                    arcToRelative(42.55f, 42.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, -9.24f, -13.81f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.84f, -9.23f)
                    arcToRelative(42.76f, 42.76f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.32f, -3.24f)
                    close()
                    moveTo(204.35f, 272.07f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22.94f, 9.48f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, 22.89f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, 22.89f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, -22.94f, 9.48f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, -22.94f, -9.48f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, -22.89f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, -22.89f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22.94f, -9.48f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(204.35f, 278.66f)
                    arcToRelative(25.86f, 25.86f, 0f, isMoreThanHalf = false, isPositiveArc = false, -18.27f, 7.55f)
                    arcToRelative(25.75f, 25.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.57f, 18.23f)
                    arcToRelative(25.75f, 25.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.57f, 18.23f)
                    arcToRelative(25.86f, 25.86f, 0f, isMoreThanHalf = false, isPositiveArc = false, 18.27f, 7.55f)
                    arcToRelative(25.88f, 25.88f, 0f, isMoreThanHalf = false, isPositiveArc = false, 18.27f, -7.55f)
                    arcToRelative(25.77f, 25.77f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.57f, -18.23f)
                    curveToRelative(0f, -3.38f, -0.67f, -6.74f, -1.97f, -9.86f)
                    arcToRelative(25.77f, 25.77f, 0f, isMoreThanHalf = false, isPositiveArc = false, -5.6f, -8.36f)
                    arcToRelative(25.88f, 25.88f, 0f, isMoreThanHalf = false, isPositiveArc = false, -18.27f, -7.55f)
                    close()
                    moveTo(204.35f, 291.25f)
                    curveToRelative(3.51f, 0f, 6.87f, 1.39f, 9.35f, 3.86f)
                    arcToRelative(13.17f, 13.17f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 18.65f)
                    arcToRelative(13.23f, 13.23f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.35f, 3.86f)
                    arcToRelative(13.23f, 13.23f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.35f, -3.86f)
                    arcToRelative(13.17f, 13.17f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -18.65f)
                    arcToRelative(13.23f, 13.23f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.35f, -3.86f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(204.35f, 311.83f)
                    curveToRelative(4.09f, 0f, 7.41f, -3.31f, 7.41f, -7.39f)
                    curveToRelative(0f, -4.08f, -3.32f, -7.39f, -7.41f, -7.39f)
                    reflectiveCurveToRelative(-7.41f, 3.31f, -7.41f, 7.39f)
                    curveToRelative(0f, 4.08f, 3.32f, 7.39f, 7.41f, 7.39f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(95.65f, 261.88f)
                    arcToRelative(42.75f, 42.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.32f, 3.24f)
                    arcToRelative(42.66f, 42.66f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.84f, 9.23f)
                    arcToRelative(42.55f, 42.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, -9.25f, 13.81f)
                    arcToRelative(42.47f, 42.47f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 32.57f)
                    arcToRelative(42.55f, 42.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9.25f, 13.81f)
                    arcToRelative(42.66f, 42.66f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.84f, 9.23f)
                    arcToRelative(42.74f, 42.74f, 0f, isMoreThanHalf = false, isPositiveArc = false, 32.65f, 0f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.84f, -9.23f)
                    arcToRelative(42.56f, 42.56f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9.25f, -13.81f)
                    arcToRelative(42.47f, 42.47f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.24f, -16.29f)
                    arcToRelative(42.47f, 42.47f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.24f, -16.29f)
                    arcToRelative(42.55f, 42.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, -9.25f, -13.81f)
                    arcToRelative(42.67f, 42.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, -13.84f, -9.23f)
                    arcToRelative(42.76f, 42.76f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.32f, -3.24f)
                    close()
                    moveTo(95.65f, 272.07f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22.94f, 9.48f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, 22.89f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, 22.89f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, -22.94f, 9.48f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, -22.94f, -9.48f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.5f, -22.89f)
                    arcToRelative(32.33f, 32.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.5f, -22.89f)
                    arcToRelative(32.48f, 32.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22.94f, -9.48f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(95.65f, 278.66f)
                    arcToRelative(25.86f, 25.86f, 0f, isMoreThanHalf = false, isPositiveArc = false, -18.27f, 7.55f)
                    arcToRelative(25.75f, 25.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.57f, 18.23f)
                    arcToRelative(25.75f, 25.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.57f, 18.23f)
                    arcToRelative(25.86f, 25.86f, 0f, isMoreThanHalf = false, isPositiveArc = false, 18.27f, 7.55f)
                    arcToRelative(25.88f, 25.88f, 0f, isMoreThanHalf = false, isPositiveArc = false, 18.27f, -7.55f)
                    arcToRelative(25.77f, 25.77f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.57f, -18.23f)
                    curveToRelative(0f, -3.38f, -0.67f, -6.74f, -1.97f, -9.86f)
                    arcToRelative(25.77f, 25.77f, 0f, isMoreThanHalf = false, isPositiveArc = false, -5.6f, -8.36f)
                    arcToRelative(25.88f, 25.88f, 0f, isMoreThanHalf = false, isPositiveArc = false, -18.27f, -7.55f)
                    close()
                    moveTo(95.65f, 291.25f)
                    curveToRelative(3.51f, 0f, 6.87f, 1.39f, 9.35f, 3.86f)
                    arcToRelative(13.17f, 13.17f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 18.65f)
                    arcToRelative(13.23f, 13.23f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.35f, 3.86f)
                    arcToRelative(13.23f, 13.23f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.35f, -3.86f)
                    arcToRelative(13.17f, 13.17f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.87f, -9.32f)
                    curveToRelative(0f, -3.5f, 1.39f, -6.85f, 3.87f, -9.32f)
                    arcToRelative(13.23f, 13.23f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.35f, -3.86f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(95.65f, 311.83f)
                    curveToRelative(4.09f, 0f, 7.41f, -3.31f, 7.41f, -7.39f)
                    curveToRelative(0f, -4.08f, -3.32f, -7.39f, -7.41f, -7.39f)
                    reflectiveCurveToRelative(-7.41f, 3.31f, -7.41f, 7.39f)
                    curveToRelative(0f, 4.08f, 3.32f, 7.39f, 7.41f, 7.39f)
                    close()
                }
            }
        }.build()

        return _20Circles4!!
    }

@Suppress("ObjectPropertyName")
private var _20Circles4: ImageVector? = null
