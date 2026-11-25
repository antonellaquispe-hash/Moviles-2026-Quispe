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
}
