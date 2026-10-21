
package com.quispe.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.ui.components.CafeBottomBar
import com.quispe.cafecampus.ui.components.CafeButton
import com.quispe.cafecampus.ui.navigation.ITEMS_ADMIN
import com.quispe.cafecampus.ui.navigation.RutasCafe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(
    nombreAdmin: String,
    modoOscuro: Boolean,
    recibirPedidos: Boolean,
    onModoOscuroChange: (Boolean) -> Unit,
    onRecibirPedidosChange: (Boolean) -> Unit,
    onCambiarPerfil: () -> Unit = {},
    onVolver: () -> Unit,
    onCerrarSesion: () -> Unit,
    onNavegar: (String) -> Unit = {}
) {
    var notificaciones by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            CafeBottomBar(
                items = ITEMS_ADMIN,
                rutaActual = RutasCafe.ADMIN_AJUSTES,
                onNavegar = onNavegar
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Column {
                        Text(
                            text = nombreAdmin.ifBlank { "Administrador" },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Administrador",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            CafeButton(
                text = "Cambiar de perfil",
                onClick = onCambiarPerfil,
                esContorno = true
            )

            Text(
                text = "Preferencias",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilaPreferencia(
                        icono = if (modoOscuro) {
                            Icons.Filled.DarkMode
                        } else {
                            Icons.Filled.LightMode
                        },
                        titulo = "Modo oscuro",
                        subtitulo = if (modoOscuro) {
                            "Tema oscuro activado"
                        } else {
                            "Tema claro activado"
                        },
                        checked = modoOscuro,
                        onCheckedChange = onModoOscuroChange
                    )

                    FilaPreferencia(
                        icono = Icons.Filled.Notifications,
                        titulo = "Notificaciones",
                        subtitulo = "Avisos de nuevos pedidos",
                        checked = notificaciones,
                        onCheckedChange = { notificaciones = it }
                    )

                    FilaPreferencia(
                        icono = Icons.Filled.Storefront,
                        titulo = "Recibir pedidos",
                        subtitulo = if (recibirPedidos) {
                            "La cafetería está recibiendo pedidos"
                        } else {
                            "La cafetería está cerrada"
                        },
                        checked = recibirPedidos,
                        onCheckedChange = onRecibirPedidosChange
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            CafeButton(
                text = "Cerrar sesión",
                onClick = onCerrarSesion
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FilaPreferencia(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    titulo: String,
    subtitulo: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Column {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
