package com.quispe.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.avisoscampus.data.AvisosRepository
import com.quispe.avisoscampus.data.AvisosRepositoryMemoria
import com.quispe.avisoscampus.model.Aviso
import com.quispe.avisoscampus.model.Origen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

enum class PermisoUi(val texto: String) {
    SinPedir("Sin pedir"),
    Concedido("Permitido"),
    Denegado("Denegado")
}

class AvisosViewModel(
    private val repository: AvisosRepository = AvisosRepositoryMemoria
) : ViewModel() {
    private val _permiso = MutableStateFlow(PermisoUi.SinPedir)
    val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()
    private val _eventos = MutableSharedFlow<Aviso>(extraBufferCapacity = 1)
    val eventos = _eventos.asSharedFlow()
    private val siguienteId = AtomicInteger(1)
    private var trabajoRecordatorio: Job? = null
    private val _faltan = MutableStateFlow<Int?>(null)
    val faltan: StateFlow<Int?> = _faltan.asStateFlow()
    private val _escuchandoAvion = MutableStateFlow(false)
    val escuchandoAvion: StateFlow<Boolean> = _escuchandoAvion.asStateFlow()

    fun permisoRespondido(concedido: Boolean) {
        _permiso.value = if (concedido) PermisoUi.Concedido else PermisoUi.Denegado
    }

    fun avisarAhora() {
        viewModelScope.launch {
            val aviso = Aviso(
                id = siguienteId.getAndIncrement(),
                titulo = "Tarea lista",
                texto = "Tu entrega fue registrada",
                origen = Origen.BOTON
            )
            repository.registrar(aviso)
            _eventos.emit(aviso)
        }
    }

    fun recordarEn(segundos: Int = 10) {
        if (trabajoRecordatorio?.isActive == true || segundos <= 0) return

        trabajoRecordatorio = viewModelScope.launch {
            try {
                for (segundo in segundos downTo 1) {
                    _faltan.value = segundo
                    delay(1_000)
                }

                val aviso = Aviso(
                    id = siguienteId.getAndIncrement(),
                    titulo = "Recordatorio",
                    texto = "Revisa tu laboratorio pendiente",
                    origen = Origen.RECORDATORIO
                )
                repository.registrar(aviso)
                _eventos.emit(aviso)
            } finally {
                _faltan.value = null
            }
        }
    }

    fun cancelarRecordatorio() {
        trabajoRecordatorio?.cancel()
    }

    fun cambiarEscucha(valor: Boolean) {
        _escuchandoAvion.value = valor
    }

    fun alCambiarModoAvion(activo: Boolean) {
        viewModelScope.launch {
            val aviso = Aviso(
                id = siguienteId.getAndIncrement(),
                titulo = "Modo avión",
                texto = if (activo) "Activado" else "Desactivado",
                origen = Origen.MODO_AVION
            )
            repository.registrar(aviso)
            _eventos.emit(aviso)
        }
    }
}
