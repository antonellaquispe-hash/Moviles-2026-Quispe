package com.tecsup.rockvault

import android.content.Context
import com.tecsup.rockvault.data.auth.AuthRepository
import com.tecsup.rockvault.data.auth.FirebaseAuthRepository
import com.tecsup.rockvault.data.local.AppDatabase
import com.tecsup.rockvault.data.remote.RetrofitClient
import com.tecsup.rockvault.data.repository.ItemRepository
import com.tecsup.rockvault.data.repository.ItemRepositoryImpl
import com.tecsup.rockvault.notification.Notificador
import com.tecsup.rockvault.notification.NotificadorAndroid

object AppContainer {
    val authRepository: AuthRepository by lazy { FirebaseAuthRepository() }

    @Volatile
    private var repository: ItemRepository? = null

    fun database(context: Context): AppDatabase = AppDatabase.getInstance(context)

    fun notificador(context: Context): Notificador = NotificadorAndroid(context.applicationContext)

    fun itemRepository(context: Context): ItemRepository = repository ?: synchronized(this) {
        repository ?: ItemRepositoryImpl(
            apiService = RetrofitClient.apiService,
            itemDao = database(context).itemDao()
        ).also { repository = it }
    }
}
