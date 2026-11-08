package com.tecsup.rockvault.ui.lista

import com.tecsup.rockvault.data.repository.ItemRepository
import com.tecsup.rockvault.domain.model.Item
import com.tecsup.rockvault.notification.Notificador
import com.tecsup.rockvault.ui.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun refrescarConDatosMuestraListaYNotifica() = runTest(mainDispatcherRule.dispatcher) {
        val repository = FakeItemRepository()
        val notificador = FakeNotificador()
        repository.resultadoRefresco = Result.success(listOf(itemRock))
        val viewModel = ListaViewModel(repository, notificador)

        advanceUntilIdle()

        assertEquals(UiState.Exito(listOf(itemRock)), viewModel.estado.value)
        assertEquals("Datos actualizados" to "Se guardaron 1 elementos", notificador.ultimaNotificacion)
    }

    @Test
    fun refrescarSinDatosYConErrorHttpMuestraCodigoDelServidor() = runTest(mainDispatcherRule.dispatcher) {
        val repository = FakeItemRepository().apply {
            resultadoRefresco = Result.failure(
                HttpException(Response.error<String>(503, "Error".toResponseBody()))
            )
        }
        val viewModel = ListaViewModel(repository, FakeNotificador())

        advanceUntilIdle()

        assertEquals(UiState.Error("El servidor respondió con error 503"), viewModel.estado.value)
        assertNull(viewModel.aviso.value)
    }

    @Test
    fun refrescarSinDatosYSinConexionMuestraMensajeDeRed() = runTest(mainDispatcherRule.dispatcher) {
        val repository = FakeItemRepository().apply {
            resultadoRefresco = Result.failure(java.io.IOException("network"))
        }
        val viewModel = ListaViewModel(repository, FakeNotificador())

        advanceUntilIdle()

        assertEquals(UiState.Error("No hay conexión a internet"), viewModel.estado.value)
    }

    @Test
    fun refrescarSinDatosYConErrorDesconocidoMuestraMensajeGenerico() = runTest(mainDispatcherRule.dispatcher) {
        val repository = FakeItemRepository().apply {
            resultadoRefresco = Result.failure(IllegalStateException("detalle interno"))
        }
        val viewModel = ListaViewModel(repository, FakeNotificador())

        advanceUntilIdle()

        assertEquals(UiState.Error("Ocurrió un error inesperado"), viewModel.estado.value)
    }

    @Test
    fun refrescarConDatosGuardadosMuestraAvisoSinConexion() = runTest(mainDispatcherRule.dispatcher) {
        val repository = FakeItemRepository().apply {
            itemsState.value = listOf(itemRock)
            resultadoRefresco = Result.failure(IllegalStateException("Sin internet"))
        }
        val viewModel = ListaViewModel(repository, FakeNotificador())

        advanceUntilIdle()

        assertEquals(UiState.Exito(listOf(itemRock)), viewModel.estado.value)
        assertEquals("Sin conexión: mostrando datos guardados", viewModel.aviso.value)
    }

    @Test
    fun alternarFavoritoDelegaAlRepositorio() = runTest(mainDispatcherRule.dispatcher) {
        val repository = FakeItemRepository()
        val viewModel = ListaViewModel(repository, FakeNotificador())

        viewModel.alternarFavorito(itemRock.id)
        advanceUntilIdle()

        assertEquals(itemRock.id, repository.idFavoritoAlternado)
    }

    private class FakeItemRepository : ItemRepository {
        val itemsState = MutableStateFlow(emptyList<Item>())
        var resultadoRefresco: Result<List<Item>> = Result.success(emptyList())
        var idFavoritoAlternado: String? = null

        override val items: Flow<List<Item>> = itemsState
        override val favoritos: Flow<List<Item>> = MutableStateFlow(emptyList())

        override suspend fun refrescar(): Result<Int> {
            resultadoRefresco.getOrNull()?.let { itemsState.value = it }
            return resultadoRefresco.map { it.size }
        }

        override suspend fun obtenerDetalle(id: String): Result<Item> =
            Result.success(itemRock.copy(id = id))

        override suspend fun alternarFavorito(id: String) {
            idFavoritoAlternado = id
        }

        override suspend fun cantidadGuardada(): Int = itemsState.value.size

        override suspend fun cantidadFavoritos(): Int = itemsState.value.count { it.esFavorito }

        override suspend fun limpiarCache() {
            itemsState.value = emptyList()
        }
    }

    private class FakeNotificador : Notificador {
        var ultimaNotificacion: Pair<String, String>? = null

        override fun mostrar(titulo: String, texto: String) {
            ultimaNotificacion = titulo to texto
        }
    }

    private companion object {
        val itemRock = Item(
            id = "123",
            titulo = "Rock Album",
            subtitulo = "Rock Artist",
            imagenUrl = null
        )
    }
}
