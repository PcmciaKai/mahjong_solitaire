package com.vortessence.mahjong

import com.vortessence.mahjong.data.model.BoardTile
import com.vortessence.mahjong.data.model.GameSettings
import com.vortessence.mahjong.data.model.SavedGameState
import com.vortessence.mahjong.data.model.TableSurface
import com.vortessence.mahjong.data.model.Tile
import com.vortessence.mahjong.data.model.TilePosition
import com.vortessence.mahjong.data.model.TileSuit
import com.vortessence.mahjong.data.repository.ConstellationRepository
import com.vortessence.mahjong.engine.BoardSolvabilityGenerator
import com.vortessence.mahjong.engine.MahjongGameEngine
import com.vortessence.mahjong.engine.TileTapResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MahjongEngineTest {

    @Test
    fun testTileMatchingRules() {
        // Standard suits match identical suit and value
        val char1A = Tile(TileSuit.CHARACTER, 1)
        val char1B = Tile(TileSuit.CHARACTER, 1)
        val char2 = Tile(TileSuit.CHARACTER, 2)
        val bam1 = Tile(TileSuit.BAMBOO, 1)

        assertTrue("Identical characters should match", char1A.matches(char1B))
        assertFalse("Different character values should not match", char1A.matches(char2))
        assertFalse("Different suits should not match", char1A.matches(bam1))

        // Special rule: Any Season matches any Season
        val spring = Tile(TileSuit.SEASON, Tile.SEASON_SPRING)
        val winter = Tile(TileSuit.SEASON, Tile.SEASON_WINTER)
        assertTrue("Any Season matches any other Season", spring.matches(winter))

        // Special rule: Any Flower matches any Flower
        val plum = Tile(TileSuit.FLOWER, Tile.FLOWER_PLUM)
        val bamboo = Tile(TileSuit.FLOWER, Tile.FLOWER_BAMBOO)
        assertTrue("Any Flower matches any other Flower", plum.matches(bamboo))

        // Season cannot match Flower
        assertFalse("Season should not match Flower", spring.matches(plum))
    }

    @Test
    fun testStandard144SetComposition() {
        val standardSet = Tile.createStandard144Set()
        assertEquals("Standard set must contain 144 tiles", 144, standardSet.size)

        // 36 characters (4 of each 1-9)
        val chars = standardSet.filter { it.suit == TileSuit.CHARACTER }
        assertEquals(36, chars.size)

        // 36 bamboos
        val bams = standardSet.filter { it.suit == TileSuit.BAMBOO }
        assertEquals(36, bams.size)

        // 36 circles
        val circles = standardSet.filter { it.suit == TileSuit.CIRCLE }
        assertEquals(36, circles.size)

        // 16 winds (4 of each E, S, W, N)
        val winds = standardSet.filter { it.suit == TileSuit.WIND }
        assertEquals(16, winds.size)

        // 12 dragons (4 of each Red, Green, White)
        val dragons = standardSet.filter { it.suit == TileSuit.DRAGON }
        assertEquals(12, dragons.size)

        // 4 seasons
        val seasons = standardSet.filter { it.suit == TileSuit.SEASON }
        assertEquals(4, seasons.size)

        // 4 flowers
        val flowers = standardSet.filter { it.suit == TileSuit.FLOWER }
        assertEquals(4, flowers.size)
    }

    @Test
    fun testTileFreedomRules() {
        // Tile size is 2x2.
        // Place 3 tiles in a horizontal row:
        // Tile L at (0, 0, 0), Tile C at (2, 0, 0), Tile R at (4, 0, 0)
        val posL = TilePosition(0f, 0f, 0)
        val posC = TilePosition(2f, 0f, 0)
        val posR = TilePosition(4f, 0f, 0)

        val positions = listOf(posL, posC, posR)

        // posC is blocked on both left (posL) and right (posR)
        assertTrue("C is blocked on left", BoardSolvabilityGenerator.isBlockedLeft(posC, positions))
        assertTrue("C is blocked on right", BoardSolvabilityGenerator.isBlockedRight(posC, positions))
        assertFalse("C should NOT be free", BoardSolvabilityGenerator.isTileFree(posC, positions))

        // posL is only blocked on right, left is open -> FREE
        assertFalse("L is not blocked on left", BoardSolvabilityGenerator.isBlockedLeft(posL, positions))
        assertTrue("L is blocked on right", BoardSolvabilityGenerator.isBlockedRight(posL, positions))
        assertTrue("L should BE free", BoardSolvabilityGenerator.isTileFree(posL, positions))

        // posR is only blocked on left, right is open -> FREE
        assertTrue("R is blocked on left", BoardSolvabilityGenerator.isBlockedLeft(posR, positions))
        assertFalse("R is not blocked on right", BoardSolvabilityGenerator.isBlockedRight(posR, positions))
        assertTrue("R should BE free", BoardSolvabilityGenerator.isTileFree(posR, positions))

        // Now place a tile T on layer 1 covering posL: (0, 0, 1)
        val posT = TilePosition(0f, 0f, 1)
        val coveredPositions = listOf(posL, posC, posR, posT)

        assertTrue("posL is covered by posT", BoardSolvabilityGenerator.isCovered(posL, coveredPositions))
        assertFalse("posL is no longer free because it is covered", BoardSolvabilityGenerator.isTileFree(posL, coveredPositions))
        assertTrue("posT is on top and free", BoardSolvabilityGenerator.isTileFree(posT, coveredPositions))
    }

    @Test
    fun testAllConstellationsHaveEvenTileCounts() {
        for (constellation in ConstellationRepository.constellations) {
            assertTrue(
                "Constellation ${constellation.name} must have an even number of tiles, had ${constellation.tileCount}",
                constellation.tileCount % 2 == 0
            )
            assertTrue(
                "Constellation ${constellation.name} must have at least 10 tiles",
                constellation.tileCount >= 10
            )
        }
    }

    @Test
    fun testSolvableBoardGenerationForAllConstellations() {
        for (constellation in ConstellationRepository.constellations) {
            val board = BoardSolvabilityGenerator.generateSolvableBoard(constellation)
            assertEquals("Board size must match constellation tile count for ${constellation.name}", constellation.tileCount, board.size)

            val tiles = board.map { it.tile }.toMutableList()
            var matchedPairs = 0
            while (tiles.isNotEmpty()) {
                val t1 = tiles.removeAt(0)
                val matchIdx = tiles.indexOfFirst { t1.matches(it) }
                assertTrue("Every tile in ${constellation.name} must have a matching partner", matchIdx != -1)
                tiles.removeAt(matchIdx)
                matchedPairs++
            }
            assertEquals(constellation.tileCount / 2, matchedPairs)
        }
    }

    @Test
    fun testGameEngineTapAndMatchFlow() {
        val miniTurtle = ConstellationRepository.getById("mini_turtle")
        val initialState = MahjongGameEngine.startNewGame(miniTurtle)

        assertTrue("Initial state must have active tiles", initialState.tiles.isNotEmpty())
        assertTrue("Must have at least one available pair to start", initialState.availablePairsCount > 0)

        // Find a matching pair among free tiles
        val availablePair = MahjongGameEngine.findAvailablePairs(initialState.tiles).firstOrNull()
        assertNotNull("Available pair should exist", availablePair)

        val id1 = availablePair!!.first
        val id2 = availablePair.second

        // Tap first tile -> selected
        val (stateAfter1, result1) = MahjongGameEngine.handleTileTap(initialState, id1)
        assertTrue(result1 is TileTapResult.Selected)
        assertEquals(id1, stateAfter1.selectedTileId)

        // Tap second matching tile -> match & removal
        val (stateAfter2, result2) = MahjongGameEngine.handleTileTap(stateAfter1, id2)
        assertTrue(result2 is TileTapResult.Matched)
        assertEquals(null, stateAfter2.selectedTileId)
        assertEquals(1, stateAfter2.movesCount)
        assertEquals(1, stateAfter2.moveHistory.size)

        val tile1 = stateAfter2.tiles.first { it.id == id1 }
        val tile2 = stateAfter2.tiles.first { it.id == id2 }
        assertTrue("Tile 1 should be removed", tile1.isRemoved)
        assertTrue("Tile 2 should be removed", tile2.isRemoved)

        // Test Undo
        val stateAfterUndo = MahjongGameEngine.undo(stateAfter2)
        assertEquals(0, stateAfterUndo.movesCount)
        assertEquals(0, stateAfterUndo.moveHistory.size)

        val tile1Restored = stateAfterUndo.tiles.first { it.id == id1 }
        val tile2Restored = stateAfterUndo.tiles.first { it.id == id2 }
        assertFalse("Tile 1 should be restored", tile1Restored.isRemoved)
        assertFalse("Tile 2 should be restored", tile2Restored.isRemoved)
    }

    @Test
    fun testBlockedTileCannotBeSelected() {
        // Build a state with a blocked tile
        val tileL = BoardTile(id = 1, tile = Tile(TileSuit.BAMBOO, 1), position = TilePosition(0f, 0f, 0))
        val tileC = BoardTile(id = 2, tile = Tile(TileSuit.BAMBOO, 2), position = TilePosition(2f, 0f, 0))
        val tileR = BoardTile(id = 3, tile = Tile(TileSuit.BAMBOO, 3), position = TilePosition(4f, 0f, 0))

        val tilesWithFreedom = MahjongGameEngine.recalculateFreedom(listOf(tileL, tileC, tileR))
        val cTile = tilesWithFreedom.first { it.id == 2 }
        assertFalse("Center tile must be blocked", cTile.isFree)

        val dummyConstellation = ConstellationRepository.constellations.first()
        val state = com.vortessence.mahjong.data.model.GameState(
            constellation = dummyConstellation,
            tiles = tilesWithFreedom
        )

        val (newState, result) = MahjongGameEngine.handleTileTap(state, 2)
        assertTrue("Tapping blocked tile must yield Blocked result", result is TileTapResult.Blocked)
        assertEquals(null, newState.selectedTileId)
        assertEquals(2, newState.blockedTileId)
    }

    @Test
    fun testNoOverlappingTilesOnSameLayerForAllConstellations() {
        for (constellation in ConstellationRepository.constellations) {
            val positions = constellation.positions
            for (i in 0 until positions.size) {
                for (j in i + 1 until positions.size) {
                    val p1 = positions[i]
                    val p2 = positions[j]
                    if (p1.z == p2.z) {
                        assertFalse(
                            "Constellation ${constellation.name} has overlapping tiles on layer ${p1.z}: $p1 and $p2",
                            BoardSolvabilityGenerator.isOverlap2D(p1.x, p1.y, p2.x, p2.y)
                        )
                    }
                }
            }
        }
    }

    @Test
    fun testLevelConstellationsArePortraitOrientedForSmartphones() {
        assertEquals("Should have 12 progressive levels", 12, ConstellationRepository.levelConstellations.size)
        assertEquals("Total levels count should match", 12, ConstellationRepository.totalLevels)

        for (constellation in ConstellationRepository.levelConstellations) {
            assertTrue("Level constellation ${constellation.name} must be marked portrait-optimized", constellation.isPortraitOptimized)

            var minX = Float.MAX_VALUE
            var maxX = -Float.MAX_VALUE
            var minY = Float.MAX_VALUE
            var maxY = -Float.MAX_VALUE

            for (p in constellation.positions) {
                if (p.x < minX) minX = p.x
                if (p.x > maxX) maxX = p.x
                if (p.y < minY) minY = p.y
                if (p.y > maxY) maxY = p.y
            }

            val totalNormW = (maxX - minX) + 2f
            val totalNormH = ((maxY - minY) + 2f) * 1.333f
            val aspectRatio = totalNormW / totalNormH

            // Phone-optimized layouts should be slender / vertical (aspect ratio <= 0.60, width <= 14 units / 7 tiles)
            assertTrue(
                "Level constellation ${constellation.name} width ($totalNormW) must be <= 14 (7 tiles max)",
                totalNormW <= 14f
            )
            assertTrue(
                "Level constellation ${constellation.name} height ($totalNormH) must be >= 18 (utilize vertical space)",
                totalNormH >= 18f
            )
            assertTrue(
                "Level constellation ${constellation.name} normalized aspect ratio ($aspectRatio) should be <= 0.60 for phones",
                aspectRatio <= 0.60f
            )
        }
    }

    @Test
    fun testClassicWideConstellationsArePreservedForManualSelection() {
        assertEquals(12, ConstellationRepository.classicConstellations.size)

        val preservedIds = listOf(
            "mini_turtle", "cross", "diamond", "pyramid", "heart", "crab",
            "butterfly", "classic_turtle", "fortress", "castle", "dragon", "pagoda"
        )

        for (id in preservedIds) {
            val classic = ConstellationRepository.getById(id)
            assertNotNull("Classic constellation with id '$id' must be preserved", classic)
            assertEquals(id, classic.id)
            assertFalse("Classic constellation '$id' should not be marked portrait-optimized", classic.isPortraitOptimized)
        }

        // Verify classic turtle is indeed wide (> 30 units wide, aspect ratio > 1.0)
        val turtle = ConstellationRepository.getById("classic_turtle")
        val turtleW = (turtle.positions.maxOf { it.x } - turtle.positions.minOf { it.x }) + 2f
        val turtleH = ((turtle.positions.maxOf { it.y } - turtle.positions.minOf { it.y }) + 2f) * 1.333f
        assertTrue("Classic turtle must be wide", turtleW > 30f)
        assertTrue("Classic turtle aspect ratio must be > 1.0", turtleW / turtleH > 1.0f)
    }

    @Test
    fun testSavedGameStateSerializationAndRestoration() {
        val constellation = ConstellationRepository.getById("jade_pillar")
        val initialState = MahjongGameEngine.startNewGame(constellation)
        val initialPairs = MahjongGameEngine.findAvailablePairs(initialState.tiles)
        assertTrue(initialPairs.isNotEmpty())

        // Make one match
        val firstPair = initialPairs.first()
        val (stateAfterMatch, matchResult) = MahjongGameEngine.handleTileTap(initialState, firstPair.first)
        assertTrue(matchResult is TileTapResult.Selected)
        val (stateMatched, matchResult2) = MahjongGameEngine.handleTileTap(stateAfterMatch, firstPair.second)
        assertTrue(matchResult2 is TileTapResult.Matched)

        // Advance timer and moves
        val simulatedState = stateMatched.copy(timerSeconds = 42L)
        assertEquals(1, simulatedState.movesCount)
        assertEquals(1, simulatedState.moveHistory.size)
        assertEquals(34, simulatedState.remainingTilesCount)

        // Serialize
        val saved = SavedGameState.fromGameState(simulatedState, levelIndex = 1)
        val raw = saved.serialize()
        assertTrue(raw.contains("jade_pillar"))

        // Deserialize
        val restoredSaved = SavedGameState.deserialize(raw)
        assertNotNull(restoredSaved)
        assertEquals("jade_pillar", restoredSaved!!.constellationId)
        assertEquals(1, restoredSaved.levelIndex)
        assertEquals(42L, restoredSaved.timerSeconds)
        assertEquals(1, restoredSaved.movesCount)
        assertEquals(1, restoredSaved.moveHistory.size)
        assertEquals(simulatedState.tiles.size, restoredSaved.tiles.size)

        // Convert back to GameState
        val restoredGameState = restoredSaved.toGameState()
        assertNotNull(restoredGameState)
        assertEquals("jade_pillar", restoredGameState.constellation.id)
        assertEquals(42L, restoredGameState.timerSeconds)
        assertEquals(1, restoredGameState.movesCount)
        assertEquals(34, restoredGameState.remainingTilesCount)
        assertFalse(restoredGameState.isWon)

        // Verify that undo still works seamlessly on the restored state!
        val stateAfterUndo = MahjongGameEngine.undo(restoredGameState)
        assertEquals(0, stateAfterUndo.movesCount)
        assertEquals(0, stateAfterUndo.moveHistory.size)
        assertEquals(36, stateAfterUndo.remainingTilesCount)
    }

    @Test
    fun testSavedGameStateHandlesNullLevelIndex() {
        val constellation = ConstellationRepository.getById("classic_turtle")
        val initialState = MahjongGameEngine.startNewGame(constellation)

        val saved = SavedGameState.fromGameState(initialState, levelIndex = null)
        val raw = saved.serialize()

        val restoredSaved = SavedGameState.deserialize(raw)
        assertNotNull(restoredSaved)
        assertEquals("classic_turtle", restoredSaved!!.constellationId)
        assertEquals(null, restoredSaved.levelIndex)
        assertEquals(144, restoredSaved.tiles.size)

        val restoredGameState = restoredSaved.toGameState()
        assertNotNull(restoredGameState)
        assertEquals(144, restoredGameState.tiles.size)
        assertEquals(144, restoredGameState.remainingTilesCount)
    }

    @Test
    fun testPlaylistCyclingLogic() {
        val playlistSize = 3
        var currentIndex = 1 // Start at middle song
        val sequence = mutableListOf<Int>()

        // Simulate 7 song completions
        repeat(7) {
            currentIndex = (currentIndex + 1) % playlistSize
            sequence.add(currentIndex)
        }

        // Expected sequence starting from 1: 2, 0, 1, 2, 0, 1, 2
        assertEquals(listOf(2, 0, 1, 2, 0, 1, 2), sequence)
    }

    @Test
    fun testTableSurfaceSettings() {
        val defaultSettings = GameSettings()
        assertEquals(TableSurface.GREEN_MAT, defaultSettings.tableSurface)
        assertEquals(TableSurface.GREEN_MAT, defaultSettings.theme)

        val walnutSettings = defaultSettings.copy(tableSurface = TableSurface.WALNUT)
        assertEquals(TableSurface.WALNUT, walnutSettings.tableSurface)
        assertEquals("Walnut", TableSurface.WALNUT.displayName)
    }

    @Test
    fun testTabletLevelConstellationsConsistOfHorizontalLevels() {
        val tabletLevels = ConstellationRepository.getLevelConstellations(isTablet = true)
        val phoneLevels = ConstellationRepository.getLevelConstellations(isTablet = false)

        assertEquals(12, tabletLevels.size)
        assertEquals(12, phoneLevels.size)

        for (constellation in tabletLevels) {
            assertFalse(
                "Tablet level constellation ${constellation.name} must not be portrait-optimized",
                constellation.isPortraitOptimized
            )
        }

        for (constellation in phoneLevels) {
            assertTrue(
                "Phone level constellation ${constellation.name} must be portrait-optimized",
                constellation.isPortraitOptimized
            )
        }
    }

    @Test
    fun testCornerIndexOffByDefault() {
        val defaultSettings = GameSettings()
        assertFalse(defaultSettings.showCornerIndex)
    }

    @Test
    fun testCheckTileSupportAcrossAllConstellations() {
        val problems = mutableListOf<String>()
        for (constellation in ConstellationRepository.constellations) {
            val positions = constellation.positions
            for (p in positions) {
                if (p.z > 0) {
                    val supporting = positions.filter { it.z == p.z - 1 && BoardSolvabilityGenerator.isOverlap2D(p.x, p.y, it.x, it.y) }
                    if (supporting.isEmpty()) {
                        problems.add("${constellation.name} (${constellation.id}) tile $p has ZERO supporting tiles at layer ${p.z - 1}")
                        continue
                    }
                    // Test 10x10 grid across tile footprint (offset by eps to stay inside boundaries)
                    val steps = 10
                    val eps = 0.05f
                    var unvisitedPoints = 0
                    for (ix in 0 until steps) {
                        for (iy in 0 until steps) {
                            val sx = p.x + eps + ix * ((2f - 2 * eps) / (steps - 1))
                            val sy = p.y + eps + iy * ((2f - 2 * eps) / (steps - 1))
                            val covered = supporting.any { b ->
                                sx >= b.x && sx <= b.x + 2f && sy >= b.y && sy <= b.y + 2f
                            }
                            if (!covered) unvisitedPoints++
                        }
                    }
                    if (unvisitedPoints > 0) {
                        problems.add("${constellation.name} (${constellation.id}) tile $p has $unvisitedPoints/100 points unsupported by layer ${p.z - 1}")
                    }
                }
            }
        }
        if (problems.isNotEmpty()) {
            throw AssertionError("Tile support issues:\n" + problems.joinToString("\n"))
        }
    }
}
