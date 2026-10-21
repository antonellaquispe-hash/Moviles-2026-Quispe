
package com.quispe.cafecampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.quispe.cafecampus.model.Rol

private val EsquemaClienteClaro = lightColorScheme(
    primary = NaranjaPrimario,
    onPrimary = NaranjaOnPrimario,
    primaryContainer = NaranjaContenedor,
    onPrimaryContainer = NaranjaOnContenedor,
    secondary = NaranjaPrimario,
    onSecondary = NaranjaOnPrimario,
    secondaryContainer = NaranjaContenedor,
    onSecondaryContainer = NaranjaOnContenedor,
    tertiary = EstadoPreparando,
    background = NaranjaFondo,
    onBackground = TextoPrincipal,
    surface = Superficie,
    onSurface = TextoPrincipal,
    surfaceVariant = NaranjaContenedor,
    onSurfaceVariant = NaranjaOnContenedor,
    outline = Color(0xFFE5D5C8),
    outlineVariant = Color(0xFFF2E4D8)
)

private val EsquemaClienteOscuro = darkColorScheme(
    primary = NaranjaPrimarioOscuro,
    onPrimary = Color(0xFF3A1A06),
    primaryContainer = NaranjaContenedorOscuro,
    onPrimaryContainer = Color(0xFFFFE1CE),
    secondary = NaranjaPrimarioOscuro,
    onSecondary = Color(0xFF3A1A06),
    secondaryContainer = NaranjaContenedorOscuro,
    onSecondaryContainer = Color(0xFFFFE1CE),
    tertiary = EstadoPreparando,
    background = NaranjaFondoOscuro,
    onBackground = Color(0xFFF3E9E1),
    surface = Color(0xFF241812),
    onSurface = Color(0xFFF3E9E1),
    surfaceVariant = NaranjaContenedorOscuro,
    onSurfaceVariant = Color(0xFFE6D4C6),
    outline = Color(0xFF6B5344),
    outlineVariant = Color(0xFF4A382D)
)

private val EsquemaAdminClaro = lightColorScheme(
    primary = IndigoPrimario,
    onPrimary = IndigoOnPrimario,
    primaryContainer = IndigoContenedor,
    onPrimaryContainer = IndigoOnContenedor,
    secondary = IndigoPrimario,
    onSecondary = IndigoOnPrimario,
    secondaryContainer = IndigoContenedor,
    onSecondaryContainer = IndigoOnContenedor,
    tertiary = EstadoPreparando,
    background = IndigoFondo,
    onBackground = TextoPrincipal,
    surface = Superficie,
    onSurface = TextoPrincipal,
    surfaceVariant = IndigoContenedor,
    onSurfaceVariant = IndigoOnContenedor,
    outline = Color(0xFFD5D9E8),
    outlineVariant = Color(0xFFE4E7F3)
)

private val EsquemaAdminOscuro = darkColorScheme(
    primary = IndigoPrimarioOscuro,
    onPrimary = Color(0xFF1E1B4B),
    primaryContainer = IndigoContenedorOscuro,
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = IndigoPrimarioOscuro,
    onSecondary = Color(0xFF1E1B4B),
    secondaryContainer = IndigoContenedorOscuro,
    onSecondaryContainer = Color(0xFFE0E7FF),
    tertiary = EstadoPreparando,
    background = IndigoFondoOscuro,
    onBackground = Color(0xFFE8E8F2),
    surface = Color(0xFF181829),
    onSurface = Color(0xFFE8E8F2),
    surfaceVariant = IndigoContenedorOscuro,
    onSurfaceVariant = Color(0xFFD5D8EC),
    outline = Color(0xFF4C4C68),
    outlineVariant = Color(0xFF333350)
)

/**
 * Tema de la aplicacion. Recibe el perfil activo para entregar el ColorScheme
 * correspondiente: naranja para el Estudiante e indigo para el Administrador.
 */
@Composable
fun CafeTheme(
    rol: Rol = Rol.CLIENTE,
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        rol == Rol.ADMIN && darkTheme -> EsquemaAdminOscuro
        rol == Rol.ADMIN -> EsquemaAdminClaro
        darkTheme -> EsquemaClienteOscuro
        else -> EsquemaClienteClaro
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
