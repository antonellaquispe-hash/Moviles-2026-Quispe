package com.tecsup.mibodega.ui.cliente.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun LoginScreen(
    onVolver: () -> Unit,
    onIngresar: () -> Unit
) {
    var usuario by remember {
        mutableStateOf("")
    }

    var contrasena by remember {
        mutableStateOf("")
    }

    var mostrarError by remember {
        mutableStateOf(false)
    }

    val camposCompletos =
        usuario.isNotBlank() &&
                contrasena.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Iniciar sesión",
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Ingresa tus datos para continuar."
        )

        CampoTexto(
            etiqueta = "Usuario",
            valor = usuario,
            onValorCambia = {
                usuario = it
                mostrarError = false
            },
            placeholder = "Ingresa tu usuario"
        )

        CampoTexto(
            etiqueta = "Contraseña",
            valor = contrasena,
            onValorCambia = {
                contrasena = it
                mostrarError = false
            },
            placeholder = "Ingresa tu contraseña"
        )

        if (mostrarError) {
            Text(
                text = "Usuario o contraseña incorrectos.",
                color = Color.Red
            )
        }

        BotonPrimario(
            texto = "Ingresar",
            onClick = {
                if (
                    usuario == "cliente" &&
                    contrasena == "123456"
                ) {
                    mostrarError = false
                    onIngresar()
                } else {
                    mostrarError = true
                }
            },
            habilitado = camposCompletos
        )

        BotonSecundario(
            texto = "Volver",
            onClick = onVolver
        )

        TextButton(
            onClick = {
                usuario = "cliente"
                contrasena = "123456"
                mostrarError = false
            }
        ) {
            Text("Usar usuario de prueba")
        }
    }
}