
package com.quispe.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.cafecampus.data.prefs.PreferenciasStore
import com.quispe.cafecampus.data.repository.CafeRepository
import com.quispe.cafecampus.data.local.entity.Usuario
import com.quispe.cafecampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repository: CafeRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = preferenciasStore.modoOscuro
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        viewModelScope.launch {
            _error.value = null

            if (correo.isBlank() || clave.isBlank()) {
                _error.value = "Completa todos los campos."
                return@launch
            }

            try {
                val usuario = repository.login(correo.trim(), clave)

                if (usuario == null) {
                    _error.value = "Correo o contraseña incorrectos"
                    return@launch
                }

                if (usuario.rol != rolSeleccionado.name) {
                    _error.value = "El rol seleccionado no corresponde a este usuario."
                    return@launch
                }

                preferenciasStore.guardarPerfil(rolSeleccionado)
                _usuarioActual.value = usuario
            } catch (e: Exception) {
                _error.value = "No se pudo iniciar sesión. Inténtalo nuevamente."
            }
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            preferenciasStore.cerrarSesion()
            _usuarioActual.value = null
            _error.value = null
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}