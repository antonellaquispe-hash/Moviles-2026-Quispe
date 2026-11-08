package com.tecsup.rockvault.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.rockvault.data.repository.ItemRepository
import com.tecsup.rockvault.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResumenAdmin(
    val totalGuardado: Int,
    val totalFavoritos: Int
)

class AdminViewModel(private val itemRepository: ItemRepository) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<ResumenAdmin>>(UiState.Cargando)
    val estado: StateFlow<UiState<ResumenAdmin>> = _estado.asStateFlow()
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        cargarTotales()
    }

    fun cargarTotales() {
        viewModelScope.launch {
            try {
                _estado.value = UiState.Exito(
                    ResumenAdmin(
                        totalGuardado = itemRepository.cantidadGuardada(),
                        totalFavoritos = itemRepository.cantidadFavoritos()
                    )
                )
            } catch (error: Exception) {
                _estado.value = UiState.Error(error.localizedMessage ?: "No se pudieron cargar los totales")
            }
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            _mensaje.value = null
            itemRepository.refrescar()
                .onSuccess { cantidad ->
                    _mensaje.value = "Se sincronizaron $cantidad elementos"
                    cargarTotales()
                }
                .onFailure { error ->
                    _mensaje.value = error.localizedMessage ?: "No se pudo sincronizar"
                }
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            try {
                itemRepository.limpiarCache()
                _mensaje.value = "Caché limpiada"
                cargarTotales()
            } catch (error: Exception) {
                _mensaje.value = error.localizedMessage ?: "No se pudo limpiar la caché"
            }
        }
    }

    class Factory(private val itemRepository: ItemRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(AdminViewModel::class.java))
            return AdminViewModel(itemRepository) as T
        }
    }
}
