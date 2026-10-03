package com.vortessence.mahjong.audio

import android.os.VibrationEffect
import android.os.VibrationEffect.Composition
import android.os.Vibrator

object HapticPatterns {

    const val MAX_MATCH_STREAK = 3

    /**
     * Tile select: a crisp click. Uses the vendor tuned primitive or predefined effect instead of
     * a raw motor pulse, since a short low amplitude one shot mostly consists of the motor's spin
     * up and spin down and feels soft and blurry rather than a clean tap.
     */
    fun select(vibrator: Vibrator): VibrationEffect =
        crisp(vibrator, Composition.PRIMITIVE_CLICK, 0.35f, VibrationEffect.EFFECT_CLICK)

    /** Tile deselect: a lighter tick than [select], so both actions can be told apart by feel. */
    fun deselect(vibrator: Vibrator): VibrationEffect =
        crisp(vibrator, Composition.PRIMITIVE_TICK, 0.4f, VibrationEffect.EFFECT_TICK)

    fun blocked(): VibrationEffect = VibrationEffect.createOneShot(25, 40)

    /**
     * Tile match, fired as the two tiles collide in the smash animation: a crisp clack followed
     * by two lighter rebounds, like tiles bouncing apart. A higher [streak] (quick consecutive
     * matches) hits harder and adds trailing sparkle ticks.
     */
    fun match(vibrator: Vibrator, streak: Int): VibrationEffect {
        val level = streak.coerceIn(0, MAX_MATCH_STREAK)
        val boost = level * 0.08f

        if (vibrator.areAllPrimitivesSupported(Composition.PRIMITIVE_CLICK, Composition.PRIMITIVE_TICK)) {
            val composition = VibrationEffect.startComposition()
                .addPrimitive(Composition.PRIMITIVE_CLICK, (0.8f + boost).coerceAtMost(1f))
                .addPrimitive(Composition.PRIMITIVE_CLICK, 0.4f + boost, 45)
                .addPrimitive(Composition.PRIMITIVE_TICK, 0.3f + boost, 40)
            repeat(level) { i ->
                composition.addPrimitive(Composition.PRIMITIVE_TICK, 0.35f + (0.15f * i), 30)
            }
            return composition.compose()
        }

        // Fallback: clack + rebounds as an amplitude waveform
        val timings = mutableListOf(0L, 20L, 45L, 12L, 40L, 8L)
        val amplitudes = mutableListOf(0, 200 + (level * 18), 0, 120 + (level * 15), 0, 70 + (level * 15))
        repeat(level) { i ->
            timings += listOf(30L, 8L)
            amplitudes += listOf(0, 80 + (40 * i))
        }
        return VibrationEffect.createWaveform(timings.toLongArray(), amplitudes.toIntArray(), -1)
    }

    /**
     * Win: follows the pentatonic chime in [AudioEffects] (one note every 160ms). Four rising
     * taps for C5, E5, G5 and A5, then a strong hit on the final C6 that shimmers out.
     */
    fun win(vibrator: Vibrator): VibrationEffect {
        if (!vibrator.hasAmplitudeControl()) {
            // On/off only motors: keep the rhythm, skip the fading tail
            return VibrationEffect.createWaveform(
                longArrayOf(0, 18, 142, 18, 142, 18, 142, 18, 142, 70),
                -1,
            )
        }
        return VibrationEffect.createWaveform(
            longArrayOf(0, 22, 138, 22, 138, 22, 138, 22, 138, 35, 50, 70, 90),
            intArrayOf(0, 70, 0, 100, 0, 135, 0, 170, 0, 255, 140, 70, 30),
            -1,
        )
    }

    private fun crisp(vibrator: Vibrator, primitive: Int, scale: Float, fallbackEffect: Int): VibrationEffect {
        if (vibrator.areAllPrimitivesSupported(primitive)) {
            return VibrationEffect.startComposition().addPrimitive(primitive, scale).compose()
        }
        return VibrationEffect.createPredefined(fallbackEffect)
    }
}
