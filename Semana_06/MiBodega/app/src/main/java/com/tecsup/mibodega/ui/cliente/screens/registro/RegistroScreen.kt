package com.tecsup.mibodega.ui.cliente.screens.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.componentes.CampoTexto

@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onCrearCuenta: (String, String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }

    val camposCompletos =
        nombre.isNotBlank() &&
                telefono.isNotBlank() &&
                direccion.isNotBlank() &&
                referencia.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Crear cuenta",
            fontWeight = FontWeight.Bold
        )

        CampoTexto(
            etiqueta = "Nombre",
            valor = nombre,
            onValorCambia = {
                nombre = it
                mostrarError = false
            },
            modifier = Modifier.fillMaxWidth()
        )

        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = {
                telefono = it
                mostrarError = false
            },
            modifier = Modifier.fillMaxWidth()
        )

        CampoTexto(
            etiqueta = "Dirección",
            valor = direccion,
            onValorCambia = {
                direccion = it
                mostrarError = false
            },
            modifier = Modifier.fillMaxWidth()
        )

        CampoTexto(
            etiqueta = "Referencia",
            valor = referencia,
            onValorCambia = {
                referencia = it
                mostrarError = false
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (mostrarError && !camposCompletos) {
            Text(
                text = "Completa todos los campos para continuar.",
                color = Color.Red
            )
        }

        BotonPrimario(
            texto = "Crear cuenta",
            onClick = {
                if (camposCompletos) {
                    onCrearCuenta(
                        nombre,
                        telefono,
                        direccion,
                        referencia
                    )
                } else {
                    mostrarError = true
                }
            }
        )

        BotonSecundario(
            texto = "Volver",
            onClick = onVolver
        )
    }
}