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
    primary = Color(0xFF7040B5),
    onPrimary = Color.White,
    secondary = Color(0xFF8655C7),
    onSecondary = Color.White,
    tertiary = Color(0xFFB34E9A),
    background = Color(0xFFFAF7FF),
    onBackground = Color(0xFF21172D),
    surface = Color.White,
    onSurface = Color(0xFF21172D),
    surfaceVariant = Color(0xFFF0E9F8),
    onSurfaceVariant = Color(0xFF61566E),
    outline = Color(0xFFD7CBE5),
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
