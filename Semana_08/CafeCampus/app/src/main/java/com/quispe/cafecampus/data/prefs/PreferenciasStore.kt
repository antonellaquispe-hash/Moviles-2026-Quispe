
package com.quispe.cafecampus.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.quispe.cafecampus.model.Rol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "ajustes")

class PreferenciasStore(private val context: Context) {

    private object Keys {
        val perfilActivo = stringPreferencesKey("perfilActivo")
        val modoOscuro = booleanPreferencesKey("modoOscuro")
        val recibirPedidos = booleanPreferencesKey("recibirPedidos")
    }

    val perfilActivo: Flow<String> = context.dataStore.data.map { preferencias ->
        preferencias[Keys.perfilActivo] ?: ""
    }

    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { preferencias ->
        preferencias[Keys.modoOscuro] ?: false
    }

    val recibirPedidos: Flow<Boolean> = context.dataStore.data.map { preferencias ->
        preferencias[Keys.recibirPedidos] ?: true
    }

    suspend fun guardarPerfil(rol: Rol?) {
        context.dataStore.edit { preferencias ->
            preferencias[Keys.perfilActivo] = rol?.name ?: ""
        }
    }

    suspend fun guardarModoOscuro(b: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[Keys.modoOscuro] = b
        }
    }

    suspend fun guardarRecibirPedidos(b: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[Keys.recibirPedidos] = b
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { preferencias ->
            preferencias[Keys.perfilActivo] = ""
        }
    }
}