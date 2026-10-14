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
fun SettingsScreen(
    state: ScrollBattleState,
    onSetDailyLimit: (Int) -> Unit,
    onSetWarningThreshold: (Int) -> Unit,
    onToggleIntervention: (Boolean) -> Unit,
    onNavigateToTrackingStatus: () -> Unit,
    onNavigateToTroubleshooting: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = CanvasBackground,
        topBar = {
            ScrollBattleTopBar(
                title = "Settings",
                subtitle = "App behavior & thresholds",
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
            // Section 1: Daily Allowance
            item {
                Text(
                    text = "DAILY ALLOWANCE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryOrange
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Strict 50 Reels / Day",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Fixed daily hard limit. Once 50 reels are watched, Instagram is immediately force-closed and blocked for the day.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(PrimaryOrange)
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "50 Reels",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = CanvasBackground
                            )
                        }
                    }
                }
            }

            // Section 2: Intervention Preferences
            item {
                Text(
                    text = "INTERVENTION PREFERENCES",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryOrange
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardSurface)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Strict Limit Enforcement",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Presents full-screen breath reminder and requires maths challenge to unlock additional reels.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                        Switch(
                            checked = state.settings.hardIntervention,
                            onCheckedChange = onToggleIntervention,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TextPrimary,
                                checkedTrackColor = PrimaryOrange,
                                uncheckedThumbColor = TextSecondary,
                                uncheckedTrackColor = SurfaceElevated
                            )
                        )
                    }
                }
            }

            // Section 3: Navigation Links
            item {
                Text(
                    text = "SYSTEM & DIAGNOSTICS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryOrange
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    SettingsNavigationRow(
                        icon = Icons.Default.Widgets,
                        title = "Add Home Screen Widget",
                        subtitle = "Pin 2x2 BrainPal Buddy to home launcher",
                        onClick = {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                val appWidgetManager = android.appwidget.AppWidgetManager.getInstance(context)
                                if (appWidgetManager.isRequestPinAppWidgetSupported) {
                                    val provider = android.content.ComponentName(context, com.scrollbattle.widget.BrainPalWidgetProvider::class.java)
                                    appWidgetManager.requestPinAppWidget(provider, null, null)
                                }
                            }
                        }
                    )
                    SettingsNavigationRow(
                        icon = Icons.Default.Security,
                        title = "Tracking Readiness & Status",
                        subtitle = "Verify on-device reel detection health",
                        onClick = onNavigateToTrackingStatus
                    )
                    SettingsNavigationRow(
                        icon = Icons.Default.Build,
                        title = "Troubleshooting & Diagnostics",
                        subtitle = "Battery settings, event test, log dumps",
                        onClick = onNavigateToTroubleshooting
                    )
                    SettingsNavigationRow(
                        icon = Icons.Default.CloudOff,
                        title = "Privacy & Local Data Management",
                        subtitle = "Stored metrics, delete history, reset options",
                        onClick = onNavigateToPrivacy
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsNavigationRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceSecondary)
            .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
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
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryOrange,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column {
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
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}
