package com.tecsup.rockvault

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tecsup.rockvault.domain.model.Sesion
import com.tecsup.rockvault.ui.UiState
import com.tecsup.rockvault.ui.admin.AdminViewModel
import com.tecsup.rockvault.ui.components.Cargando
import com.tecsup.rockvault.ui.detalle.DetalleViewModel
import com.tecsup.rockvault.ui.favoritos.FavoritosViewModel
import com.tecsup.rockvault.ui.lista.ListaViewModel
import com.tecsup.rockvault.ui.login.LoginScreen
import com.tecsup.rockvault.ui.login.LoginViewModel
import com.tecsup.rockvault.ui.navigation.AppNav
import com.tecsup.rockvault.ui.theme.RockVaultTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = AppContainer.itemRepository(applicationContext)
        val notificador = AppContainer.notificador(applicationContext)
        val listaViewModel = ViewModelProvider(
            this,
            ListaViewModel.Factory(repository, notificador)
        )[ListaViewModel::class.java]
        val detalleViewModel = ViewModelProvider(
            this,
            DetalleViewModel.Factory(repository)
        )[DetalleViewModel::class.java]
        val favoritosViewModel = ViewModelProvider(
            this,
            FavoritosViewModel.Factory(repository)
        )[FavoritosViewModel::class.java]
        val adminViewModel = ViewModelProvider(
            this,
            AdminViewModel.Factory(repository, notificador)
        )[AdminViewModel::class.java]
        val loginViewModel = ViewModelProvider(
            this,
            LoginViewModel.Factory(AppContainer.authRepository)
        )[LoginViewModel::class.java]

        setContent {
            RockVaultTheme {
                val estadoSesion by loginViewModel.estado.collectAsState()
                val estadoLista by listaViewModel.estado.collectAsState()
                val avisoLista by listaViewModel.aviso.collectAsState()
                val estadoDetalle by detalleViewModel.estado.collectAsState()
                val estadoFavoritos by favoritosViewModel.estado.collectAsState()
                val estadoAdmin by adminViewModel.estado.collectAsState()
                val mensajeAdmin by adminViewModel.mensaje.collectAsState()
                val sesion = (estadoSesion as? UiState.Exito<Sesion>)?.datos
                var solicitoPermiso by remember { mutableStateOf(false) }
                val permisoNotificaciones = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { concedido ->
                    if (concedido) listaViewModel.cargar()
                }

                LaunchedEffect(sesion?.uid) {
                    if (
                        sesion != null &&
                        Build.VERSION.SDK_INT >= 33 &&
                        !solicitoPermiso &&
                        ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        solicitoPermiso = true
                        permisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                if (sesion == null) {
                    when (estadoSesion) {
                        UiState.Cargando -> Cargando()
                        UiState.Vacio, is UiState.Error -> LoginScreen(
                            estado = estadoSesion,
                            onIniciarSesion = loginViewModel::iniciarSesion,
                            onReintentar = loginViewModel::verificarSesion
                        )
                        is UiState.Exito -> Cargando()
                    }
                } else {
                    AppNav(
                        sesion = sesion,
                        estadoLista = estadoLista,
                        avisoLista = avisoLista,
                        estadoDetalle = estadoDetalle,
                        estadoFavoritos = estadoFavoritos,
                        estadoAdmin = estadoAdmin,
                        mensajeAdmin = mensajeAdmin,
                        onReintentarLista = listaViewModel::cargar,
                        onCargarDetalle = detalleViewModel::cargar,
                        onAlternarFavoritoLista = listaViewModel::alternarFavorito,
                        onAlternarFavoritoFavoritos = favoritosViewModel::alternarFavorito,
                        onAlternarFavoritoDetalle = detalleViewModel::alternarFavorito,
                        onSincronizar = adminViewModel::sincronizar,
                        onLimpiarCache = adminViewModel::limpiarCache,
                        onCargarTotales = adminViewModel::cargarTotales,
                        onCerrarSesion = loginViewModel::cerrarSesion
                    )
                }
            }
        }
    }
}
