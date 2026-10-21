
package com.quispe.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.cafecampus.data.local.entity.Usuario
import com.quispe.cafecampus.data.prefs.PreferenciasStore
import com.quispe.cafecampus.data.repository.CafeRepository
import com.quispe.cafecampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    val recibirPedidos: StateFlow<Boolean> = preferenciasStore.recibirPedidos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = true
        )

    private val _perfilResuelto = MutableStateFlow(false)
    val perfilResuelto: StateFlow<Boolean> = _perfilResuelto.asStateFlow()

    init {
        viewModelScope.launch {
            preferenciasStore.perfilActivo.first()
            _perfilResuelto.value = true
        }
    }

    val perfilActivo: StateFlow<String> = preferenciasStore.perfilActivo
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ""
        )

    val perfilGuardado: StateFlow<Rol?> = perfilActivo
        .map { valor ->
            runCatching { Rol.valueOf(valor) }.getOrNull()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun cambiarModoOscuro(activado: Boolean) {
        viewModelScope.launch {
            preferenciasStore.guardarModoOscuro(activado)
        }
    }

    fun cambiarRecepcionPedidos(activado: Boolean) {
        viewModelScope.launch {
            preferenciasStore.guardarRecibirPedidos(activado)
        }
    }

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
                    _error.value =
                        "El rol seleccionado no corresponde a este usuario."
                    return@launch
                }

                preferenciasStore.guardarPerfil(rolSeleccionado)
                _usuarioActual.value = usuario
            } catch (e: Exception) {
                _error.value =
                    "No se pudo iniciar sesión. Inténtalo nuevamente."
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

    fun cambiarPerfil(rol: Rol) {
        viewModelScope.launch {
            preferenciasStore.guardarPerfil(rol)
            _usuarioActual.value = null
            _error.value = null
        }
    }

    fun cargarPerfilGuardado() {
        viewModelScope.launch {
            val perfil = preferenciasStore.perfilActivo.first()
            if (perfil.isNotBlank()) {
                val usuario = runCatching {
                    repository.login(
                        if (perfil == Rol.ADMIN.name) {
                            "admin@cafe.test"
                        } else {
                            "juan@campus.test"
                        },
                        if (perfil == Rol.ADMIN.name) "admin" else "1234"
                    )
                }.getOrNull()

                _usuarioActual.value = usuario
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}