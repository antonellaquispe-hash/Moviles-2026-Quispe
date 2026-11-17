package com.quispe.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.campusgps.data.LugaresRepository
import com.quispe.campusgps.data.UbicacionRepository
import com.quispe.campusgps.model.Lugar
import com.quispe.campusgps.model.LugarConDistancia
import com.quispe.campusgps.model.Posicion
import com.quispe.campusgps.model.TipoError
import com.quispe.campusgps.model.UbicacionUi
import com.quispe.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    val masCercano: StateFlow<Lugar?> = lugares
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado = _seleccionado.asStateFlow()

    fun buscar() {
        if (_estado.value == UbicacionUi.Buscando) return

        viewModelScope.launch {
            _estado.value = UbicacionUi.Buscando
            if (!ubicacionRepository.playServicesDisponible()) {
                _estado.value = UbicacionUi.Error(
                    mensaje = "Este dispositivo no tiene Google Play Services.",
                    tipo = TipoError.SIN_PLAY_SERVICES
                )
                return@launch
            }

            ubicacionRepository.obtenerPosicion()
                .onSuccess { posicion ->
                    actualizarDistancias(posicion)
                    _estado.value = UbicacionUi.Lista(posicion)
                }
                .onFailure { error ->
                    _estado.value = when (error) {
                        is SecurityException -> UbicacionUi.Error(
                            "Sin permiso de ubicación no puedo mostrar dónde estás.",
                            TipoError.SIN_PERMISO
                        )
                        is IllegalStateException -> UbicacionUi.Error(
                            "Activa el GPS e intenta de nuevo.",
                            TipoError.GPS_APAGADO
                        )
                        else -> UbicacionUi.Error(
                            error.message ?: "No se pudo obtener la ubicación.",
                            TipoError.OTRO
                        )
                    }
                }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás.",
            TipoError.SIN_PERMISO
        )
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    private fun actualizarDistancias(posicion: Posicion) {
        _lugares.value = _lugares.value
            .map { item ->
                item.copy(metros = distanciaMetros(posicion, item.lugar))
            }
            .sortedBy { it.metros }
    }
}
