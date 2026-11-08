package com.tecsup.rockvault.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.rockvault.domain.model.Rol
import com.tecsup.rockvault.domain.model.Sesion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {
    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> {
        return try {
            val usuario = auth.signInWithEmailAndPassword(correo, clave).await().user
                ?: return Result.failure(IllegalStateException("No se pudo iniciar la sesión"))
            Result.success(leerSesion(usuario.uid, usuario.email ?: correo))
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    override suspend fun sesionActual(): Sesion? {
        val usuario = auth.currentUser ?: return null
        return try {
            leerSesion(usuario.uid, usuario.email.orEmpty())
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            Sesion(usuario.uid, usuario.email.orEmpty(), Rol.USUARIO)
        }
    }

    override fun cerrarSesion() {
        auth.signOut()
    }

    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val documento = firestore.collection("usuarios").document(uid).get().await()
        val rol = if (documento.getString("rol") == "admin") Rol.ADMIN else Rol.USUARIO
        return Sesion(uid, correo, rol)
    }
}
