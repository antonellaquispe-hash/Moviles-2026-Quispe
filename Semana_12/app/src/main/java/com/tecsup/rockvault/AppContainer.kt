package com.tecsup.rockvault

import android.content.Context
import com.tecsup.rockvault.data.local.AppDatabase
import com.tecsup.rockvault.data.remote.RetrofitClient
import com.tecsup.rockvault.data.repository.ItemRepository
import com.tecsup.rockvault.data.repository.ItemRepositoryImpl

object AppContainer {
    fun database(context: Context): AppDatabase = AppDatabase.getInstance(context)

    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(RetrofitClient.apiService)
    }
}
