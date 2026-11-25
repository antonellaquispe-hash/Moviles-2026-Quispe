package com.quispe.avisoscampus.data

import com.quispe.avisoscampus.model.Aviso

interface AvisosRepository {
    fun registrar(aviso: Aviso)
    fun obtenerAvisos(): List<Aviso>
}

object AvisosRepositoryMemoria : AvisosRepository {
    private val avisos = mutableListOf<Aviso>()

    override fun registrar(aviso: Aviso) {
        avisos.add(0, aviso)
    }

    override fun obtenerAvisos(): List<Aviso> = avisos.toList()
}
