package com.vortessence.mahjong.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.vortessence.mahjong.data.model.TableSurface

val MahjongColorScheme = lightColorScheme(
    primary = AppThemePrimary,
    onPrimary = Color.White,
    primaryContainer = AppThemeSurface,
    onPrimaryContainer = AppThemeText,
    secondary = AppThemeSecondary,
    onSecondary = Color.White,
    secondaryContainer = AppThemeSurfaceVariant,
    onSecondaryContainer = AppThemeText,
    tertiary = AppThemeTertiary,
    onTertiary = Color.White,
    background = AppThemeBackground,
    onBackground = AppThemeText,
    surface = AppThemeSurface,
    onSurface = AppThemeText,
    surfaceVariant = AppThemeSurfaceVariant,
    onSurfaceVariant = Color(0xFF5C554E),
    outline = AppThemeBorder,
    outlineVariant = Color(0xFFC0B6AC),
)

@Composable
fun MahjongTheme(
    @Suppress("UNUSED_PARAMETER") appTheme: Any? = null,
    @Suppress("UNUSED_PARAMETER") tableSurface: TableSurface = TableSurface.GREEN_MAT,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = MahjongColorScheme,
        typography = Typography,
        content = content,
    )
}
