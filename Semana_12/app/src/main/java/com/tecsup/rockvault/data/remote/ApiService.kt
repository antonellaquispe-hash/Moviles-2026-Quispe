package com.tecsup.rockvault.data.remote

import com.tecsup.rockvault.data.remote.dto.ItemDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("articles")
    suspend fun listarPublicaciones(
        @Query("per_page") cantidad: Int = 30,
        @Query("top") dias: Int = 30
    ): List<ItemDto>

    @GET("articles/{id}")
    suspend fun obtenerPublicacion(@Path("id") id: String): ItemDto
}
