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
import com.scrollbattle.theme.*
import com.scrollbattle.ui.components.*

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onLearnMore: () -> Unit
) {
    Scaffold(containerColor = CanvasBackground) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Spacer(modifier = Modifier.statusBarsPadding())

                // App Brand
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = PrimaryOrange,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Text(
                        text = "Scroll Battle",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Take back your scroll.",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "A personal, offline-first tool designed to curb mindless Instagram Reels doom-scrolling with an unyielding 50-Reels daily limit.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )

                // Feature Highlights
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    FeatureRow(
                        icon = Icons.Default.CloudOff,
                        title = "100% Local & Private",
                        description = "No accounts, no servers, zero telemetry sent to third parties."
                    )
                    FeatureRow(
                        icon = Icons.Default.Shield,
                        title = "Default 50 Reels Daily Limit",
                        description = "Clear, measurable boundary that resets every morning at 4 AM."
                    )
                    FeatureRow(
                        icon = Icons.Default.Calculate,
                        title = "Intentional Challenge Unlock",
                        description = "Earn emergency Reels only by solving conscious maths exercises."
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PrimaryActionButton(
                    text = "Get started",
                    trailingIcon = Icons.Default.ArrowForward,
                    onClick = onGetStarted
                )
                SecondaryActionButton(
                    text = "How it works",
                    leadingIcon = Icons.Default.Info,
                    onClick = onLearnMore
                )
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
private fun FeatureRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardSurface)
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryOrange,
                modifier = Modifier.size(20.dp)
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
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}
