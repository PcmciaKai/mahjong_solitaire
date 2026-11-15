package com.vortessence.mahjong.data.model

data class GameStats(
    val gamesPlayed: Int = 0,
    val gamesCompleted: Int = 0,
    // Phone and tablet use different level sets, so their progress is tracked separately
    val highestLevelReached: Int = 1,
    val highestTabletLevelReached: Int = 1,
    val bestTimes: Map<String, Long> = emptyMap(),
    // Constellation ids whose best time was set with hints, undos or reshuffles
    val assistedBestTimes: Set<String> = emptySet(),
    val completedConstellations: Set<String> = emptySet(),
) {
    fun highestLevel(isTablet: Boolean): Int =
        if (isTablet) highestTabletLevelReached else highestLevelReached
}
