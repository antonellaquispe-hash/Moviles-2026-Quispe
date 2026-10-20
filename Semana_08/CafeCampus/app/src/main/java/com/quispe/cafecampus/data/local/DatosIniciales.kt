
package com.quispe.cafecampus.data.local

import com.quispe.cafecampus.data.local.entity.Producto
import com.quispe.cafecampus.data.local.entity.Usuario
import com.quispe.cafecampus.model.Rol

object DatosIniciales {

    suspend fun insertarSiVacio(database: CafeDatabase) {
        if (database.productoDao().contar() == 0) {
            database.productoDao().insertarTodos(
                listOf(
                    Producto(
                        nombre = "Café americano",
                        descripcion = "Café recién preparado",
                        categoria = "Bebidas calientes",
                        precio = 4.00,
                        icono = "☕"
                    ),
                    Producto(
                        nombre = "Sándwich de pollo",
                        descripcion = "Sándwich relleno de pollo",
                        categoria = "Comidas",
                        precio = 9.00,
                        icono = "🥪"
                    ),
                    Producto(
                        nombre = "Menú del día",
                        descripcion = "Menú preparado del día",
                        categoria = "Menús",
                        precio = 12.00,
                        icono = "🍛"
                    ),
                    Producto(
                        nombre = "Jugo de naranja",
                        descripcion = "Jugo de naranja natural",
                        categoria = "Bebidas frías",
                        precio = 5.00,
                        icono = "🍊"
                    ),
                    Producto(
                        nombre = "Empanada",
                        descripcion = "Empanada recién horneada",
                        categoria = "Snacks",
                        precio = 4.50,
                        icono = "🥟"
                    ),
                    Producto(
                        nombre = "Chicha morada",
                        descripcion = "Bebida tradicional peruana",
                        categoria = "Bebidas frías",
                        precio = 3.50,
                        icono = "🥤"
                    )
                )
            )
        }

        if (database.usuarioDao().contar() == 0) {
            database.usuarioDao().insertar(
                Usuario(
                    nombre = "Juan",
                    correo = "juan@campus.test",
                    clave = "1234",
                    rol = Rol.CLIENTE.name
                )
            )

            database.usuarioDao().insertar(
                Usuario(
                    nombre = "Administrador",
                    correo = "admin@cafe.test",
                    clave = "admin",
                    rol = Rol.ADMIN.name
                )
            )
        }
    }
}