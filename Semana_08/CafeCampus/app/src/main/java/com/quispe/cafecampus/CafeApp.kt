
package com.quispe.cafecampus

import android.app.Application
import com.quispe.cafecampus.data.local.CafeDatabase
import com.quispe.cafecampus.data.local.DatosIniciales
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CafeApp : Application() {

    val database: CafeDatabase by lazy {
        CafeDatabase.obtenerInstancia(this)
    }

    private val applicationScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        applicationScope.launch {
            DatosIniciales.insertarSiVacio(database)
        }
    }
}