package com.quispe.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.quispe.campusgps.model.LugarConDistancia
import com.quispe.campusgps.model.UbicacionUi

private val PUNTO_INICIAL_LIMA = LatLng(-12.0464, -77.0428)

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
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

    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
            ?: return@LaunchedEffect
        camara.animate(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(lugar.latitud, lugar.longitud),
                17f
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

        lugares.forEach { lugarConDistancia ->
            val lugar = lugarConDistancia.lugar
            key(lugar.id) {
                val estadoMarcador = rememberUpdatedMarkerState(
                    position = LatLng(lugar.latitud, lugar.longitud)
                )
                Marker(
                    state = estadoMarcador,
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_AZURE
                    )
                )
                if (seleccionado == lugar.id) {
                    LaunchedEffect(seleccionado) {
                        estadoMarcador.showInfoWindow()
                    }
                }
            }
        }
    }
}
