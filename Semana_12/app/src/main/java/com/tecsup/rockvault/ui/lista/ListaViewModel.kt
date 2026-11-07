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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ListaViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            itemRepository.refrescar()
                .onSuccess {
                    val items = itemRepository.items.first()
                    _estado.value = if (items.isEmpty()) {
                        UiState.Vacio
                    } else {
                        UiState.Exito(items)
                    }
                }
                .onFailure { error ->
                    _estado.value = UiState.Error(
                        error.message ?: "No se pudo cargar la lista"
                    )
                }
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
