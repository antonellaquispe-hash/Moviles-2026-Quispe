package com.tecsup.rockvault.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM items ORDER BY titulo")
    fun todos(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE esFavorito = 1 ORDER BY titulo")
    fun favoritos(): Flow<List<ItemEntity>>

    @Query("SELECT id FROM items WHERE esFavorito = 1")
    suspend fun idsFavoritos(): List<String>

    @Query("SELECT * FROM items WHERE id = :id LIMIT 1")
    suspend fun porId(id: String): ItemEntity?

    @Query("SELECT COUNT(*) FROM items")
    suspend fun contar(): Int

    @Query("SELECT COUNT(*) FROM items WHERE esFavorito = 1")
    suspend fun contarFavoritos(): Int

    @Upsert
    suspend fun guardarTodos(items: List<ItemEntity>)

    @Query("DELETE FROM items WHERE esFavorito = 0 AND id NOT IN (:ids)")
    suspend fun podarCatalogo(ids: List<String>)

    @Query("UPDATE items SET esFavorito = NOT esFavorito WHERE id = :id")
    suspend fun alternarFavorito(id: String)

    @Query("DELETE FROM items")
    suspend fun borrarTodo()
}
