package com.quispe.murocampus.data.translation

interface TraductorContenido {
    suspend fun traducir(textos: List<String>): List<String>
}

class ErrorDeTraduccion(causa: Throwable) :
    Exception("No se pudo traducir el contenido.", causa)
