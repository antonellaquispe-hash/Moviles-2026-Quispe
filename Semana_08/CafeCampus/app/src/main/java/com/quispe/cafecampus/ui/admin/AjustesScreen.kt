
package com.quispe.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AjustesScreen(
    nombreAdmin: String,
    modoOscuro: Boolean,
    recibirPedidos: Boolean,
    onModoOscuroChange: (Boolean) -> Unit,
    onRecibirPedidosChange: (Boolean) -> Unit,
    onVolver: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Ajustes de CafeCampus",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Sesión administrativa: $nombreAdmin",
                style = MaterialTheme.typography.bodyLarge
            )

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Modo oscuro",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = if (modoOscuro) {
                            "Tema oscuro activado"
                        } else {
                            "Tema claro activado"
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Switch(
                    checked = modoOscuro,
                    onCheckedChange = onModoOscuroChange
                )
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Recepción de pedidos",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = if (recibirPedidos) {
                            "La cafetería está recibiendo pedidos"
                        } else {
                            "La cafetería está cerrada"
                        },
                        color = if (recibirPedidos) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Switch(
                    checked = recibirPedidos,
                    onCheckedChange = onRecibirPedidosChange
                )
            }

            HorizontalDivider()

            Text(
                text = "Los cambios se guardan automáticamente y se conservan al reiniciar la aplicación.",
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al panel")
            }

            Button(
                onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}