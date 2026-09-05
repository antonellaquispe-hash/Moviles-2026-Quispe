package com.quispe.registronotas.logic

import com.quispe.registronotas.model.Curso
import kotlin.math.roundToInt

class CalculadoraPromedio {

    fun calcular(cursos: List<Curso>): Double {
        return cursos.sumOf {
            it.nota.toDouble() * (it.peso / 100.0)
        }
    }

    fun redondear(promedio: Double): Int {
        return promedio.roundToInt()
    }

    fun obtenerObservacion(promedio: Double): String {
        return when {
            promedio >= 17 -> "EXCELENTE"
            promedio >= 13 -> "APROBADO"
            promedio >= 10 -> "EN RECUPERACIÓN"
            else -> "DESAPROBADO"
        }
    }
}