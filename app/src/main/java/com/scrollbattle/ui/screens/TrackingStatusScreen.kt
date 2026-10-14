package com.scrollbattle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.scrollbattle.domain.model.TrackingStatus
import com.scrollbattle.theme.*
import com.scrollbattle.ui.components.*

@Composable
fun TrackingStatusScreen(
    state: ScrollBattleState,
    onSetTrackingStatus: (TrackingStatus) -> Unit,
    onTestDetection: () -> Unit,
    onGoToDashboard: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = CanvasBackground,
        topBar = {
            ScrollBattleTopBar(
                title = "Tracking Readiness",
                subtitle = "Diagnostic telemetry status",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
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
            // Hero Status Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SYSTEM HEALTH",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                            StatusBadge(
                                text = state.trackingStatus.name,
                                dotColor = when (state.trackingStatus) {
                                    TrackingStatus.READY -> RecoveryGreen
                                    TrackingStatus.PARTIAL_TRACKING -> AccentGold
                                    TrackingStatus.PERMISSION_MISSING -> CriticalRed
                                    else -> TextTertiary
                                }
                            )
                        }

                        Text(
                            text = state.trackingStatus.label,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = when (state.trackingStatus) {
                                TrackingStatus.READY -> "All required subsystems are operational. Background reel detector is standing by for Instagram launches."
                                TrackingStatus.PERMISSION_MISSING -> "App usage access or accessibility permissions are missing. Please complete setup in settings."
                                TrackingStatus.PARTIAL_TRACKING -> "Usage duration is active, but vertical swipe scroll detection is running in estimated mode."
                                TrackingStatus.TESTING -> "Running on-device diagnostic detection test. Simulating reel transition events."
                                TrackingStatus.UNAVAILABLE -> "Instagram tracking service is currently halted by system battery optimizer."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Status Selector Test Chips
            item {
                Text(
                    text = "Simulate Readiness States",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        TrackingStatus.READY to "Ready",
                        TrackingStatus.PERMISSION_MISSING to "Incomplete",
                        TrackingStatus.PARTIAL_TRACKING to "Limited"
                    ).forEach { (status, label) ->
                        val isSelected = state.trackingStatus == status
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) PrimaryOrange else SurfaceElevated)
                                .clickable { onSetTrackingStatus(status) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) CanvasBackground else TextPrimary
                            )
                        }
                    }
                }
            }

            // Subsystem Diagnostic Checks
            item {
                Text(
                    text = "Subsystem Checklist",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DiagnosticCheckRow(
                        title = "Usage Access Authorization",
                        subtitle = "Checks foreground package detection",
                        isPassed = state.usageAccessGranted
                    )
                    DiagnosticCheckRow(
                        title = "Reel Swipe Gesture Hook",
                        subtitle = "Accessibility event-driven detector",
                        isPassed = state.accessibilityGranted
                    )
                    DiagnosticCheckRow(
                        title = "Display Overlay Permission",
                        subtitle = "Floating counter pill & limit interruption card",
                        isPassed = state.overlayPermissionGranted
                    )
                    DiagnosticCheckRow(
                        title = "Battery Optimization Exemption",
                        subtitle = "Allows background daily reset at 04:00 AM",
                        isPassed = state.batteryOptimizationIgnored
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SecondaryActionButton(
                    text = "Run 30s Detection Test",
                    leadingIcon = Icons.Default.PlayCircle,
                    onClick = onTestDetection
                )
                Spacer(modifier = Modifier.height(10.dp))
                PrimaryActionButton(
                    text = "Go to Dashboard",
                    trailingIcon = Icons.Default.ArrowForward,
                    onClick = onGoToDashboard
                )
            }
        }
    }
}

@Composable
private fun DiagnosticCheckRow(
    title: String,
    subtitle: String,
    isPassed: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceSecondary)
            .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
        Icon(
            imageVector = if (isPassed) Icons.Default.CheckCircle else Icons.Default.Warning,
            contentDescription = null,
            tint = if (isPassed) RecoveryGreen else AccentGold,
            modifier = Modifier.size(20.dp)
        )
    }
}
