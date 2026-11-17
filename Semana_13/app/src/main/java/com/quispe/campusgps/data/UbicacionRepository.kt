package com.quispe.campusgps.data

import com.quispe.campusgps.model.Posicion

interface UbicacionRepository {
    fun playServicesDisponible(): Boolean
    suspend fun obtenerPosicion(): Result<Posicion>
}
