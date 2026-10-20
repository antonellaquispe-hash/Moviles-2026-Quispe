
package com.quispe.cafecampus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.quispe.cafecampus.model.Rol

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String,
    val rol: String = Rol.CLIENTE.name
)
