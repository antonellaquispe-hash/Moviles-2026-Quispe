package com.quispe.murocampus.data

import com.quispe.murocampus.data.remote.PostApi
import com.quispe.murocampus.data.translation.TraductorContenido
import com.quispe.murocampus.model.Comentario
import com.quispe.murocampus.model.NuevoPost
import com.quispe.murocampus.model.Post
import kotlinx.coroutines.CancellationException

interface PostRepository {
    suspend fun listar(): Result<List<Post>>

    suspend fun detalle(id: Int): Result<Post>

    suspend fun comentarios(id: Int): Result<List<Comentario>>

    suspend fun porUsuario(userId: Int): Result<List<Post>>

    suspend fun crear(nuevo: NuevoPost): Result<Post>
}

class PostRepositoryRemote(
    private val api: PostApi,
    private val traductor: TraductorContenido
) : PostRepository {
    override suspend fun listar(): Result<List<Post>> =
        llamar { api.listar().traducirPublicaciones() }

    override suspend fun detalle(id: Int): Result<Post> =
        llamar { api.detalle(id).traducirAlEspanol() }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> =
        llamar { api.comentarios(id).traducirComentarios() }

    override suspend fun porUsuario(userId: Int): Result<List<Post>> =
        llamar { api.porUsuario(userId).traducirPublicaciones() }

    override suspend fun crear(nuevo: NuevoPost): Result<Post> =
        llamar { api.crear(nuevo) }

    private suspend fun List<Post>.traducirPublicaciones(): List<Post> {
        val textos = flatMap { listOf(it.title, it.body) }
        val traducidos = traductor.traducir(textos)
        require(traducidos.size == textos.size) {
            "El traductor devolvió una cantidad distinta de textos."
        }

        return mapIndexed { index, post ->
            post.copy(title = traducidos[index * 2], body = traducidos[index * 2 + 1])
        }
    }

    private suspend fun Post.traducirAlEspanol(): Post {
        val traducidos = traductor.traducir(listOf(title, body))
        require(traducidos.size == 2) {
            "El traductor devolvió una cantidad distinta de textos."
        }
        return copy(title = traducidos[0], body = traducidos[1])
    }

    private suspend fun List<Comentario>.traducirComentarios(): List<Comentario> {
        val traducidos = traductor.traducir(map(Comentario::body))
        require(traducidos.size == size) {
            "El traductor devolvió una cantidad distinta de textos."
        }
        return zip(traducidos) { comentario, texto -> comentario.copy(body = texto) }
    }

    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}
