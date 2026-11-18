package com.quispe.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.centrotareas.data.TareasRepository
import com.quispe.centrotareas.model.CargaUi
import com.quispe.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {
    private var trabajo: Job? = null
    private var trabajoPrimos: Job? = null

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

    fun tareaLarga() {
        if (_corriendo.value) return
        _corriendo.value = true
        trabajo = viewModelScope.launch {
            try {
                _progreso.value = 0f
                _mensaje.value = "Trabajando..."
                Log.d("HILO", "Antes: ${Thread.currentThread().name}")
                repo.tareaConProgreso().collect { avance ->
                    _progreso.value = avance
                }
                Log.d("HILO", "Después: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
            } finally {
                _corriendo.value = false
            }
        }
    }

    fun cancelar() {
        if (!_corriendo.value) return
        trabajo?.cancel()
        _mensaje.value = "Cancelado"
    }

    fun contar(hasta: Int) {
        if (hasta < 2 || _primos.value == PrimosUi.Calculando) return
        trabajoPrimos = viewModelScope.launch {
            _primos.value = PrimosUi.Calculando
            val inicio = System.currentTimeMillis()
            try {
                val cantidad = repo.contarPrimos(hasta)
                val milisegundos = System.currentTimeMillis() - inicio
                _primos.value = PrimosUi.Listo(cantidad, milisegundos)
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }

    fun cargarUnaPorUna() {
        ejecutarCarga("Una por una", enParalelo = false)
    }

    fun cargarALaVez() {
        ejecutarCarga("A la vez", enParalelo = true)
    }

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    private fun ejecutarCarga(modo: String, enParalelo: Boolean) {
        if (_carga.value == CargaUi.Cargando) return
        _carga.value = CargaUi.Cargando
        val simularError = _simularError.value
        viewModelScope.launch {
            try {
                val inicio = System.currentTimeMillis()
                val (perfil, pedidos) = if (enParalelo) {
                    coroutineScope {
                        val perfilDeferred = async { repo.cargarPerfil() }
                        val pedidosDeferred = async {
                            repo.cargarPedidos(simularError)
                        }
                        perfilDeferred.await() to pedidosDeferred.await()
                    }
                } else {
                    val perfil = repo.cargarPerfil()
                    val pedidos = repo.cargarPedidos(simularError)
                    perfil to pedidos
                }
                _carga.value = CargaUi.Exito(
                    modo = modo,
                    perfil = perfil,
                    pedidos = pedidos.size,
                    milisegundos = System.currentTimeMillis() - inicio
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _carga.value = CargaUi.Error(
                    "No se pudo cargar: ${e.message ?: "Error desconocido"}"
                )
            }
        }
    }
}
