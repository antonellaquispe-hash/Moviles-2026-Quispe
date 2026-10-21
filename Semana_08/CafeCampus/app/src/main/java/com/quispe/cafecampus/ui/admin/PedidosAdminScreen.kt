
package com.quispe.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.model.EstadoPedido
import com.quispe.cafecampus.ui.components.CafeBottomBar
import com.quispe.cafecampus.ui.components.EstadoChip
import com.quispe.cafecampus.ui.navigation.ITEMS_ADMIN
import com.quispe.cafecampus.ui.navigation.RutasCafe
import com.quispe.cafecampus.viewmodel.PedidosAdminViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosAdminScreen(
    viewModel: PedidosAdminViewModel,
    onVerDetalle: (Int) -> Unit,
    onVolver: () -> Unit,
    onAvanzar: (PedidoConDetalle) -> Unit = {},
    onCancelar: (PedidoConDetalle) -> Unit = {},
    onNavegar: (String) -> Unit = {}
) {
    val pedidos by viewModel.pedidos.collectAsState()
    val estadoSeleccionado by viewModel.estadoSeleccionado.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedidos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            CafeBottomBar(
                items = ITEMS_ADMIN,
                rutaActual = RutasCafe.ADMIN_PEDIDOS,
                onNavegar = onNavegar
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.estadosVisibles) { estado ->
                    FilterChip(
                        selected = estadoSeleccionado == estado,
                        onClick = { viewModel.seleccionarEstado(estado) },
                        label = { Text(estado.etiqueta()) },
                        shape = RoundedCornerShape(10.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor =
                                MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor =
                                MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            if (pedidos.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No hay pedidos en estado ${estadoSeleccionado.etiqueta()}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Los pedidos aparecerán aquí en cuanto lleguen.",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        bottom = 16.dp
                    )
                ) {
                    items(pedidos, key = { it.pedido.id }) { pedido ->
                        PedidoAdminCard(
                            pedido = pedido,
                            onVerDetalle = { onVerDetalle(pedido.pedido.id) },
                            onAvanzar = { onAvanzar(pedido) },
                            onCancelar = { onCancelar(pedido) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PedidoAdminCard(
    pedido: PedidoConDetalle,
    onVerDetalle: () -> Unit,
    onAvanzar: () -> Unit,
    onCancelar: () -> Unit
) {
    val estado = runCatching {
        EstadoPedido.valueOf(pedido.pedido.estado)
    }.getOrDefault(EstadoPedido.RECIBIDO)

    val siguiente = estado.siguiente()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pedido #${pedido.pedido.id}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = pedido.cliente.nombre,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                EstadoChip(estado = estado)
            }

            Text(
                text = "Recojo: ${pedido.pedido.horaRecojo} · " +
                    "Pago: ${pedido.pedido.pago}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (pedido.pedido.nota.isNotBlank()) {
                Text(
                    text = "Nota: ${pedido.pedido.nota}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = String.format(
                    Locale("es", "PE"),
                    "Total: S/ %.2f",
                    pedido.pedido.total
                ),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onVerDetalle,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Ver detalle")
                }

                if (siguiente != null) {
                    androidx.compose.material3.Button(
                        onClick = onAvanzar,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 8.dp
                        )
                    ) {
                        Text(
                            text = "Pasar a ${siguiente.etiqueta()}",
                            maxLines = 1
                        )
                    }
                }

                if (estado.puedeCancelarse()) {
                    IconButton(onClick = onCancelar) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Cancelar pedido",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
