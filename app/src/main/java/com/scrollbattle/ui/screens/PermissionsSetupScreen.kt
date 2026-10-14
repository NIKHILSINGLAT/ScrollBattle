package com.scrollbattle.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.scrollbattle.domain.model.ScrollBattleState
import com.scrollbattle.theme.*
import com.scrollbattle.ui.components.*

@Composable
fun PermissionsSetupScreen(
    state: ScrollBattleState,
    onContinue: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val isReady = state.accessibilityGranted && state.overlayPermissionGranted

    Scaffold(
        containerColor = CanvasBackground,
        topBar = {
            ScrollBattleTopBar(
                title = "Quick Setup",
                subtitle = "2 simple steps to get started",
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
            item {
                Text(
                    text = "Scroll Battle needs 2 Android permissions to detect Reel swipes and show your live counter on screen.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            // Step 1: Accessibility Service (Reel Counting)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardSurface)
                        .border(1.dp, if (!state.accessibilityGranted) AccentGold else BorderSubtle, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "1. Reel Counting (Accessibility)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            StatusBadge(
                                text = if (state.accessibilityGranted) "Enabled ✓" else "Required",
                                dotColor = if (state.accessibilityGranted) RecoveryGreen else CriticalRed
                            )
                        }

                        Text(
                            text = "Detects when you swipe to the next Reel in Instagram to update your count.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        if (!state.accessibilityGranted) {
                            PrimaryActionButton(
                                text = "Open Accessibility Settings",
                                trailingIcon = Icons.Default.OpenInNew,
                                onClick = {
                                    try {
                                        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                                    } catch (e: Exception) {
                                        context.startActivity(Intent(Settings.ACTION_SETTINGS))
                                    }
                                }
                            )

                            Text(
                                text = "👉 In Settings: Tap 'Installed apps' (or 'Downloaded apps') → tap 'ScrollBattle' → toggle ON.",
                                style = MaterialTheme.typography.labelSmall,
                                color = AccentGold
                            )

                            // 1-Tap Unblock for Restricted Setting on Android 13/14/15/Samsung/Xiaomi
                            OutlinedButton(
                                onClick = {
                                    try {
                                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                            data = Uri.parse("package:${context.packageName}")
                                        }
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        context.startActivity(Intent(Settings.ACTION_SETTINGS))
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LockOpen,
                                    contentDescription = null,
                                    tint = PrimaryOrange,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "If Android says 'Restricted Setting' → Tap Here",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Text(
                                text = "💡 Fix: Tap the button above → Tap the 3 dots (⋮) in the top right corner → Tap 'Allow restricted settings'. Then come back and turn on Accessibility.",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextTertiary
                            )
                        } else {
                            SecondaryActionButton(
                                text = "Reel Counting Active",
                                trailingIcon = Icons.Default.Check,
                                onClick = {}
                            )
                        }
                    }
                }
            }

            // Step 2: Display Over Other Apps (Floating Pill)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardSurface)
                        .border(1.dp, if (!state.overlayPermissionGranted) AccentGold else BorderSubtle, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "2. Floating Counter (Appear on Top)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            StatusBadge(
                                text = if (state.overlayPermissionGranted) "Enabled ✓" else "Required",
                                dotColor = if (state.overlayPermissionGranted) RecoveryGreen else CriticalRed
                            )
                        }

                        Text(
                            text = "Displays the floating brain counter over Instagram and shows the limit interruption card.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        if (!state.overlayPermissionGranted) {
                            PrimaryActionButton(
                                text = "Enable 'Appear on Top'",
                                trailingIcon = Icons.Default.OpenInNew,
                                onClick = {
                                    try {
                                        val intent = Intent(
                                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                            Uri.parse("package:${context.packageName}")
                                        )
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        context.startActivity(Intent(Settings.ACTION_SETTINGS))
                                    }
                                }
                            )

                            Text(
                                text = "👉 Turn ON 'Allow permission' for ScrollBattle. (Xiaomi/Vivo: Also allow 'Display pop-up windows in background' in App info).",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextTertiary
                            )
                        } else {
                            SecondaryActionButton(
                                text = "Floating Counter Active",
                                trailingIcon = Icons.Default.Check,
                                onClick = {}
                            )
                        }
                    }
                }
            }

            // Step 3 (Optional): App Usage Access
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceSecondary)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "App Usage Access (Optional)",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            StatusBadge(
                                text = if (state.usageAccessGranted) "Active" else "Optional",
                                dotColor = if (state.usageAccessGranted) RecoveryGreen else TextTertiary
                            )
                        }

                        Text(
                            text = "Allows background calculation of total scrolling minutes. Not required for counting Reels.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        if (!state.usageAccessGranted) {
                            OutlinedButton(
                                onClick = {
                                    try {
                                        context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                                    } catch (e: Exception) {
                                        context.startActivity(Intent(Settings.ACTION_SETTINGS))
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Enable Usage Access (Optional)", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            // Privacy Guarantee Notice
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceElevated)
                        .padding(14.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = RecoveryGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "100% Private & On-Device",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "All reel counting happens strictly on your phone. No personal data, messages, or keystrokes are ever accessed.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Finish button
            item {
                Spacer(modifier = Modifier.height(4.dp))
                PrimaryActionButton(
                    text = if (isReady) "✓ All Set — Open Dashboard" else "Continue to Dashboard",
                    trailingIcon = Icons.Default.ArrowForward,
                    onClick = onContinue
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
