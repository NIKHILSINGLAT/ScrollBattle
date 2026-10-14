package com.scrollbattle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
fun PrivacyDataScreen(
    state: ScrollBattleState,
    onDeleteHistory: () -> Unit,
    onResetSettings: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    if (showDeleteConfirmDialog) {
        ConfirmationDialog(
            title = "Delete Usage History?",
            message = "This will permanently erase all local session records and daily reel counters on this device. This action cannot be undone.",
            confirmText = "Delete History",
            isDestructive = true,
            onConfirm = {
                onDeleteHistory()
                showDeleteConfirmDialog = false
            },
            onDismiss = { showDeleteConfirmDialog = false }
        )
    }

    if (showResetConfirmDialog) {
        ConfirmationDialog(
            title = "Reset App Settings?",
            message = "Restore daily allowance to 50 Reels and default alert thresholds. Your recorded usage history will be preserved.",
            confirmText = "Reset Settings",
            isDestructive = false,
            onConfirm = {
                onResetSettings()
                showResetConfirmDialog = false
            },
            onDismiss = { showResetConfirmDialog = false }
        )
    }

    Scaffold(
        containerColor = CanvasBackground,
        topBar = {
            ScrollBattleTopBar(
                title = "Privacy & Data",
                subtitle = "Local storage & data governance",
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
            // Hero card
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
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(RecoveryGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudOff,
                                    contentDescription = null,
                                    tint = RecoveryGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                text = "Radical Disconnection",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Text(
                            text = "Your habits. Your device. Your choice.",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = "Scroll Battle is an offline-first tool. No accounts, no advertising trackers, and zero telemetry servers exist.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Stored Data Summary
            item {
                Text(
                    text = "Stored Data Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    StoredDataRow("Reel Count & Sessions", "${state.todayReels} reels today, ${state.recentSessions.size} sessions")
                    StoredDataRow("Daily Goal Configuration", "${state.baseAllowance} reels/day allowance")
                    StoredDataRow("Local Diagnostics", "All logs stored on device only")
                }
            }

            // Data Management Actions
            item {
                Text(
                    text = "Data Management",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PrimaryActionButton(
                        text = "Delete History",
                        leadingIcon = Icons.Default.DeleteForever,
                        containerColor = CriticalRed,
                        contentColor = TextPrimary,
                        onClick = { showDeleteConfirmDialog = true }
                    )

                    SecondaryActionButton(
                        text = "Reset Settings",
                        leadingIcon = Icons.Default.RestartAlt,
                        onClick = { showResetConfirmDialog = true }
                    )
                }
            }
        }
    }
}

@Composable
private fun StoredDataRow(title: String, detail: String) {
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
        Column {
            Text(text = title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(text = detail, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        Icon(imageVector = Icons.Default.Storage, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
    }
}
