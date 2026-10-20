package com.quispe.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.viewmodel.PanelAdminViewModel
import java.util.Locale

@Composable
fun PanelScreen(
    nombreAdmin: String,
    viewModel: PanelAdminViewModel,
    onGestionarMenu: () -> Unit,
    onVerPedidos: () -> Unit,
    onAjustes: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val productos by viewModel.productos.collectAsState()
    val recibidos by viewModel.pedidosRecibidos.collectAsState()
    val preparando by viewModel.pedidosEnPreparacion.collectAsState()
    val listos by viewModel.pedidosListos.collectAsState()
    val entregados by viewModel.pedidosEntregados.collectAsState()
    val ventas by viewModel.ventasHoy.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "CafeCampus | Administración",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "¡Bienvenido, $nombreAdmin!",
                style = MaterialTheme.typography.titleMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Ventas de hoy",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = String.format(Locale("es", "PE"), "S/ %.2f", ventas),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Text(
                text = "Resumen de pedidos",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResumenEstado("Recibidos", recibidos, Modifier.weight(1f))
                ResumenEstado("Preparando", preparando, Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResumenEstado("Listos", listos, Modifier.weight(1f))
                ResumenEstado("Entregados", entregados, Modifier.weight(1f))
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Productos registrados",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = productos.size.toString(),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Button(
                onClick = onGestionarMenu,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar menú")
            }

            Button(
                onClick = onVerPedidos,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar pedidos")
            }

            OutlinedButton(
                onClick = onAjustes,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ajustes")
            }

            OutlinedButton(
                onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Composable
private fun ResumenEstado(
    titulo: String,
    cantidad: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(titulo, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = cantidad.toString(),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}