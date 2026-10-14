package com.scrollbattle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun ProgressScreen(
    state: ScrollBattleState,
    onNavigateToHome: () -> Unit,
    onNavigateToBattle: () -> Unit,
    onSelectDay: (Int) -> Unit,
    onSetTimeSpan: (String) -> Unit,
    onNavigateToDetails: () -> Unit
) {
    val selectedDay = state.weeklyDays.getOrElse(state.selectedAnalyticsDayIndex) { state.weeklyDays.last() }

    Scaffold(
        containerColor = CanvasBackground,
        bottomBar = {
            ScrollBattleBottomNavigation(
                currentDestination = "Progress",
                onNavigate = { dest ->
                    when (dest) {
                        "You" -> onNavigateToHome()
                        "Battle" -> onNavigateToBattle()
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
                    title = "Progress & Analytics",
                    subtitle = "Telemetry trends & habits"
                )
            }

            // Day / Week Filter Selector
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceSecondary)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Day", "Week").forEach { span ->
                        val isSelected = state.analyticsTimeSpan == span
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PrimaryOrange else CanvasBackground)
                                .clickable { onSetTimeSpan(span) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = span,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) CanvasBackground else TextSecondary
                            )
                        }
                    }
                }
            }

            // 7-Day Bar Chart Module
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Weekly Consumption",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            StatusBadge(
                                text = "Avg 41 Reels/day",
                                dotColor = RecoveryGreen
                            )
                        }

                        AnalyticsBarChart(
                            days = state.weeklyDays,
                            selectedIndex = state.selectedAnalyticsDayIndex,
                            onSelectDay = onSelectDay
                        )
                    }
                }
            }

            // Selected Day Telemetry Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = selectedDay.dayName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Goal: ${selectedDay.allowance} Reels • Status: ${selectedDay.status}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                            StatusBadge(
                                text = "${selectedDay.reelsCount} Reels",
                                dotColor = if (selectedDay.reelsCount > selectedDay.allowance) CriticalRed else RecoveryGreen
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricCard(
                                title = "Duration",
                                value = "${selectedDay.durationMinutes}m",
                                subtitle = "Screen time",
                                icon = Icons.Default.Schedule,
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                title = "Sessions",
                                value = "${selectedDay.sessionCount}",
                                subtitle = "App opens",
                                icon = Icons.Default.FlipToFront,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        SecondaryActionButton(
                            text = "View Day Sessions Breakdown",
                            trailingIcon = Icons.Default.ArrowForward,
                            onClick = onNavigateToDetails
                        )
                    }
                }
            }
        }
    }
}
