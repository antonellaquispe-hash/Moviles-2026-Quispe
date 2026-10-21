
package com.quispe.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.data.repository.CafeRepository
import com.quispe.cafecampus.model.EstadoPedido
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PedidosAdminViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    private val _estadoSeleccionado = MutableStateFlow(EstadoPedido.RECIBIDO)
    val estadoSeleccionado: StateFlow<EstadoPedido> = _estadoSeleccionado.asStateFlow()

    val estadosVisibles: List<EstadoPedido> = listOf(
        EstadoPedido.RECIBIDO,
        EstadoPedido.PREPARANDO,
        EstadoPedido.LISTO,
        EstadoPedido.ENTREGADO,
        EstadoPedido.CANCELADO
    )

    val pedidos: StateFlow<List<PedidoConDetalle>> = _estadoSeleccionado
        .flatMapLatest { estado ->
            repository.observarPedidosPorEstado(estado)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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

    val todosLosPedidos = combine(
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

    fun seleccionarEstado(estado: EstadoPedido) {
        _estadoSeleccionado.value = estado
    }

    fun cambiarEstado(pedidoId: Int, nuevoEstado: EstadoPedido) {
        viewModelScope.launch {
            val pedido = repository.observarPedido(pedidoId).first()
                ?: return@launch

            val estadoActual = pedido.estadoActual() ?: return@launch

            if (estadoActual.siguiente() == nuevoEstado) {
                repository.cambiarEstado(pedidoId, nuevoEstado)
            }
        }
    }

    fun avanzar(pedido: PedidoConDetalle) {
        val siguiente = pedido.estadoActual()?.siguiente() ?: return

        viewModelScope.launch {
            repository.cambiarEstado(pedido.pedido.id, siguiente)
        }
    }

    fun cancelar(pedido: PedidoConDetalle) {
        viewModelScope.launch {
            if (pedido.estadoActual()?.puedeCancelarse() == true) {
                repository.cambiarEstado(
                    pedido.pedido.id,
                    EstadoPedido.CANCELADO
                )
            }
        }
    }
}

internal fun PedidoConDetalle.estadoActual(): EstadoPedido? =
    runCatching { EstadoPedido.valueOf(pedido.estado) }.getOrNull()
