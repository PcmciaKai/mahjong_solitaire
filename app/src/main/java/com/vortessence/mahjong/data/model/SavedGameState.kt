package com.vortessence.mahjong.data.model

import com.vortessence.mahjong.data.repository.ConstellationRepository
import com.vortessence.mahjong.engine.MahjongGameEngine
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class SavedTile(
    val id: Int,
    val suit: String,
    val value: Int,
    val x: Float,
    val y: Float,
    val z: Int,
    val isRemoved: Boolean,
) {
    val position: TilePosition get() = TilePosition(x, y, z)
}

/**
 * What the main menu shows about the game that can be continued.
 */
data class ContinueInfo(
    val levelIndex: Int?,
    val constellationName: String,
    val remainingTiles: Int,
)

/**
 * Persisted snapshot of a game in progress.
 *
 * Stored as JSON with a [version] field. New fields must get a default value so older saves keep
 * loading; unknown fields from newer saves are ignored.
 */
@Serializable
data class SavedGameState(
    val version: Int = CURRENT_VERSION,
    val constellationId: String,
    val levelIndex: Int?,
    val timerSeconds: Long,
    val movesCount: Int,
    val tiles: List<SavedTile>,
    val moveHistory: List<Move>,
    val hintsUsed: Int = 0,
    val undosUsed: Int = 0,
    val reshufflesUsed: Int = 0,
) {
    fun toGameState(): GameState {
        val constellation = ConstellationRepository.getById(constellationId)
        val boardTiles = tiles.map { st ->
            BoardTile(
                id = st.id,
                tile = Tile(TileSuit.valueOf(st.suit), st.value),
                position = st.position,
                isRemoved = st.isRemoved,
                isFree = false,
            )
        }
        val tilesWithFreedom = MahjongGameEngine.recalculateFreedom(boardTiles)
        val availablePairs = MahjongGameEngine.findAvailablePairs(tilesWithFreedom)
        val remainingCount = tilesWithFreedom.count { !it.isRemoved }
        val isWon = remainingCount == 0
        val isStalemate = MahjongGameEngine.isStuck(tilesWithFreedom, availablePairs)

        return GameState(
            constellation = constellation,
            tiles = tilesWithFreedom,
            selectedTileId = null,
            hintPair = null,
            moveHistory = moveHistory,
            movesCount = movesCount,
            isPlaying = true,
            isWon = isWon,
            isStalemate = isStalemate,
            availablePairsCount = MahjongGameEngine.countRemovablePairs(tilesWithFreedom),
            blockedTileId = null,
            hintsUsed = hintsUsed,
            undosUsed = undosUsed,
            reshufflesUsed = reshufflesUsed,
        )
    }

    fun serialize(): String = json.encodeToString(serializer(), this)

    companion object {
        const val CURRENT_VERSION = 2

        private val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        fun fromGameState(state: GameState, levelIndex: Int?, timerSeconds: Long): SavedGameState {
            return SavedGameState(
                constellationId = state.constellation.id,
                levelIndex = levelIndex,
                timerSeconds = timerSeconds,
                movesCount = state.movesCount,
                tiles = state.tiles.map { bt ->
                    SavedTile(
                        id = bt.id,
                        suit = bt.tile.suit.name,
                        value = bt.tile.value,
                        x = bt.position.x,
                        y = bt.position.y,
                        z = bt.position.z,
                        isRemoved = bt.isRemoved,
                    )
                },
                moveHistory = state.moveHistory,
                hintsUsed = state.hintsUsed,
                undosUsed = state.undosUsed,
                reshufflesUsed = state.reshufflesUsed,
            )
        }

        fun deserialize(raw: String): SavedGameState? {
            if (raw.isBlank()) return null
            val saved = if (raw.trimStart().startsWith("{")) {
                try {
                    json.decodeFromString(serializer(), raw)
                } catch (_: Exception) {
                    null
                }
            } else {
                deserializeLegacy(raw)
            }
            return saved?.takeIf { isValid(it) }
        }

        private fun isValid(saved: SavedGameState): Boolean =
            saved.constellationId.isNotBlank() &&
                saved.tiles.isNotEmpty() &&
                saved.tiles.all { t -> TileSuit.entries.any { it.name == t.suit } }

        /**
         * Reads the pipe-separated format written by version 1 (app 1.2.4 and earlier).
         */
        private fun deserializeLegacy(raw: String): SavedGameState? {
            try {
                val parts = raw.split("|")
                if (parts.size < 6) return null

                val constellationId = parts[0]
                if (constellationId.isBlank()) return null
                val levelIndex = parts[1].toIntOrNull()
                val timerSeconds = parts[2].toLongOrNull() ?: 0L
                val movesCount = parts[3].toIntOrNull() ?: 0

                val tilesRaw = parts[4]
                if (tilesRaw.isBlank()) return null
                val tiles = tilesRaw.split(";").mapNotNull { tStr ->
                    val tParts = tStr.split(",")
                    if (tParts.size >= 7) {
                        val id = tParts[0].toIntOrNull() ?: return@mapNotNull null
                        val suit = tParts[1].takeIf { name -> TileSuit.entries.any { it.name == name } }
                            ?: return@mapNotNull null
                        val value = tParts[2].toIntOrNull() ?: return@mapNotNull null
                        val x = tParts[3].toFloatOrNull() ?: return@mapNotNull null
                        val y = tParts[4].toFloatOrNull() ?: return@mapNotNull null
                        val z = tParts[5].toIntOrNull() ?: return@mapNotNull null
                        val isRemoved = tParts[6] == "1"
                        SavedTile(id, suit, value, x, y, z, isRemoved)
                    } else {
                        null
                    }
                }
                if (tiles.isEmpty()) return null

                val movesRaw = parts[5]
                val moves = if (movesRaw.isNotBlank()) {
                    movesRaw.split(";").mapNotNull { mStr ->
                        val mParts = mStr.split(",")
                        if (mParts.size >= 2) {
                            val t1 = mParts[0].toIntOrNull() ?: return@mapNotNull null
                            val t2 = mParts[1].toIntOrNull() ?: return@mapNotNull null
                            Move(t1, t2)
                        } else {
                            null
                        }
                    }
                } else {
                    emptyList()
                }

                return SavedGameState(
                    version = 1,
                    constellationId = constellationId,
                    levelIndex = levelIndex,
                    timerSeconds = timerSeconds,
                    movesCount = movesCount,
                    tiles = tiles,
                    moveHistory = moves,
                )
            } catch (_: Exception) {
                return null
            }
        }
    }
}
