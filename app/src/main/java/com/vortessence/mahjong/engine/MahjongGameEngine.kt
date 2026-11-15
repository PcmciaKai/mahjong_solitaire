package com.vortessence.mahjong.engine

import com.vortessence.mahjong.data.model.BoardTile
import com.vortessence.mahjong.data.model.Constellation
import com.vortessence.mahjong.data.model.GameState
import com.vortessence.mahjong.data.model.Move
import com.vortessence.mahjong.data.model.Tile
import com.vortessence.mahjong.data.model.TilePosition

sealed class TileTapResult {
    data class Blocked(val tileId: Int) : TileTapResult()
    data class Selected(val tileId: Int) : TileTapResult()
    data object Deselected : TileTapResult()
    data class Matched(val tileId1: Int, val tileId2: Int, val isWon: Boolean, val isStalemate: Boolean) : TileTapResult()
    data class SelectionChanged(val newSelectedTileId: Int) : TileTapResult()
}

object MahjongGameEngine {

    /** Boards up to this size are searched exhaustively for dead ends after every change. */
    private const val DEAD_END_CHECK_MAX_TILES = 20

    /** Solver budget per suggested pair when ranking hints. */
    private const val HINT_SOLVER_MAX_STATES = 5_000

    /** Solver budget per position when looking for the last winnable position. */
    private const val WINNABLE_SOLVER_MAX_STATES = 20_000

    /** Solver budget for the background dead-end check on bigger boards. */
    const val BACKGROUND_DEAD_END_MAX_STATES = 50_000

    /**
     * Initializes a new game state with a solvable board layout.
     */
    fun startNewGame(constellation: Constellation): GameState {
        val boardTiles = BoardSolvabilityGenerator.generateSolvableBoard(constellation)
        val tilesWithFreedom = recalculateFreedom(boardTiles)

        return GameState(
            constellation = constellation,
            tiles = tilesWithFreedom,
            selectedTileId = null,
            hintPair = null,
            moveHistory = emptyList(),
            movesCount = 0,
            isPlaying = true,
            isWon = false,
            isStalemate = false,
            availablePairsCount = countRemovablePairs(tilesWithFreedom),
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
            val isStalemate = isStuck(refreshedTiles, availablePairs)

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
                availablePairsCount = countRemovablePairs(refreshedTiles)
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
     * Undoes the most recent matched pair. [countAsAssist] is false when the engine takes moves
     * back on its own (e.g. while reshuffling) rather than the player asking for it.
     */
    fun undo(currentState: GameState, countAsAssist: Boolean = true): GameState {
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
            isStalemate = isStuck(refreshedTiles, availablePairs),
            availablePairsCount = countRemovablePairs(refreshedTiles),
            undosUsed = currentState.undosUsed + if (countAsAssist) 1 else 0,
        )
    }

    /**
     * Marks a hint pair. Asking again while a hint is shown moves on to the next suggestion.
     */
    fun requestHint(currentState: GameState): GameState {
        val hints = rankedHints(currentState.tiles)
        if (hints.isEmpty()) return currentState
        val currentIndex = hints.indexOf(currentState.hintPair)
        return currentState.copy(
            hintPair = hints[(currentIndex + 1) % hints.size],
            blockedTileId = null,
            hintsUsed = currentState.hintsUsed + 1,
        )
    }

    /**
     * The available pairs that make the best hints, best first.
     *
     * The solver checks which pairs keep the board clearable: pairs with a known solution come
     * first, pairs that provably lead into a dead end are left out. Ties are broken by how many
     * tiles a pair frees up.
     */
    fun rankedHints(tiles: List<BoardTile>): List<Pair<Int, Int>> {
        val pairs = findAvailablePairs(tiles)
        if (pairs.size <= 1) return pairs

        val activeTiles = tiles.filter { !it.isRemoved }
        val scored = pairs.map { pair ->
            val remaining = activeTiles.filter { it.id != pair.first && it.id != pair.second }
            val clearable = BoardSolvabilityGenerator.canBeCleared(
                remaining.map { it.position to it.tile },
                maxStates = HINT_SOLVER_MAX_STATES,
            )
            val safety = when (clearable) {
                true -> 0
                null -> 1
                false -> 2
            }
            val positions = remaining.map { it.position }
            val newlyFreed = remaining.count { !it.isFree && BoardSolvabilityGenerator.isTileFree(it.position, positions) }
            Triple(pair, safety, newlyFreed)
        }
        val bestSafety = scored.minOf { it.second }
        return scored
            .filter { it.second == bestSafety }
            .sortedByDescending { it.third }
            .map { it.first }
    }

    /**
     * Takes back moves until the board can be cleared again, i.e. back to the last position from
     * which the game can still be won. Always takes back at least one move.
     *
     * A game can become unwinnable many moves before the dead end shows, so going back a single
     * move is often not enough.
     */
    fun undoToWinnable(currentState: GameState): GameState {
        if (currentState.moveHistory.isEmpty()) return currentState
        var state = undo(currentState)
        while (state.moveHistory.isNotEmpty() && !isClearable(state.tiles, WINNABLE_SOLVER_MAX_STATES)) {
            state = undo(state)
        }
        return state
    }

    /**
     * Whether a solution for the remaining [tiles] was found within [maxStates] solver steps.
     */
    private fun isClearable(tiles: List<BoardTile>, maxStates: Int): Boolean =
        BoardSolvabilityGenerator.canBeCleared(
            tiles.filter { !it.isRemoved }.map { it.position to it.tile },
            maxStates = maxStates,
        ) == true

    /**
     * Whether the board of [state] provably cannot be cleared anymore. Meant for boards too big for
     * the check that runs after every move, so it is slower and should run in the background.
     */
    fun isDeadEnd(state: GameState, maxStates: Int = BACKGROUND_DEAD_END_MAX_STATES): Boolean {
        if (state.isWon || state.remainingTilesCount == 0) return false
        return BoardSolvabilityGenerator.canBeCleared(
            state.tiles.filter { !it.isRemoved }.map { it.position to it.tile },
            maxStates = maxStates,
        ) == false
    }

    /**
     * Reshuffles remaining unremoved tiles in case of stalemate.
     */
    fun reshuffleRemaining(currentState: GameState): GameState {
        // Some layouts cannot be solved by any arrangement (e.g. the last two tiles stacked on
        // top of each other), so take back matched pairs until a solvable redistribution exists.
        var state = currentState
        var solvable: Map<TilePosition, Tile>?
        while (true) {
            val activeTiles = state.tiles.filter { !it.isRemoved }
            if (activeTiles.isEmpty()) return currentState
            solvable = BoardSolvabilityGenerator.reassignSolvable(
                positions = activeTiles.map { it.position },
                tiles = activeTiles.map { it.tile },
            )
            if (solvable != null || state.moveHistory.isEmpty()) break
            state = undo(state, countAsAssist = false)
        }

        // Prefer a solvable redistribution; fall back to a plain shuffle if none exists
        val activeTiles = state.tiles.filter { !it.isRemoved }
        val solvableMap = solvable
        val activeMap = if (solvableMap != null) {
            activeTiles.associate { it.id to solvableMap.getValue(it.position) }
        } else {
            val activeTilesValues = activeTiles.map { it.tile }.shuffled()
            activeTiles.indices.associate { i ->
                activeTiles[i].id to activeTilesValues[i]
            }
        }

        val updatedTiles = state.tiles.map { t ->
            if (activeMap.containsKey(t.id)) {
                t.copy(tile = activeMap[t.id]!!)
            } else {
                t
            }
        }

        val refreshedTiles = recalculateFreedom(updatedTiles)
        val availablePairs = findAvailablePairs(refreshedTiles)

        return state.copy(
            tiles = refreshedTiles,
            selectedTileId = null,
            hintPair = null,
            blockedTileId = null,
            isStalemate = isStuck(refreshedTiles, availablePairs),
            availablePairsCount = countRemovablePairs(refreshedTiles),
            reshufflesUsed = currentState.reshufflesUsed + 1,
        )
    }

    /**
     * Whether the game cannot be finished from here: either no free pair is left, or the few
     * remaining tiles provably can no longer be cleared (e.g. two matching tiles stacked).
     */
    fun isStuck(tiles: List<BoardTile>, availablePairs: List<Pair<Int, Int>>): Boolean {
        val activeTiles = tiles.filter { !it.isRemoved }
        if (activeTiles.isEmpty()) return false
        if (availablePairs.isEmpty()) return true
        if (activeTiles.size > DEAD_END_CHECK_MAX_TILES) return false
        return BoardSolvabilityGenerator.canBeCleared(activeTiles.map { it.position to it.tile }) == false
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
     * How many pairs could be removed right now at the same time. Unlike the size of
     * [findAvailablePairs], four free Seasons count as two pairs, not as six combinations.
     */
    fun countRemovablePairs(tiles: List<BoardTile>): Int =
        tiles.asSequence()
            .filter { !it.isRemoved && it.isFree }
            .groupingBy { it.tile.matchKey }
            .eachCount()
            .values
            .sumOf { it / 2 }

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
