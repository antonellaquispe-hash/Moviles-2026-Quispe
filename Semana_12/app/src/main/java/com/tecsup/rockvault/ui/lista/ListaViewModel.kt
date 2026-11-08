package com.tecsup.rockvault.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.rockvault.data.repository.ItemRepository
import com.tecsup.rockvault.domain.model.Item
import com.tecsup.rockvault.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListaViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

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
                .onSuccess {
                    if (itemRepository.cantidadGuardada() == 0) {
                        _estado.value = UiState.Vacio
                    }
                }
                .onFailure { error ->
                    if (itemRepository.cantidadGuardada() == 0) {
                        _estado.value = UiState.Error(
                            error.message ?: "No se pudo cargar la lista"
                        )
                    }
                }
        }
    }

    fun alternarFavorito(id: String) {
        viewModelScope.launch {
            itemRepository.alternarFavorito(id)
        }
    }

    class Factory(
        private val itemRepository: ItemRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(ListaViewModel::class.java))
            return ListaViewModel(itemRepository) as T
        }
    }
}
