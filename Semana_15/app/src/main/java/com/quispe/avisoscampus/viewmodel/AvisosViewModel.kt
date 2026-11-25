package com.quispe.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class PermisoUi(val texto: String) {
    SinPedir("Sin pedir"),
    Concedido("Permitido"),
    Denegado("Denegado")
}

class AvisosViewModel : ViewModel() {
    private val _permiso = MutableStateFlow(PermisoUi.SinPedir)
    val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()

    fun permisoRespondido(concedido: Boolean) {
        _permiso.value = if (concedido) PermisoUi.Concedido else PermisoUi.Denegado
    }
}
