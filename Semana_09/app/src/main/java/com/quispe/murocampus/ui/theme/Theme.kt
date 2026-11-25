package com.quispe.murocampus.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AzulMuro,
    secondary = AzulMuroOscuro,
    tertiary = EtiquetaUsuarioDos,
    background = FondoMuro,
    surface = SuperficiePublicacion,
    onSurface = Color(0xFF20242A)
)

private val LightColorScheme = lightColorScheme(
    primary = AzulMuro,
    secondary = AzulMuroOscuro,
    tertiary = EtiquetaUsuarioDos,
    background = FondoMuro,
    surface = SuperficiePublicacion,
    onPrimary = Color.White,
    onBackground = Color(0xFF20242A),
    onSurface = Color(0xFF20242A),
    errorContainer = FondoErrorMuro,
    onErrorContainer = Color(0xFF8F2929)
)

@Composable
fun MuroCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
