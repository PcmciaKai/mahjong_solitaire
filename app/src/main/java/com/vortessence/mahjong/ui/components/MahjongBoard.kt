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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.vortessence.mahjong.data.model.BoardTile
import com.vortessence.mahjong.data.model.GameSettings
import com.vortessence.mahjong.data.model.GameState
import com.vortessence.mahjong.ui.theme.GoldHighlight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
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
    val paddingPx = with(density) { 16.dp.toPx() }

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

    // Blocked shake/flash state
    var blockedFlashId by remember { mutableStateOf<Int?>(null) }
    LaunchedEffect(gameState.blockedTileId) {
        val blockedId = gameState.blockedTileId
        if (blockedId != null) {
            blockedFlashId = blockedId
            try {
                delay(350.milliseconds)
            } finally {
                blockedFlashId = null
            }
        } else {
            blockedFlashId = null
        }
    }

    var boardSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
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
                    blockedFlashId = blockedFlashId
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
    blockedFlashId: Int?
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
                    maxZoomFactor = 1.8f,
                    baseUnitX = baseTransform.unitX
                )
            } else {
                baseTransform
            }
        } else {
            baseTransform
        }
    }

    val zoomAnimSpec = if (settings.animationsEnabled) {
        tween<Float>(durationMillis = 500, easing = FastOutSlowInEasing)
    } else {
        snap()
    }

    val animatedUnitX by animateFloatAsState(
        targetValue = targetTransform.unitX,
        animationSpec = zoomAnimSpec,
        label = "boardUnitX"
    )
    val animatedStartX by animateFloatAsState(
        targetValue = targetTransform.startX,
        animationSpec = zoomAnimSpec,
        label = "boardStartX"
    )
    val animatedStartY by animateFloatAsState(
        targetValue = targetTransform.startY,
        animationSpec = zoomAnimSpec,
        label = "boardStartY"
    )

    val currentTileW = animatedUnitX * 2f
    val currentTileH = currentTileW * BoardTransform.ASPECT_RATIO
    val currentShiftX = currentTileW * BoardTransform.LAYER_SHIFT_FACTOR_X
    val currentShiftY = currentTileH * BoardTransform.LAYER_SHIFT_FACTOR_Y

    val currentTransform = BoardTransform(
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
                    delay(280.milliseconds)
                    onTileMatch()
                }
                launch {
                    smashItem.animatable.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 420, easing = LinearEasing)
                    )
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
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val tappedId = findTappedTileId(offset.x, offset.y, latestTiles, latestTransform)
                    if (tappedId != null) {
                        latestOnTileClick(tappedId)
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            TableSurfaceRenderer.drawTableSurface(
                scope = this,
                surface = settings.tableSurface,
                width = canvasW,
                height = canvasH,
                context = context,
            )

            val sortedTiles = gameState.tiles
                .filter { !it.isRemoved }
                .sortedWith(TileDrawOrder)

            for (tile in sortedTiles) {
                val px = currentTransform.tilePixelX(tile.x, tile.z)
                val py = currentTransform.tilePixelY(tile.y, tile.z)
                val isSelected = tile.id == gameState.selectedTileId
                val isHinted = gameState.hintPair?.let { it.first == tile.id || it.second == tile.id } ?: false
                val isBlockedFlash = (blockedFlashId != null) && (tile.id == blockedFlashId)

                TileRenderer.drawTile(
                    scope = this,
                    boardTile = tile,
                    x = px,
                    y = py,
                    width = currentTransform.tileWidth,
                    height = currentTransform.tileHeight,
                    isSelected = isSelected,
                    isHinted = isHinted,
                    isBlockedHighlight = isBlockedFlash,
                    isTutorialHighlight = tile.id in highlightedTileIds,
                    highlightFreeTiles = settings.highlightFreeTiles,
                    showCornerIndex = settings.showCornerIndex
                )
            }

            // Draw matching pairs being raised, smashed into each other, and disappearing
            for (smash in activeSmashes) {
                drawSmashAnimation(
                    smash = smash,
                    transform = currentTransform,
                    density = density,
                    showCornerIndex = settings.showCornerIndex
                )
            }
        }
    }
}

private fun DrawScope.drawSmashAnimation(
    smash: SmashAnimationItem,
    transform: BoardTransform,
    density: Density,
    showCornerIndex: Boolean
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
    // Phase 1 (0f..0.25f): Raise off board
    // Phase 2 (0.25f..0.70f): Smash into midpoint
    // Phase 3 (0.70f..1.0f): Impact & Disappear
    val liftFraction = (p / 0.25f).coerceIn(0f, 1f)
    val liftY = -maxLift * liftFraction

    val frame = when {
        p < 0.25f -> {
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
        p < 0.70f -> {
            val rushRaw = (p - 0.25f) / 0.45f
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
            val impact = (p - 0.70f) / 0.30f
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
    if (frame.alpha > 0.05f && p < 0.70f) {
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
                showCornerIndex = showCornerIndex
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
    showCornerIndex: Boolean
) {
    val pivot = Offset(posX + transform.tileWidth / 2f, posY + transform.tileHeight / 2f)
    if (alpha < 0.99f) {
        val bounds = Rect(0f, 0f, size.width, size.height)
        val paint = Paint().apply { this.alpha = alpha }
        drawContext.canvas.saveLayer(bounds, paint)
        try {
            withTransform({
                scale(scale, scale, pivot = pivot)
            }) {
                TileRenderer.drawTile(
                    scope = this,
                    boardTile = tile.copy(isRemoved = false, isFree = true),
                    x = posX,
                    y = posY,
                    width = transform.tileWidth,
                    height = transform.tileHeight,
                    isSelected = true,
                    isHinted = false,
                    isBlockedHighlight = false,
                    highlightFreeTiles = false,
                    showCornerIndex = showCornerIndex
                )
            }
        } finally {
            drawContext.canvas.restore()
        }
    } else {
        withTransform({
            scale(scale, scale, pivot = pivot)
        }) {
            TileRenderer.drawTile(
                scope = this,
                boardTile = tile.copy(isRemoved = false, isFree = true),
                x = posX,
                y = posY,
                width = transform.tileWidth,
                height = transform.tileHeight,
                isSelected = true,
                isHinted = false,
                isBlockedHighlight = false,
                highlightFreeTiles = false,
                showCornerIndex = showCornerIndex
            )
        }
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
    baseUnitX: Float = 0f
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
