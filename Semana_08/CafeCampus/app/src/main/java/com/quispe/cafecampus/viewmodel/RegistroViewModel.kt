
package com.quispe.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.cafecampus.data.local.entity.Usuario
import com.quispe.cafecampus.data.repository.CafeRepository
import com.quispe.cafecampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Registro de estudiante. Valida el formulario y delega la escritura al
 * Repository (nunca al DAO).
 */
class RegistroViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _registrado = MutableStateFlow(false)
    val registrado: StateFlow<Boolean> = _registrado.asStateFlow()

    fun registrar(
        nombre: String,
        correo: String,
        clave: String,
        confirmacion: String
    ) {
        val nombreLimpio = nombre.trim()
        val correoLimpio = correo.trim()

        if (nombreLimpio.isBlank()) {
            _error.value = "Ingresa tu nombre."
            return
        }

        if (correoLimpio.isBlank()) {
            _error.value = "Ingresa tu correo."
            return
        }

        if (!formatoCorreoValido(correoLimpio)) {
            _error.value = "El correo no tiene un formato válido."
            return
        }

        if (clave.isBlank()) {
            _error.value = "Ingresa una contraseña."
            return
        }

        if (clave.length < 4) {
            _error.value = "La contraseña debe tener al menos 4 caracteres."
            return
        }

        if (clave != confirmacion) {
            _error.value = "Las contraseñas no coinciden."
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            try {
                if (repository.existeUsuarioPorCorreo(correoLimpio)) {
                    _error.value = "Ese correo ya está registrado."
                    return@launch
                }

                repository.crearUsuario(
                    Usuario(
                        nombre = nombreLimpio,
                        correo = correoLimpio,
                        clave = clave,
                        rol = Rol.CLIENTE.name
                    )
                )

                _registrado.value = true
            } catch (excepcion: Exception) {
                _error.value = excepcion.message
                    ?: "No se pudo crear la cuenta."
            } finally {
                _cargando.value = false
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    private fun formatoCorreoValido(correo: String): Boolean {
        return REGEX_CORREO.matches(correo)
    }

    companion object {
        private val REGEX_CORREO = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
