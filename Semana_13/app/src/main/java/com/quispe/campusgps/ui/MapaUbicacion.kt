package com.quispe.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.quispe.campusgps.model.UbicacionUi

private val PUNTO_INICIAL_LIMA = LatLng(-12.0464, -77.0428)

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(PUNTO_INICIAL_LIMA, 12f)
    }

    LaunchedEffect(estado) {
        val ubicacion = (estado as? UbicacionUi.Lista)?.posicion ?: return@LaunchedEffect
        camara.animate(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(ubicacion.latitud, ubicacion.longitud),
                16f
            )
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara
    ) {
        val ubicacion = (estado as? UbicacionUi.Lista)?.posicion
        if (ubicacion != null) {
            Marker(
                state = rememberUpdatedMarkerState(
                    position = LatLng(ubicacion.latitud, ubicacion.longitud)
                ),
                title = "Estás aquí"
            )
        }
    }
}
