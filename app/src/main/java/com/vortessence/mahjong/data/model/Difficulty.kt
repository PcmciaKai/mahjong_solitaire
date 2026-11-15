package com.vortessence.mahjong.data.model

import androidx.annotation.StringRes
import com.vortessence.mahjong.R

enum class Difficulty(@param:StringRes val labelRes: Int, val stars: Int) {
    EASY(R.string.difficulty_easy, 1),
    MEDIUM(R.string.difficulty_medium, 2),
    HARD(R.string.difficulty_hard, 3),
    EXPERT(R.string.difficulty_expert, 4)
}
