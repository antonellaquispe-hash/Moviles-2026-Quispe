package com.tecsup.rockvault.data.local

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: String,
    val titulo: String,
    val subtitulo: String,
    val imagenUrl: String? = null,
    val esFavorito: Boolean = false,
    @ColumnInfo(defaultValue = "'en'") val idioma: String = "en"
)
