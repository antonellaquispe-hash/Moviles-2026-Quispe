package com.tecsup.rockvault.ui.lista

import androidx.lifecycle.ViewModel
import com.tecsup.rockvault.domain.model.Item
import com.tecsup.rockvault.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListaViewModel : ViewModel() {
    private val elementosPrueba = listOf(
        Item(1, "Back in Black", "AC/DC · 1980"),
        Item(2, "The Dark Side of the Moon", "Pink Floyd · 1973"),
        Item(3, "Led Zeppelin IV", "Led Zeppelin · 1971"),
        Item(4, "A Night at the Opera", "Queen · 1975"),
        Item(5, "Appetite for Destruction", "Guns N' Roses · 1987")
    )

    private val _estado = MutableStateFlow<UiState<List<Item>>>(
        UiState.Exito(elementosPrueba)
    )
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    fun cargarDatosPrueba() {
        _estado.value = UiState.Exito(elementosPrueba)
    }
}
