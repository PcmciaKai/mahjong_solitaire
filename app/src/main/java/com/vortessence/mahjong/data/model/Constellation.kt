package com.vortessence.mahjong.data.model

data class Constellation(
    val id: String,
    val name: String,
    val difficulty: Difficulty,
    val description: String,
    val positions: List<TilePosition>,
    val isPortraitOptimized: Boolean = true,
    // Predefined tile for each entry in [positions]; null means tiles are dealt randomly
    val fixedTiles: List<Tile>? = null,
) {
    val tileCount: Int get() = positions.size
}
