package com.vortessence.mahjong.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vortessence.mahjong.data.model.Constellation
import com.vortessence.mahjong.data.model.GameStats
import com.vortessence.mahjong.data.repository.ConstellationRepository
import com.vortessence.mahjong.ui.components.formatTime
import com.vortessence.mahjong.ui.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectScreen(
    stats: GameStats,
    levels: List<Constellation> = ConstellationRepository.levelConstellations,
    tutorial: Constellation = ConstellationRepository.tutorialConstellation,
    onLevelSelected: (Int) -> Unit,
    onBack: () -> Unit,
) {
    BackHandler {
        onBack()
    }

    val highestUnlocked = stats.highestLevelReached.coerceAtLeast(1)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Levels Progression",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // The tutorial is always unlocked, independent of level progress
            item {
                LevelCard(
                    levelIndex = ConstellationRepository.TUTORIAL_LEVEL,
                    name = tutorial.name,
                    difficultyName = tutorial.difficulty.displayName,
                    stars = tutorial.difficulty.stars,
                    tileCount = tutorial.tileCount,
                    isUnlocked = true,
                    isCompleted = stats.completedConstellations.contains(tutorial.id),
                    bestTime = stats.bestTimes[tutorial.id],
                    onClick = { onLevelSelected(ConstellationRepository.TUTORIAL_LEVEL) }
                )
            }

            itemsIndexed(levels) { index, constellation ->
                val levelIndex = index + 1
                val isUnlocked = levelIndex <= highestUnlocked
                val isCompleted = stats.completedConstellations.contains(constellation.id)
                val bestTime = stats.bestTimes[constellation.id]

                LevelCard(
                    levelIndex = levelIndex,
                    name = constellation.name,
                    difficultyName = constellation.difficulty.displayName,
                    stars = constellation.difficulty.stars,
                    tileCount = constellation.tileCount,
                    isUnlocked = isUnlocked,
                    isCompleted = isCompleted,
                    bestTime = bestTime,
                    onClick = {
                        if (isUnlocked) {
                            onLevelSelected(levelIndex)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun LevelCard(
    levelIndex: Int,
    name: String,
    difficultyName: String,
    stars: Int,
    tileCount: Int,
    isUnlocked: Boolean,
    isCompleted: Boolean,
    bestTime: Long?,
    onClick: () -> Unit
) {
    val containerColor = if (isUnlocked) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 2.dp else 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isUnlocked, onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Level badge / Status circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> MaterialTheme.colorScheme.primaryContainer
                            isUnlocked -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else if (!isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                } else {
                    Text(
                        text = levelIndex.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Level $levelIndex: $name",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(stars) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (isUnlocked) GoldAccent else GoldAccent.copy(alpha = 0.4f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$difficultyName • $tileCount tiles",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (bestTime != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Best: ${formatTime(bestTime)}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
