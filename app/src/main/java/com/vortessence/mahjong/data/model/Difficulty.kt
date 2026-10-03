package com.vortessence.mahjong.data.model

enum class Difficulty(val displayName: String, val stars: Int) {
    EASY("Easy", 1),
    MEDIUM("Medium", 2),
    HARD("Hard", 3),
    EXPERT("Expert", 4)
}
