package com.vortessence.mahjong.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Move(
    val tileId1: Int,
    val tileId2: Int,
)

/**
 * The elapsed time is deliberately not part of this state: it ticks every second and lives in its
 * own flow so the board does not have to recompose and redraw with every tick.
 */
data class GameState(
    val constellation: Constellation,
    val tiles: List<BoardTile> = emptyList(),
    val selectedTileId: Int? = null,
    val hintPair: Pair<Int, Int>? = null,
    val moveHistory: List<Move> = emptyList(),
    val movesCount: Int = 0,
    val isPlaying: Boolean = true,
    val isWon: Boolean = false,
    val isStalemate: Boolean = false,
    // Number of pairs that could be removed right now at the same time
    val availablePairsCount: Int = 0,
    val blockedTileId: Int? = null,
    val hintsUsed: Int = 0,
    val undosUsed: Int = 0,
    val reshufflesUsed: Int = 0,
) {
    val remainingTilesCount: Int get() = tiles.count { !it.isRemoved }
    val totalTilesCount: Int get() = tiles.size
    @Suppress("unused")
    val removedTilesCount: Int get() = totalTilesCount - remainingTilesCount

    /** Whether hints, undos or reshuffles were used in this game. */
    val isAssisted: Boolean get() = (hintsUsed + undosUsed + reshufflesUsed) > 0
}
