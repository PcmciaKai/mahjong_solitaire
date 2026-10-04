@file:Suppress("ObjectPropertyName", "UnusedReceiverParameter", "unused")

package com.vortessence.mahjong.ui.components.tileset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MahjongTiles.`27Bamboos2`: ImageVector
    get() {
        if (_27Bamboos2 != null) {
            return _27Bamboos2!!
        }
        _27Bamboos2 = ImageVector.Builder(
            name = "27Bamboos2",
            defaultWidth = 300.dp,
            defaultHeight = 420.dp,
            viewportWidth = 300f,
            viewportHeight = 420f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(125.02f, 46.46f)
                    horizontalLineToRelative(49.96f)
                    verticalLineToRelative(327.07f)
                    lineTo(125.02f, 373.54f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF006F00))) {
                    moveTo(150.02f, 46.47f)
                    curveToRelative(-13.78f, 0f, -24.95f, 12.52f, -24.95f, 27.95f)
                    curveToRelative(0.02f, 11.18f, 9.93f, 21.49f, 9.98f, 25.63f)
                    curveToRelative(0.05f, 4.14f, -10.01f, 14.49f, -10.03f, 25.67f)
                    curveToRelative(0.02f, 11.18f, 10.05f, 21.5f, 10.04f, 25.69f)
                    curveToRelative(-0.01f, 4.19f, -10.02f, 14.46f, -10.04f, 25.64f)
                    curveToRelative(0f, 15.44f, 11.21f, 27.99f, 25f, 27.99f)
                    curveToRelative(13.79f, 0f, 24.96f, -12.52f, 24.96f, -27.96f)
                    curveToRelative(0f, -11.17f, -10.08f, -21.49f, -10.1f, -25.66f)
                    curveToRelative(-0.01f, -4.18f, 10.08f, -14.5f, 10.1f, -25.66f)
                    curveToRelative(0f, -11.18f, -10.04f, -21.39f, -10.1f, -25.65f)
                    curveToRelative(-0.06f, -4.26f, 10.09f, -14.52f, 10.1f, -25.68f)
                    curveToRelative(0f, -15.44f, -11.17f, -27.95f, -24.96f, -27.95f)
                    close()
                    moveTo(150f, 64.9f)
                    curveToRelative(2.3f, 0f, 4.14f, 2.05f, 4.14f, 4.59f)
                    lineTo(154.14f, 182.01f)
                    curveToRelative(0f, 2.54f, -1.84f, 4.59f, -4.14f, 4.59f)
                    curveToRelative(-2.29f, 0f, -4.14f, -2.05f, -4.14f, -4.59f)
                    lineTo(145.86f, 69.49f)
                    curveToRelative(0f, -2.54f, 1.85f, -4.59f, 4.14f, -4.59f)
                    horizontalLineToRelative(-0f)
                    close()
                    moveTo(150.02f, 214.97f)
                    curveToRelative(-13.78f, 0f, -24.95f, 12.52f, -24.95f, 27.96f)
                    curveToRelative(0.02f, 11.18f, 9.93f, 21.49f, 9.98f, 25.63f)
                    curveToRelative(0.05f, 4.13f, -10.01f, 14.49f, -10.03f, 25.67f)
                    curveToRelative(0.02f, 11.18f, 10.05f, 21.5f, 10.04f, 25.68f)
                    curveToRelative(-0.01f, 4.18f, -10.02f, 14.46f, -10.04f, 25.64f)
                    curveToRelative(0f, 15.44f, 11.21f, 27.98f, 25f, 27.98f)
                    curveToRelative(13.79f, 0f, 24.96f, -12.51f, 24.96f, -27.95f)
                    curveToRelative(0f, -11.18f, -10.08f, -21.49f, -10.1f, -25.67f)
                    curveToRelative(-0.01f, -4.18f, 10.08f, -14.49f, 10.1f, -25.66f)
                    curveToRelative(0f, -11.18f, -10.04f, -21.39f, -10.1f, -25.64f)
                    curveToRelative(-0.06f, -4.26f, 10.09f, -14.52f, 10.1f, -25.68f)
                    curveToRelative(0f, -15.44f, -11.17f, -27.96f, -24.96f, -27.96f)
                    close()
                    moveTo(150f, 233.4f)
                    curveToRelative(2.3f, 0f, 4.14f, 2.05f, 4.14f, 4.59f)
                    verticalLineToRelative(112.53f)
                    curveToRelative(0f, 2.54f, -1.84f, 4.59f, -4.14f, 4.59f)
                    curveToRelative(-2.29f, 0f, -4.14f, -2.05f, -4.14f, -4.59f)
                    lineTo(145.86f, 237.99f)
                    curveToRelative(0f, -2.54f, 1.85f, -4.59f, 4.14f, -4.59f)
                    horizontalLineToRelative(-0f)
                    close()
                }
            }
        }.build()

        return _27Bamboos2!!
    }

@Suppress("ObjectPropertyName")
private var _27Bamboos2: ImageVector? = null
