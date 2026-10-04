package com.vortessence.mahjong.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import java.util.concurrent.Executors
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

object AudioEffects {

    private const val SAMPLE_RATE = 44100
    private val executor = Executors.newFixedThreadPool(2)

    private val blockedSoundBuffer: ShortArray by lazy { generateBlockedBuffer() }
    private val winSoundBuffer: ShortArray by lazy { generateWinChimeBuffer() }

    fun playBlocked(volume: Float) {
        if (volume <= 0f) return
        playSound(blockedSoundBuffer, volume)
    }

    fun playWin(volume: Float) {
        if (volume <= 0f) return
        playSound(winSoundBuffer, volume)
    }

    private fun playSound(buffer: ShortArray, volume: Float) {
        val scaledVolume = volume.coerceIn(0f, 1f)
        executor.execute {
            try {
                val scaledBuffer = ShortArray(buffer.size) { i ->
                    (buffer[i] * scaledVolume).toInt().toShort()
                }
                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build(),
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(SAMPLE_RATE)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build(),
                    )
                    .setBufferSizeInBytes(scaledBuffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(scaledBuffer, 0, scaledBuffer.size)
                track.play()
                // Auto release after playback
                Thread.sleep(((buffer.size * 1000L) / SAMPLE_RATE) + 50)
                track.release()
            } catch (_: Exception) {
                // Ignore audio interruption
            }
        }
    }

    /**
     * Blocked tile sound: dull muted wooden knock (40ms).
     */
    private fun generateBlockedBuffer(): ShortArray {
        val durationMs = 40
        val numSamples = (SAMPLE_RATE * durationMs) / 1000
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val decay = exp(-t * 90.0)
            val wave = 0.8 * sin(2 * PI * 240 * t) + 0.2 * sin(2 * PI * 480 * t)
            buffer[i] = (wave * decay * 20000).toInt().coerceIn(-32767, 32767).toShort()
        }
        return buffer
    }

    /**
     * Win sound: celestial pentatonic chime arpeggio (C5, E5, G5, A5, C6).
     */
    private fun generateWinChimeBuffer(): ShortArray {
        val durationMs = 1500
        val numSamples = (SAMPLE_RATE * durationMs) / 1000
        val buffer = ShortArray(numSamples)
        val notes = listOf(523.25, 659.25, 783.99, 880.0, 1046.50)
        val noteInterval = (SAMPLE_RATE * 0.16).toInt()

        for (i in 0 until numSamples) {
            var sample = 0.0
            for ((noteIdx, freq) in notes.withIndex()) {
                val noteStart = noteIdx * noteInterval
                if (i >= noteStart) {
                    val t = (i - noteStart).toDouble() / SAMPLE_RATE
                    val decay = exp(-t * 3.5)
                    sample += (sin(2 * PI * freq * t) + 0.3 * sin(2 * PI * freq * 2 * t)) * decay * 0.2
                }
            }
            buffer[i] = (sample * 28000).toInt().coerceIn(-32767, 32767).toShort()
        }
        return buffer
    }
}
