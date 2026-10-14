package com.scrollbattle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun HomeScreen(
    state: ScrollBattleState,
    onNavigateToBattle: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToDetails: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToReadiness: () -> Unit,
    onNavigateToPermissions: () -> Unit = {}
) {
    Scaffold(
        containerColor = CanvasBackground,
        bottomBar = {
            ScrollBattleBottomNavigation(
                currentDestination = "You",
                onNavigate = { dest ->
                    when (dest) {
                        "Battle" -> onNavigateToBattle()
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
            }

            // Warning banner if tracking permissions are missing
            if (!state.accessibilityGranted || !state.overlayPermissionGranted) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(androidx.compose.ui.graphics.Color(0xFF451A03))
                            .border(1.dp, androidx.compose.ui.graphics.Color(0xFFF59E0B), RoundedCornerShape(16.dp))
                            .clickable { onNavigateToPermissions() }
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(text = "⚠️", fontSize = androidx.compose.ui.unit.TextUnit(22f, androidx.compose.ui.unit.TextUnitType.Sp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Reel Tracking is Inactive",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = androidx.compose.ui.graphics.Color(0xFFFDE68A)
                                )
                                Text(
                                    text = "Tap here to enable Accessibility & Overlay permissions so Scroll Battle can detect Reels and show your live counter.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = androidx.compose.ui.graphics.Color(0xFFF3F4F6)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = androidx.compose.ui.graphics.Color(0xFFFDE68A)
                            )
                        }
                    }
                }
            }

            item {
                // Top Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = PrimaryOrange,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Scroll Battle",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(RecoveryGreen)
                                )
                            }
                            Text(
                                text = "Your day, your control.",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = onNavigateToSettings,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceElevated)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Settings",
                            tint = TextSecondary
                        )
                    }
                }
            }

            // Hero Progress Card: Daily Reels Allocation
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "TODAY'S REELS",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary
                                )
                                Text(text = "•", color = TextTertiary)
                                Text(
                                    text = "Limit: ${state.totalAllowance}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }
                            StatusBadge(
                                text = "${state.percentageUsed}% USED",
                                dotColor = if (state.percentageUsed >= 80) AccentGold else RecoveryGreen
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "${state.todayReels}",
                                        style = MaterialTheme.typography.displayMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = " / ${state.totalAllowance}",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = null,
                                        tint = PrimaryOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${state.remainingReels} Reels remaining",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryOrange
                                    )
                                }
                                Text(
                                    text = "Reset schedule at 04:00 AM",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextTertiary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            ReelProgressGauge(
                                percentage = state.percentageUsed,
                                size = 100.dp,
                                strokeWidth = 8.dp
                            )
                        }

                        // Contextual Discipline Status Banner
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(CanvasBackground)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (state.isLimitExceeded) CriticalRed else RecoveryGreen)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (state.isLimitExceeded) "Daily limit reached" else "Within today's limit",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (state.isLimitExceeded) CriticalRed else RecoveryGreen,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            val paceText = when {
                                state.percentageUsed >= 100 -> "Pace: Limit Exceeded"
                                state.percentageUsed >= 80 -> "Pace: High Screen-Time"
                                state.percentageUsed >= 50 -> "Pace: Moderate"
                                else -> "Pace: Mindful"
                            }
                            Text(
                                text = paceText,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Telemetry Row: 2-Column Metrics
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Time scrolling",
                        value = "${state.todayDurationMinutes / 60}h ${state.todayDurationMinutes % 60}m",
                        subtitle = if (state.todayDurationMinutes > 0) "Estimated reel watch time" else "0m recorded today",
                        icon = Icons.Default.Schedule,
                        iconTint = PrimaryOrange,
                        modifier = Modifier.weight(1f)
                    )
                    val avgMins = if (state.todaySessionsCount > 0) state.todayDurationMinutes / state.todaySessionsCount else 0
                    MetricCard(
                        title = "Scroll sessions",
                        value = "${state.todaySessionsCount}",
                        subtitle = if (state.todaySessionsCount > 0) "Avg. ${avgMins}m / session" else "No active sessions",
                        icon = Icons.Default.FlipToFront,
                        iconTint = AccentGold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 1-Tap Home Screen Widget Card
            item {
                val context = androidx.compose.ui.platform.LocalContext.current

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                        .clickable {
                            val success = com.scrollbattle.widget.BrainPalWidgetProvider.requestPinWidget(context)
                            val msg = if (success) "Widget pin requested!" else "Long press home screen to add widget"
                            android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show()
                        }
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(PrimaryOrange.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Widgets,
                                contentDescription = null,
                                tint = PrimaryOrange,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Home Screen Widget",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Tap to pin live Reel Buddy to home screen",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = "Pin Widget",
                        tint = PrimaryOrange,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Tracking Status Quick Glance
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                        .clickable(onClick = onNavigateToReadiness)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(RecoveryGreen.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = RecoveryGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Tracking Status: ${state.trackingStatus.label}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "On-device detection active",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Session Friction History Header & Action
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Sessions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    TextButton(onClick = onNavigateToDetails) {
                        Text(
                            text = "View details",
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryOrange
                        )
                    }
                }
            }

            items(state.recentSessions.take(3)) { session ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceSecondary)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
                        .clickable(onClick = onNavigateToDetails)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = PrimaryOrange,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "${session.reelCount} Reels • ${session.durationMinutes}m",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${session.timeLabel} • ${session.periodTag}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                    StatusBadge(
                        text = session.paceTag,
                        dotColor = AccentGold
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
