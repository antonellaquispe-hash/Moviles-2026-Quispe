
package com.quispe.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.model.ItemCarrito

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarPedidoScreen(
    items: List<ItemCarrito>,
    cargando: Boolean,
    error: String?,
    recibirPedidos: Boolean,
    onConfirmar: (String, String, String) -> Unit,
    onVolver: () -> Unit
) {
    var horaRecojo by remember { mutableStateOf("") }
    var pago by remember { mutableStateOf("") }
    var nota by remember { mutableStateOf("") }
    var mostrarHoras by remember { mutableStateOf(false) }
    var mostrarPagos by remember { mutableStateOf(false) }

    val horas = listOf(
        "10:00", "10:30", "11:00", "11:30",
        "12:00", "12:30", "13:00", "13:30",
        "14:00", "14:30", "15:00", "15:30",
        "16:00", "16:30", "17:00"
    )

    val metodosPago = listOf(
        "Efectivo",
        "Yape",
        "Plin",
        "Tarjeta"
    )

    val total = items.sumOf { it.subtotal }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar pedido") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Resumen de tu compra",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (!recibirPedidos) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Cafetería cerrada: no es posible confirmar pedidos en este momento.",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            if (items.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Tu carrito está vacío. Regresa al menú para agregar productos.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = items,
                        key = { it.producto.id }
                    ) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = item.producto.nombre,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "${item.cantidad} x S/ ${
                                            "%.2f".format(item.producto.precio)
                                        }"
                                    )
                                    Text(
                                        text = "S/ ${"%.2f".format(item.subtotal)}",
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (item.nota.isNotBlank()) {
                                    Text(
                                        text = "Nota: ${item.nota}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total a pagar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "S/ ${"%.2f".format(total)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = "Hora de recojo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Column {
                OutlinedButton(
                    onClick = { mostrarHoras = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !cargando && recibirPedidos
                ) {
                    Text(
                        if (horaRecojo.isBlank()) {
                            "Seleccionar hora"
                        } else {
                            horaRecojo
                        }
                    )
                }

                DropdownMenu(
                    expanded = mostrarHoras,
                    onDismissRequest = { mostrarHoras = false }
                ) {
                    horas.forEach { hora ->
                        DropdownMenuItem(
                            text = { Text(hora) },
                            onClick = {
                                horaRecojo = hora
                                mostrarHoras = false
                            }
                        )
                    }
                }
            }

            Text(
                text = "Método de pago",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Column {
                OutlinedButton(
                    onClick = { mostrarPagos = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !cargando && recibirPedidos
                ) {
                    Text(
                        if (pago.isBlank()) {
                            "Seleccionar método de pago"
                        } else {
                            pago
                        }
                    )
                }

                DropdownMenu(
                    expanded = mostrarPagos,
                    onDismissRequest = { mostrarPagos = false }
                ) {
                    metodosPago.forEach { metodo ->
                        DropdownMenuItem(
                            text = { Text(metodo) },
                            onClick = {
                                pago = metodo
                                mostrarPagos = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = nota,
                onValueChange = { nota = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nota para tu pedido (opcional)") },
                placeholder = { Text("Ej. Sin azúcar, por favor") },
                enabled = !cargando,
                maxLines = 3
            )

            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Button(
                onClick = {
                    onConfirmar(horaRecojo, pago, nota)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = recibirPedidos &&
                        items.isNotEmpty() &&
                        horaRecojo.isNotBlank() &&
                        pago.isNotBlank() &&
                        !cargando
            ) {
                Text(
                    text = if (cargando) {
                        "Registrando pedido..."
                    } else {
                        "Confirmar pedido"
                    }
                )
            }

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            ) {
                Text("Volver al menú")
            }
        }
    }
}