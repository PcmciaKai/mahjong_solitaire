package com.vortessence.mahjong.data.model

data class Move(
    val tileId1: Int,
    val tileId2: Int,
)

data class GameState(
    val constellation: Constellation,
    val tiles: List<BoardTile> = emptyList(),
    val selectedTileId: Int? = null,
    val hintPair: Pair<Int, Int>? = null,
    val moveHistory: List<Move> = emptyList(),
    val timerSeconds: Long = 0L,
    val movesCount: Int = 0,
    val isPlaying: Boolean = true,
    val isWon: Boolean = false,
    val isStalemate: Boolean = false,
    val availablePairsCount: Int = 0,
    val blockedTileId: Int? = null,
) {
    val remainingTilesCount: Int get() = tiles.count { !it.isRemoved }
    val totalTilesCount: Int get() = tiles.size
    @Suppress("unused")
    val removedTilesCount: Int get() = totalTilesCount - remainingTilesCount
}
