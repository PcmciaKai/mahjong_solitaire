package com.vortessence.mahjong.audio

import com.vortessence.mahjong.data.model.GameSettings

/**
 * Sound, music and haptic feedback used by the game.
 */
interface GameSounds {
    fun updateSettings(settings: GameSettings)
    fun onResume()
    fun onPause()
    fun release()
    fun playTileSelect()
    fun playTileDeselect()
    fun playTileMatch()
    fun playTileBlocked()
    fun playWin()
}
