package com.tecsup.rockvault

import com.tecsup.rockvault.data.remote.RetrofitClient
import com.tecsup.rockvault.data.repository.ItemRepository
import com.tecsup.rockvault.data.repository.ItemRepositoryImpl

object AppContainer {
    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(RetrofitClient.apiService)
    }
}
