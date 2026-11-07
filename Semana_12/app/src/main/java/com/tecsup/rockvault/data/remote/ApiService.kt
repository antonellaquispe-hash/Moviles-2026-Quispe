package com.tecsup.rockvault.data.remote

import com.tecsup.rockvault.data.remote.dto.RespuestaItunesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun buscarAlbumes(
        @Query("term") termino: String,
        @Query("entity") entidad: String = "album",
        @Query("limit") limite: Int = 25,
        @Query("country") pais: String = "US"
    ): RespuestaItunesDto
}
