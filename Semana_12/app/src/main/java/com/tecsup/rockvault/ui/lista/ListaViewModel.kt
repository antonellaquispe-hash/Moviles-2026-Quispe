package com.tecsup.rockvault.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import java.io.IOException
import com.tecsup.rockvault.data.repository.ItemRepository
import com.tecsup.rockvault.domain.model.Item
import com.tecsup.rockvault.notification.Notificador
import com.tecsup.rockvault.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ListaViewModel(
    private val itemRepository: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()
    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    init {
        viewModelScope.launch {
            itemRepository.items.collect { items ->
                if (items.isNotEmpty()) {
                    _estado.value = UiState.Exito(items)
                } else if (_estado.value is UiState.Exito) {
                    _estado.value = UiState.Vacio
                }
            }
        }
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            if (itemRepository.cantidadGuardada() == 0) {
                _estado.value = UiState.Cargando
            }
            itemRepository.refrescar()
                .onSuccess { cantidad ->
                    _aviso.value = null
                    notificador.mostrar("Datos actualizados", "Se guardaron $cantidad elementos")
                    if (itemRepository.cantidadGuardada() == 0) {
                        _estado.value = UiState.Vacio
                    }
                }
                .onFailure { error ->
                    if (itemRepository.cantidadGuardada() == 0) {
                        _estado.value = UiState.Error(
                            mensajeDe(error)
                        )
                    } else {
                        _aviso.value = "Sin conexión: mostrando datos guardados"
                    }
                }
        }
    }

    fun alternarFavorito(id: String) {
        viewModelScope.launch {
            itemRepository.alternarFavorito(id)
        }
    }

    private fun mensajeDe(error: Throwable): String = when (error) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${error.code()}"
        else -> "Ocurrió un error inesperado"
    }

    class Factory(
        private val itemRepository: ItemRepository,
        private val notificador: Notificador
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(ListaViewModel::class.java))
            return ListaViewModel(itemRepository, notificador) as T
        }
    }
}
