package com.vortessence.mahjong.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vortessence.mahjong.data.model.ContinueInfo
import com.vortessence.mahjong.ui.components.tileset.MahjongTiles
import com.vortessence.mahjong.ui.components.tileset.`03RedDragon`
import com.vortessence.mahjong.ui.theme.MahjongTheme
import com.vortessence.mahjong.ui.theme.TomorrowBold
import com.vortessence.mahjong.ui.theme.TomorrowMedium

@Composable
fun MainMenuScreen(
    continueInfo: ContinueInfo? = null,
    onContinue: () -> Unit = {},
    onPlayLevels: () -> Unit,
    onChooseConstellation: () -> Unit,
    onStats: () -> Unit,
    onSettings: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE9E1DA)),
    ) {
        // Red Dragon background illustration
        Image(
            imageVector = MahjongTiles.`03RedDragon`,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = 1.5f,
                    scaleY = 1.5f,
                    translationX = 300f,
                    translationY = 200f
                )
        )

        // Menu content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val contentModifier = Modifier
                .padding(horizontal = 28.dp)
                .then(
                    if (isLandscape) Modifier.widthIn(max = 420.dp) else Modifier.fillMaxWidth()
                )

            Column(
                modifier = contentModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(140.dp))

                // Main menu headings: automatically fit to the available width (same as buttons)
                FitWidthText(
                    text = "Mahjong",
                    fontFamily = TomorrowBold,
                    color = Color(0xFF363636),
                    letterSpacing = 2.sp,
                )

                FitWidthText(
                    text = "Solitaire",
                    fontFamily = TomorrowBold,
                    color = Color(0xFF363636),
                    letterSpacing = 2.sp,
                    maxWidthFraction = 0.70f,
                    modifier = Modifier.offset(y = (-16).dp)
                )

                Spacer(modifier = Modifier.height(44.dp))

                // Main Menu Action Buttons
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (continueInfo != null) {
                        MainMenuButton(
                            text = "Continue Game",
                            subtitle = "${continueInfo.title} • ${continueInfo.subtitle}",
                            onClick = onContinue,
                        )
                    }

                    MainMenuButton(
                        text = "Play Levels",
                        onClick = onPlayLevels,
                    )

                    MainMenuButton(
                        text = "Choose Constellation",
                        onClick = onChooseConstellation,
                    )

                    MainMenuButton(
                        text = "Statistics",
                        onClick = onStats,
                    )

                    MainMenuButton(
                        text = "Settings",
                        onClick = onSettings,
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

/**
 * A text component that automatically scales its font size to exactly fill the available width.
 */
@Composable
private fun FitWidthText(
    text: String,
    fontFamily: FontFamily,
    color: Color,
    modifier: Modifier = Modifier,
    letterSpacing: androidx.compose.ui.unit.TextUnit = 0.sp,
    maxWidthFraction: Float = 1.0f,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val maxWidthPx = with(LocalDensity.current) { this@BoxWithConstraints.maxWidth.toPx() } * maxWidthFraction
        val textMeasurer = rememberTextMeasurer()
        
        // Measure with a base size to calculate the scaling factor
        val baseFontSize = 100.sp
        val textLayoutResult = textMeasurer.measure(
            text = text,
            style = TextStyle(
                fontFamily = fontFamily,
                fontSize = baseFontSize,
                letterSpacing = letterSpacing,
            ),
            softWrap = false,
        )
        
        val measuredWidth = textLayoutResult.size.width.toFloat()
        val factor = if (measuredWidth > 0) maxWidthPx / measuredWidth else 1f
        val calculatedFontSize = (baseFontSize.value * factor).sp

        Text(
            text = text,
            style = TextStyle(
                fontFamily = fontFamily,
                fontSize = calculatedFontSize,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center,
                letterSpacing = letterSpacing,
            ),
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MainMenuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFE2D9D1),
        border = BorderStroke(1.5.dp, Color(0xFF363636)),
        modifier = modifier
            .fillMaxWidth()
            .height(if (subtitle != null) 64.dp else 56.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontFamily = TomorrowMedium,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = Color(0xFF363636),
                    textAlign = TextAlign.Center,
                ),
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        fontFamily = TomorrowMedium,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color(0xFF363636).copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                    ),
                )
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
fun MainMenuPreview() {
    MahjongTheme {
        MainMenuScreen(
            continueInfo = null,
            onContinue = {},
            onPlayLevels = {},
            onChooseConstellation = {},
            onStats = {},
            onSettings = {}
        )
    }
}

@Preview(widthDp = 800, heightDp = 360)
@Composable
fun MainMenuLandscapePreview() {
    MahjongTheme {
        MainMenuScreen(
            continueInfo = null,
            onContinue = {},
            onPlayLevels = {},
            onChooseConstellation = {},
            onStats = {},
            onSettings = {}
        )
    }
}

@Preview(widthDp = 600, heightDp = 1024)
@Composable
fun MainMenuWidePortraitPreview() {
    MahjongTheme {
        MainMenuScreen(
            continueInfo = null,
            onContinue = {},
            onPlayLevels = {},
            onChooseConstellation = {},
            onStats = {},
            onSettings = {}
        )
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
fun MainMenuWithContinuePreview() {
    MahjongTheme {
        MainMenuScreen(
            continueInfo = ContinueInfo(
                title = "Level 1: Jade Pillar",
                subtitle = "32 tiles remaining"
            ),
            onContinue = {},
            onPlayLevels = {},
            onChooseConstellation = {},
            onStats = {},
            onSettings = {}
        )
    }
}
