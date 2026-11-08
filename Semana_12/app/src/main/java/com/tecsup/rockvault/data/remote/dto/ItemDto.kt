package com.tecsup.rockvault.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    @SerialName("collectionId") val collectionId: Long? = null,
    @SerialName("collectionName") val collectionName: String? = null,
    @SerialName("artistName") val artistName: String = "",
    @SerialName("artworkUrl100") val artworkUrl: String? = null
)
