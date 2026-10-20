
package com.quispe.cafecampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.quispe.cafecampus.data.local.entity.DetallePedido
import com.quispe.cafecampus.data.local.entity.Pedido
import com.quispe.cafecampus.data.local.entity.Usuario

data class PedidoConDetalle(
    @Embedded
    val pedido: Pedido,

    @Relation(
        parentColumn = "id",
        entityColumn = "pedidoId"
    )
    val detalles: List<DetallePedido>,

    @Relation(
        parentColumn = "clienteId",
        entityColumn = "id"
    )
    val cliente: Usuario
)