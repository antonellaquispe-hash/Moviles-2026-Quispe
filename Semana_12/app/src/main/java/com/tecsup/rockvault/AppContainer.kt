package com.tecsup.rockvault

import android.content.Context
import com.tecsup.rockvault.data.local.AppDatabase
import com.tecsup.rockvault.data.remote.RetrofitClient
import com.tecsup.rockvault.data.repository.ItemRepository
import com.tecsup.rockvault.data.repository.ItemRepositoryImpl

object AppContainer {
    @Volatile
    private var repository: ItemRepository? = null

    fun database(context: Context): AppDatabase = AppDatabase.getInstance(context)

    fun itemRepository(context: Context): ItemRepository = repository ?: synchronized(this) {
        repository ?: ItemRepositoryImpl(
            apiService = RetrofitClient.apiService,
            itemDao = database(context).itemDao()
        ).also { repository = it }
    }
}
