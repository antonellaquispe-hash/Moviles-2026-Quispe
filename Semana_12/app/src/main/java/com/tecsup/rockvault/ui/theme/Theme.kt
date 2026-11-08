package com.tecsup.rockvault.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = VaultAmber,
    onPrimary = VaultInk,
    secondary = VaultMint,
    onSecondary = VaultInk,
    tertiary = VaultRose,
    background = Color(0xFF11131A),
    onBackground = Color(0xFFF7F3EE),
    surface = Color(0xFF1C1F28),
    onSurface = Color(0xFFF7F3EE),
    surfaceVariant = Color(0xFF292C36),
    onSurfaceVariant = Color(0xFFC6C2CC),
    outline = Color(0xFF464955)
)

private val LightColorScheme = lightColorScheme(
    primary = VaultAmberDeep,
    onPrimary = Color.White,
    secondary = VaultPlum,
    onSecondary = Color.White,
    tertiary = VaultRose,
    background = VaultPaper,
    onBackground = VaultInk,
    surface = VaultCard,
    onSurface = VaultInk,
    surfaceVariant = Color(0xFFEDE6DF),
    onSurfaceVariant = VaultMuted,
    outline = VaultLine,
    error = Color(0xFFB3261E)
)

@Composable
fun RockVaultTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
