package com.vortessence.mahjong.data.model

import androidx.annotation.StringRes
import com.vortessence.mahjong.R

enum class TableSurface(@param:StringRes val labelRes: Int, @param:StringRes val descriptionRes: Int) {
    GREEN_MAT(R.string.surface_green_mat, R.string.surface_green_mat_description),
    BLUE_MAT(R.string.surface_blue_mat, R.string.surface_blue_mat_description),
    LEATHER_MAT(R.string.surface_leather_mat, R.string.surface_leather_mat_description),
    CARD_TABLE(R.string.surface_card_table, R.string.surface_card_table_description),
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
    /** Warm lamp light on the table and the tiles. */
    val lampLightingEnabled: Boolean = true,
    /** Tiles with signs of years of use. */
    val wornTilesEnabled: Boolean = false,
    val tileSet: TileSet = TileSet.Default,
    /** Index into the colour variants of [tileSet]; out of range falls back to the first one. */
    val tileColorway: Int = 0,
    val tableSurface: TableSurface = TableSurface.GREEN_MAT,
) {
    val theme: TableSurface get() = tableSurface
}
