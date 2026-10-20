package com.quispe.cafecampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.model.EstadoPedido

@Composable
fun EstadoChip(
    estado: EstadoPedido,
    modifier: Modifier = Modifier
) {
    val colorFondo = when (estado) {
        EstadoPedido.RECIBIDO -> Color(0xFFE3E8FF)
        EstadoPedido.PREPARANDO -> Color(0xFFFFE8C2)
        EstadoPedido.LISTO -> Color(0xFFD9F5E3)
        EstadoPedido.ENTREGADO -> Color(0xFFD9F0F5)
        EstadoPedido.CANCELADO -> Color(0xFFFFDCDC)
    }

    val colorTexto = when (estado) {
        EstadoPedido.RECIBIDO -> Color(0xFF3045A5)
        EstadoPedido.PREPARANDO -> Color(0xFF855000)
        EstadoPedido.LISTO -> Color(0xFF176B3A)
        EstadoPedido.ENTREGADO -> Color(0xFF155A70)
        EstadoPedido.CANCELADO -> Color(0xFF9C2525)
    }

    Row(
        modifier = modifier
            .background(colorFondo, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = estado.etiqueta(),
            color = colorTexto,
            style = MaterialTheme.typography.labelMedium
        )
    }
}