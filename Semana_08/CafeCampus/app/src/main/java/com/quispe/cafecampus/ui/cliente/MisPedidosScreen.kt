
package com.quispe.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.model.EstadoPedido
import com.quispe.cafecampus.ui.components.CafeBottomBar
import com.quispe.cafecampus.ui.components.PedidoCard
import com.quispe.cafecampus.ui.navigation.ITEMS_CLIENTE
import com.quispe.cafecampus.ui.navigation.RutasCafe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisPedidosScreen(
    pedidos: List<PedidoConDetalle>,
    cargando: Boolean,
    onVerPedido: (Int) -> Unit,
    onVolver: () -> Unit,
    onNavegar: (String) -> Unit = {}
) {
    var pestaña by remember { mutableIntStateOf(0) }
    var filtro by remember { mutableStateOf<EstadoPedido?>(null) }

    val estados = listOf(
        EstadoPedido.RECIBIDO,
        EstadoPedido.PREPARANDO,
        EstadoPedido.LISTO,
        EstadoPedido.ENTREGADO,
        EstadoPedido.CANCELADO
    )

    val activos = remember(pedidos) {
        pedidos.filter {
            val estado = runCatching {
                EstadoPedido.valueOf(it.pedido.estado)
            }.getOrNull()

            estado == EstadoPedido.RECIBIDO ||
                estado == EstadoPedido.PREPARANDO ||
                estado == EstadoPedido.LISTO
        }
    }

    val historial = remember(pedidos) {
        pedidos.filter {
            val estado = runCatching {
                EstadoPedido.valueOf(it.pedido.estado)
            }.getOrNull()

            estado == EstadoPedido.ENTREGADO ||
                estado == EstadoPedido.CANCELADO
        }
    }

    val listaBase = if (pestaña == 0) activos else historial

    val listaFiltrada = remember(listaBase, filtro) {
        if (filtro == null) {
            listaBase
        } else {
            listaBase.filter { it.pedido.estado == filtro!!.name }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis pedidos") },
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
        },
        bottomBar = {
            CafeBottomBar(
                items = ITEMS_CLIENTE,
                rutaActual = RutasCafe.MIS_PEDIDOS,
                onNavegar = onNavegar
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = pestaña,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Tab(
                    selected = pestaña == 0,
                    onClick = { pestaña = 0; filtro = null },
                    text = { Text("Activos") }
                )

                Tab(
                    selected = pestaña == 1,
                    onClick = { pestaña = 1; filtro = null },
                    text = { Text("Historial") }
                )
            }

            androidx.compose.foundation.lazy.LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(estados) { estado ->
                    FilterChip(
                        selected = filtro == estado,
                        onClick = {
                            filtro = if (filtro == estado) null else estado
                        },
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

            when {
                cargando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Cargando tus pedidos...")
                    }
                }

                listaFiltrada.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (pestaña == 0) {
                                    "Todavía no tienes pedidos activos"
                                } else {
                                    "Aún no tienes historial de pedidos"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Cuando realices una compra, aparecerá aquí.",
                                modifier = Modifier.padding(top = 8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            bottom = 16.dp
                        )
                    ) {
                        items(
                            items = listaFiltrada,
                            key = { it.pedido.id }
                        ) { pedido ->
                            PedidoCard(
                                pedido = pedido,
                                onClick = { onVerPedido(pedido.pedido.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
