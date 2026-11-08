package com.tecsup.rockvault.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("title") val title: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("cover_image") val coverImage: String? = null,
    @SerialName("body_html") val bodyHtml: String? = null,
    @SerialName("body_markdown") val bodyMarkdown: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("user") val author: AuthorDto? = null
)

@Serializable
data class AuthorDto(
    @SerialName("name") val name: String = "",
    @SerialName("profile_image_90") val profileImage: String? = null
)
