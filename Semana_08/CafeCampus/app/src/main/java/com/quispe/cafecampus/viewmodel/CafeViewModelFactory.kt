package com.quispe.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quispe.cafecampus.data.prefs.PreferenciasStore
import com.quispe.cafecampus.data.repository.CafeRepository

class CafeViewModelFactory(
    private val repository: CafeRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(SesionViewModel::class.java) -> {
                SesionViewModel(repository, preferenciasStore) as T
            }

            modelClass.isAssignableFrom(MenuViewModel::class.java) -> {
                MenuViewModel(repository) as T
            }

            modelClass.isAssignableFrom(PedidosClienteViewModel::class.java) -> {
                PedidosClienteViewModel(repository) as T
            }

            modelClass.isAssignableFrom(PanelAdminViewModel::class.java) -> {
                PanelAdminViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MenuAdminViewModel::class.java) -> {
                MenuAdminViewModel(repository) as T
            }

            modelClass.isAssignableFrom(PedidosAdminViewModel::class.java) -> {
                PedidosAdminViewModel(repository) as T
            }

            modelClass.isAssignableFrom(RegistroViewModel::class.java) -> {
                RegistroViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ReporteAdminViewModel::class.java) -> {
                ReporteAdminViewModel(repository) as T
            }

            else -> throw IllegalArgumentException(
                "ViewModel desconocido: ${modelClass.name}"
            )
        }
    }
}