package com.quispe.cafecampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.model.EstadoPedido

@Composable
fun StepperEstado(
    estado: EstadoPedido,
    modifier: Modifier = Modifier
) {
    val estados = listOf(
        EstadoPedido.RECIBIDO,
        EstadoPedido.PREPARANDO,
        EstadoPedido.LISTO,
        EstadoPedido.ENTREGADO
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (estado == EstadoPedido.CANCELADO) {
            EstadoChip(estado = estado)
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                estados.forEachIndexed { index, paso ->
                    val completado = estados.indexOf(estado) >= index
                    val color = if (completado) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        androidx.compose.foundation.layout.Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(color),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (completado) "✓" else "${index + 1}",
                                color = if (completado) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    Color.Gray
                                },
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Text(
                            text = paso.etiqueta(),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (completado) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
            }
        }
    }
}