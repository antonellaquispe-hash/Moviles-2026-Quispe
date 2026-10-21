package com.quispe.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.cafecampus.data.local.entity.Producto
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.data.repository.CafeRepository
import com.quispe.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    val productos: StateFlow<List<Producto>> =
        repository.observarProductos().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()

    // Texto escrito en la barra de busqueda
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    // Categoria seleccionada en los chips
    private val _categoriaSeleccionada = MutableStateFlow("Todos")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada.asStateFlow()

    val categorias: StateFlow<List<String>> = productos
        .map { lista ->
            listOf("Todos") + lista.map { it.categoria }.distinct()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf("Todos")
        )

    /**
     * El filtrado vive aqui, no en el Composable: se combinan tres StateFlow
     * (productos, busqueda y categoria) y la View solo observa el resultado.
     */
    val productosFiltrados: StateFlow<List<Producto>> =
        combine(productos, _busqueda, _categoriaSeleccionada) { lista, texto, categoria ->
            val textoLimpio = texto.trim()

            lista.filter { producto ->
                val coincideNombre = textoLimpio.isBlank() ||
                    producto.nombre.contains(textoLimpio, ignoreCase = true) ||
                    producto.descripcion.contains(textoLimpio, ignoreCase = true)

                val coincideCategoria = categoria == "Todos" ||
                    producto.categoria == categoria

                coincideNombre && coincideCategoria
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun actualizarBusqueda(texto: String) {
        _busqueda.value = texto
    }

    fun seleccionarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }

    fun limpiarBusqueda() {
        _busqueda.value = ""
        _categoriaSeleccionada.value = "Todos"
    }

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

    /**
     * Vuelve a cargar el carrito con los productos de un pedido anterior.
     * Los productos que ya no existan o esten agotados se omiten.
     *
     * @return cantidad de lineas agregadas al carrito
     */
    fun repetirPedido(pedido: PedidoConDetalle): Int {
        val menu = productos.value
        var lineasAgregadas = 0

        pedido.detalles.forEach { detalle ->
            val producto = menu.firstOrNull { it.id == detalle.productoId }

            if (producto != null && producto.disponible) {
                agregar(producto, detalle.cantidad, "")
                lineasAgregadas++
            }
        }

        return lineasAgregadas
    }
}
