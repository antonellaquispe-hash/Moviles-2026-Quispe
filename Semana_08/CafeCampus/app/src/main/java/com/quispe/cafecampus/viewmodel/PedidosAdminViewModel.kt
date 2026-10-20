
package com.quispe.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.data.repository.CafeRepository
import com.quispe.cafecampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PedidosAdminViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    val pedidosRecibidos = repository
        .observarPedidosPorEstado(EstadoPedido.RECIBIDO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pedidosPreparando = repository
        .observarPedidosPorEstado(EstadoPedido.PREPARANDO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pedidosListos = repository
        .observarPedidosPorEstado(EstadoPedido.LISTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pedidosEntregados = repository
        .observarPedidosPorEstado(EstadoPedido.ENTREGADO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pedidosCancelados = repository
        .observarPedidosPorEstado(EstadoPedido.CANCELADO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todosLosPedidos = kotlinx.coroutines.flow.combine(
        pedidosRecibidos,
        pedidosPreparando,
        pedidosListos,
        pedidosEntregados,
        pedidosCancelados
    ) { recibidos, preparando, listos, entregados, cancelados ->
        (recibidos + preparando + listos + entregados + cancelados)
            .distinctBy { it.pedido.id }
            .sortedByDescending { it.pedido.fecha }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun cambiarEstado(pedidoId: Int, nuevoEstado: EstadoPedido) {
        viewModelScope.launch {
            val pedido = repository.observarPedido(pedidoId).first()
                ?: return@launch

            val estadoActual = runCatching {
                EstadoPedido.valueOf(pedido.pedido.estado)
            }.getOrNull() ?: return@launch

            if (estadoActual.siguiente() == nuevoEstado) {
                repository.cambiarEstado(pedidoId, nuevoEstado)
            }
        }
    }

    fun cancelar(pedidoId: Int) {
        viewModelScope.launch {
            val pedido = repository.observarPedido(pedidoId).first()
                ?: return@launch

            val estadoActual = runCatching {
                EstadoPedido.valueOf(pedido.pedido.estado)
            }.getOrNull() ?: return@launch

            if (estadoActual.puedeCancelarse()) {
                repository.cambiarEstado(pedidoId, EstadoPedido.CANCELADO)
            }
        }
    }
}