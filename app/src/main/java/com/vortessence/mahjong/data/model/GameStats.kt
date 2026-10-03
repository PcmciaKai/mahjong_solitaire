package com.vortessence.mahjong.data.model

data class GameStats(
    val gamesPlayed: Int = 0,
    val gamesCompleted: Int = 0,
    val highestLevelReached: Int = 1,
    val bestTimes: Map<String, Long> = emptyMap(),
    val completedConstellations: Set<String> = emptySet(),
)
