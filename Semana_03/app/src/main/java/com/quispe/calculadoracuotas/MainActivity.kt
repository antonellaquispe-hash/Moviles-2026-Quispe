package com.quispe.calculadoracuotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                PantallaRegistro()
            }
        }
    }
}

@Composable
fun PantallaRegistro() {

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }

    var mostrarResumen by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Nuevo Producto de Tienda",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Registro de Producto",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    mensajeError = null
                },
                label = {
                    Text("Nombre del producto")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(
                    value = precio,
                    onValueChange = {
                        precio = it
                        mensajeError = null
                    },
                    label = {
                        Text("Precio (S/)")
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                OutlinedTextField(
                    value = cantidad,
                    onValueChange = {
                        cantidad = it
                        mensajeError = null
                    },
                    label = {
                        Text("Cantidad")
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {

                    when {

                        nombre.isBlank() -> {
                            mensajeError =
                                "Error: completa el nombre del producto."
                            mostrarResumen = false
                        }

                        precio.isBlank() -> {
                            mensajeError =
                                "Error: completa el precio."
                            mostrarResumen = false
                        }

                        precio.toDoubleOrNull() == null -> {
                            mensajeError =
                                "Error: el precio debe ser un número válido."
                            mostrarResumen = false
                        }

                        precio.toDouble() <= 0 -> {
                            mensajeError =
                                "Error: el precio debe ser mayor que 0."
                            mostrarResumen = false
                        }

                        cantidad.isBlank() -> {
                            mensajeError =
                                "Error: completa la cantidad."
                            mostrarResumen = false
                        }

                        cantidad.toIntOrNull() == null -> {
                            mensajeError =
                                "Error: la cantidad debe ser un número entero."
                            mostrarResumen = false
                        }

                        cantidad.toInt() <= 0 -> {
                            mensajeError =
                                "Error: la cantidad debe ser mayor que 0."
                            mostrarResumen = false
                        }

                        else -> {
                            mensajeError = null
                            mostrarResumen = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("AGREGAR PRODUCTO")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    nombre = ""
                    precio = ""
                    cantidad = ""
                    mostrarResumen = false
                    mensajeError = null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("LIMPIAR")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (mensajeError != null) {

                Text(
                    text = mensajeError!!,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }

            if (mostrarResumen) {

                val precioNum = precio.toDoubleOrNull() ?: 0.0
                val cantidadNum = cantidad.toIntOrNull() ?: 0
                val importe = precioNum * cantidadNum

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = nombre,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Precio: S/ " +
                                    String.format("%.2f", precioNum)
                        )

                        Text(
                            text = "Cantidad: $cantidadNum"
                        )

                        Text(
                            text = "Importe: S/ " +
                                    String.format("%.2f", importe),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "✓ Producto registrado correctamente",
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}