@file:Suppress("unused")

package com.vortessence.mahjong.data.model

enum class TileSuit(val displayName: String) {
    CHARACTER("Characters"),
    BAMBOO("Bamboo"),
    CIRCLE("Circles"),
    WIND("Winds"),
    DRAGON("Dragons"),
    SEASON("Seasons"),
    FLOWER("Flowers")
}

data class Tile(
    val suit: TileSuit,
    val value: Int, // 1..9 for suits; 1..4 for Winds; 1..3 for Dragons; 1..4 for Seasons/Flowers
) {
    /**
     * Determines if two tiles match according to traditional Mahjong Solitaire rules:
     * - Standard suits match identical suit and value
     * - Any Season matches any Season
     * - Any Flower matches any Flower
     */
    fun matches(other: Tile): Boolean {
        if ((suit == TileSuit.SEASON) && (other.suit == TileSuit.SEASON)) return true
        if ((suit == TileSuit.FLOWER) && (other.suit == TileSuit.FLOWER)) return true
        return (suit == other.suit) && (value == other.value)
    }

    val identifier: String
        get() = "${suit.name}_$value"

    companion object {
        // Wind indices: 1 = East, 2 = South, 3 = West, 4 = North
        const val WIND_EAST = 1
        const val WIND_SOUTH = 2
        const val WIND_WEST = 3
        const val WIND_NORTH = 4

        // Dragon indices: 1 = Red (中), 2 = Green (發), 3 = White (白)
        const val DRAGON_RED = 1
        const val DRAGON_GREEN = 2
        const val DRAGON_WHITE = 3

        // Season indices: 1 = Spring (春), 2 = Summer (夏), 3 = Autumn (秋), 4 = Winter (冬)
        const val SEASON_SPRING = 1
        const val SEASON_SUMMER = 2
        const val SEASON_AUTUMN = 3
        const val SEASON_WINTER = 4

        // Flower indices: 1 = Plum (梅), 2 = Orchid (蘭), 3 = Chrysanthemum (菊), 4 = Bamboo (竹)
        const val FLOWER_PLUM = 1
        const val FLOWER_ORCHID = 2
        const val FLOWER_CHRYSANTHEMUM = 3
        const val FLOWER_BAMBOO = 4

        /**
         * Standard complete set of 144 Mahjong tiles (72 pairs)
         */
        fun createStandard144Set(): List<Tile> {
            val tiles = mutableListOf<Tile>()
            // 4 copies of Characters 1-9 (36 tiles)
            for (v in 1..9) {
                repeat(4) { tiles.add(Tile(TileSuit.CHARACTER, v)) }
            }
            // 4 copies of Bamboo 1-9 (36 tiles)
            for (v in 1..9) {
                repeat(4) { tiles.add(Tile(TileSuit.BAMBOO, v)) }
            }
            // 4 copies of Circles 1-9 (36 tiles)
            for (v in 1..9) {
                repeat(4) { tiles.add(Tile(TileSuit.CIRCLE, v)) }
            }
            // 4 copies of Winds 1-4 (16 tiles)
            for (v in 1..4) {
                repeat(4) { tiles.add(Tile(TileSuit.WIND, v)) }
            }
            // 4 copies of Dragons 1-3 (12 tiles)
            for (v in 1..3) {
                repeat(4) { tiles.add(Tile(TileSuit.DRAGON, v)) }
            }
            // 1 copy of Seasons 1-4 (4 tiles)
            for (v in 1..4) {
                tiles.add(Tile(TileSuit.SEASON, v))
            }
            // 1 copy of Flowers 1-4 (4 tiles)
            for (v in 1..4) {
                tiles.add(Tile(TileSuit.FLOWER, v))
            }
            return tiles
        }
    }
}
