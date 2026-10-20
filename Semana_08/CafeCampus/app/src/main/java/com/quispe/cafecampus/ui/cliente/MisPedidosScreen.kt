package com.quispe.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MisPedidosScreen(
    pedidos: List<PedidoConDetalle>,
    cargando: Boolean,
    onVerPedido: (Int) -> Unit,
    onVolver: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = "Mis pedidos",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Consulta tus pedidos y revisa su estado.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            when {
                cargando -> {
                    Text("Cargando tus pedidos...")
                }

                pedidos.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Todavía no tienes pedidos",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Cuando realices una compra, aparecerá aquí.",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = pedidos,
                            key = { it.pedido.id }
                        ) { pedidoDetalle ->
                            val pedido = pedidoDetalle.pedido
                            val fecha = SimpleDateFormat(
                                "dd/MM/yyyy HH:mm",
                                Locale("es", "PE")
                            ).format(Date(pedido.fecha))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Pedido #${pedido.id}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Text(
                                            text = pedido.estado,
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    Text(
                                        text = "Fecha: $fecha",
                                        modifier = Modifier.padding(top = 8.dp)
                                    )

                                    Text(
                                        text = "Recojo: ${pedido.horaRecojo}",
                                        modifier = Modifier.padding(top = 4.dp)
                                    )

                                    Text(
                                        text = "Pago: ${pedido.pago}",
                                        modifier = Modifier.padding(top = 4.dp)
                                    )

                                    Text(
                                        text = "Productos: ${pedidoDetalle.detalles.sumOf { it.cantidad }}",
                                        modifier = Modifier.padding(top = 4.dp)
                                    )

                                    Text(
                                        text = "Total: S/ %.2f".format(pedido.total),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )

                                    OutlinedButton(
                                        onClick = {
                                            onVerPedido(pedido.id)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp)
                                    ) {
                                        Text("Ver seguimiento")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(top = 4.dp))

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}