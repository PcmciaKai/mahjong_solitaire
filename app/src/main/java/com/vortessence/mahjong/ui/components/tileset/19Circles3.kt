@file:Suppress("ObjectPropertyName", "UnusedReceiverParameter", "unused")

package com.vortessence.mahjong.ui.components.tileset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MahjongTiles.`19Circles3`: ImageVector
    get() {
        if (_19Circles3 != null) {
            return _19Circles3!!
        }
        _19Circles3 = ImageVector.Builder(
            name = "19Circles3",
            defaultWidth = 300.dp,
            defaultHeight = 420.dp,
            viewportWidth = 300f,
            viewportHeight = 420f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(32.5f, 49.5f)
                    horizontalLineToRelative(235f)
                    verticalLineToRelative(321f)
                    lineTo(32.5f, 370.5f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(84.99f, 49.5f)
                    arcToRelative(52.55f, 52.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, -37.12f, 15.34f)
                    arcToRelative(52.33f, 52.33f, 0f, isMoreThanHalf = false, isPositiveArc = false, -15.38f, 37.04f)
                    arcToRelative(52.33f, 52.33f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.37f, 37.04f)
                    arcToRelative(52.55f, 52.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 37.12f, 15.34f)
                    arcToRelative(52.55f, 52.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 37.12f, -15.34f)
                    arcToRelative(52.33f, 52.33f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.38f, -37.04f)
                    arcToRelative(52.33f, 52.33f, 0f, isMoreThanHalf = false, isPositiveArc = false, -15.38f, -37.04f)
                    arcTo(52.55f, 52.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 84.99f, 49.5f)
                    close()
                    moveTo(84.99f, 65.12f)
                    arcToRelative(36.88f, 36.88f, 0f, isMoreThanHalf = false, isPositiveArc = true, 26.05f, 10.77f)
                    arcToRelative(36.72f, 36.72f, 0f, isMoreThanHalf = false, isPositiveArc = true, 10.79f, 26f)
                    arcToRelative(36.73f, 36.73f, 0f, isMoreThanHalf = false, isPositiveArc = true, -10.79f, 26f)
                    arcTo(36.89f, 36.89f, 0f, isMoreThanHalf = false, isPositiveArc = true, 84.99f, 138.65f)
                    arcToRelative(36.89f, 36.89f, 0f, isMoreThanHalf = false, isPositiveArc = true, -26.05f, -10.77f)
                    arcToRelative(36.73f, 36.73f, 0f, isMoreThanHalf = false, isPositiveArc = true, -10.79f, -26f)
                    arcTo(36.73f, 36.73f, 0f, isMoreThanHalf = false, isPositiveArc = true, 58.94f, 75.89f)
                    arcTo(36.88f, 36.88f, 0f, isMoreThanHalf = false, isPositiveArc = true, 84.99f, 65.12f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(84.99f, 74.73f)
                    arcToRelative(27.24f, 27.24f, 0f, isMoreThanHalf = false, isPositiveArc = false, -19.24f, 7.95f)
                    arcToRelative(27.13f, 27.13f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.97f, 19.2f)
                    curveToRelative(0f, 7.2f, 2.87f, 14.11f, 7.97f, 19.2f)
                    arcToRelative(27.24f, 27.24f, 0f, isMoreThanHalf = false, isPositiveArc = false, 19.24f, 7.95f)
                    curveToRelative(7.22f, 0f, 14.14f, -2.86f, 19.24f, -7.95f)
                    curveToRelative(5.1f, -5.09f, 7.97f, -12f, 7.97f, -19.2f)
                    arcToRelative(27.13f, 27.13f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.97f, -19.2f)
                    arcTo(27.24f, 27.24f, 0f, isMoreThanHalf = false, isPositiveArc = false, 84.99f, 74.73f)
                    close()
                    moveTo(84.99f, 85.54f)
                    curveToRelative(4.34f, 0f, 8.51f, 1.72f, 11.58f, 4.79f)
                    arcToRelative(16.33f, 16.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.8f, 11.55f)
                    curveToRelative(0f, 4.33f, -1.73f, 8.49f, -4.8f, 11.56f)
                    arcToRelative(16.4f, 16.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -11.58f, 4.79f)
                    curveToRelative(-4.34f, 0f, -8.51f, -1.72f, -11.58f, -4.79f)
                    arcToRelative(16.33f, 16.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, -4.8f, -11.56f)
                    arcToRelative(16.33f, 16.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.8f, -11.55f)
                    arcToRelative(16.4f, 16.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11.58f, -4.79f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1E39CD))) {
                    moveTo(84.99f, 109.42f)
                    curveToRelative(4.17f, 0f, 7.55f, -3.37f, 7.55f, -7.53f)
                    reflectiveCurveToRelative(-3.38f, -7.53f, -7.55f, -7.53f)
                    reflectiveCurveToRelative(-7.55f, 3.37f, -7.55f, 7.53f)
                    reflectiveCurveToRelative(3.38f, 7.53f, 7.55f, 7.53f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFB93C3C))) {
                    moveTo(151.21f, 158.42f)
                    arcToRelative(52.55f, 52.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, -37.12f, 15.34f)
                    arcToRelative(52.33f, 52.33f, 0f, isMoreThanHalf = false, isPositiveArc = false, -15.38f, 37.04f)
                    arcToRelative(52.33f, 52.33f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.38f, 37.04f)
                    arcToRelative(52.55f, 52.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 37.12f, 15.34f)
                    arcToRelative(52.55f, 52.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 37.12f, -15.34f)
                    arcToRelative(52.33f, 52.33f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.37f, -37.04f)
                    arcToRelative(52.33f, 52.33f, 0f, isMoreThanHalf = false, isPositiveArc = false, -15.37f, -37.04f)
                    arcToRelative(52.55f, 52.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, -37.12f, -15.34f)
                    close()
                    moveTo(151.21f, 174.04f)
                    arcToRelative(36.89f, 36.89f, 0f, isMoreThanHalf = false, isPositiveArc = true, 26.05f, 10.77f)
                    arcTo(36.73f, 36.73f, 0f, isMoreThanHalf = false, isPositiveArc = true, 188.05f, 210.8f)
                    arcToRelative(36.73f, 36.73f, 0f, isMoreThanHalf = false, isPositiveArc = true, -10.79f, 26f)
                    arcToRelative(36.88f, 36.88f, 0f, isMoreThanHalf = false, isPositiveArc = true, -26.05f, 10.77f)
                    arcToRelative(36.88f, 36.88f, 0f, isMoreThanHalf = false, isPositiveArc = true, -26.05f, -10.77f)
                    arcToRelative(36.73f, 36.73f, 0f, isMoreThanHalf = false, isPositiveArc = true, -10.79f, -26f)
                    arcToRelative(36.72f, 36.72f, 0f, isMoreThanHalf = false, isPositiveArc = true, 10.79f, -25.99f)
                    arcToRelative(36.88f, 36.88f, 0f, isMoreThanHalf = false, isPositiveArc = true, 26.05f, -10.77f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFB93C3C))) {
                    moveTo(151.21f, 183.65f)
                    arcToRelative(27.24f, 27.24f, 0f, isMoreThanHalf = false, isPositiveArc = false, -19.24f, 7.95f)
                    arcToRelative(27.13f, 27.13f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.97f, 19.2f)
                    arcToRelative(27.13f, 27.13f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.97f, 19.2f)
                    arcToRelative(27.24f, 27.24f, 0f, isMoreThanHalf = false, isPositiveArc = false, 19.24f, 7.95f)
                    curveToRelative(7.22f, 0f, 14.14f, -2.86f, 19.24f, -7.95f)
                    arcToRelative(27.13f, 27.13f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.97f, -19.2f)
                    arcToRelative(27.12f, 27.12f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.97f, -19.2f)
                    arcToRelative(27.24f, 27.24f, 0f, isMoreThanHalf = false, isPositiveArc = false, -19.24f, -7.95f)
                    close()
                    moveTo(151.21f, 194.46f)
                    curveToRelative(4.34f, 0f, 8.51f, 1.72f, 11.58f, 4.79f)
                    arcToRelative(16.33f, 16.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.8f, 11.55f)
                    curveToRelative(0f, 4.34f, -1.73f, 8.49f, -4.8f, 11.56f)
                    arcToRelative(16.39f, 16.39f, 0f, isMoreThanHalf = false, isPositiveArc = true, -11.58f, 4.79f)
                    arcToRelative(16.4f, 16.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -11.58f, -4.79f)
                    arcToRelative(16.33f, 16.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, -4.8f, -11.56f)
                    arcToRelative(16.32f, 16.32f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.8f, -11.55f)
                    arcToRelative(16.39f, 16.39f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11.58f, -4.79f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFB93C3C))) {
                    moveTo(151.21f, 218.33f)
                    curveToRelative(4.17f, 0f, 7.55f, -3.37f, 7.55f, -7.53f)
                    curveToRelative(0f, -4.16f, -3.38f, -7.53f, -7.55f, -7.53f)
                    curveToRelative(-4.17f, 0f, -7.55f, 3.37f, -7.55f, 7.53f)
                    curveToRelative(0f, 4.16f, 3.38f, 7.53f, 7.55f, 7.53f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(215.01f, 265.73f)
                    arcToRelative(52.55f, 52.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, -37.12f, 15.34f)
                    arcToRelative(52.33f, 52.33f, 0f, isMoreThanHalf = false, isPositiveArc = false, -15.38f, 37.04f)
                    arcToRelative(52.33f, 52.33f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.38f, 37.04f)
                    arcTo(52.55f, 52.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 215.01f, 370.5f)
                    arcToRelative(52.55f, 52.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 37.12f, -15.34f)
                    arcToRelative(52.33f, 52.33f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.38f, -37.04f)
                    arcToRelative(52.33f, 52.33f, 0f, isMoreThanHalf = false, isPositiveArc = false, -15.38f, -37.04f)
                    arcToRelative(52.55f, 52.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, -37.12f, -15.34f)
                    close()
                    moveTo(215.01f, 281.36f)
                    arcToRelative(36.89f, 36.89f, 0f, isMoreThanHalf = false, isPositiveArc = true, 26.05f, 10.77f)
                    arcToRelative(36.73f, 36.73f, 0f, isMoreThanHalf = false, isPositiveArc = true, 10.79f, 26f)
                    arcToRelative(36.73f, 36.73f, 0f, isMoreThanHalf = false, isPositiveArc = true, -10.79f, 26f)
                    arcToRelative(36.88f, 36.88f, 0f, isMoreThanHalf = false, isPositiveArc = true, -26.05f, 10.77f)
                    arcToRelative(36.88f, 36.88f, 0f, isMoreThanHalf = false, isPositiveArc = true, -26.05f, -10.77f)
                    arcToRelative(36.73f, 36.73f, 0f, isMoreThanHalf = false, isPositiveArc = true, -10.79f, -26f)
                    arcToRelative(36.73f, 36.73f, 0f, isMoreThanHalf = false, isPositiveArc = true, 10.79f, -25.99f)
                    arcToRelative(36.88f, 36.88f, 0f, isMoreThanHalf = false, isPositiveArc = true, 26.05f, -10.77f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(215.01f, 290.96f)
                    arcToRelative(27.24f, 27.24f, 0f, isMoreThanHalf = false, isPositiveArc = false, -19.24f, 7.95f)
                    arcToRelative(27.13f, 27.13f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.97f, 19.2f)
                    arcToRelative(27.13f, 27.13f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.97f, 19.2f)
                    arcToRelative(27.24f, 27.24f, 0f, isMoreThanHalf = false, isPositiveArc = false, 19.24f, 7.95f)
                    arcToRelative(27.24f, 27.24f, 0f, isMoreThanHalf = false, isPositiveArc = false, 19.24f, -7.95f)
                    arcToRelative(27.13f, 27.13f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.97f, -19.2f)
                    arcToRelative(27.13f, 27.13f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.97f, -19.2f)
                    arcToRelative(27.24f, 27.24f, 0f, isMoreThanHalf = false, isPositiveArc = false, -19.24f, -7.95f)
                    close()
                    moveTo(215.01f, 301.78f)
                    curveToRelative(4.34f, 0f, 8.51f, 1.72f, 11.58f, 4.78f)
                    arcToRelative(16.33f, 16.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.8f, 11.55f)
                    curveToRelative(0f, 4.34f, -1.73f, 8.49f, -4.8f, 11.56f)
                    arcToRelative(16.4f, 16.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -11.58f, 4.79f)
                    curveToRelative(-4.34f, 0f, -8.51f, -1.72f, -11.58f, -4.79f)
                    arcToRelative(16.33f, 16.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, -4.8f, -11.56f)
                    arcToRelative(16.33f, 16.33f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.8f, -11.55f)
                    arcToRelative(16.4f, 16.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11.58f, -4.78f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(215.01f, 325.65f)
                    curveToRelative(4.17f, 0f, 7.55f, -3.37f, 7.55f, -7.53f)
                    reflectiveCurveToRelative(-3.38f, -7.53f, -7.55f, -7.53f)
                    curveToRelative(-4.17f, 0f, -7.55f, 3.37f, -7.55f, 7.53f)
                    reflectiveCurveToRelative(3.38f, 7.53f, 7.55f, 7.53f)
                    close()
                }
            }
        }.build()

        return _19Circles3!!
    }

@Suppress("ObjectPropertyName")
private var _19Circles3: ImageVector? = null
