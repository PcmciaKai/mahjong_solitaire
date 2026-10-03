package com.vortessence.mahjong.engine

import com.vortessence.mahjong.data.model.BoardTile
import com.vortessence.mahjong.data.model.Constellation
import com.vortessence.mahjong.data.model.Tile
import com.vortessence.mahjong.data.model.TilePosition
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object BoardSolvabilityGenerator {

    private const val TILE_W = 2.0f
    private const val TILE_H = 2.0f
    private const val EPS = 0.05f

    /**
     * Checks if tile B overlaps tile A in the 2D plane.
     */
    fun isOverlap2D(ax: Float, ay: Float, bx: Float, by: Float): Boolean {
        val overlapX = max(ax, bx) < min(ax + TILE_W, bx + TILE_W) - EPS
        val overlapY = max(ay, by) < min(ay + TILE_H, by + TILE_H) - EPS
        return overlapX && overlapY
    }

    /**
     * Checks if tile A is covered from above by any higher layer tile in [positions].
     */
    fun isCovered(a: TilePosition, positions: Collection<TilePosition>): Boolean {
        for (b in positions) {
            if (b.z > a.z && isOverlap2D(a.x, a.y, b.x, b.y)) {
                return true
            }
        }
        return false
    }

    /**
     * Checks if tile A is blocked on the left by another tile on the same layer.
     */
    fun isBlockedLeft(a: TilePosition, positions: Collection<TilePosition>): Boolean {
        for (b in positions) {
            if (b.z == a.z && b != a) {
                // b is to the left of a and touching or overlapping
                val touching = abs((b.x + TILE_W) - a.x) < 0.15f || (b.x < a.x && b.x + TILE_W > a.x)
                if (touching) {
                    val verticalOverlap = max(a.y, b.y) < min(a.y + TILE_H, b.y + TILE_H) - EPS
                    if (verticalOverlap) return true
                }
            }
        }
        return false
    }

    /**
     * Checks if tile A is blocked on the right by another tile on the same layer.
     */
    fun isBlockedRight(a: TilePosition, positions: Collection<TilePosition>): Boolean {
        for (b in positions) {
            if (b.z == a.z && b != a) {
                // b is to the right of a and touching or overlapping
                val touching = abs((a.x + TILE_W) - b.x) < 0.15f || (b.x > a.x && a.x + TILE_W > b.x)
                if (touching) {
                    val verticalOverlap = max(a.y, b.y) < min(a.y + TILE_H, b.y + TILE_H) - EPS
                    if (verticalOverlap) return true
                }
            }
        }
        return false
    }

    /**
     * A tile is free if not covered and free on at least one horizontal side.
     */
    fun isTileFree(a: TilePosition, positions: Collection<TilePosition>): Boolean {
        if (isCovered(a, positions)) return false
        val left = isBlockedLeft(a, positions)
        val right = isBlockedRight(a, positions)
        return !left || !right
    }

    /**
     * Generates a board for [constellation] guaranteed to be solvable.
     */
    fun generateSolvableBoard(constellation: Constellation, random: Random = Random): List<BoardTile> {
        val totalCount = constellation.positions.size
        require(totalCount % 2 == 0) { "Constellation tile count must be even" }

        // Scripted layouts (e.g. the tutorial) deal the same tiles every time
        constellation.fixedTiles?.let { fixed ->
            require(fixed.size == totalCount) { "Fixed tiles must match the constellation tile count" }
            return constellation.positions.mapIndexed { index, pos ->
                BoardTile(id = index, tile = fixed[index], position = pos)
            }
        }

        val pairCount = totalCount / 2

        // Prepare matching pairs of tiles
        val tilePairs = generateTilePairs(pairCount, random)

        // Try reverse simulation to guarantee a valid solution path
        val assigned = assignSolvable(constellation.positions, tilePairs, random)
        if (assigned != null) {
            return constellation.positions.mapIndexed { index, pos ->
                BoardTile(
                    id = index,
                    tile = assigned.getValue(pos),
                    position = pos,
                    isRemoved = false,
                    isFree = false
                )
            }
        }

        // Fallback: direct pairing if constellation is unusually constrained
        return fallbackAssignment(constellation.positions, tilePairs, random)
    }

    /**
     * Redistributes [tiles] over [positions] so that the resulting board is solvable.
     * Returns null if the tiles cannot be paired or no solvable arrangement was found.
     */
    fun reassignSolvable(
        positions: List<TilePosition>,
        tiles: List<Tile>,
        random: Random = Random,
    ): Map<TilePosition, Tile>? {
        val pairs = pairUp(tiles) ?: return null
        return assignSolvable(positions, pairs.shuffled(random), random)
    }

    private fun assignSolvable(
        positions: List<TilePosition>,
        pairs: List<Pair<Tile, Tile>>,
        random: Random,
    ): Map<TilePosition, Tile>? {
        for (@Suppress("UNUSED_VARIABLE") attempt in 0 until 50) {
            val assigned = tryReverseSimulation(positions, pairs, random)
            if (assigned != null) {
                return assigned
            }
        }
        return null
    }

    /**
     * Groups [tiles] into matching pairs, or returns null if some tile has no partner.
     */
    private fun pairUp(tiles: List<Tile>): List<Pair<Tile, Tile>>? {
        val available = tiles.toMutableList()
        val pairs = mutableListOf<Pair<Tile, Tile>>()
        while (available.isNotEmpty()) {
            val t1 = available.removeAt(0)
            val matchIdx = available.indexOfFirst { t1.matches(it) }
            if (matchIdx == -1) return null
            pairs.add(Pair(t1, available.removeAt(matchIdx)))
        }
        return pairs
    }

    private fun tryReverseSimulation(
        allPositions: List<TilePosition>,
        pairs: List<Pair<Tile, Tile>>,
        random: Random,
    ): Map<TilePosition, Tile>? {
        val remaining = allPositions.toMutableList()
        val assignedTiles = mutableMapOf<TilePosition, Tile>()

        // In reverse generation:
        // We start with all tiles present. At each step, we find free tiles,
        // assign a matching pair to 2 of them, and "remove" them from our simulation set.
        // That way, in forward play, removing this pair in reverse order is always valid!
        for (pair in pairs) {
            val free = remaining.filter { isTileFree(it, remaining) }
            if (free.size < 2) {
                return null // Need to backtrack/retry
            }
            val shuffled = free.shuffled(random)
            val pos1 = shuffled[0]
            val pos2 = shuffled[1]

            assignedTiles[pos1] = pair.first
            assignedTiles[pos2] = pair.second

            remaining.remove(pos1)
            remaining.remove(pos2)
        }

        return assignedTiles
    }

    private fun fallbackAssignment(
        allPositions: List<TilePosition>,
        pairs: List<Pair<Tile, Tile>>,
        random: Random
    ): List<BoardTile> {
        val flatTiles = mutableListOf<Tile>()
        for (p in pairs) {
            flatTiles.add(p.first)
            flatTiles.add(p.second)
        }
        flatTiles.shuffle(random)

        return allPositions.mapIndexed { index, pos ->
            BoardTile(
                id = index,
                tile = flatTiles[index % flatTiles.size],
                position = pos,
                isRemoved = false,
                isFree = false
            )
        }
    }

    /**
     * Generates [pairCount] matching pairs of traditional Mahjong tiles.
     */
    fun generateTilePairs(pairCount: Int, random: Random = Random): List<Pair<Tile, Tile>> {
        val full144 = Tile.createStandard144Set().shuffled(random)
        val pairs = mutableListOf<Pair<Tile, Tile>>()

        // Form pairs from full144
        val available = full144.toMutableList()
        var i = 0
        while (i < available.size && pairs.size < pairCount) {
            val t1 = available[i]
            var matchIdx = -1
            for (j in (i + 1) until available.size) {
                if (t1.matches(available[j])) {
                    matchIdx = j
                    break
                }
            }
            if (matchIdx != -1) {
                val t2 = available.removeAt(matchIdx)
                available.removeAt(i)
                pairs.add(Pair(t1, t2))
            } else {
                i++
            }
        }

        // If pairCount > available from standard 144, duplicate existing pairs
        while (pairs.size < pairCount) {
            val randomPair = pairs.random(random)
            pairs.add(Pair(randomPair.first.copy(), randomPair.second.copy()))
        }

        return pairs.shuffled(random)
    }
}
