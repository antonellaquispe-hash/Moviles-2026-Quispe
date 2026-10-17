package com.quispe.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quispe.cafecampus.data.repository.CafeRepository

class CafeViewModelFactory(
    private val repository: CafeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> {
                MenuViewModel(repository) as T
            }

            modelClass.isAssignableFrom(PedidosClienteViewModel::class.java) -> {
                PedidosClienteViewModel(repository) as T
            }

            else -> throw IllegalArgumentException(
                "ViewModel desconocido: ${modelClass.name}"
            )
        }
    }
}