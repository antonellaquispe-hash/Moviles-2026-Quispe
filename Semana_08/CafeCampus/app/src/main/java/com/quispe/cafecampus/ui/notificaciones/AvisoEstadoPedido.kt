package com.quispe.cafecampus.ui.notificaciones

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.model.EstadoPedido
import com.quispe.cafecampus.util.NotificacionesPedido

/**
 * Observa los pedidos del estudiante y lanza un aviso local cada vez que el
 * administrador cambia el estado. Solo vive en la capa View: no conoce el DAO
 * ni el Repository, recibe la lista ya observada.
 */
@Composable
fun AvisoEstadoPedido(
    pedidos: List<PedidoConDetalle>,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val estadosPrevios = remember { mutableStateMapOf<Int, String>() }

    LaunchedEffect(pedidos) {
        pedidos.forEach { pedido ->
            val id = pedido.pedido.id
            val actual = pedido.pedido.estado
            val anterior = estadosPrevios[id]

            if (anterior != null && anterior != actual) {
                val estado = runCatching {
                    EstadoPedido.valueOf(actual)
                }.getOrNull()

                if (estado != null) {
                    NotificacionesPedido.notificarCambioEstado(
                        contexto,
                        id,
                        estado
                    )
                }
            }

            estadosPrevios[id] = actual
        }
    }
}
