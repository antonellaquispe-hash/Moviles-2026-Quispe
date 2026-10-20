package com.quispe.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.cafecampus.data.local.entity.Producto
import com.quispe.cafecampus.data.repository.CafeRepository
import com.quispe.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    repository: CafeRepository
) : ViewModel() {

    val productos: StateFlow<List<Producto>> =
        repository.observarProductos().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()

    fun agregar(producto: Producto, cantidad: Int, nota: String) {
        if (!producto.disponible || cantidad < 1) return

        val itemsActuales = _carrito.value.toMutableList()
        val indice = itemsActuales.indexOfFirst {
            it.producto.id == producto.id
        }

        if (indice >= 0) {
            val anterior = itemsActuales[indice]
            itemsActuales[indice] = anterior.copy(
                cantidad = anterior.cantidad + cantidad,
                nota = nota.trim().ifBlank { anterior.nota }
            )
        } else {
            itemsActuales.add(
                ItemCarrito(
                    producto = producto,
                    cantidad = cantidad,
                    nota = nota.trim()
                )
            )
        }

        _carrito.value = itemsActuales
    }

    fun quitar(productoId: Int) {
        _carrito.value = _carrito.value.filter {
            it.producto.id != productoId
        }
    }

    fun vaciar() {
        _carrito.value = emptyList()
    }

    fun total(): Double {
        return _carrito.value.sumOf { it.subtotal }
    }

    fun cantidadTotal(): Int {
        return _carrito.value.sumOf { it.cantidad }
    }
}