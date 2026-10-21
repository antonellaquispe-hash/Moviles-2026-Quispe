
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.model.PedidoPorHora
import com.quispe.cafecampus.ui.components.CafeBottomBar
import com.quispe.cafecampus.ui.components.CafeButton
import com.quispe.cafecampus.ui.components.EstadoVacio
import com.quispe.cafecampus.ui.navigation.ITEMS_ADMIN
import com.quispe.cafecampus.ui.navigation.RutasCafe
import com.quispe.cafecampus.ui.theme.EstadoListo
import com.quispe.cafecampus.ui.theme.EstadoPreparando
import com.quispe.cafecampus.viewmodel.PanelAdminViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelScreen(
    nombreAdmin: String,
    viewModel: PanelAdminViewModel,
    onGestionarMenu: () -> Unit,
    onVerPedidos: () -> Unit,
    onAjustes: () -> Unit,
    onCerrarSesion: () -> Unit,
    onVerReporte: () -> Unit = {},
    onPrepararPedido: (Int) -> Unit = {},
    onNavegar: (String) -> Unit = {}
) {
    val productos by viewModel.productos.collectAsState()
    val nuevos by viewModel.nuevos.collectAsState()
    val preparando by viewModel.preparando.collectAsState()
    val listos by viewModel.listos.collectAsState()
    val ventas by viewModel.ventasHoy.collectAsState()
    val pedidosPorHora by viewModel.pedidosPorHora.collectAsState()
    val pedidosNuevos by viewModel.pedidosNuevos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Panel de control",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "CafeCampus · Administración",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            CafeBottomBar(
                items = ITEMS_ADMIN,
                rutaActual = RutasCafe.ADMIN,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ContadorCard(
                    titulo = "Nuevos",
                    cantidad = nuevos,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                ContadorCard(
                    titulo = "Preparando",
                    cantidad = preparando,
                    color = EstadoPreparando,
                    modifier = Modifier.weight(1f)
                )

                ContadorCard(
                    titulo = "Listos",
                    cantidad = listos,
                    color = EstadoListo,
                    modifier = Modifier.weight(1f)
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Ventas de hoy",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        Text(
                            text = String.format(Locale("es", "PE"), "S/ %.2f", ventas),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "15 pedidos",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        Text(
                            text = "${productos.size} productos",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            Text(
                text = "Pedidos por hora",
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
                GraficoPedidosPorHora(
                    pedidosPorHora = pedidosPorHora,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nuevos pedidos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${pedidosNuevos.size} pendientes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (pedidosNuevos.isEmpty()) {
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
                    EstadoVacio(
                        ilustracion = "✅",
                        titulo = "Todo al día",
                        mensaje = "No hay pedidos nuevos por ahora."
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(pedidosNuevos, key = { it.pedido.id }) { pedido ->
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Pedido #${pedido.pedido.id}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = String.format(
                                            Locale("es", "PE"),
                                            "S/ %.2f",
                                            pedido.pedido.total
                                        ),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = "Cliente: ${pedido.cliente.nombre}" +
                                        " · Recojo: ${pedido.pedido.horaRecojo}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                CafeButton(
                                    text = "Preparar",
                                    onClick = { onPrepararPedido(pedido.pedido.id) }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            CafeButton(
                text = "Reporte del día",
                onClick = onVerReporte,
                esContorno = true
            )

            CafeButton(
                text = "Gestionar menú",
                onClick = onGestionarMenu,
                esContorno = true
            )

            CafeButton(
                text = "Gestionar pedidos",
                onClick = onVerPedidos,
                esContorno = true
            )

            CafeButton(
                text = "Ajustes",
                onClick = onAjustes,
                esContorno = true
            )

            CafeButton(
                text = "Cerrar sesión",
                onClick = onCerrarSesion
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ContadorCard(
    titulo: String,
    cantidad: Int,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = cantidad.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun GraficoPedidosPorHora(
    pedidosPorHora: List<PedidoPorHora>,
    modifier: Modifier = Modifier
) {
    val maximo = pedidosPorHora.maxOfOrNull { it.cantidad }?.coerceAtLeast(1) ?: 1

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            pedidosPorHora.forEach { punto ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = punto.cantidad.toString(),
                        style = MaterialTheme.typography.labelSmall
                    )

                    Box(
                        modifier = Modifier
                            .width(22.dp)
                            .height(
                                (24 + (76 * punto.cantidad / maximo)).dp
                            )
                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            pedidosPorHora.forEach { punto ->
                Text(
                    text = "${punto.hora}h",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
