package com.scrollbattle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.scrollbattle.domain.model.ScrollBattleState
import com.scrollbattle.theme.*
import com.scrollbattle.ui.components.*

@Composable
fun BattleScreen(
    state: ScrollBattleState,
    onNavigateToHome: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onStartChallenge: () -> Unit,
    onNavigateToLimitReached: () -> Unit,
    onSetDailyLimit: (Int) -> Unit = {},
    onSimulateReel: () -> Unit = {}
) {
    Scaffold(
        containerColor = CanvasBackground,
        bottomBar = {
            ScrollBattleBottomNavigation(
                currentDestination = "Battle",
                onNavigate = { dest ->
                    when (dest) {
                        "You" -> onNavigateToHome()
                        "Progress" -> onNavigateToProgress()
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.statusBarsPadding())
                ScrollBattleTopBar(
                    title = "Daily Battle",
                    subtitle = "Habit threshold control"
                )
            }

            // Battle Status Hero
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "THRESHOLD STATUS",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                            StatusBadge(
                                text = if (state.isLimitExceeded) "EXHAUSTED" else "ACTIVE",
                                dotColor = if (state.isLimitExceeded) CriticalRed else RecoveryGreen
                            )
                        }

                        Text(
                            text = if (state.isLimitExceeded) "Your daily allowance is reached." else "Holding the 50-Reel line.",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = if (state.isLimitExceeded)
                                "You have consumed all ${state.totalAllowance} Reels allocated for today. Complete the intentional maths challenge to unlock an emergency +20 Reels buffer."
                            else
                                "You have consumed ${state.todayReels} of your ${state.totalAllowance} daily Reels. Stay mindful during high-friction scrolling hours.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        MilestoneTrack(
                            currentCount = state.todayReels,
                            maxLimit = state.totalAllowance
                        )
                    }
                }
            }

            // Milestone Breakdown Cards
            item {
                Text(
                    text = "Daily Milestones",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    MilestoneCard(
                        number = 25,
                        label = "Halfway Checkpoint",
                        description = "Gentle awareness notice; 25 Reels remaining.",
                        achieved = state.todayReels >= 25
                    )
                    MilestoneCard(
                        number = 40,
                        label = "Warning Boundary",
                        description = "Elevated friction reminder; 10 Reels remaining.",
                        achieved = state.todayReels >= 40
                    )
                    MilestoneCard(
                        number = 45,
                        label = "Critical Alert",
                        description = "Final warning before complete feed interruption.",
                        achieved = state.todayReels >= 45
                    )
                    MilestoneCard(
                        number = 50,
                        label = "Daily Hard Limit",
                        description = "Feed locked. Maths challenge required to unlock +20.",
                        achieved = state.todayReels >= 50
                    )
                }
            }

            // Testing & Simulation Presets
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "QUICK TEST LAB",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryOrange
                            )
                            StatusBadge(text = "Dev Preset", dotColor = AccentGold)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceSecondary)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Strict Boundary: 50 Reels/day. At 50 Reels, Instagram is immediately locked and force-closed.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SecondaryActionButton(
                                text = "+ Simulate Swipe",
                                leadingIcon = Icons.Default.Add,
                                onClick = onSimulateReel,
                                modifier = Modifier.weight(1f)
                            )
                            val context = androidx.compose.ui.platform.LocalContext.current
                            SecondaryActionButton(
                                text = "Test Pill Overlay",
                                leadingIcon = Icons.Default.Visibility,
                                onClick = {
                                    val intent = android.content.Intent("com.scrollbattle.ACTION_SHOW_PILL")
                                    context.sendBroadcast(intent)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Action section
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    if (state.isLimitExceeded) {
                        PrimaryActionButton(
                            text = if (state.bonusClaimedToday) "Bonus allowance claimed (+20)" else "Start Maths Challenge (+20 Reels)",
                            leadingIcon = Icons.Default.Calculate,
                            enabled = !state.bonusClaimedToday,
                            onClick = onStartChallenge
                        )
                        SecondaryActionButton(
                            text = "View Limit Reached Screen",
                            onClick = onNavigateToLimitReached
                        )
                    } else {
                        SecondaryActionButton(
                            text = "Practice Maths Challenge",
                            leadingIcon = Icons.Default.Calculate,
                            onClick = onStartChallenge
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MilestoneCard(
    number: Int,
    label: String,
    description: String,
    achieved: Boolean
) {
    val borderColor = if (achieved) AccentGold else BorderSubtle
    val iconColor = if (achieved) AccentGold else TextTertiary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceSecondary)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (achieved) AccentGold.copy(alpha = 0.15f) else SurfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$number",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = iconColor
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (achieved) TextPrimary else TextSecondary
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
        if (achieved) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = AccentGold,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
