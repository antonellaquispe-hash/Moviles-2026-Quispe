package com.tecsup.mibodega.ui.cliente.screens.confirmacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.BotonSecundario

@Composable
fun ConfirmacionScreen(
    onVolver: () -> Unit,
    onFinalizar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Pedido confirmado",
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "¡Gracias por tu compra!"
        )

        Text(
            text = "Tu pedido ha sido registrado correctamente."
        )

        BotonPrimario(
            texto = "Finalizar",
            onClick = onFinalizar
        )

        BotonSecundario(
            texto = "Volver",
            onClick = onVolver
        )
    }
}