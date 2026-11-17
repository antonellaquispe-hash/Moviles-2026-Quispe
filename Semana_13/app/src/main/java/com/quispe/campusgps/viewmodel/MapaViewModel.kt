package com.quispe.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.campusgps.data.LugaresRepository
import com.quispe.campusgps.data.UbicacionRepository
import com.quispe.campusgps.model.LugarConDistancia
import com.quispe.campusgps.model.Posicion
import com.quispe.campusgps.model.UbicacionUi
import com.quispe.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepository: UbicacionRepository,
    lugaresRepository: LugaresRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepository.obtenerLugares().map { LugarConDistancia(it, null) }
    )
    val lugares = _lugares.asStateFlow()

    fun buscar() {
        if (_estado.value == UbicacionUi.Buscando) return

        viewModelScope.launch {
            _estado.value = UbicacionUi.Buscando
            ubicacionRepository.obtenerPosicion()
                .onSuccess { posicion ->
                    actualizarDistancias(posicion)
                    _estado.value = UbicacionUi.Lista(posicion)
                }
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

    private fun actualizarDistancias(posicion: Posicion) {
        _lugares.value = _lugares.value
            .map { item ->
                item.copy(metros = distanciaMetros(posicion, item.lugar))
            }
            .sortedBy { it.metros }
    }
}
