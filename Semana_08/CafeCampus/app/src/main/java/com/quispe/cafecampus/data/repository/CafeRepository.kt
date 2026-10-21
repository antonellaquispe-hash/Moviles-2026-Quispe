
package com.quispe.cafecampus.data.repository

import com.quispe.cafecampus.data.local.entity.Producto
import com.quispe.cafecampus.data.local.entity.Usuario
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.model.EstadoPedido
import com.quispe.cafecampus.model.ItemCarrito
import com.quispe.cafecampus.model.PedidoPorHora
import com.quispe.cafecampus.model.ReporteDelDia
import kotlinx.coroutines.flow.Flow

interface CafeRepository {
    suspend fun login(correo: String, clave: String): Usuario?

    // Registro de usuarios
    suspend fun crearUsuario(usuario: Usuario): Long
    suspend fun existeUsuarioPorCorreo(correo: String): Boolean

    fun observarProductos(): Flow<List<Producto>>
    suspend fun guardarProducto(p: Producto)
    suspend fun eliminarProducto(p: Producto)
    suspend fun cambiarDisponibilidad(id: Int, disponible: Boolean)

    suspend fun crearPedido(
        clienteId: Int,
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String
    ): Long

    fun observarPedidosCliente(clienteId: Int): Flow<List<PedidoConDetalle>>
    fun observarPedidosPorEstado(estado: EstadoPedido): Flow<List<PedidoConDetalle>>
    fun observarPedido(id: Int): Flow<PedidoConDetalle?>
    suspend fun cambiarEstado(pedidoId: Int, estado: EstadoPedido)

    fun observarVentasHoy(inicioDia: Long): Flow<Double>
    fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int>

    // Analitica del dia
    fun observarPedidosPorHora(inicioDia: Long): Flow<List<PedidoPorHora>>
    fun observarReporteDelDia(inicioDia: Long): Flow<ReporteDelDia>
}