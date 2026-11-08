package com.tecsup.rockvault.data.mapper

import com.tecsup.rockvault.data.local.ItemEntity
import com.tecsup.rockvault.data.remote.dto.ItemDto
import com.tecsup.rockvault.domain.model.Item

fun ItemDto.toDomain(): Item? {
    val id = collectionId ?: return null
    val titulo = collectionName?.takeIf { it.isNotBlank() } ?: return null
    return Item(
        id = id.toString(),
        titulo = titulo,
        subtitulo = artistName,
        imagenUrl = artworkUrl
    )
}

fun ItemEntity.toDomain(): Item = Item(
    id = id,
    titulo = titulo,
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito
)

fun Item.toEntity(): ItemEntity = ItemEntity(
    id = id,
    titulo = titulo,
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito
)
