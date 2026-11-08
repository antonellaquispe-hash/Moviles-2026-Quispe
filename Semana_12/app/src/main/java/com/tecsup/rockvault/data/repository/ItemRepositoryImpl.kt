package com.tecsup.rockvault.data.repository

import com.tecsup.rockvault.data.local.ItemDao
import com.tecsup.rockvault.data.mapper.toDomain
import com.tecsup.rockvault.data.mapper.toEntity
import com.tecsup.rockvault.data.remote.ApiService
import com.tecsup.rockvault.domain.model.Item
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ItemRepositoryImpl(
    private val apiService: ApiService,
    private val itemDao: ItemDao
) : ItemRepository {
    override val items: Flow<List<Item>> = itemDao.todos().map { entities ->
        entities.map { it.toDomain() }
    }

    override val favoritos: Flow<List<Item>> = itemDao.favoritos().map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun refrescar(): Result<Int> = try {
        val idsFavoritos = itemDao.idsFavoritos().toSet()
        val nuevosItems = apiService.listarPublicaciones()
            .filter { it.language == "es" }
            .mapNotNull { it.toDomain() }
            .map { it.copy(esFavorito = it.id in idsFavoritos) }
        if (nuevosItems.isNotEmpty()) {
            itemDao.guardarTodos(nuevosItems.map { it.toEntity() })
            itemDao.podarCatalogo(nuevosItems.map(Item::id))
        }
        Result.success(nuevosItems.size)
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    override suspend fun obtenerDetalle(id: String): Result<Item> {
        val itemGuardado = itemDao.porId(id)
        return try {
            val detalle = apiService.obtenerPublicacion(id).toDomain()
                ?: throw NoSuchElementException("No se encontró la publicación")
            val itemActualizado = detalle.copy(esFavorito = itemGuardado?.esFavorito ?: false)
            itemDao.guardarTodos(listOf(itemActualizado.toEntity()))
            Result.success(itemActualizado)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            itemGuardado?.let { Result.success(it.toDomain()) } ?: Result.failure(exception)
        }
    }

    override suspend fun alternarFavorito(id: String) {
        itemDao.alternarFavorito(id)
    }

    override suspend fun cantidadGuardada(): Int = itemDao.contar()

    override suspend fun cantidadFavoritos(): Int = itemDao.contarFavoritos()

    override suspend fun limpiarCache() {
        itemDao.borrarTodo()
    }
}
