package com.quispe.campusgps.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quispe.campusgps.model.UbicacionUi
import com.quispe.campusgps.util.formatearDistancia
import com.quispe.campusgps.viewmodel.CampusViewModelFactory
import com.quispe.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(viewModelFactory: CampusViewModelFactory) {
    val viewModel: MapaViewModel = viewModel(factory = viewModelFactory)
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val lugares by viewModel.lugares.collectAsStateWithLifecycle()
    val seleccionado by viewModel.seleccionado.collectAsStateWithLifecycle()
    val masCercano by viewModel.masCercano.collectAsStateWithLifecycle()
    val contexto = LocalContext.current

    val lanzadorPermisos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        if (permisos.values.any { it }) {
            viewModel.buscar()
        } else {
            viewModel.permisoDenegado()
        }
    }

    fun obtenerUbicacion() {
        val tienePermisoPreciso = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val tienePermisoAproximado = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (tienePermisoPreciso || tienePermisoAproximado) {
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
        topBar = { TopAppBar(title = { Text("CampusGPS") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercano = masCercano,
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
                onClick = viewModel::seleccionar,
                modifier = Modifier.height(220.dp)
            )
        }
    }
}
