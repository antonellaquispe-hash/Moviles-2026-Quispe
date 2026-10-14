
package com.quispe.cafecampus.data.repository

import com.quispe.cafecampus.data.local.dao.PedidoDao
import com.quispe.cafecampus.data.local.dao.ProductoDao
import com.quispe.cafecampus.data.local.dao.UsuarioDao
import com.quispe.cafecampus.data.local.entity.DetallePedido
import com.quispe.cafecampus.data.local.entity.Pedido
import com.quispe.cafecampus.data.local.entity.Producto
import com.quispe.cafecampus.data.local.entity.Usuario
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.model.EstadoPedido
import com.quispe.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

class CafeRepositoryRoom(
    private val usuarioDao: UsuarioDao,
    private val productoDao: ProductoDao,
    private val pedidoDao: PedidoDao
) : CafeRepository {

    override suspend fun login(correo: String, clave: String): Usuario? {
        return usuarioDao.login(correo.trim(), clave)
    }

    override fun observarProductos(): Flow<List<Producto>> {
        return productoDao.observarTodos()
    }

    override suspend fun guardarProducto(p: Producto) {
        if (p.id == 0) {
            productoDao.insertar(p)
        } else {
            productoDao.actualizar(p)
        }
    }

    override suspend fun eliminarProducto(p: Producto) {
        productoDao.eliminar(p)
    }

    override suspend fun cambiarDisponibilidad(id: Int, disponible: Boolean) {
        val producto = productoDao.obtenerPorId(id) ?: return
        productoDao.actualizar(producto.copy(disponible = disponible))
    }

    override suspend fun crearPedido(
        clienteId: Int,
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String
    ): Long {
        require(items.isNotEmpty()) { "El carrito está vacío." }
        require(items.all { it.cantidad > 0 }) { "Las cantidades deben ser mayores que cero." }

        val detalles = items.map { item ->
            DetallePedido(
                pedidoId = 0,
                productoId = item.producto.id,
                nombreProducto = item.producto.nombre,
                cantidad = item.cantidad,
                precioUnitario = item.producto.precio
            )
        }

        val total = detalles.sumOf { it.cantidad * it.precioUnitario }

        val pedido = Pedido(
            clienteId = clienteId,
            estado = EstadoPedido.RECIBIDO.name,
            horaRecojo = horaRecojo,
            pago = pago,
            nota = nota,
            total = total
        )

        return pedidoDao.crearPedido(pedido, detalles)
    }

    override fun observarPedidosCliente(
        clienteId: Int
    ): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarPorCliente(clienteId)
    }

    override fun observarPedidosPorEstado(
        estado: EstadoPedido
    ): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarPorEstado(estado.name)
    }

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> {
        return pedidoDao.observarUno(id)
    }

    override suspend fun cambiarEstado(pedidoId: Int, estado: EstadoPedido) {
        pedidoDao.cambiarEstado(pedidoId, estado.name)
    }

    override fun observarVentasHoy(inicioDia: Long): Flow<Double> {
        return pedidoDao.ventasDesde(inicioDia)
    }

    override fun observarCantidadPorEstado(
        estado: EstadoPedido
    ): Flow<Int> {
        return pedidoDao.contarPorEstado(estado.name)
    }
}