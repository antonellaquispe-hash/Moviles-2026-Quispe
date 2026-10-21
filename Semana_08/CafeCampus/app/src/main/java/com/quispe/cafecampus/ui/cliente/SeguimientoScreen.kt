
package com.quispe.cafecampus.ui.cliente

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.model.EstadoPedido
import com.quispe.cafecampus.ui.components.CafeButton
import com.quispe.cafecampus.ui.components.EstadoChip
import com.quispe.cafecampus.ui.components.StepperEstado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoScreen(
    pedido: PedidoConDetalle?,
    onVolver: () -> Unit,
    onVerMisPedidos: () -> Unit
) {
    val estado = runCatching {
        EstadoPedido.valueOf(pedido?.pedido?.estado ?: "")
    }.getOrDefault(EstadoPedido.RECIBIDO)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seguimiento del pedido") },
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
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Pedido #${pedido?.pedido?.id ?: "-"}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Recojo: ${pedido?.pedido?.horaRecojo ?: "-"}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        EstadoChip(estado = estado)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.height(18.dp)
                        )

                        Text(
                            text = "Tiempo estimado: ${tiempoEstimado(estado)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Text(
                text = "Estado del pedido",
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
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Animacion del stepper: la transicion entre estados
                    // del pedido se ve al instante.
                    AnimatedContent(
                        targetState = estado,
                        transitionSpec = {
                            (fadeIn(tween(320)) + slideInVertically { it / 4 })
                                .togetherWith(
                                    fadeOut(tween(220))
                                )
                        },
                        label = "stepperEstado"
                    ) { estadoActual ->
                        StepperEstado(estado = estadoActual)
                    }
                }
            }

            if (!pedido?.detalles.isNullOrEmpty()) {
                Text(
                    text = "Tu pedido",
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
                        pedido?.detalles?.forEach { detalle ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${detalle.cantidad} x ${detalle.nombreProducto}",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text(
                                    text = "S/ %.2f".format(
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
                                text = "S/ %.2f".format(pedido?.pedido?.total ?: 0.0),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            CafeButton(
                text = "Ver mis pedidos",
                onClick = onVerMisPedidos,
                esContorno = true
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun tiempoEstimado(estado: EstadoPedido): String {
    return when (estado) {
        EstadoPedido.RECIBIDO -> "10-15 min"
        EstadoPedido.PREPARANDO -> "5-10 min"
        EstadoPedido.LISTO -> "Listo para recoger"
        EstadoPedido.ENTREGADO -> "Entregado"
        EstadoPedido.CANCELADO -> "Cancelado"
    }
}
