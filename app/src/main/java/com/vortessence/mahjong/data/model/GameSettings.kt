package com.vortessence.mahjong.data.model

import androidx.annotation.StringRes
import com.vortessence.mahjong.R

enum class TableSurface(@param:StringRes val labelRes: Int) {
    GREEN_MAT(R.string.surface_green_mat),
    WALNUT(R.string.surface_walnut),
}

@Suppress("unused")
typealias AppTheme = TableSurface

data class GameSettings(
    val soundEnabled: Boolean = true,
    val soundVolume: Float = 1.0f,
    val musicEnabled: Boolean = true,
    val musicVolume: Float = 0.4f,
    val autoZoomEnabled: Boolean = true,
    val highlightFreeTiles: Boolean = false,
    val animationsEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val showCornerIndex: Boolean = false,
    val tableSurface: TableSurface = TableSurface.GREEN_MAT,
) {
    val theme: TableSurface get() = tableSurface
}
