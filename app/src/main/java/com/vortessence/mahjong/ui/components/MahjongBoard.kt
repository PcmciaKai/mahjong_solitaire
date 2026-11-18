package com.vortessence.mahjong.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.vortessence.mahjong.R
import com.vortessence.mahjong.data.model.BoardTile
import com.vortessence.mahjong.data.model.GameSettings
import com.vortessence.mahjong.data.model.GameState
import com.vortessence.mahjong.data.model.Tile
import com.vortessence.mahjong.data.model.TileSuit
import com.vortessence.mahjong.engine.BoardSolvabilityGenerator
import com.vortessence.mahjong.ui.components.tiles.TileLook
import com.vortessence.mahjong.ui.components.tiles.rememberTileLook
import com.vortessence.mahjong.ui.theme.GoldHighlight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

/**
 * Painter's Algorithm order for tiles:
 * 1. Layer Z ascending (bottom layer first, apex layer last)
 * 2. X + Y ascending: a tile's depth edge extends right and down, so any tile overlapping it from
 *    the east or south must be drawn later. Tiles are 2x2 grid units, so such a neighbor always has
 *    a strictly larger X + Y, even on half-offset rows/columns where plain X-then-Y ordering fails.
 * 3. X ascending as a tie-breaker for diagonal neighbors that only touch at a corner
 */
private val TileDrawOrder: Comparator<BoardTile> =
    compareBy<BoardTile> { it.z }
        .thenBy { it.x + it.y }
        .thenBy { it.x }

/** Limits for the player's own pinch zoom, on top of the automatic board scaling. */
private const val MIN_USER_ZOOM = 1f
private const val MAX_USER_ZOOM = 4f

/** Space between the board and the edges of its area. */
private val BOARD_MARGIN = 28.dp

/**
 * Automatic zoom as tiles are cleared: at most this much closer than the full board, and never
 * so close that fewer than this many tiles fit across the shorter side of the screen.
 */
private const val AUTO_ZOOM_MAX_FACTOR = 1.5f
private const val AUTO_ZOOM_MIN_TILES_ACROSS = 8f

/**
 * The match animation: tiles rise, rush together and collide, then vanish. The sound and haptic
 * are timed to the collision, so they follow these values.
 */
private const val MATCH_ANIMATION_MS = 600
private const val MATCH_LIFT_END = 0.25f
private const val MATCH_IMPACT = 0.70f

/** Starts the sound this much before the collision, to make up for audio output latency. */
private const val MATCH_SOUND_LEAD_MS = 15L

/** Ignores the system animator duration scale; the in-app animationsEnabled setting is the switch. */
private object UnscaledMotion : MotionDurationScale {
    override val scaleFactor: Float = 1f
}

private class SmashAnimationItem(
    val id: String,
    val tile1: BoardTile,
    val tile2: BoardTile,
    val animatable: Animatable<Float, AnimationVector1D>,
)

private data class SmashFrame(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
    val scale: Float,
    val alpha: Float,
    val impactFraction: Float
)

@Composable
fun MahjongBoard(
    gameState: GameState,
    modifier: Modifier = Modifier,
    settings: GameSettings = GameSettings(),
    highlightedTileIds: Set<Int> = emptySet(),
    onTileClick: (Int) -> Unit,
    onTileMatch: () -> Unit = {},
) {
    val density = LocalDensity.current
    val paddingPx = with(density) { BOARD_MARGIN.toPx() }

    // Hint pulse animation
    val hintPulse = remember { Animatable(0f) }
    LaunchedEffect(gameState.hintPair) {
        if (gameState.hintPair != null) {
            hintPulse.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            hintPulse.snapTo(0f)
        }
    }

    var boardSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            // Zoomed-in tiles must not spill over the header or other surrounding UI
            .clipToBounds()
            .onSizeChanged { boardSize = it }
    ) {
        val canvasW = boardSize.width.toFloat()
        val canvasH = boardSize.height.toFloat()

        if ((canvasW > 0f) && (canvasH > 0f) && gameState.tiles.isNotEmpty()) {
            key(gameState.constellation.id) {
                BoardContent(
                    gameState = gameState,
                    settings = settings,
                    highlightedTileIds = highlightedTileIds,
                    onTileClick = onTileClick,
                    onTileMatch = onTileMatch,
                    canvasW = canvasW,
                    canvasH = canvasH,
                    paddingPx = paddingPx,
                    density = density,
                    // The view model clears this again once the flash is over
                    blockedFlashId = gameState.blockedTileId,
                    hintPulse = { hintPulse.value }
                )
            }
        }
    }
}

@Composable
private fun BoardContent(
    gameState: GameState,
    settings: GameSettings,
    highlightedTileIds: Set<Int>,
    onTileClick: (Int) -> Unit,
    onTileMatch: () -> Unit = {},
    canvasW: Float,
    canvasH: Float,
    paddingPx: Float,
    density: Density,
    blockedFlashId: Int?,
    hintPulse: () -> Float
) {
    val context = LocalContext.current

    // Base transform: unzoomed full layout bounds
    val baseTransform = remember(gameState.tiles.size, canvasW, canvasH, paddingPx) {
        computeBoardTransform(
            tiles = gameState.tiles,
            availableWidth = canvasW,
            availableHeight = canvasH,
            paddingPx = paddingPx
        )
    }

    // Target transform: shrinks/zooms in on remaining active tiles when autoZoomEnabled is true
    val targetTransform = remember(
        gameState.tiles,
        canvasW,
        canvasH,
        paddingPx,
        settings.autoZoomEnabled,
        baseTransform
    ) {
        if (settings.autoZoomEnabled) {
            val activeTiles = gameState.tiles.filter { !it.isRemoved }
            if (activeTiles.isNotEmpty()) {
                computeBoardTransform(
                    tiles = activeTiles,
                    availableWidth = canvasW,
                    availableHeight = canvasH,
                    paddingPx = paddingPx,
                    maxZoomFactor = AUTO_ZOOM_MAX_FACTOR,
                    baseUnitX = baseTransform.unitX,
                    // Never larger than about AUTO_ZOOM_MIN_TILES_ACROSS tiles across the short side
                    // of the screen, unless the full board already shows them bigger
                    maxUnitX = max(
                        baseTransform.unitX,
                        min(canvasW, canvasH) / (2f * AUTO_ZOOM_MIN_TILES_ACROSS)
                    )
                )
            } else {
                baseTransform
            }
        } else {
            baseTransform
        }
    }

    // While the player is zoomed in, the automatic camera stays where it was so the board doesn't
    // shift under their fingers. Once they zoom back out, it catches up with the remaining tiles.
    var frozenCamera by remember { mutableStateOf<BoardTransform?>(null) }
    val cameraTarget = frozenCamera ?: targetTransform

    val zoomAnimSpec = if (settings.animationsEnabled) {
        tween<Float>(durationMillis = 500, easing = FastOutSlowInEasing)
    } else {
        snap()
    }

    val animatedUnitX by animateFloatAsState(
        targetValue = cameraTarget.unitX,
        animationSpec = zoomAnimSpec,
        label = "boardUnitX"
    )
    val animatedStartX by animateFloatAsState(
        targetValue = cameraTarget.startX,
        animationSpec = zoomAnimSpec,
        label = "boardStartX"
    )
    val animatedStartY by animateFloatAsState(
        targetValue = cameraTarget.startY,
        animationSpec = zoomAnimSpec,
        label = "boardStartY"
    )

    val currentTileW = animatedUnitX * 2f
    val currentTileH = currentTileW * BoardTransform.ASPECT_RATIO
    val currentShiftX = currentTileW * BoardTransform.LAYER_SHIFT_FACTOR_X
    val currentShiftY = currentTileH * BoardTransform.LAYER_SHIFT_FACTOR_Y

    // Pinch-to-zoom and panning by the player. The board leaves the composition whenever a new
    // board is dealt, which resets the zoom.
    var userScale by remember { mutableFloatStateOf(MIN_USER_ZOOM) }
    var userOffset by remember { mutableStateOf(Offset.Zero) }
    val canvasCenter = Offset(canvasW / 2f, canvasH / 2f)

    // The automatic camera alone, before the player's own zoom is applied
    val cameraTransform = BoardTransform(
        unitX = animatedUnitX,
        tileWidth = currentTileW,
        tileHeight = currentTileH,
        startX = animatedStartX,
        startY = animatedStartY,
        layerShiftX = currentShiftX,
        layerShiftY = currentShiftY,
        depthX = currentShiftX,
        depthY = currentShiftY
    )
    val currentTransform = cameraTransform.zoomed(scale = userScale, offset = userOffset, pivot = canvasCenter)
    val latestCameraTransform by rememberUpdatedState(cameraTransform)

    // Lamp lighting and the tile set's look
    val lighting = settings.lampLightingEnabled
    val tileLook = rememberTileLook(
        set = settings.tileSet,
        colorway = settings.tileColorway,
        worn = settings.wornTilesEnabled,
    )
    val boardLighting = remember(canvasW, canvasH) { BoardLighting(canvasW, canvasH) }

    // Matching tiles smash animations
    var activeSmashes by remember { mutableStateOf<List<SmashAnimationItem>>(emptyList()) }
    var prevMoveCount by remember { mutableIntStateOf(gameState.moveHistory.size) }

    LaunchedEffect(gameState.moveHistory.size, settings.animationsEnabled) {
        val currentCount = gameState.moveHistory.size
        if (currentCount > prevMoveCount && settings.animationsEnabled) {
            val latestMove = gameState.moveHistory.last()
            val t1 = gameState.tiles.firstOrNull { it.id == latestMove.tileId1 }
            val t2 = gameState.tiles.firstOrNull { it.id == latestMove.tileId2 }
            if (t1 != null && t2 != null) {
                val animId = "${t1.id}_${t2.id}_${System.currentTimeMillis()}"
                val smashItem = SmashAnimationItem(
                    id = animId,
                    tile1 = t1,
                    tile2 = t2,
                    animatable = Animatable(0f)
                )
                activeSmashes += smashItem
                launch {
                    delay(((MATCH_ANIMATION_MS * MATCH_IMPACT).toLong() - MATCH_SOUND_LEAD_MS).milliseconds)
                    onTileMatch()
                }
                launch {
                    // Pinned to 1x so the collision stays in sync with the delayed sound/haptic
                    withContext(UnscaledMotion) {
                        smashItem.animatable.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = MATCH_ANIMATION_MS, easing = LinearEasing)
                        )
                    }
                    activeSmashes = activeSmashes.filter { it.id != animId }
                }
            }
        } else if (currentCount < prevMoveCount) {
            // Undo occurred: cancel in-flight animations so restored tiles display normally
            activeSmashes = emptyList()
        }
        prevMoveCount = currentCount
    }

    // Read the latest values inside the gesture detector so it is not restarted on every
    // zoom animation frame (which would drop in-flight taps)
    val latestTiles by rememberUpdatedState(gameState.tiles)
    val latestTransform by rememberUpdatedState(currentTransform)
    val latestOnTileClick by rememberUpdatedState(onTileClick)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(canvasW, canvasH) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    val oldScale = userScale
                    val newScale = (oldScale * zoom).coerceIn(MIN_USER_ZOOM, MAX_USER_ZOOM)
                    // Keep the board point under the fingers in place while zooming
                    val newOffset = centroid + pan - canvasCenter -
                        (centroid - canvasCenter - userOffset) * (newScale / oldScale)
                    // Never pan further than the zoomed-in board reaches
                    val maxX = canvasW * (newScale - 1f) / 2f
                    val maxY = canvasH * (newScale - 1f) / 2f
                    if (newScale > MIN_USER_ZOOM) {
                        // Stop the camera where it is right now, even mid-animation
                        if (frozenCamera == null) frozenCamera = latestCameraTransform
                    } else {
                        frozenCamera = null
                    }
                    userScale = newScale
                    userOffset = Offset(
                        newOffset.x.coerceIn(-maxX, maxX),
                        newOffset.y.coerceIn(-maxY, maxY)
                    )
                }
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val tappedId = findTappedTileId(offset.x, offset.y, latestTiles, latestTransform)
                    if (tappedId != null) {
                        latestOnTileClick(tappedId)
                    }
                }
            }
    ) {
        // The table has its own canvas, which only reads the surface setting: it is recorded once
        // and then replayed, instead of being redrawn on every animation frame of the tiles.
        //
        // It moves and zooms together with the board, through its layer's transform, as if the
        // camera came closer to the mat: when edge tiles are cleared and the board zooms in, the
        // mat's edges slide out of view with them.
        val tableSurface = settings.tableSurface
        val tableScale = currentTransform.unitX / baseTransform.unitX
        // Where the base layout's origin ends up: tiles map from base to current as
        // current = base * scale + offset
        val tableOffset = Offset(
            currentTransform.startX - (baseTransform.startX * tableScale),
            currentTransform.startY - (baseTransform.startY * tableScale)
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Never zoom out past the full mat, and never pan past its edges, so no empty
                    // space shows around it (near the edges the mat moves a little less than the tiles)
                    val scale = tableScale.coerceAtLeast(1f)
                    transformOrigin = TransformOrigin(0f, 0f)
                    scaleX = scale
                    scaleY = scale
                    translationX = tableOffset.x.coerceIn(canvasW * (1f - scale), 0f)
                    translationY = tableOffset.y.coerceIn(canvasH * (1f - scale), 0f)
                }
        ) {
            TableSurfaceRenderer.drawTableSurface(
                scope = this,
                surface = tableSurface,
                width = size.width,
                height = size.height,
                context = context,
            )
        }

        val boardDescription = stringResource(R.string.board_description)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = boardDescription }
        ) {
            val sortedTiles = gameState.tiles
                .filter { !it.isRemoved }
                .sortedWith(TileDrawOrder)

            val tileW = currentTransform.tileWidth
            val tileH = currentTransform.tileHeight
            val selectedTile = sortedTiles.firstOrNull { it.id == gameState.selectedTileId }
            val hinted = gameState.hintPair
            val pulse = if (hinted != null) 0.55f + (0.45f * hintPulse()) else 1f

            for (tile in sortedTiles) {
                // The selected tile is picked up off the board, so it is drawn last, on top
                if (tile === selectedTile) continue
                val x = currentTransform.tilePixelX(tile.x, tile.z)
                val y = currentTransform.tilePixelY(tile.y, tile.z)
                TileRenderer.drawTile(
                    scope = this,
                    boardTile = tile,
                    x = x,
                    y = y,
                    width = tileW,
                    height = tileH,
                    showCornerIndex = settings.showCornerIndex,
                    look = tileLook,
                    layerShading = lighting,
                    dimmed = settings.highlightFreeTiles && !tile.isFree
                )
                // Drawn right after its tile, so tiles in front still cover it like the tile itself
                val highlight = when {
                    tile.id == blockedFlashId -> TileRenderer.Highlight.BLOCKED
                    (hinted != null) && ((tile.id == hinted.first) || (tile.id == hinted.second)) ->
                        TileRenderer.Highlight.HINT
                    tile.id in highlightedTileIds -> TileRenderer.Highlight.TUTORIAL
                    else -> null
                }
                if (highlight != null) {
                    TileRenderer.drawHighlight(
                        scope = this,
                        x = x,
                        y = y,
                        width = tileW,
                        height = tileH,
                        highlight = highlight,
                        intensity = if (highlight == TileRenderer.Highlight.HINT) pulse else 1f
                    )
                }
            }

            if (selectedTile != null) {
                val lift = TileRenderer.selectionLift(tileW)
                val x = currentTransform.tilePixelX(selectedTile.x, selectedTile.z)
                val y = currentTransform.tilePixelY(selectedTile.y, selectedTile.z)
                TileRenderer.drawTile(
                    scope = this,
                    boardTile = selectedTile,
                    x = x,
                    y = y,
                    width = tileW,
                    height = tileH,
                    showCornerIndex = settings.showCornerIndex,
                    look = tileLook,
                    layerShading = lighting,
                    lift = lift
                )
                TileRenderer.drawHighlight(
                    scope = this,
                    x = x,
                    y = y,
                    width = tileW,
                    height = tileH,
                    highlight = TileRenderer.Highlight.SELECTED,
                    lift = lift
                )
            }

            // Draw matching pairs being raised, smashed into each other, and disappearing
            for (smash in activeSmashes) {
                drawSmashAnimation(
                    smash = smash,
                    transform = currentTransform,
                    density = density,
                    showCornerIndex = settings.showCornerIndex,
                    look = tileLook
                )
            }

            if (lighting) boardLighting.drawFalloff(this)
        }

        TileAccessibilityNodes(
            gameState = gameState,
            transform = currentTransform,
            density = density,
            onTileClick = onTileClick
        )
    }
}

/**
 * Invisible nodes on top of the canvas so screen readers can find, read and activate the tiles
 * that are not covered by other tiles. They have no pointer input, so normal taps still go to the
 * board's gesture detector.
 */
@Composable
private fun TileAccessibilityNodes(
    gameState: GameState,
    transform: BoardTransform,
    density: Density,
    onTileClick: (Int) -> Unit,
) {
    val uncoveredTiles = remember(gameState.tiles) {
        val activeTiles = gameState.tiles.filter { !it.isRemoved }
        val positions = activeTiles.map { it.position }
        activeTiles
            .filter { !BoardSolvabilityGenerator.isCovered(it.position, positions) }
            .sortedWith(compareBy<BoardTile> { it.y }.thenBy { it.x })
    }
    val nodeWidth = with(density) { (transform.tileWidth + transform.depthX).toDp() }
    val nodeHeight = with(density) { (transform.tileHeight + transform.depthY).toDp() }

    for (tile in uncoveredTiles) {
        key(tile.id) {
            val label = tileLabel(tile.tile)
            val state = stringResource(if (tile.isFree) R.string.tile_free else R.string.tile_blocked, label)
            val isHinted = gameState.hintPair?.let { it.first == tile.id || it.second == tile.id } == true
            val description = if (isHinted) stringResource(R.string.tile_hinted, state) else state
            val isSelected = tile.id == gameState.selectedTileId
            val px = transform.tilePixelX(tile.x, tile.z)
            val py = transform.tilePixelY(tile.y, tile.z)

            Box(
                modifier = Modifier
                    .offset { IntOffset(px.roundToInt(), py.roundToInt()) }
                    .size(nodeWidth, nodeHeight)
                    .semantics {
                        contentDescription = description
                        role = Role.Button
                        selected = isSelected
                        onClick {
                            onTileClick(tile.id)
                            true
                        }
                    }
            )
        }
    }
}

@Composable
private fun tileLabel(tile: Tile): String = when (tile.suit) {
    TileSuit.CHARACTER -> stringResource(R.string.tile_characters, tile.value)
    TileSuit.BAMBOO -> stringResource(R.string.tile_bamboo, tile.value)
    TileSuit.CIRCLE -> stringResource(R.string.tile_circles, tile.value)
    TileSuit.WIND -> stringResource(
        when (tile.value) {
            Tile.WIND_EAST -> R.string.tile_wind_east
            Tile.WIND_SOUTH -> R.string.tile_wind_south
            Tile.WIND_WEST -> R.string.tile_wind_west
            else -> R.string.tile_wind_north
        }
    )
    TileSuit.DRAGON -> stringResource(
        when (tile.value) {
            Tile.DRAGON_RED -> R.string.tile_dragon_red
            Tile.DRAGON_GREEN -> R.string.tile_dragon_green
            else -> R.string.tile_dragon_white
        }
    )
    TileSuit.SEASON -> stringResource(
        when (tile.value) {
            Tile.SEASON_SPRING -> R.string.tile_season_spring
            Tile.SEASON_SUMMER -> R.string.tile_season_summer
            Tile.SEASON_AUTUMN -> R.string.tile_season_autumn
            else -> R.string.tile_season_winter
        }
    )
    TileSuit.FLOWER -> stringResource(
        when (tile.value) {
            Tile.FLOWER_PLUM -> R.string.tile_flower_plum
            Tile.FLOWER_ORCHID -> R.string.tile_flower_orchid
            Tile.FLOWER_CHRYSANTHEMUM -> R.string.tile_flower_chrysanthemum
            else -> R.string.tile_flower_bamboo
        }
    )
}

private fun DrawScope.drawSmashAnimation(
    smash: SmashAnimationItem,
    transform: BoardTransform,
    density: Density,
    showCornerIndex: Boolean,
    look: TileLook
) {
    val p = smash.animatable.value
    val t1 = smash.tile1
    val t2 = smash.tile2

    val start1X = transform.tilePixelX(t1.x, t1.z)
    val start1Y = transform.tilePixelY(t1.y, t1.z)
    val start2X = transform.tilePixelX(t2.x, t2.z)
    val start2Y = transform.tilePixelY(t2.y, t2.z)

    val midX = (start1X + start2X) / 2f
    val midY = (start1Y + start2Y) / 2f

    val maxLift = with(density) { 16.dp.toPx() }

    // Phases:
    // Phase 1 (0..MATCH_LIFT_END): Raise off board
    // Phase 2 (MATCH_LIFT_END..MATCH_IMPACT): Smash into midpoint
    // Phase 3 (MATCH_IMPACT..1): Impact & Disappear
    val liftFraction = (p / MATCH_LIFT_END).coerceIn(0f, 1f)
    val liftY = -maxLift * liftFraction

    val frame = when {
        p < MATCH_LIFT_END -> {
            val s = 1f + 0.18f * liftFraction
            SmashFrame(
                x1 = start1X,
                y1 = start1Y + liftY,
                x2 = start2X,
                y2 = start2Y + liftY,
                scale = s,
                alpha = 1f,
                impactFraction = 0f
            )
        }
        p < MATCH_IMPACT -> {
            val rushRaw = (p - MATCH_LIFT_END) / (MATCH_IMPACT - MATCH_LIFT_END)
            val rushEased = rushRaw * rushRaw
            val s = 1.18f
            SmashFrame(
                x1 = start1X + (midX - start1X) * rushEased,
                y1 = start1Y + (midY - start1Y) * rushEased + liftY,
                x2 = start2X + (midX - start2X) * rushEased,
                y2 = start2Y + (midY - start2Y) * rushEased + liftY,
                scale = s,
                alpha = 1f,
                impactFraction = 0f
            )
        }
        else -> {
            val impact = (p - MATCH_IMPACT) / (1f - MATCH_IMPACT)
            val s = 1.18f * (1f - impact * 0.7f)
            val a = (1f - impact).coerceIn(0f, 1f)
            SmashFrame(
                x1 = midX,
                y1 = midY + liftY,
                x2 = midX,
                y2 = midY + liftY,
                scale = s,
                alpha = a,
                impactFraction = impact
            )
        }
    }

    // 1. Raised shadow cast on table plane during lift and rush
    if (frame.alpha > 0.05f && p < MATCH_IMPACT) {
        val shadowAlpha = 0.35f * liftFraction
        val shadowCorner = CornerRadius(transform.tileWidth * 0.085f)
        val shadowW = transform.tileWidth * frame.scale
        val shadowH = transform.tileHeight * frame.scale
        val shadowOffset = with(density) { (6.dp + 10.dp * liftFraction).toPx() }

        drawRoundRect(
            color = Color.Black.copy(alpha = shadowAlpha),
            topLeft = Offset(frame.x1 + shadowOffset, frame.y1 - liftY + shadowOffset * 0.7f),
            size = Size(shadowW, shadowH),
            cornerRadius = shadowCorner
        )
        drawRoundRect(
            color = Color.Black.copy(alpha = shadowAlpha),
            topLeft = Offset(frame.x2 + shadowOffset, frame.y2 - liftY + shadowOffset * 0.7f),
            size = Size(shadowW, shadowH),
            cornerRadius = shadowCorner
        )
    }

    // 2. Impact shockwave ring at collision midpoint
    if (frame.impactFraction > 0f) {
        val shockRadius = frame.impactFraction * transform.tileWidth * 1.3f
        val shockAlpha = (1f - frame.impactFraction).coerceIn(0f, 1f)
        val center = Offset(
            midX + transform.tileWidth / 2f,
            midY + liftY + transform.tileHeight / 2f
        )
        drawCircle(
            color = GoldHighlight.copy(alpha = shockAlpha * 0.85f),
            radius = shockRadius,
            center = center,
            style = Stroke(width = with(density) { 3.5.dp.toPx() } * shockAlpha)
        )
        drawCircle(
            color = Color.White.copy(alpha = shockAlpha * 0.7f),
            radius = shockRadius * 0.65f,
            center = center,
            style = Stroke(width = with(density) { 2.dp.toPx() } * shockAlpha)
        )
    }

    // 3. Draw colliding raised tiles
    if (frame.alpha > 0.01f) {
        val sorted = listOf(t1 to (frame.x1 to frame.y1), t2 to (frame.x2 to frame.y2))
            .sortedWith(
                compareBy<Pair<BoardTile, Pair<Float, Float>>> { it.first.z }
                    .thenBy { it.second.first }  // X
                    .thenBy { it.second.second } // Y
            )

        for ((tile, pos) in sorted) {
            drawSingleAnimatingTile(
                tile = tile,
                posX = pos.first,
                posY = pos.second,
                scale = frame.scale,
                alpha = frame.alpha,
                transform = transform,
                showCornerIndex = showCornerIndex,
                look = look
            )
        }
    }
}

private fun DrawScope.drawSingleAnimatingTile(
    tile: BoardTile,
    posX: Float,
    posY: Float,
    scale: Float,
    alpha: Float,
    transform: BoardTransform,
    showCornerIndex: Boolean,
    look: TileLook
) {
    val pivot = Offset(posX + transform.tileWidth / 2f, posY + transform.tileHeight / 2f)
    // Matched tiles keep the selection ring while they fly together
    val drawTileAndRing: DrawScope.() -> Unit = {
        TileRenderer.drawTile(
            scope = this,
            boardTile = tile.copy(isRemoved = false, isFree = true),
            x = posX,
            y = posY,
            width = transform.tileWidth,
            height = transform.tileHeight,
            showCornerIndex = showCornerIndex,
            look = look
        )
        TileRenderer.drawHighlight(
            scope = this,
            x = posX,
            y = posY,
            width = transform.tileWidth,
            height = transform.tileHeight,
            highlight = TileRenderer.Highlight.SELECTED
        )
    }
    if (alpha < 0.99f) {
        val bounds = Rect(0f, 0f, size.width, size.height)
        val paint = Paint().apply { this.alpha = alpha }
        drawContext.canvas.saveLayer(bounds, paint)
        try {
            withTransform({ scale(scale, scale, pivot = pivot) }, drawTileAndRing)
        } finally {
            drawContext.canvas.restore()
        }
    } else {
        withTransform({ scale(scale, scale, pivot = pivot) }, drawTileAndRing)
    }
}

/**
 * Geometric transformation calculating exact tile positions and sizes
 * to guarantee that the constellation fits in the viewport.
 */
data class BoardTransform(
    val unitX: Float,
    val tileWidth: Float,
    val tileHeight: Float,
    val startX: Float,
    val startY: Float,
    val layerShiftX: Float,
    val layerShiftY: Float,
    val depthX: Float,
    val depthY: Float
) {
    fun tilePixelX(gridX: Float, layerZ: Int): Float {
        return startX + gridX * unitX - layerZ * layerShiftX
    }

    fun tilePixelY(gridY: Float, layerZ: Int): Float {
        return startY + gridY * (unitX * ASPECT_RATIO) - layerZ * layerShiftY
    }

    /**
     * This transform scaled by [scale] around [pivot] and then moved by [offset].
     */
    fun zoomed(scale: Float, offset: Offset, pivot: Offset): BoardTransform {
        if (scale == 1f && offset == Offset.Zero) return this
        return copy(
            unitX = unitX * scale,
            tileWidth = tileWidth * scale,
            tileHeight = tileHeight * scale,
            startX = (startX - pivot.x) * scale + pivot.x + offset.x,
            startY = (startY - pivot.y) * scale + pivot.y + offset.y,
            layerShiftX = layerShiftX * scale,
            layerShiftY = layerShiftY * scale,
            depthX = depthX * scale,
            depthY = depthY * scale,
        )
    }

    companion object {
        const val ASPECT_RATIO = 1.333f // Mahjong tile ratio (width : height = 3 : 4)
        const val LAYER_SHIFT_FACTOR_X = 0.08f
        const val LAYER_SHIFT_FACTOR_Y = 0.08f
    }
}

private fun computeBoardTransform(
    tiles: List<BoardTile>,
    availableWidth: Float,
    availableHeight: Float,
    paddingPx: Float,
    maxZoomFactor: Float = 1f,
    baseUnitX: Float = 0f,
    maxUnitX: Float = Float.MAX_VALUE
): BoardTransform {
    val aspectRatio = BoardTransform.ASPECT_RATIO

    // Calculate normalized bounds of the given tiles
    var minNormX = Float.MAX_VALUE
    var maxNormX = -Float.MAX_VALUE
    var minNormY = Float.MAX_VALUE
    var maxNormY = -Float.MAX_VALUE

    for (t in tiles) {
        val normLeft = t.x - t.z * (2f * BoardTransform.LAYER_SHIFT_FACTOR_X)
        val normRight = t.x + 2f - t.z * (2f * BoardTransform.LAYER_SHIFT_FACTOR_X) + (2f * BoardTransform.LAYER_SHIFT_FACTOR_X)
        val normTop = (t.y * aspectRatio) - t.z * (2f * aspectRatio * BoardTransform.LAYER_SHIFT_FACTOR_Y)
        val normBottom = ((t.y + 2f) * aspectRatio) - t.z * (2f * aspectRatio * BoardTransform.LAYER_SHIFT_FACTOR_Y) + (2f * aspectRatio * BoardTransform.LAYER_SHIFT_FACTOR_Y)

        if (normLeft < minNormX) minNormX = normLeft
        if (normRight > maxNormX) maxNormX = normRight
        if (normTop < minNormY) minNormY = normTop
        if (normBottom > maxNormY) maxNormY = normBottom
    }

    if (minNormX == Float.MAX_VALUE) {
        minNormX = 0f
        maxNormX = 10f
        minNormY = 0f
        maxNormY = 10f
    }

    val totalNormW = max(1f, maxNormX - minNormX)
    val totalNormH = max(1f, maxNormY - minNormY)

    val contentW = max(10f, availableWidth - paddingPx * 2f)
    val contentH = max(10f, availableHeight - paddingPx * 2f)

    var unitX = min(contentW / totalNormW, contentH / totalNormH)
    if (baseUnitX > 0f && maxZoomFactor > 1f) {
        unitX = min(unitX, baseUnitX * maxZoomFactor)
    }
    unitX = min(unitX, maxUnitX)

    val tileW = unitX * 2f
    val tileH = tileW * aspectRatio

    val layerShiftX = tileW * BoardTransform.LAYER_SHIFT_FACTOR_X
    val layerShiftY = tileH * BoardTransform.LAYER_SHIFT_FACTOR_Y

    val boardPixelW = totalNormW * unitX
    val boardPixelH = totalNormH * unitX

    val startX = paddingPx + (contentW - boardPixelW) * 0.5f - minNormX * unitX
    val startY = paddingPx + (contentH - boardPixelH) * 0.5f - minNormY * unitX

    return BoardTransform(
        unitX = unitX,
        tileWidth = tileW,
        tileHeight = tileH,
        startX = startX,
        startY = startY,
        layerShiftX = layerShiftX,
        layerShiftY = layerShiftY,
        depthX = layerShiftX,
        depthY = layerShiftY
    )
}

/**
 * Maps screen touch coordinate to top-most active tile.
 */
private fun findTappedTileId(
    touchX: Float,
    touchY: Float,
    tiles: List<BoardTile>,
    transform: BoardTransform
): Int? {
    // Test in reverse draw order (top-most visible tile first)
    val sorted = tiles
        .filter { !it.isRemoved }
        .sortedWith(TileDrawOrder.reversed())

    for (tile in sorted) {
        val px = transform.tilePixelX(tile.x, tile.z)
        val py = transform.tilePixelY(tile.y, tile.z)
        val right = px + transform.tileWidth + transform.depthX
        val bottom = py + transform.tileHeight + transform.depthY

        if (touchX in px..right && touchY in py..bottom) {
            return tile.id
        }
    }
    return null
}
