package com.tecsup.rockvault.ui.detalle

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

class DetalleViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<Item>>(UiState.Vacio)
    val estado: StateFlow<UiState<Item>> = _estado.asStateFlow()

    fun cargar(id: String) {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            itemRepository.obtenerDetalle(id)
                .onSuccess { item -> _estado.value = UiState.Exito(item) }
                .onFailure { error ->
                    _estado.value = UiState.Error(
                        error.message ?: "No se pudo cargar el detalle"
                    )
                }
        }
    }

    class Factory(
        private val itemRepository: ItemRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(DetalleViewModel::class.java))
            return DetalleViewModel(itemRepository) as T
        }
    }
}
