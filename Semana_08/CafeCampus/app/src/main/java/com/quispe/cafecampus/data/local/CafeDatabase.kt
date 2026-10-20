
package com.quispe.cafecampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.quispe.cafecampus.data.local.dao.PedidoDao
import com.quispe.cafecampus.data.local.dao.ProductoDao
import com.quispe.cafecampus.data.local.dao.UsuarioDao
import com.quispe.cafecampus.data.local.entity.DetallePedido
import com.quispe.cafecampus.data.local.entity.Pedido
import com.quispe.cafecampus.data.local.entity.Producto
import com.quispe.cafecampus.data.local.entity.Usuario

@Database(
    entities = [
        Usuario::class,
        Producto::class,
        Pedido::class,
        DetallePedido::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CafeDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun productoDao(): ProductoDao

    abstract fun pedidoDao(): PedidoDao

    companion object {
        @Volatile
        private var INSTANCE: CafeDatabase? = null

        fun obtenerInstancia(context: Context): CafeDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CafeDatabase::class.java,
                    "cafecampus_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}