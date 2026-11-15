package com.vortessence.mahjong.engine

import androidx.annotation.StringRes
import com.vortessence.mahjong.R
import com.vortessence.mahjong.data.model.GameState
import com.vortessence.mahjong.data.repository.ConstellationRepository

enum class TutorialAction {
    // Player reads the text and presses the continue button
    CONTINUE,
    // Player taps the blocked target tile to see that it can't be selected
    TAP_BLOCKED,
    // Player matches the target tiles
    MATCH_PAIR,
}

data class TutorialStep(
    @param:StringRes val titleRes: Int,
    @param:StringRes val messageRes: Int,
    val action: TutorialAction = TutorialAction.CONTINUE,
    val targetTileIds: Set<Int> = emptySet(),
    val highlightFreeTiles: Boolean = false,
    val showSettingsToggles: Boolean = false,
    @param:StringRes val continueLabelRes: Int = R.string.tutorial_next,
)

/**
 * Guided walkthrough for the tutorial constellation. Tile ids refer to the position order in
 * ConstellationRepository.createTutorial().
 */
object TutorialScript {

    private const val TOP_LEFT_BAMBOO = 0
    private const val SPRING = 1
    private const val AUTUMN = 2
    private const val TOP_RIGHT_BAMBOO = 3
    private const val PLUM = 12
    private const val CHRYSANTHEMUM = 13

    val steps: List<TutorialStep> = listOf(
        TutorialStep(
            titleRes = R.string.tutorial_welcome_title,
            messageRes = R.string.tutorial_welcome_message,
            continueLabelRes = R.string.tutorial_start,
        ),
        TutorialStep(
            titleRes = R.string.tutorial_free_title,
            messageRes = R.string.tutorial_free_message,
            highlightFreeTiles = true,
        ),
        TutorialStep(
            titleRes = R.string.tutorial_blocked_title,
            messageRes = R.string.tutorial_blocked_message,
            action = TutorialAction.TAP_BLOCKED,
            targetTileIds = setOf(SPRING),
        ),
        TutorialStep(
            titleRes = R.string.tutorial_pair_title,
            messageRes = R.string.tutorial_pair_message,
            action = TutorialAction.MATCH_PAIR,
            targetTileIds = setOf(TOP_LEFT_BAMBOO, TOP_RIGHT_BAMBOO),
        ),
        TutorialStep(
            titleRes = R.string.tutorial_seasons_title,
            messageRes = R.string.tutorial_seasons_message,
            action = TutorialAction.MATCH_PAIR,
            targetTileIds = setOf(SPRING, AUTUMN),
        ),
        TutorialStep(
            titleRes = R.string.tutorial_flowers_title,
            messageRes = R.string.tutorial_flowers_message,
            action = TutorialAction.MATCH_PAIR,
            targetTileIds = setOf(PLUM, CHRYSANTHEMUM),
        ),
        TutorialStep(
            titleRes = R.string.tutorial_settings_title,
            messageRes = R.string.tutorial_settings_message,
            showSettingsToggles = true,
        ),
        TutorialStep(
            titleRes = R.string.tutorial_your_turn_title,
            messageRes = R.string.tutorial_your_turn_message,
            continueLabelRes = R.string.tutorial_lets_go,
        ),
    )

    private val guidedPairs: Set<Int> = steps
        .filter { it.action == TutorialAction.MATCH_PAIR }
        .flatMap { it.targetTileIds }
        .toSet()

    fun isTutorial(state: GameState): Boolean =
        state.constellation.id == ConstellationRepository.TUTORIAL_ID

    /**
     * Picks the step to resume at for a restored tutorial game, or null if the guided part is over.
     */
    fun resumeStepFor(state: GameState): Int? {
        if (!isTutorial(state)) return null
        val removed = state.tiles.filter { it.isRemoved }.map { it.id }.toSet()
        if (removed.isEmpty()) return 0
        // Tiles outside the guided pairs are only removable in free play
        if (!guidedPairs.containsAll(removed)) return null

        val nextMatch = steps.indexOfFirst {
            it.action == TutorialAction.MATCH_PAIR && !removed.containsAll(it.targetTileIds)
        }
        return if (nextMatch != -1) {
            nextMatch
        } else {
            steps.indexOfLast { it.action == TutorialAction.MATCH_PAIR } + 1
        }
    }

    /**
     * Whether the player may tap [tileId] while [step] is shown.
     */
    fun isTapAllowed(step: TutorialStep, tileId: Int): Boolean =
        step.action != TutorialAction.CONTINUE && tileId in step.targetTileIds

    /**
     * Whether the tap that produced [result] and [newState] completes [step].
     */
    fun isStepComplete(step: TutorialStep, result: TileTapResult, newState: GameState): Boolean =
        when (step.action) {
            TutorialAction.CONTINUE -> false
            TutorialAction.TAP_BLOCKED -> result is TileTapResult.Blocked && result.tileId in step.targetTileIds
            TutorialAction.MATCH_PAIR -> newState.tiles
                .filter { it.id in step.targetTileIds }
                .all { it.isRemoved }
        }
}
