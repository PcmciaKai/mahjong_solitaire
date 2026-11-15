package com.vortessence.mahjong.data.repository

import android.content.Context
import android.provider.Settings
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vortessence.mahjong.data.model.GameSettings
import com.vortessence.mahjong.data.model.GameStats
import com.vortessence.mahjong.data.model.SavedGameState
import com.vortessence.mahjong.data.model.TableSurface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mahjong_preferences")

/**
 * Outcome of a won game, as recorded in the statistics.
 */
data class GameResult(
    val constellationId: String,
    val levelIndex: Int?,
    // Which level set [levelIndex] refers to
    val isTabletLevel: Boolean,
    val timeSeconds: Long,
    // Hints, undos or reshuffles were used
    val isAssisted: Boolean,
    // The tutorial is not counted in games played/completed and has no best time
    val isTutorial: Boolean,
)

/**
 * Persistent storage for settings, statistics and the game in progress.
 */
interface GameRepository {
    val settingsFlow: Flow<GameSettings>
    val statsFlow: Flow<GameStats>
    val savedGameFlow: Flow<SavedGameState?>
    suspend fun updateSettings(settings: GameSettings)
    suspend fun recordGameStarted()
    suspend fun recordGameWon(result: GameResult)
    suspend fun saveGame(savedState: SavedGameState)
    suspend fun clearSavedGame()
}

class PreferencesRepository(private val context: Context) : GameRepository {

    private object Keys {
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val SOUND_VOLUME = floatPreferencesKey("sound_volume")
        val MUSIC_ENABLED = booleanPreferencesKey("music_enabled")
        val MUSIC_VOLUME = floatPreferencesKey("music_volume")
        val AUTO_ZOOM = booleanPreferencesKey("auto_zoom")
        val HIGHLIGHT_FREE_TILES = booleanPreferencesKey("highlight_free_tiles")
        val ANIMATIONS = booleanPreferencesKey("animations")
        val VIBRATION = booleanPreferencesKey("vibration")
        val SHOW_CORNER_INDEX = booleanPreferencesKey("show_corner_index")
        val THEME = stringPreferencesKey("theme")

        val GAMES_PLAYED = intPreferencesKey("games_played")
        val GAMES_COMPLETED = intPreferencesKey("games_completed")
        // Phone level progress; also the shared progress of app versions before 1.2.5
        val HIGHEST_LEVEL = intPreferencesKey("highest_level")
        val HIGHEST_LEVEL_TABLET = intPreferencesKey("highest_level_tablet")
        val COMPLETED_CONSTELLATIONS = stringSetPreferencesKey("completed_constellations")
        // format: "id1:120,id2:85:a" where a trailing ":a" marks a time set with assists
        val BEST_TIMES = stringPreferencesKey("best_times")
        val SAVED_GAME = stringPreferencesKey("saved_game")
        val TABLE_SURFACE = stringPreferencesKey("table_surface")
    }

    /** True when "Animator duration scale" in the developer options is set to "Animation off". */
    private fun isSystemAnimationScaleZero(): Boolean =
        Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f

    private data class BestTime(val seconds: Long, val isAssisted: Boolean)

    private fun parseBestTimes(raw: String): MutableMap<String, BestTime> {
        val result = mutableMapOf<String, BestTime>()
        if (raw.isEmpty()) return result
        raw.split(",").forEach { entry ->
            val parts = entry.split(":")
            if (parts.size in 2..3) {
                parts[1].toLongOrNull()?.let { seconds ->
                    result[parts[0]] = BestTime(seconds, isAssisted = parts.getOrNull(2) == "a")
                }
            }
        }
        return result
    }

    private fun formatBestTimes(times: Map<String, BestTime>): String =
        times.entries.joinToString(",") { (id, best) ->
            if (best.isAssisted) "$id:${best.seconds}:a" else "$id:${best.seconds}"
        }

    override val settingsFlow: Flow<GameSettings> = context.dataStore.data.map { prefs ->
        val surfaceStr = prefs[Keys.TABLE_SURFACE] ?: prefs[Keys.THEME] ?: TableSurface.GREEN_MAT.name
        val surface = when (surfaceStr) {
            "WOOD" -> TableSurface.WALNUT
            "DARK" -> TableSurface.GREEN_MAT
            else -> try {
                TableSurface.valueOf(surfaceStr)
            } catch (_: Exception) {
                TableSurface.GREEN_MAT
            }
        }

        GameSettings(
            soundEnabled = prefs[Keys.SOUND_ENABLED] ?: true,
            soundVolume = prefs[Keys.SOUND_VOLUME] ?: 1.0f,
            musicEnabled = prefs[Keys.MUSIC_ENABLED] ?: true,
            musicVolume = prefs[Keys.MUSIC_VOLUME] ?: 0.4f,
            autoZoomEnabled = prefs[Keys.AUTO_ZOOM] ?: true,
            highlightFreeTiles = prefs[Keys.HIGHLIGHT_FREE_TILES] ?: false,
            animationsEnabled = !isSystemAnimationScaleZero() && (prefs[Keys.ANIMATIONS] ?: true),
            vibrationEnabled = prefs[Keys.VIBRATION] ?: true,
            showCornerIndex = prefs[Keys.SHOW_CORNER_INDEX] ?: false,
            tableSurface = surface,
        )
    }

    override val statsFlow: Flow<GameStats> = context.dataStore.data.map { prefs ->
        val played = prefs[Keys.GAMES_PLAYED] ?: 0
        val completed = prefs[Keys.GAMES_COMPLETED] ?: 0
        val highest = prefs[Keys.HIGHEST_LEVEL] ?: 1
        // Tablets used the shared counter before it was split, so fall back to it
        val highestTablet = prefs[Keys.HIGHEST_LEVEL_TABLET] ?: highest
        val completedSet = prefs[Keys.COMPLETED_CONSTELLATIONS] ?: emptySet()
        val bestTimes = parseBestTimes(prefs[Keys.BEST_TIMES] ?: "")

        GameStats(
            gamesPlayed = played,
            gamesCompleted = completed,
            highestLevelReached = highest,
            highestTabletLevelReached = highestTablet,
            bestTimes = bestTimes.mapValues { it.value.seconds },
            assistedBestTimes = bestTimes.filterValues { it.isAssisted }.keys,
            completedConstellations = completedSet,
        )
    }

    override suspend fun updateSettings(settings: GameSettings) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SOUND_ENABLED] = settings.soundEnabled
            prefs[Keys.SOUND_VOLUME] = settings.soundVolume
            prefs[Keys.MUSIC_ENABLED] = settings.musicEnabled
            prefs[Keys.MUSIC_VOLUME] = settings.musicVolume
            prefs[Keys.AUTO_ZOOM] = settings.autoZoomEnabled
            prefs[Keys.HIGHLIGHT_FREE_TILES] = settings.highlightFreeTiles
            // While the system scale is 0x the exposed value is forced to false, so don't let it
            // overwrite the user's own preference
            if (!isSystemAnimationScaleZero()) {
                prefs[Keys.ANIMATIONS] = settings.animationsEnabled
            }
            prefs[Keys.VIBRATION] = settings.vibrationEnabled
            prefs[Keys.SHOW_CORNER_INDEX] = settings.showCornerIndex
            prefs[Keys.TABLE_SURFACE] = settings.tableSurface.name
            prefs[Keys.THEME] = settings.tableSurface.name
        }
    }

    override suspend fun recordGameStarted() {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.GAMES_PLAYED] ?: 0
            prefs[Keys.GAMES_PLAYED] = current + 1
        }
    }

    override suspend fun recordGameWon(result: GameResult) {
        context.dataStore.edit { prefs ->
            if (!result.isTutorial) {
                val completed = prefs[Keys.GAMES_COMPLETED] ?: 0
                prefs[Keys.GAMES_COMPLETED] = completed + 1
            }

            // Level progression, tracked per level set
            val levelIndex = result.levelIndex
            if (levelIndex != null) {
                val legacyHighest = prefs[Keys.HIGHEST_LEVEL] ?: 1
                val key = if (result.isTabletLevel) Keys.HIGHEST_LEVEL_TABLET else Keys.HIGHEST_LEVEL
                val currentHighest = prefs[key] ?: legacyHighest
                if (levelIndex >= currentHighest) {
                    prefs[key] = levelIndex + 1
                }
            }

            // Completed set
            val completedSet = (prefs[Keys.COMPLETED_CONSTELLATIONS] ?: emptySet()).toMutableSet()
            completedSet.add(result.constellationId)
            prefs[Keys.COMPLETED_CONSTELLATIONS] = completedSet

            // Best times: an unassisted time always beats an assisted one
            if (!result.isTutorial) {
                val bestTimes = parseBestTimes(prefs[Keys.BEST_TIMES] ?: "")
                val new = BestTime(result.timeSeconds, result.isAssisted)
                val existing = bestTimes[result.constellationId]
                val isBetter = when {
                    existing == null -> true
                    existing.isAssisted != new.isAssisted -> !new.isAssisted
                    else -> new.seconds < existing.seconds
                }
                if (isBetter) {
                    bestTimes[result.constellationId] = new
                }
                prefs[Keys.BEST_TIMES] = formatBestTimes(bestTimes)
            }
        }
    }

    override val savedGameFlow: Flow<SavedGameState?> = context.dataStore.data.map { prefs ->
        val raw = prefs[Keys.SAVED_GAME] ?: ""
        if (raw.isNotEmpty()) {
            SavedGameState.deserialize(raw)
        } else {
            null
        }
    }

    override suspend fun saveGame(savedState: SavedGameState) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SAVED_GAME] = savedState.serialize()
        }
    }

    override suspend fun clearSavedGame() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.SAVED_GAME)
        }
    }
}
