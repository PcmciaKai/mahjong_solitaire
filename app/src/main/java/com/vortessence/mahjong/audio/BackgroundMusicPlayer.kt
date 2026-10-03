package com.vortessence.mahjong.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.vortessence.mahjong.R
import kotlin.random.Random

class BackgroundMusicPlayer(private val context: Context) {

    private val playlist = intArrayOf(
        R.raw.mountain_mist,
        R.raw.cleansed_heart,
        R.raw.jade_rain_pavillion,
    )

    private var mediaPlayer: MediaPlayer? = null
    private var currentIndex = -1
    private var currentVolume = 0.4f
    private var isStarted = false
    private var isPaused = false
    private val lock = Any()

    fun setVolume(volume: Float) {
        synchronized(lock) {
            currentVolume = volume.coerceIn(0f, 1f)
            try {
                mediaPlayer?.setVolume(currentVolume, currentVolume)
            } catch (_: Exception) {
                // Ignore if media player is not in a valid state
            }
        }
    }

    fun startRandom() {
        synchronized(lock) {
            if (playlist.isEmpty()) return
            currentIndex = Random.nextInt(playlist.size)
            isStarted = true
            isPaused = false
            playTrack(currentIndex)
        }
    }

    private fun playNextTrack() {
        synchronized(lock) {
            if (!isStarted || isPaused || playlist.isEmpty()) return
            currentIndex = (currentIndex + 1) % playlist.size
            playTrack(currentIndex)
        }
    }

    private fun playTrack(index: Int) {
        releasePlayer()

        if (index !in playlist.indices) return
        val resId = playlist[index]

        try {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            val player = MediaPlayer.create(context, resId, audioAttributes, 0)
                ?: MediaPlayer.create(context, resId)
                ?: return

            player.setVolume(currentVolume, currentVolume)
            player.setOnCompletionListener {
                playNextTrack()
            }
            player.start()
            mediaPlayer = player
        } catch (_: Exception) {
            // Ignore playback interruption
        }
    }

    fun pause() {
        synchronized(lock) {
            isPaused = true
            try {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()
                }
            } catch (_: Exception) {
                // Ignore
            }
        }
    }

    fun resume() {
        synchronized(lock) {
            if (!isStarted) {
                startRandom()
                return
            }
            isPaused = false
            try {
                if (mediaPlayer != null) {
                    mediaPlayer?.start()
                } else if (currentIndex in playlist.indices) {
                    playTrack(currentIndex)
                } else {
                    startRandom()
                }
            } catch (_: Exception) {
                startRandom()
            }
        }
    }

    fun stop() {
        synchronized(lock) {
            isStarted = false
            isPaused = false
            releasePlayer()
        }
    }

    fun release() {
        stop()
    }

    private fun releasePlayer() {
        try {
            mediaPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                release()
            }
        } catch (_: Exception) {
            // Ignore
        }
        mediaPlayer = null
    }
}
