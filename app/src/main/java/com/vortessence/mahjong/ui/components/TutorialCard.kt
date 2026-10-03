package com.vortessence.mahjong.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vortessence.mahjong.data.model.GameSettings
import com.vortessence.mahjong.engine.TutorialAction
import com.vortessence.mahjong.engine.TutorialScript
import com.vortessence.mahjong.engine.TutorialStep

/**
 * Instruction panel shown below the board while the guided tutorial runs.
 */
@Composable
fun TutorialCard(
    step: TutorialStep,
    settings: GameSettings,
    onContinue: () -> Unit,
    onSettingsChanged: (GameSettings) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        AnimatedContent(targetState = step, label = "TutorialStep") { current ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Step ${TutorialScript.steps.indexOf(current) + 1} of ${TutorialScript.steps.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = current.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = current.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (current.showSettingsToggles) {
                    Spacer(modifier = Modifier.height(12.dp))
                    SettingToggle(
                        title = "Highlight Selectable Tiles",
                        checked = settings.highlightFreeTiles,
                        onCheckedChange = { onSettingsChanged(settings.copy(highlightFreeTiles = it)) }
                    )
                    SettingToggle(
                        title = "Draw Corner Index",
                        checked = settings.showCornerIndex,
                        onCheckedChange = { onSettingsChanged(settings.copy(showCornerIndex = it)) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (current.action == TutorialAction.CONTINUE) {
                    Button(
                        onClick = onContinue,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(current.continueLabel, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(
                        text = if (current.action == TutorialAction.TAP_BLOCKED) {
                            "Tap the blue tile"
                        } else {
                            "Match the blue tiles"
                        },
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingToggle(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
