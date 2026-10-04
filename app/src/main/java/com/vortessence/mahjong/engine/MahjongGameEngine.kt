package com.vortessence.mahjong.engine

import com.vortessence.mahjong.data.model.BoardTile
import com.vortessence.mahjong.data.model.Constellation
import com.vortessence.mahjong.data.model.GameState
import com.vortessence.mahjong.data.model.Move

sealed class TileTapResult {
    data class Blocked(val tileId: Int) : TileTapResult()
    data class Selected(val tileId: Int) : TileTapResult()
    data object Deselected : TileTapResult()
    data class Matched(val tileId1: Int, val tileId2: Int, val isWon: Boolean, val isStalemate: Boolean) : TileTapResult()
    data class SelectionChanged(val newSelectedTileId: Int) : TileTapResult()
}

object MahjongGameEngine {

    /**
     * Initializes a new game state with a solvable board layout.
     */
    fun startNewGame(constellation: Constellation): GameState {
        val boardTiles = BoardSolvabilityGenerator.generateSolvableBoard(constellation)
        val tilesWithFreedom = recalculateFreedom(boardTiles)
        val availablePairs = findAvailablePairs(tilesWithFreedom)

        return GameState(
            constellation = constellation,
            tiles = tilesWithFreedom,
            selectedTileId = null,
            hintPair = null,
            moveHistory = emptyList(),
            timerSeconds = 0L,
            movesCount = 0,
            isPlaying = true,
            isWon = false,
            isStalemate = false,
            availablePairsCount = availablePairs.size,
            blockedTileId = null,
        )
    }

    /**
     * Handles user interaction with a tile.
     */
    fun handleTileTap(currentState: GameState, tileId: Int): Pair<GameState, TileTapResult> {
        val targetTile = currentState.tiles.firstOrNull { it.id == tileId }
            ?: return currentState to TileTapResult.Deselected

        if (targetTile.isRemoved) {
            return currentState to TileTapResult.Deselected
        }

        // 1. Tapped a blocked tile
        if (!targetTile.isFree) {
            val updatedState = currentState.copy(
                blockedTileId = tileId
            )
            return updatedState to TileTapResult.Blocked(tileId)
        }

        val currentSelectedId = currentState.selectedTileId

        // 2. No tile currently selected: select this one
        if (currentSelectedId == null) {
            val updatedState = currentState.copy(
                selectedTileId = tileId,
                hintPair = null,
                blockedTileId = null
            )
            return updatedState to TileTapResult.Selected(tileId)
        }

        // 3. Tapped the same tile again: deselect it
        if (currentSelectedId == tileId) {
            val updatedState = currentState.copy(
                selectedTileId = null,
                hintPair = null,
                blockedTileId = null
            )
            return updatedState to TileTapResult.Deselected
        }

        // 4. Another free tile was already selected: check for match!
        val selectedTile = currentState.tiles.firstOrNull { it.id == currentSelectedId }
        if (selectedTile != null && (selectedTile.tile.matches(targetTile.tile))) {
            // MATCH!
            val updatedTiles = currentState.tiles.map { t ->
                if (t.id == currentSelectedId || t.id == tileId) {
                    t.copy(isRemoved = true, isFree = false)
                } else {
                    t
                }
            }

            val refreshedTiles = recalculateFreedom(updatedTiles)
            val availablePairs = findAvailablePairs(refreshedTiles)
            val remainingCount = refreshedTiles.count { !it.isRemoved }
            val isWon = remainingCount == 0
            val isStalemate = !isWon && availablePairs.isEmpty()

            val newHistory = currentState.moveHistory + Move(currentSelectedId, tileId)

            val updatedState = currentState.copy(
                tiles = refreshedTiles,
                selectedTileId = null,
                hintPair = null,
                blockedTileId = null,
                moveHistory = newHistory,
                movesCount = currentState.movesCount + 1,
                isWon = isWon,
                isStalemate = isStalemate,
                availablePairsCount = availablePairs.size
            )

            return updatedState to TileTapResult.Matched(currentSelectedId, tileId, isWon, isStalemate)
        }

        // 5. Does not match: switch selection to the new free tile
        val updatedState = currentState.copy(
            selectedTileId = tileId,
            hintPair = null,
            blockedTileId = null
        )
        return updatedState to TileTapResult.SelectionChanged(tileId)
    }

    /**
     * Undoes the most recent matched pair.
     */
    fun undo(currentState: GameState): GameState {
        if (currentState.moveHistory.isEmpty()) return currentState

        val lastMove = currentState.moveHistory.last()
        val remainingHistory = currentState.moveHistory.dropLast(1)

        val updatedTiles = currentState.tiles.map { t ->
            if (t.id == lastMove.tileId1 || t.id == lastMove.tileId2) {
                t.copy(isRemoved = false)
            } else {
                t
            }
        }

        val refreshedTiles = recalculateFreedom(updatedTiles)
        val availablePairs = findAvailablePairs(refreshedTiles)

        return currentState.copy(
            tiles = refreshedTiles,
            selectedTileId = null,
            hintPair = null,
            blockedTileId = null,
            moveHistory = remainingHistory,
            movesCount = (currentState.movesCount - 1).coerceAtLeast(0),
            isWon = false,
            isStalemate = false,
            availablePairsCount = availablePairs.size
        )
    }

    /**
     * Finds and marks a hint pair.
     */
    fun requestHint(currentState: GameState): GameState {
        val pairs = findAvailablePairs(currentState.tiles)
        return if (pairs.isNotEmpty()) {
            val hint = pairs.first()
            currentState.copy(
                hintPair = hint,
                blockedTileId = null
            )
        } else {
            currentState
        }
    }

    /**
     * Reshuffles remaining unremoved tiles in case of stalemate.
     */
    fun reshuffleRemaining(currentState: GameState): GameState {
        val activeTiles = currentState.tiles.filter { !it.isRemoved }
        if (activeTiles.size <= 2) return currentState

        // Prefer a solvable redistribution; fall back to a plain shuffle if none exists
        val solvable = BoardSolvabilityGenerator.reassignSolvable(
            positions = activeTiles.map { it.position },
            tiles = activeTiles.map { it.tile },
        )
        val activeMap = if (solvable != null) {
            activeTiles.associate { it.id to solvable.getValue(it.position) }
        } else {
            val activeTilesValues = activeTiles.map { it.tile }.shuffled()
            activeTiles.indices.associate { i ->
                activeTiles[i].id to activeTilesValues[i]
            }
        }

        val updatedTiles = currentState.tiles.map { t ->
            if (activeMap.containsKey(t.id)) {
                t.copy(tile = activeMap[t.id]!!)
            } else {
                t
            }
        }

        val refreshedTiles = recalculateFreedom(updatedTiles)
        val availablePairs = findAvailablePairs(refreshedTiles)

        return currentState.copy(
            tiles = refreshedTiles,
            selectedTileId = null,
            hintPair = null,
            blockedTileId = null,
            isStalemate = availablePairs.isEmpty() && refreshedTiles.any { !it.isRemoved },
            availablePairsCount = availablePairs.size
        )
    }

    /**
     * Recalculates the `isFree` status for every active tile on the board.
     */
    fun recalculateFreedom(tiles: List<BoardTile>): List<BoardTile> {
        val activePositions = tiles.asSequence().filter { !it.isRemoved }.map { it.position }.toSet()
        return tiles.map { tile ->
            if (tile.isRemoved) {
                tile.copy(isFree = false)
            } else {
                val free = BoardSolvabilityGenerator.isTileFree(tile.position, activePositions)
                tile.copy(isFree = free)
            }
        }
    }

    /**
     * Finds all currently available matching free tile pairs.
     */
    fun findAvailablePairs(tiles: List<BoardTile>): List<Pair<Int, Int>> {
        val freeTiles = tiles.filter { !it.isRemoved && it.isFree }
        val pairs = mutableListOf<Pair<Int, Int>>()

        for (i in freeTiles.indices) {
            for (j in (i + 1) until freeTiles.size) {
                if (freeTiles[i].tile.matches(freeTiles[j].tile)) {
                    pairs.add(Pair(freeTiles[i].id, freeTiles[j].id))
                }
            }
        }
        return pairs
    }
}
