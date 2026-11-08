package com.tecsup.rockvault.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = VaultAmber,
    onPrimary = Color(0xFF21132F),
    secondary = VaultMint,
    onSecondary = VaultInk,
    tertiary = VaultRose,
    background = Color(0xFF100B18),
    onBackground = Color(0xFFF7F0FF),
    surface = Color(0xFF1B1425),
    onSurface = Color(0xFFF7F0FF),
    surfaceVariant = Color(0xFF281D35),
    onSurfaceVariant = Color(0xFFC9BED5),
    outline = Color(0xFF49375E),
    error = Color(0xFFFF8A9A)
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
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
