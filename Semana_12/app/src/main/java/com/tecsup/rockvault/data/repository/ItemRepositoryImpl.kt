package com.tecsup.rockvault.data.repository

import com.tecsup.rockvault.data.mapper.toDomain
import com.tecsup.rockvault.data.remote.ApiService
import com.tecsup.rockvault.domain.model.Item
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ItemRepositoryImpl(
    private val apiService: ApiService
) : ItemRepository {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    private val _favoritos = MutableStateFlow<List<Item>>(emptyList())

    override val items: Flow<List<Item>> = _items.asStateFlow()
    override val favoritos: Flow<List<Item>> = _favoritos.asStateFlow()

    override suspend fun refrescar(): Result<Int> = try {
        val idsFavoritos = _favoritos.value.mapTo(mutableSetOf()) { it.id }
        val nuevosItems = apiService.buscarAlbumes("rock").results
            .mapNotNull { it.toDomain() }
            .map { it.copy(esFavorito = it.id in idsFavoritos) }
        _items.value = nuevosItems
        _favoritos.value = nuevosItems.filter { it.esFavorito }
        Result.success(nuevosItems.size)
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    override suspend fun obtenerDetalle(id: String): Result<Item> =
        _items.value.firstOrNull { it.id == id }
            ?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("No se encontró el álbum"))

    override suspend fun alternarFavorito(id: String) {
        _items.value = _items.value.map { item ->
            if (item.id == id) item.copy(esFavorito = !item.esFavorito) else item
        }
        _favoritos.value = _items.value.filter { it.esFavorito }
    }

    override suspend fun cantidadGuardada(): Int = _items.value.size

    override suspend fun cantidadFavoritos(): Int = _favoritos.value.size

    override suspend fun limpiarCache() {
        _items.value = emptyList()
        _favoritos.value = emptyList()
    }
}
