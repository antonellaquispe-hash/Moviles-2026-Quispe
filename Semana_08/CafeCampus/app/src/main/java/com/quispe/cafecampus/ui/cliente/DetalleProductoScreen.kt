package com.quispe.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.AssistChip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.data.local.entity.Producto
import com.quispe.cafecampus.ui.components.ProductoIcono
import com.quispe.cafecampus.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    producto: Producto,
    vm: MenuViewModel,
    onVolver: () -> Unit,
    onAgregado: () -> Unit
) {
    var cantidad by remember(producto.id) { mutableIntStateOf(1) }
    var nota by remember(producto.id) { mutableStateOf("") }
    val disponible = producto.disponible

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del producto") },
                navigationIcon = {
                    OutlinedButton(onClick = onVolver) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProductoIcono(icono = producto.icono)

                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = producto.descripcion,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "S/ %.2f".format(producto.precio),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    if (disponible) {
                        AssistChip(
                            onClick = {},
                            label = { Text("Disponible") }
                        )
                    } else {
                        AssistChip(
                            onClick = {},
                            enabled = false,
                            label = { Text("Agotado") }
                        )
                    }
                }
            }

            Text(
                text = "Cantidad",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { if (cantidad > 1) cantidad-- },
                    enabled = disponible && cantidad > 1
                ) {
                    Text("−")
                }

                Text(
                    text = cantidad.toString(),
                    modifier = Modifier.padding(horizontal = 28.dp),
                    style = MaterialTheme.typography.headlineSmall
                )

                Button(
                    onClick = { cantidad++ },
                    enabled = disponible
                ) {
                    Text("+")
                }
            }

            OutlinedTextField(
                value = nota,
                onValueChange = { nota = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Observaciones (opcional)") },
                placeholder = { Text("Ej. Sin azúcar, poca salsa...") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                minLines = 2,
                enabled = disponible
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Subtotal: S/ %.2f".format(producto.precio * cantidad),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    vm.agregar(producto, cantidad, nota)
                    onAgregado()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = disponible,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    if (disponible) "Agregar al pedido" else "Producto agotado"
                )
            }
        }
    }
}