package com.quispe.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.campusgps.data.UbicacionRepository
import com.quispe.campusgps.model.UbicacionUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepository: UbicacionRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado = _estado.asStateFlow()

    fun buscar() {
        if (_estado.value == UbicacionUi.Buscando) return

        viewModelScope.launch {
            _estado.value = UbicacionUi.Buscando
            ubicacionRepository.obtenerPosicion()
                .onSuccess { posicion -> _estado.value = UbicacionUi.Lista(posicion) }
                .onFailure { error ->
                    _estado.value = UbicacionUi.Error(
                        error.message ?: "No se pudo obtener la ubicación."
                    )
                }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Necesitas conceder el permiso de ubicación para continuar."
        )
    }
}
