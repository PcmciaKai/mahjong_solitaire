package com.vortessence.mahjong.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.vortessence.mahjong.R
import com.vortessence.mahjong.data.model.GameSettings
import kotlin.random.Random

class AudioManager(private val context: Context) {

    private val musicPlayer = BackgroundMusicPlayer(context.applicationContext)
    private var currentSettings: GameSettings? = null
    private var isInForeground = false
    private var matchStreak = 0
    private var lastMatchTimeMs = 0L

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(8)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build(),
        )
        .build()

    private val pickSoundIds = intArrayOf(
        soundPool.load(context.applicationContext, R.raw.mahjong_pick_1, 1),
        soundPool.load(context.applicationContext, R.raw.mahjong_pick_2, 1),
        soundPool.load(context.applicationContext, R.raw.mahjong_pick_3, 1),
        soundPool.load(context.applicationContext, R.raw.mahjong_pick_4, 1),
    )

    private val matchSoundIds = intArrayOf(
        soundPool.load(context.applicationContext, R.raw.mahjong_match_1, 1),
        soundPool.load(context.applicationContext, R.raw.mahjong_match_2, 1),
        soundPool.load(context.applicationContext, R.raw.mahjong_match_3, 1),
        soundPool.load(context.applicationContext, R.raw.mahjong_match_4, 1),
    )

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    fun updateSettings(settings: GameSettings) {
        val previousSettings = currentSettings
        currentSettings = settings

        if (settings.musicEnabled && (settings.musicVolume > 0f)) {
            musicPlayer.setVolume(settings.musicVolume)
            val wasMusicEnabled = previousSettings?.let { it.musicEnabled && (it.musicVolume > 0f) } ?: false
            if (!isInForeground) {
                // Never start playback while in the background; onResume() will start it.
                // Stopping here ensures a random song is picked when music was just enabled.
                if (!wasMusicEnabled) musicPlayer.stop()
            } else if (!wasMusicEnabled) {
                // When music is enabled on startup or toggled on, start with a random song
                musicPlayer.startRandom()
            } else {
                musicPlayer.resume()
            }
        } else {
            // When music is disabled, stop playback so re-enabling starts with a random song
            musicPlayer.stop()
        }
    }

    fun onResume() {
        isInForeground = true
        // Do not play any music until settings have actually been checked and loaded
        val settings = currentSettings ?: return
        if (settings.musicEnabled && (settings.musicVolume > 0f)) {
            musicPlayer.resume()
        }
    }

    fun onPause() {
        isInForeground = false
        musicPlayer.pause()
    }

    fun release() {
        musicPlayer.release()
        try {
            soundPool.release()
        } catch (_: Exception) {
            // Ignore
        }
    }

    fun playTileSelect() {
        val settings = currentSettings ?: return
        if (settings.soundEnabled) {
            playRandomSound(pickSoundIds, settings.soundVolume)
        }
        vibrate { HapticPatterns.select(it) }
    }

    fun playTileDeselect() {
        val settings = currentSettings ?: return
        if (settings.soundEnabled) {
            playRandomSound(pickSoundIds, settings.soundVolume)
        }
        vibrate { HapticPatterns.deselect(it) }
    }

    fun playTileMatch() {
        val settings = currentSettings ?: return
        if (settings.soundEnabled) {
            playRandomSound(matchSoundIds, settings.soundVolume)
        }
        val now = SystemClock.elapsedRealtime()
        matchStreak = if ((now - lastMatchTimeMs) <= MATCH_STREAK_WINDOW_MS) {
            (matchStreak + 1).coerceAtMost(HapticPatterns.MAX_MATCH_STREAK)
        } else {
            0
        }
        lastMatchTimeMs = now
        vibrate { HapticPatterns.match(it, matchStreak) }
    }

    fun playTileBlocked() {
        val settings = currentSettings ?: return
        if (settings.soundEnabled) {
            AudioEffects.playBlocked(settings.soundVolume)
        }
        vibrate { HapticPatterns.blocked() }
    }

    fun playWin() {
        val settings = currentSettings ?: return
        if (settings.soundEnabled) {
            AudioEffects.playWin(settings.soundVolume)
        }
        vibrate { HapticPatterns.win(it) }
    }

    private fun playRandomSound(soundIds: IntArray, volume: Float) {
        if ((volume <= 0f) || soundIds.isEmpty()) return
        val soundId = soundIds[Random.nextInt(soundIds.size)]
        val scaledVolume = volume.coerceIn(0f, 1f)
        try {
            soundPool.play(soundId, scaledVolume, scaledVolume, 1, 0, 1.0f)
        } catch (_: Exception) {
            // Ignore audio playback exception
        }
    }

    private fun vibrate(effect: (Vibrator) -> VibrationEffect) {
        if (currentSettings?.vibrationEnabled != true) return
        val vibrator = vibrator ?: return
        try {
            vibrator.vibrate(effect(vibrator))
        } catch (_: Exception) {
            // Ignore if device has no vibration motor or permission denied
        }
    }

    private companion object {
        // Matches made within this window of each other build up the haptic streak
        const val MATCH_STREAK_WINDOW_MS = 4000L
    }
}
