package com.tecsup.rockvault.data.remote

import com.tecsup.rockvault.data.remote.dto.ItemDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("posts")
    suspend fun listarPublicaciones(): List<ItemDto>

    @GET("posts/{id}")
    suspend fun obtenerPublicacion(@Path("id") id: String): ItemDto
}
