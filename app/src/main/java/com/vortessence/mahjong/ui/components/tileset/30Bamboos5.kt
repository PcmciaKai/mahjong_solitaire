@file:Suppress("ObjectPropertyName", "UnusedReceiverParameter", "unused")

package com.vortessence.mahjong.ui.components.tileset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MahjongTiles.`30Bamboos5`: ImageVector
    get() {
        if (_30Bamboos5 != null) {
            return _30Bamboos5!!
        }
        _30Bamboos5 = ImageVector.Builder(
            name = "30Bamboos5",
            defaultWidth = 300.dp,
            defaultHeight = 420.dp,
            viewportWidth = 300f,
            viewportHeight = 420f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(46.26f, 45.72f)
                    horizontalLineToRelative(207.49f)
                    verticalLineToRelative(328.57f)
                    lineTo(46.26f, 374.28f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFFB93C3C))) {
                    moveTo(150.02f, 130.72f)
                    curveToRelative(-13.78f, 0f, -24.96f, 12.52f, -24.96f, 27.96f)
                    curveToRelative(0.02f, 11.18f, 9.93f, 21.49f, 9.98f, 25.63f)
                    curveToRelative(0.05f, 4.13f, -10.01f, 14.49f, -10.02f, 25.67f)
                    curveToRelative(0.02f, 11.18f, 10.05f, 21.5f, 10.04f, 25.69f)
                    curveToRelative(-0.01f, 4.18f, -10.02f, 14.46f, -10.04f, 25.64f)
                    curveToRelative(0f, 15.44f, 11.21f, 27.99f, 25f, 27.98f)
                    curveToRelative(13.78f, 0f, 24.96f, -12.51f, 24.96f, -27.95f)
                    curveToRelative(0f, -11.18f, -10.08f, -21.49f, -10.09f, -25.67f)
                    curveToRelative(-0.01f, -4.18f, 10.07f, -14.49f, 10.09f, -25.66f)
                    curveToRelative(0f, -11.18f, -10.04f, -21.39f, -10.1f, -25.64f)
                    curveToRelative(-0.06f, -4.26f, 10.09f, -14.52f, 10.1f, -25.68f)
                    curveToRelative(0f, -15.44f, -11.17f, -27.96f, -24.96f, -27.96f)
                    horizontalLineToRelative(-0f)
                    close()
                    moveTo(150f, 149.15f)
                    curveToRelative(2.29f, 0f, 4.14f, 2.05f, 4.14f, 4.59f)
                    verticalLineToRelative(112.53f)
                    curveToRelative(0f, 2.54f, -1.85f, 4.59f, -4.14f, 4.59f)
                    curveToRelative(-2.3f, 0f, -4.14f, -2.05f, -4.14f, -4.59f)
                    verticalLineToRelative(-112.53f)
                    curveToRelative(0f, -2.54f, 1.84f, -4.59f, 4.14f, -4.59f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(71.25f, 45.72f)
                    curveToRelative(-13.79f, 0f, -24.96f, 12.52f, -24.96f, 27.95f)
                    curveToRelative(0.02f, 11.19f, 9.94f, 21.5f, 9.99f, 25.63f)
                    curveToRelative(0.05f, 4.14f, -10.01f, 14.49f, -10.03f, 25.67f)
                    curveToRelative(0.02f, 11.18f, 10.05f, 21.5f, 10.04f, 25.69f)
                    curveToRelative(-0.01f, 4.19f, -10.02f, 14.46f, -10.04f, 25.64f)
                    curveToRelative(0f, 15.44f, 11.21f, 27.99f, 25f, 27.99f)
                    curveToRelative(13.79f, 0f, 24.96f, -12.52f, 24.96f, -27.95f)
                    curveToRelative(-0f, -11.18f, -10.09f, -21.49f, -10.1f, -25.67f)
                    curveToRelative(-0.01f, -4.18f, 10.08f, -14.5f, 10.1f, -25.66f)
                    curveToRelative(0f, -11.18f, -10.04f, -21.39f, -10.1f, -25.65f)
                    curveToRelative(-0.06f, -4.26f, 10.09f, -14.51f, 10.1f, -25.68f)
                    curveToRelative(0f, -15.44f, -11.18f, -27.95f, -24.96f, -27.95f)
                    close()
                    moveTo(225.66f, 45.72f)
                    curveToRelative(-13.78f, 0f, -24.96f, 12.52f, -24.96f, 27.95f)
                    curveToRelative(0.02f, 11.19f, 9.93f, 21.5f, 9.98f, 25.63f)
                    curveToRelative(0.05f, 4.14f, -10.01f, 14.49f, -10.03f, 25.67f)
                    curveToRelative(0.02f, 11.18f, 10.05f, 21.5f, 10.05f, 25.69f)
                    curveToRelative(-0.01f, 4.19f, -10.03f, 14.46f, -10.05f, 25.64f)
                    curveToRelative(0f, 15.44f, 11.22f, 27.99f, 25f, 27.99f)
                    curveToRelative(13.79f, 0f, 24.97f, -12.52f, 24.97f, -27.95f)
                    curveToRelative(-0f, -11.18f, -10.09f, -21.49f, -10.1f, -25.67f)
                    curveToRelative(-0.01f, -4.18f, 10.08f, -14.5f, 10.1f, -25.66f)
                    curveToRelative(-0f, -11.18f, -10.04f, -21.39f, -10.1f, -25.65f)
                    curveToRelative(-0.06f, -4.26f, 10.09f, -14.51f, 10.1f, -25.68f)
                    curveToRelative(0f, -15.44f, -11.18f, -27.95f, -24.97f, -27.95f)
                    close()
                    moveTo(71.23f, 64.15f)
                    curveToRelative(2.3f, 0f, 4.14f, 2.05f, 4.14f, 4.59f)
                    verticalLineToRelative(112.53f)
                    curveToRelative(0f, 2.54f, -1.84f, 4.59f, -4.14f, 4.59f)
                    curveToRelative(-2.29f, 0f, -4.14f, -2.05f, -4.14f, -4.59f)
                    lineTo(67.09f, 68.73f)
                    curveToRelative(0f, -2.54f, 1.85f, -4.59f, 4.14f, -4.59f)
                    close()
                    moveTo(225.64f, 64.15f)
                    curveToRelative(2.3f, 0f, 4.14f, 2.05f, 4.14f, 4.59f)
                    verticalLineToRelative(112.53f)
                    curveToRelative(0f, 2.54f, -1.84f, 4.59f, -4.14f, 4.59f)
                    curveToRelative(-2.29f, 0f, -4.14f, -2.05f, -4.14f, -4.59f)
                    lineTo(221.51f, 68.73f)
                    curveToRelative(0f, -2.54f, 1.85f, -4.59f, 4.14f, -4.59f)
                    horizontalLineToRelative(-0f)
                    close()
                    moveTo(74.38f, 215.72f)
                    curveToRelative(-13.79f, 0f, -24.96f, 12.52f, -24.96f, 27.95f)
                    curveToRelative(0.02f, 11.18f, 9.93f, 21.49f, 9.98f, 25.63f)
                    curveToRelative(0.05f, 4.14f, -10.01f, 14.49f, -10.02f, 25.67f)
                    curveToRelative(0.02f, 11.18f, 10.05f, 21.5f, 10.04f, 25.69f)
                    curveToRelative(-0.01f, 4.19f, -10.03f, 14.46f, -10.04f, 25.64f)
                    curveToRelative(0f, 15.44f, 11.22f, 27.99f, 25f, 27.99f)
                    curveToRelative(13.78f, 0f, 24.96f, -12.52f, 24.96f, -27.96f)
                    curveToRelative(0f, -11.17f, -10.08f, -21.49f, -10.09f, -25.67f)
                    curveToRelative(-0.01f, -4.18f, 10.07f, -14.5f, 10.09f, -25.66f)
                    curveToRelative(0f, -11.18f, -10.04f, -21.39f, -10.1f, -25.65f)
                    curveToRelative(-0.07f, -4.26f, 10.09f, -14.52f, 10.1f, -25.68f)
                    curveToRelative(0f, -15.44f, -11.17f, -27.95f, -24.96f, -27.95f)
                    close()
                    moveTo(228.78f, 215.72f)
                    curveToRelative(-13.78f, 0f, -24.96f, 12.52f, -24.96f, 27.95f)
                    curveToRelative(0.02f, 11.18f, 9.94f, 21.49f, 9.99f, 25.63f)
                    curveToRelative(0.05f, 4.14f, -10.01f, 14.49f, -10.03f, 25.67f)
                    curveToRelative(0.02f, 11.18f, 10.05f, 21.5f, 10.04f, 25.69f)
                    curveToRelative(-0.01f, 4.19f, -10.02f, 14.46f, -10.04f, 25.64f)
                    curveToRelative(0f, 15.44f, 11.21f, 27.99f, 25f, 27.99f)
                    curveToRelative(13.79f, 0f, 24.96f, -12.52f, 24.96f, -27.96f)
                    curveToRelative(-0f, -11.17f, -10.08f, -21.49f, -10.1f, -25.67f)
                    curveToRelative(-0.01f, -4.18f, 10.08f, -14.5f, 10.1f, -25.66f)
                    curveToRelative(0f, -11.18f, -10.04f, -21.39f, -10.1f, -25.65f)
                    curveToRelative(-0.06f, -4.26f, 10.09f, -14.52f, 10.1f, -25.68f)
                    curveToRelative(0f, -15.44f, -11.18f, -27.95f, -24.96f, -27.95f)
                    close()
                    moveTo(74.35f, 234.15f)
                    curveToRelative(2.29f, 0f, 4.14f, 2.05f, 4.14f, 4.59f)
                    verticalLineToRelative(112.53f)
                    curveToRelative(0f, 2.54f, -1.85f, 4.59f, -4.14f, 4.59f)
                    curveToRelative(-2.29f, 0f, -4.14f, -2.05f, -4.14f, -4.59f)
                    lineTo(70.21f, 238.73f)
                    curveToRelative(0f, -2.54f, 1.85f, -4.58f, 4.14f, -4.58f)
                    close()
                    moveTo(228.76f, 234.15f)
                    curveToRelative(2.3f, 0f, 4.14f, 2.05f, 4.14f, 4.59f)
                    verticalLineToRelative(112.53f)
                    curveToRelative(0f, 2.54f, -1.85f, 4.59f, -4.14f, 4.59f)
                    curveToRelative(-2.29f, 0f, -4.14f, -2.05f, -4.14f, -4.59f)
                    lineTo(224.63f, 238.73f)
                    curveToRelative(0f, -2.54f, 1.84f, -4.58f, 4.14f, -4.58f)
                    horizontalLineToRelative(-0f)
                    close()
                }
            }
        }.build()

        return _30Bamboos5!!
    }

@Suppress("ObjectPropertyName")
private var _30Bamboos5: ImageVector? = null
