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
}