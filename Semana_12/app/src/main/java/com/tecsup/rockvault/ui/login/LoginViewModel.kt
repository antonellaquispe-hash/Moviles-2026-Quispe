package com.tecsup.rockvault.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.FirebaseNetworkException
import com.tecsup.rockvault.data.auth.AuthRepository
import com.tecsup.rockvault.domain.model.Sesion
import com.tecsup.rockvault.ui.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<Sesion>>(UiState.Cargando)
    val estado: StateFlow<UiState<Sesion>> = _estado.asStateFlow()

    init {
        verificarSesion()
    }

    fun verificarSesion() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            _estado.value = try {
                authRepository.sesionActual()?.let { UiState.Exito(it) } ?: UiState.Vacio
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                UiState.Error("No se pudo verificar la sesión. Comprueba tu conexión.")
            }
        }
    }

    fun iniciarSesion(correo: String, clave: String) {
        val correoLimpio = correo.trim()
        if (correoLimpio.isEmpty() || clave.isEmpty()) {
            _estado.value = UiState.Error("Ingresa tu correo y contraseña")
            return
        }
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            authRepository.iniciarSesion(correoLimpio, clave)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun cerrarSesion() {
        authRepository.cerrarSesion()
        _estado.value = UiState.Vacio
    }

    private fun mensajeDe(error: Throwable): String = when (error) {
        is FirebaseAuthInvalidCredentialsException, is FirebaseAuthInvalidUserException ->
            "Correo o contraseña incorrectos"
        is FirebaseNetworkException -> "No hay conexión a internet"
        else -> "No se pudo iniciar sesión. Inténtalo de nuevo."
    }

    class Factory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(LoginViewModel::class.java))
            return LoginViewModel(authRepository) as T
        }
    }
}
