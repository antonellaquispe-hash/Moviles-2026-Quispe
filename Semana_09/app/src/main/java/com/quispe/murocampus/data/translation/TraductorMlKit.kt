package com.quispe.murocampus.data.translation

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import java.util.concurrent.ConcurrentHashMap

class TraductorMlKit : TraductorContenido {
    private val cache = ConcurrentHashMap<String, String>()

    override suspend fun traducir(textos: List<String>): List<String> {
        if (textos.isEmpty()) return emptyList()

        val pendientes = textos
            .asSequence()
            .filter(String::isNotBlank)
            .distinct()
            .filterNot(cache::containsKey)
            .toList()

        if (pendientes.isNotEmpty()) {
            val opciones = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.SPANISH)
                .build()
            val traductor = Translation.getClient(opciones)

            try {
                traductor.downloadModelIfNeeded(
                    DownloadConditions.Builder().requireWifi().build()
                ).await()

                for (texto in pendientes) {
                    cache[texto] = traductor.translate(texto).await()
                }
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (error: Exception) {
                throw ErrorDeTraduccion(error)
            } finally {
                traductor.close()
            }
        }

        return textos.map { texto ->
            if (texto.isBlank()) texto else cache[texto] ?: texto
        }
    }
}
