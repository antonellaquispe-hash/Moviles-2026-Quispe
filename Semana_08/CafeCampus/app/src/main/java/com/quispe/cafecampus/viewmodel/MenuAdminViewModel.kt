package com.quispe.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.cafecampus.data.local.entity.Producto
import com.quispe.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MenuAdminViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    val productos = repository.observarProductos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun guardarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.guardarProducto(producto)
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.eliminarProducto(producto)
        }
    }

    fun cambiarDisponibilidad(producto: Producto, disponible: Boolean) {
        viewModelScope.launch {
            repository.cambiarDisponibilidad(
                id = producto.id,
                disponible = disponible
            )
        }
    }
}