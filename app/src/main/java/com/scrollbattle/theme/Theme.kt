package com.scrollbattle.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ScrollBattleDarkColorScheme = darkColorScheme(
    primary = PrimaryOrange,
    onPrimary = CanvasBackground,
    primaryContainer = PrimaryOrange,
    onPrimaryContainer = CanvasBackground,
    secondary = AccentGold,
    onSecondary = CanvasBackground,
    tertiary = RecoveryGreen,
    onTertiary = CanvasBackground,
    background = CanvasBackground,
    onBackground = TextPrimary,
    surface = CanvasBackground,
    onSurface = TextPrimary,
    surfaceVariant = CardSurface,
    onSurfaceVariant = TextSecondary,
    surfaceContainer = CardSurface,
    surfaceContainerHigh = SurfaceElevated,
    surfaceContainerHighest = SurfaceContainerHighest,
    outline = BorderSubtle,
    outlineVariant = BorderSubtle,
    error = CriticalRed,
    onError = CanvasBackground
)

@Composable
fun ScrollBattleTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = ScrollBattleDarkColorScheme,
        typography = Typography,
        content = content
    )
}
