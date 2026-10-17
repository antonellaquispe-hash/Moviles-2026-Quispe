package com.quispe.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.data.repository.CafeRepository
import com.quispe.cafecampus.model.EstadoPedido
import com.quispe.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PedidosClienteViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    private val _pedidoCreado = MutableStateFlow<Long?>(null)
    val pedidoCreado: StateFlow<Long?> = _pedidoCreado.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _pedidoActual = MutableStateFlow<PedidoConDetalle?>(null)
    val pedidoActual: StateFlow<PedidoConDetalle?> = _pedidoActual.asStateFlow()

    private val _misPedidos = MutableStateFlow<List<PedidoConDetalle>>(emptyList())
    val misPedidos: StateFlow<List<PedidoConDetalle>> = _misPedidos.asStateFlow()

    fun cargarMisPedidos(clienteId: Int) {
        if (clienteId <= 0) {
            _misPedidos.value = emptyList()
            return
        }

        viewModelScope.launch {
            repository.observarPedidosCliente(clienteId)
                .catch { excepcion ->
                    _error.value = excepcion.message
                        ?: "No se pudieron cargar tus pedidos."
                }
                .collect { pedidos ->
                    _misPedidos.value = pedidos
                }
        }
    }

    fun confirmarPedido(
        clienteId: Int,
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String
    ) {
        if (clienteId <= 0) {
            _error.value = "Inicia sesión nuevamente para continuar."
            return
        }

        if (items.isEmpty()) {
            _error.value = "Tu carrito está vacío."
            return
        }

        if (horaRecojo.isBlank()) {
            _error.value = "Selecciona la hora de recojo."
            return
        }

        if (pago.isBlank()) {
            _error.value = "Selecciona un método de pago."
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            try {
                val id = repository.crearPedido(
                    clienteId = clienteId,
                    items = items,
                    horaRecojo = horaRecojo,
                    pago = pago,
                    nota = nota.trim()
                )
                _pedidoCreado.value = id
            } catch (excepcion: Exception) {
                _error.value = excepcion.message
                    ?: "No se pudo registrar el pedido."
            } finally {
                _cargando.value = false
            }
        }
    }

    fun observarPedido(pedidoId: Int) {
        if (pedidoId <= 0) return

        viewModelScope.launch {
            repository.observarPedido(pedidoId)
                .catch { excepcion ->
                    _error.value = excepcion.message
                        ?: "No se pudo consultar el seguimiento."
                }
                .collect { pedido ->
                    _pedidoActual.value = pedido
                }
        }
    }

    fun limpiarPedidoCreado() {
        _pedidoCreado.value = null
    }

    fun limpiarError() {
        _error.value = null
    }

    fun activos(): List<PedidoConDetalle> {
        return _misPedidos.value.filter {
            val estado = runCatching {
                EstadoPedido.valueOf(it.pedido.estado)
            }.getOrNull()

            estado != EstadoPedido.ENTREGADO &&
                    estado != EstadoPedido.CANCELADO
        }
    }

    fun historial(): List<PedidoConDetalle> {
        return _misPedidos.value.filter {
            it.pedido.estado == EstadoPedido.ENTREGADO.name ||
                    it.pedido.estado == EstadoPedido.CANCELADO.name
        }
    }
}