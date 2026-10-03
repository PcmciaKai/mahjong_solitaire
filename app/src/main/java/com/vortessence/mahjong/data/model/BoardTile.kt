package com.vortessence.mahjong.data.model

data class TilePosition(
    val x: Float,
    val y: Float,
    val z: Int,
)

data class BoardTile(
    val id: Int,
    val tile: Tile,
    val position: TilePosition,
    val isRemoved: Boolean = false,
    val isFree: Boolean = false,
) {
    val x: Float get() = position.x
    val y: Float get() = position.y
    val z: Int get() = position.z
}
