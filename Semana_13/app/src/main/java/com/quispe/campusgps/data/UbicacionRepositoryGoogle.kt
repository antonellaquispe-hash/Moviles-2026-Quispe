package com.quispe.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.quispe.campusgps.model.Posicion
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(context: Context) : UbicacionRepository {
    private val appContext = context.applicationContext
    private val clienteUbicacion = LocationServices.getFusedLocationProviderClient(context)

    override fun playServicesDisponible(): Boolean =
        GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(appContext) == ConnectionResult.SUCCESS

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = runCatching {
        val tokenCancelacion = CancellationTokenSource()
        val ubicacion = clienteUbicacion.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            tokenCancelacion.token
        ).await() ?: error("No se pudo obtener la ubicación. Intenta de nuevo.")

        Posicion(
            latitud = ubicacion.latitude,
            longitud = ubicacion.longitude
        )
    }
}
