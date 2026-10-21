
package com.quispe.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.model.EstadoPedido
import com.quispe.cafecampus.ui.components.CafeButton
import com.quispe.cafecampus.ui.components.EstadoChip
import com.quispe.cafecampus.ui.theme.ColorNotaAmbar
import com.quispe.cafecampus.ui.theme.ColorNotaTexto
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePedidoAdminScreen(
    pedido: PedidoConDetalle,
    onCambiarEstado: (EstadoPedido) -> Unit,
    onCancelar: () -> Unit,
    onVolver: () -> Unit
) {
    var mostrarConfirmacion by remember(pedido.pedido.id) {
        mutableStateOf(false)
    }

    val estadoActual = runCatching {
        EstadoPedido.valueOf(pedido.pedido.estado)
    }.getOrNull()

    val siguienteEstado = estadoActual?.siguiente()

    val estadosPosibles = listOf(
        EstadoPedido.RECIBIDO,
        EstadoPedido.PREPARANDO,
        EstadoPedido.LISTO,
        EstadoPedido.ENTREGADO
    )

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            title = { Text("Cancelar pedido") },
            text = {
                Text(
                    "¿Seguro que deseas cancelar el pedido #${pedido.pedido.id}? " +
                        "Esta acción cambiará su estado a Cancelado."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                        onCancelar()
                    }
                ) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacion = false }) {
                    Text("Volver")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del pedido") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
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
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pedido #${pedido.pedido.id}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        EstadoChip(
                            estado = estadoActual ?: EstadoPedido.RECIBIDO
                        )
                    }

                    Text(
                        text = "Cliente: ${pedido.cliente.nombre}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Recojo: ${pedido.pedido.horaRecojo} · " +
                            "Pago: ${pedido.pedido.pago}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (pedido.pedido.nota.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ColorNotaAmbar
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Nota del cliente",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = ColorNotaTexto
                        )

                        Text(
                            text = pedido.pedido.nota,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ColorNotaTexto
                        )
                    }
                }
            }

            Text(
                text = "Productos del pedido",
                style = MaterialTheme.typography.titleMedium,
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
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (pedido.detalles.isEmpty()) {
                        Text(
                            text = "No hay productos registrados en este pedido.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        pedido.detalles.forEach { detalle ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${detalle.cantidad} x ${detalle.nombreProducto}",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text(
                                    text = String.format(
                                        Locale("es", "PE"),
                                        "S/ %.2f",
                                        detalle.cantidad * detalle.precioUnitario
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = String.format(
                                    Locale("es", "PE"),
                                    "S/ %.2f",
                                    pedido.pedido.total
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Text(
                text = "Cambiar estado",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            androidx.compose.foundation.lazy.LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(estadosPosibles) { estado ->
                    FilterChip(
                        selected = estadoActual == estado,
                        onClick = { onCambiarEstado(estado) },
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

            Spacer(modifier = Modifier.height(2.dp))

            if (siguienteEstado != null) {
                CafeButton(
                    text = when (siguienteEstado) {
                        EstadoPedido.PREPARANDO -> "Pasar a preparación"
                        EstadoPedido.LISTO -> "Pasar a listo"
                        EstadoPedido.ENTREGADO -> "Pasar a entregado"
                        else -> "Actualizar estado"
                    },
                    onClick = { onCambiarEstado(siguienteEstado) }
                )
            } else {
                Text(
                    text = when (estadoActual) {
                        EstadoPedido.ENTREGADO -> "El pedido ya fue entregado."
                        EstadoPedido.CANCELADO -> "Este pedido fue cancelado."
                        else -> "No hay más transiciones disponibles."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (estadoActual?.puedeCancelarse() == true) {
                CafeButton(
                    text = "Cancelar pedido",
                    onClick = { mostrarConfirmacion = true },
                    esContorno = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
