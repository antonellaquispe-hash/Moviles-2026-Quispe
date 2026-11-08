package com.tecsup.rockvault.data.mapper

import com.tecsup.rockvault.data.local.ItemEntity
import com.tecsup.rockvault.data.remote.dto.ItemDto
import com.tecsup.rockvault.domain.model.Item

fun ItemDto.toDomain(): Item? {
    val id = id ?: return null
    val titulo = title.takeIf { it.isNotBlank() } ?: return null
    val contenido = bodyHtml?.takeIf { it.isNotBlank() }
        ?: bodyMarkdown?.takeIf { it.isNotBlank() }
        ?: description
    val autor = author?.name?.takeIf { it.isNotBlank() }
    val resumen = if (autor != null && contenido == description) "$autor · $description" else contenido
    return Item(
        id = id.toString(),
        titulo = titulo,
        subtitulo = resumen,
        imagenUrl = coverImage ?: author?.profileImage,
        idioma = language
    )
}

fun ItemEntity.toDomain(): Item = Item(
    id = id,
    titulo = titulo,
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito,
    idioma = idioma
)

fun Item.toEntity(): ItemEntity = ItemEntity(
    id = id,
    titulo = titulo,
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito,
    idioma = idioma
)
