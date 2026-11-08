package com.tecsup.rockvault.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("userId") val userId: Int? = null,
    @SerialName("title") val title: String = "",
    @SerialName("body") val body: String = ""
)
