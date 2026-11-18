package com.quispe.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import com.quispe.centrotareas.data.TareasRepository
import com.quispe.centrotareas.model.CargaUi
import com.quispe.centrotareas.model.PrimosUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso = _progreso.asStateFlow()

    private val _corriendo = MutableStateFlow(false)
    val corriendo = _corriendo.asStateFlow()

    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos = _primos.asStateFlow()

    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga = _carga.asStateFlow()

    private val _simularError = MutableStateFlow(false)
    val simularError = _simularError.asStateFlow()

    // Paso 1: se ejecuta deliberadamente en el hilo principal para observar el bloqueo.
    fun tareaLargaMala() {
        _mensaje.value = "Trabajando..."
        Thread.sleep(4_000)
        _mensaje.value = "Terminó"
    }
}
