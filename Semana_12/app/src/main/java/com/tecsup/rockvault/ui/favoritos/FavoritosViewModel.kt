package com.tecsup.rockvault.ui.favoritos

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

class FavoritosViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            itemRepository.favoritos.collect { items ->
                _estado.value = if (items.isEmpty()) {
                    UiState.Vacio
                } else {
                    UiState.Exito(items)
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
            require(modelClass.isAssignableFrom(FavoritosViewModel::class.java))
            return FavoritosViewModel(itemRepository) as T
        }
    }
}
