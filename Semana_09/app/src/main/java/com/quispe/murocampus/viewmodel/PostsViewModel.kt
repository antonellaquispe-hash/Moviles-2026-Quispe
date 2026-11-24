package com.quispe.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.quispe.murocampus.data.PostRepository
import com.quispe.murocampus.model.Comentario
import com.quispe.murocampus.model.NuevoPost
import com.quispe.murocampus.model.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

fun mensajeDe(error: Throwable): String = when (error) {
    is IOException -> "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."
    is HttpException -> "Error del servidor (${error.code()})"
    is SerializationException -> "Respuesta inesperada"
    else -> "Error desconocido"
}

data class DetalleUi(
    val post: Post,
    val comentarios: List<Comentario>
)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista = _lista.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle = _detalle.asStateFlow()

    private val _publicacion = MutableStateFlow<UiState<Post>?>(null)
    val publicacion = _publicacion.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            val userId = _usuarioFiltro.value
            val resultado = if (userId == null) {
                repo.listar()
            } else {
                repo.porUsuario(userId)
            }
            resultado
                .onSuccess { posts -> _lista.value = UiState.Exito(posts) }
                .onFailure { error -> _lista.value = UiState.Error(mensajeDe(error)) }
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun publicar(titulo: String, texto: String) {
        if (titulo.isBlank() || texto.isBlank()) return

        viewModelScope.launch {
            _publicacion.value = UiState.Cargando
            repo.crear(
                NuevoPost(
                    userId = 1,
                    title = titulo,
                    body = texto
                )
            ).onSuccess { post ->
                _publicacion.value = UiState.Exito(post)
            }.onFailure { error ->
                _publicacion.value = UiState.Error(mensajeDe(error))
            }
        }
    }

    fun limpiarResultadoPublicacion() {
        _publicacion.value = null
    }

    fun abrir(id: Int) {
        viewModelScope.launch {
            _detalle.value = UiState.Cargando
            coroutineScope {
                val postPendiente = async { repo.detalle(id) }
                val comentariosPendientes = async { repo.comentarios(id) }
                val resultadoPost = postPendiente.await()
                val resultadoComentarios = comentariosPendientes.await()

                resultadoPost.fold(
                    onSuccess = { post ->
                        resultadoComentarios.fold(
                            onSuccess = { comentarios ->
                                _detalle.value = UiState.Exito(DetalleUi(post, comentarios))
                            },
                            onFailure = { error ->
                                _detalle.value = UiState.Error(mensajeDe(error))
                            }
                        )
                    },
                    onFailure = { error ->
                        _detalle.value = UiState.Error(mensajeDe(error))
                    }
                )
            }
        }
    }
}

class PostsViewModelFactory(private val repo: PostRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(PostsViewModel::class.java)) {
            throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
        return PostsViewModel(repo) as T
    }
}
