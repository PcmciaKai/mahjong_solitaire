package com.vortessence.mahjong.engine

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
    val title: String,
    val message: String,
    val action: TutorialAction = TutorialAction.CONTINUE,
    val targetTileIds: Set<Int> = emptySet(),
    val highlightFreeTiles: Boolean = false,
    val showSettingsToggles: Boolean = false,
    val continueLabel: String = "Next",
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
            title = "Welcome to Mahjong Solitaire",
            message = "Your goal is to clear the board by removing all tiles in matching pairs. " +
                "This short tutorial shows you how it works.",
            continueLabel = "Start",
        ),
        TutorialStep(
            title = "Selectable Tiles",
            message = "You can only pick a tile that is free: no other tile may lie on top of it, " +
                "and its left or right side must be open. The blue tiles are free right now.",
            highlightFreeTiles = true,
        ),
        TutorialStep(
            title = "Blocked Tiles",
            message = "The blue tile has neighbours on both its left and its right side, " +
                "so it is blocked. Tap it and see what happens.",
            action = TutorialAction.TAP_BLOCKED,
            targetTileIds = setOf(SPRING),
        ),
        TutorialStep(
            title = "Match a Pair",
            message = "Blocked tiles can't be selected. Now tap the two blue tiles. The first one turns " +
                "gold to show it is selected. They are identical, so together they form a pair and are removed.",
            action = TutorialAction.MATCH_PAIR,
            targetTileIds = setOf(TOP_LEFT_BAMBOO, TOP_RIGHT_BAMBOO),
        ),
        TutorialStep(
            title = "Seasons",
            message = "Removing the pair opened up its neighbours. These are Season tiles " +
                "(Spring, Summer, Autumn and Winter). Any two Seasons match each other, " +
                "even though they look different. Match them!",
            action = TutorialAction.MATCH_PAIR,
            targetTileIds = setOf(SPRING, AUTUMN),
        ),
        TutorialStep(
            title = "Flowers",
            message = "These two Flower tiles lie on top of other tiles, which stay blocked until the " +
                "Flowers are gone. Any two Flowers (Plum, Orchid, Chrysanthemum and Bamboo) " +
                "match each other, just like the Seasons.",
            action = TutorialAction.MATCH_PAIR,
            targetTileIds = setOf(PLUM, CHRYSANTHEMUM),
        ),
        TutorialStep(
            title = "Helpful Settings",
            message = "Want a little help? You can turn these on right here, " +
                "or change them any time in the Settings menu.",
            showSettingsToggles = true,
        ),
        TutorialStep(
            title = "Your Turn",
            message = "Clear the remaining tiles on your own. Stuck? The lightbulb shows a hint " +
                "and the arrow undoes your last move.",
            continueLabel = "Let's go",
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
