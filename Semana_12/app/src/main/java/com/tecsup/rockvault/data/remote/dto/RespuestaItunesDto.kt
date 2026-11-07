package com.tecsup.rockvault.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RespuestaItunesDto(
    @SerialName("resultCount") val resultCount: Int = 0,
    @SerialName("results") val results: List<ItemDto> = emptyList()
)
