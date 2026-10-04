package com.vortessence.mahjong.data.repository

import android.content.Context
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

class PreferencesRepository(private val context: Context) {

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
        val HIGHEST_LEVEL = intPreferencesKey("highest_level")
        val COMPLETED_CONSTELLATIONS = stringSetPreferencesKey("completed_constellations")
        val BEST_TIMES = stringPreferencesKey("best_times") // format: "id1:120,id2:85"
        val SAVED_GAME = stringPreferencesKey("saved_game")
        val TABLE_SURFACE = stringPreferencesKey("table_surface")
    }

    val settingsFlow: Flow<GameSettings> = context.dataStore.data.map { prefs ->
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
            animationsEnabled = prefs[Keys.ANIMATIONS] ?: true,
            vibrationEnabled = prefs[Keys.VIBRATION] ?: true,
            showCornerIndex = prefs[Keys.SHOW_CORNER_INDEX] ?: false,
            tableSurface = surface,
        )
    }

    val statsFlow: Flow<GameStats> = context.dataStore.data.map { prefs ->
        val played = prefs[Keys.GAMES_PLAYED] ?: 0
        val completed = prefs[Keys.GAMES_COMPLETED] ?: 0
        val highest = prefs[Keys.HIGHEST_LEVEL] ?: 1
        val completedSet = prefs[Keys.COMPLETED_CONSTELLATIONS] ?: emptySet()
        val bestTimesRaw = prefs[Keys.BEST_TIMES] ?: ""

        val bestTimesMap = mutableMapOf<String, Long>()
        if (bestTimesRaw.isNotEmpty()) {
            bestTimesRaw.split(",").forEach { entry ->
                val parts = entry.split(":")
                if (parts.size == 2) {
                    parts[1].toLongOrNull()?.let { time ->
                        bestTimesMap[parts[0]] = time
                    }
                }
            }
        }

        GameStats(
            gamesPlayed = played,
            gamesCompleted = completed,
            highestLevelReached = highest,
            bestTimes = bestTimesMap,
            completedConstellations = completedSet,
        )
    }

    suspend fun updateSettings(settings: GameSettings) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SOUND_ENABLED] = settings.soundEnabled
            prefs[Keys.SOUND_VOLUME] = settings.soundVolume
            prefs[Keys.MUSIC_ENABLED] = settings.musicEnabled
            prefs[Keys.MUSIC_VOLUME] = settings.musicVolume
            prefs[Keys.AUTO_ZOOM] = settings.autoZoomEnabled
            prefs[Keys.HIGHLIGHT_FREE_TILES] = settings.highlightFreeTiles
            prefs[Keys.ANIMATIONS] = settings.animationsEnabled
            prefs[Keys.VIBRATION] = settings.vibrationEnabled
            prefs[Keys.SHOW_CORNER_INDEX] = settings.showCornerIndex
            prefs[Keys.TABLE_SURFACE] = settings.tableSurface.name
            prefs[Keys.THEME] = settings.tableSurface.name
        }
    }

    suspend fun recordGameStarted() {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.GAMES_PLAYED] ?: 0
            prefs[Keys.GAMES_PLAYED] = current + 1
        }
    }

    suspend fun recordGameWon(constellationId: String, levelIndex: Int?, timeSeconds: Long) {
        context.dataStore.edit { prefs ->
            val completed = prefs[Keys.GAMES_COMPLETED] ?: 0
            prefs[Keys.GAMES_COMPLETED] = completed + 1

            // Level progression
            if (levelIndex != null) {
                val currentHighest = prefs[Keys.HIGHEST_LEVEL] ?: 1
                if (levelIndex >= currentHighest) {
                    prefs[Keys.HIGHEST_LEVEL] = levelIndex + 1
                }
            }

            // Completed set
            val completedSet = (prefs[Keys.COMPLETED_CONSTELLATIONS] ?: emptySet()).toMutableSet()
            completedSet.add(constellationId)
            prefs[Keys.COMPLETED_CONSTELLATIONS] = completedSet

            // Best times
            val bestTimesRaw = prefs[Keys.BEST_TIMES] ?: ""
            val bestTimesMap = mutableMapOf<String, Long>()
            if (bestTimesRaw.isNotEmpty()) {
                bestTimesRaw.split(",").forEach { entry ->
                    val parts = entry.split(":")
                    if (parts.size == 2) {
                        parts[1].toLongOrNull()?.let { t ->
                            bestTimesMap[parts[0]] = t
                        }
                    }
                }
            }
            val existing = bestTimesMap[constellationId]
            if (existing == null || (timeSeconds < existing)) {
                bestTimesMap[constellationId] = timeSeconds
            }
            prefs[Keys.BEST_TIMES] = bestTimesMap.entries.joinToString(",") { "${it.key}:${it.value}" }
        }
    }

    val savedGameFlow: Flow<SavedGameState?> = context.dataStore.data.map { prefs ->
        val raw = prefs[Keys.SAVED_GAME] ?: ""
        if (raw.isNotEmpty()) {
            SavedGameState.deserialize(raw)
        } else {
            null
        }
    }

    suspend fun saveGame(savedState: SavedGameState) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SAVED_GAME] = savedState.serialize()
        }
    }

    suspend fun clearSavedGame() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.SAVED_GAME)
        }
    }
}
