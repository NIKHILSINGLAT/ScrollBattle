package com.scrollbattle.ui.screens

import android.content.Intent
import android.provider.Settings
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.scrollbattle.domain.model.ScrollBattleState
import com.scrollbattle.theme.*
import com.scrollbattle.ui.components.*

@Composable
fun TroubleshootingScreen(
    state: ScrollBattleState,
    onTestDetection: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var copiedNotice by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = CanvasBackground,
        topBar = {
            ScrollBattleTopBar(
                title = "Troubleshooting",
                subtitle = "Diagnostics & error recovery",
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
            // Operational Node Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = RecoveryGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Telemetry Node Operational",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Local database active • 0 uncommitted events",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Common Interventions Actions
            item {
                Text(
                    text = "Common Interventions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SecondaryActionButton(
                        text = "Check Background Battery Optimization",
                        leadingIcon = Icons.Default.BatteryAlert,
                        onClick = {
                            try {
                                context.startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
                            } catch (e: Exception) {
                                context.startActivity(Intent(Settings.ACTION_SETTINGS))
                            }
                        }
                    )

                    SecondaryActionButton(
                        text = "Run Live 30s Detection Test",
                        leadingIcon = Icons.Default.PlayArrow,
                        onClick = onTestDetection
                    )

                    SecondaryActionButton(
                        text = "Review Android Permissions",
                        leadingIcon = Icons.Default.Security,
                        onClick = onNavigateToPermissions
                    )
                }
            }

            // Technical Diagnostics Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceElevated)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Technical Diagnostics (On-Device)",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = """
OS: Android (API 36)
App ID: com.scrollbattle
Architecture: Jetpack Compose + Offline-First
Tracking Status: ${state.trackingStatus.name}
Usage Access: ${if (state.usageAccessGranted) "GRANTED" else "MISSING"}
Accessibility: ${if (state.accessibilityGranted) "ACTIVE" else "UNCONFIGURED"}
Local Events Stored: ${state.recentSessions.size}
Daily Allowance: ${state.baseAllowance} (+${state.bonusAllowance} bonus)
""".trimIndent(),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        PrimaryActionButton(
                            text = if (copiedNotice) "Copied to Clipboard!" else "Copy Diagnostic Dump",
                            leadingIcon = Icons.Default.ContentCopy,
                            onClick = {
                                clipboardManager.setText(AnnotatedString("ScrollBattle Diagnostic Dump: Tracking=${state.trackingStatus.name}, TodayReels=${state.todayReels}"))
                                copiedNotice = true
                            }
                        )
                    }
                }
            }
        }
    }
}
