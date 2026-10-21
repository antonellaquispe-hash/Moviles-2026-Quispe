
package com.quispe.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.data.repository.CafeRepository
import com.quispe.cafecampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

class PanelAdminViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    val productos = repository.observarProductos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val nuevos: StateFlow<Int> = repository
        .observarCantidadPorEstado(EstadoPedido.RECIBIDO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val preparando: StateFlow<Int> = repository
        .observarCantidadPorEstado(EstadoPedido.PREPARANDO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val listos: StateFlow<Int> = repository
        .observarCantidadPorEstado(EstadoPedido.LISTO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val entregados: StateFlow<Int> = repository
        .observarCantidadPorEstado(EstadoPedido.ENTREGADO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val pedidosRecibidos = nuevos

    val pedidosEnPreparacion = preparando

    val pedidosListos = listos

    val pedidosEntregados = entregados

    val ventasHoy: StateFlow<Double> = repository.observarVentasHoy(inicioDelDia())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    private val pedidosPorEstado = listOf(
        EstadoPedido.RECIBIDO,
        EstadoPedido.PREPARANDO,
        EstadoPedido.LISTO,
        EstadoPedido.ENTREGADO,
        EstadoPedido.CANCELADO
    ).map { estado ->
        repository.observarPedidosPorEstado(estado)
    }

    val todosLosPedidos: StateFlow<List<PedidoConDetalle>> =
        combine(pedidosPorEstado) { listas ->
            listas
                .flatMap { it }
                .distinctBy { it.pedido.id }
                .sortedByDescending { it.pedido.fecha }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val pedidosNuevos: StateFlow<List<PedidoConDetalle>> = todosLosPedidos
        .map { pedidos ->
            pedidos.filter {
                it.pedido.estado == EstadoPedido.RECIBIDO.name
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val pedidosPorHora: StateFlow<List<PedidoPorHora>> = todosLosPedidos
        .map { pedidos ->
            val inicio = inicioDelDia()
            val fin = inicio + 24L * 60L * 60L * 1000L
            val deHoy = pedidos.filter {
                it.pedido.fecha in inicio until fin
            }

            HORAS.map { hora ->
                PedidoPorHora(
                    hora = hora,
                    cantidad = deHoy.count { pedido ->
                        val calendario = Calendar.getInstance().apply {
                            timeInMillis = pedido.pedido.fecha
                        }
                        calendario.get(Calendar.HOUR_OF_DAY) == hora
                    }
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HORAS.map { PedidoPorHora(it, 0) }
        )

    private fun inicioDelDia(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    companion object {
        val HORAS = listOf(8, 10, 12, 14, 16, 18)
    }
}

data class PedidoPorHora(
    val hora: Int,
    val cantidad: Int
)
