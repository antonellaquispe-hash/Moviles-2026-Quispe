package com.quispe.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.cafecampus.data.repository.CafeRepository
import com.quispe.cafecampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PedidosAdminViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    val pedidosRecibidos = repository
        .observarPedidosPorEstado(EstadoPedido.RECIBIDO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val pedidosPreparando = repository
        .observarPedidosPorEstado(EstadoPedido.PREPARANDO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val pedidosListos = repository
        .observarPedidosPorEstado(EstadoPedido.LISTO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun cambiarEstado(pedidoId: Int, estado: EstadoPedido) {
        viewModelScope.launch {
            repository.cambiarEstado(pedidoId, estado)
        }
    }
}