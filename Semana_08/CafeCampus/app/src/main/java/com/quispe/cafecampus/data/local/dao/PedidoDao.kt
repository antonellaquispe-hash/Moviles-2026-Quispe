
package com.quispe.cafecampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.quispe.cafecampus.data.local.entity.DetallePedido
import com.quispe.cafecampus.data.local.entity.Pedido
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.model.PedidoPorHora
import com.quispe.cafecampus.model.ProductoMasVendido
import com.quispe.cafecampus.model.TotalPorFormaPago
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPedido(pedido: Pedido): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarDetalles(detalles: List<DetallePedido>): List<Long>

    @Transaction
    suspend fun crearPedido(
        pedido: Pedido,
        detalles: List<DetallePedido>
    ): Long {
        val pedidoId = insertarPedido(pedido)
        insertarDetalles(
            detalles.map { it.copy(pedidoId = pedidoId.toInt()) }
        )
        return pedidoId
    }

    @Transaction
    @Query("SELECT * FROM pedidos WHERE clienteId = :clienteId ORDER BY fecha DESC")
    fun observarPorCliente(clienteId: Int): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos ORDER BY fecha DESC")
    fun observarTodos(): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE estado = :estado ORDER BY fecha DESC")
    fun observarPorEstado(estado: String): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE id = :id LIMIT 1")
    fun observarUno(id: Int): Flow<PedidoConDetalle?>

    @Query("UPDATE pedidos SET estado = :estado WHERE id = :id")
    suspend fun cambiarEstado(id: Int, estado: String): Int

    @Query("SELECT COUNT(*) FROM pedidos WHERE estado = :estado")
    fun cantidadPorEstado(estado: String): Flow<Int>

    @Query("SELECT COALESCE(SUM(total), 0.0) FROM pedidos WHERE fecha >= :inicio AND estado != 'CANCELADO'")
    fun ventasDesde(inicio: Long): Flow<Double>

    @Query("""
        SELECT CAST(strftime('%H', fecha / 1000, 'unixepoch', 'localtime') AS INTEGER) AS hora,
               COUNT(*) AS cantidad
        FROM pedidos
        WHERE fecha >= :inicio AND estado != 'CANCELADO'
        GROUP BY hora
        ORDER BY hora ASC
    """)
    fun observarPedidosPorHora(inicio: Long): Flow<List<PedidoPorHora>>

    @Query("""
        SELECT d.nombreProducto AS nombre,
               CAST(SUM(d.cantidad) AS INTEGER) AS cantidad
        FROM detalle_pedido d
        INNER JOIN pedidos p ON p.id = d.pedidoId
        WHERE p.fecha >= :inicio AND p.estado != 'CANCELADO'
        GROUP BY d.nombreProducto
        ORDER BY cantidad DESC
        LIMIT 1
    """)
    fun observarProductoMasVendido(inicio: Long): Flow<ProductoMasVendido?>

    @Query("""
        SELECT p.pago AS forma,
               COALESCE(SUM(p.total), 0.0) AS total,
               COUNT(*) AS cantidad
        FROM pedidos p
        WHERE p.fecha >= :inicio AND p.estado != 'CANCELADO'
        GROUP BY p.pago
        ORDER BY total DESC
    """)
    fun observarTotalesPorFormaPago(inicio: Long): Flow<List<TotalPorFormaPago>>
}