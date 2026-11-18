package com.quispe.campusgps.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quispe.campusgps.model.UbicacionUi
import com.quispe.campusgps.util.formatearDistancia
import com.quispe.campusgps.viewmodel.CampusViewModelFactory
import com.quispe.campusgps.viewmodel.MapaViewModel

private fun Context.tienePermisoDeUbicacion(): Boolean =
    ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(viewModelFactory: CampusViewModelFactory) {
    val viewModel: MapaViewModel = viewModel(factory = viewModelFactory)
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val lugares by viewModel.lugares.collectAsStateWithLifecycle()
    val seleccionado by viewModel.seleccionado.collectAsStateWithLifecycle()
    val masCercano by viewModel.masCercano.collectAsStateWithLifecycle()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsStateWithLifecycle()
    val radioMetros by viewModel.radioMetros.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    var permisoUbicacion by remember(contexto) {
        mutableStateOf(contexto.tienePermisoDeUbicacion())
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, contexto) {
        val observador = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_RESUME) {
                permisoUbicacion = contexto.tienePermisoDeUbicacion()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observador)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observador) }
    }

    val lanzadorPermisos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        permisoUbicacion = contexto.tienePermisoDeUbicacion()
        if (permisos.values.any { it }) {
            viewModel.buscar()
        } else {
            viewModel.permisoDenegado()
        }
    }

    fun obtenerUbicacion() {
        permisoUbicacion = contexto.tienePermisoDeUbicacion()
        if (permisoUbicacion) {
            viewModel.buscar()
        } else {
            lanzadorPermisos.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = categoriaSeleccionada == null,
                    onClick = { viewModel.filtrarPorCategoria(null) },
                    label = { Text("Todos") }
                )
                listOf("Estudio", "Comida", "Eventos", "Servicios").forEach { categoria ->
                    FilterChip(
                        selected = categoriaSeleccionada == categoria,
                        onClick = {
                            viewModel.filtrarPorCategoria(
                                if (categoriaSeleccionada == categoria) null else categoria
                            )
                        },
                        label = { Text(categoria) }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Radio:", style = MaterialTheme.typography.labelMedium)
                listOf(100, 200, 500).forEach { radio ->
                    FilterChip(
                        selected = radioMetros == radio,
                        onClick = { viewModel.seleccionarRadio(radio) },
                        label = { Text("${radio} m") }
                    )
                }
            }
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercano = masCercano,
                radioMetros = radioMetros,
                permisoUbicacion = permisoUbicacion,
                onSeleccionarLugar = viewModel::seleccionar,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Button(
                onClick = ::obtenerUbicacion,
                enabled = estado != UbicacionUi.Buscando
            ) {
                Text(if (estado == UbicacionUi.Buscando) "Buscando…" else "Obtener mi ubicación")
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { masCercano?.let { viewModel.seleccionar(it.id) } },
                enabled = masCercano != null
            ) {
                Text("Ir al más cercano")
            }
            val textoMasCercano = masCercano?.let { lugar ->
                val distancia = lugares.firstOrNull { it.lugar.id == lugar.id }?.metros
                distancia?.let {
                    "Más cercano: ${lugar.nombre} (${formatearDistancia(it)})"
                }
            } ?: "Más cercano: sin calcular"
            Text(
                text = textoMasCercano,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            val error = estado as? UbicacionUi.Error
            if (error != null) {
                MensajeError(
                    error = error,
                    onAbrirAjustes = {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", contexto.packageName, null)
                        )
                        contexto.startActivity(intent)
                    },
                    onReintentar = ::obtenerUbicacion
                )
            }
            if (estado == UbicacionUi.Buscando) {
                CircularProgressIndicator(modifier = Modifier.padding(bottom = 8.dp))
            }
            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                radioMetros = radioMetros,
                onClick = viewModel::seleccionar,
                modifier = Modifier.height(220.dp)
            )
        }
    }
}
