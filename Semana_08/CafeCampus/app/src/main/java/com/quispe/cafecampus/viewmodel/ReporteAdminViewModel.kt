
package com.quispe.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.cafecampus.data.repository.CafeRepository
import com.quispe.cafecampus.model.ReporteDelDia
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

/**
 * Reporte del dia: producto mas vendido y total por forma de pago.
 * Los datos provienen de consultas Room a traves del Repository.
 */
class ReporteAdminViewModel(
    repository: CafeRepository
) : ViewModel() {

    val reporte: StateFlow<ReporteDelDia?> = repository
        .observarReporteDelDia(inicioDelDia())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private fun inicioDelDia(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}
