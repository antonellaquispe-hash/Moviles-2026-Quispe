package com.quispe.campusgps.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.CameraUpdate
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.FillLayer
import org.maplibre.compose.layers.LocationIndicatorLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.rememberMapState
import org.maplibre.compose.overlay.MapOverlay
import org.maplibre.compose.overlay.include
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position
import com.quispe.campusgps.model.Lugar
import com.quispe.campusgps.model.LugarConDistancia
import com.quispe.campusgps.model.UbicacionUi
import kotlin.math.*
import java.util.Locale

private const val OPEN_FREE_MAP_STYLE = "https://tiles.openfreemap.org/styles/liberty"
private val PUNTO_INICIAL_LIMA = Position(latitude = -12.0464, longitude = -77.0428)

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    radioMetros: Int,
    permisoUbicacion: Boolean,
    onSeleccionarLugar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val ubicacion = (estado as? UbicacionUi.Lista)?.posicion
    val posicionUsuario = ubicacion?.let { Position(it.latitud, it.longitud) }
    val mapState = rememberMapState(
        baseStyle = BaseStyle.Uri(OPEN_FREE_MAP_STYLE),
        initialCameraPosition = CameraPosition(center = PUNTO_INICIAL_LIMA, zoom = 12.0)
    ) {
        if (posicionUsuario != null) {
            val radioSource = rememberGeoJsonSource(
                GeoJsonData.JsonString(geoJsonCirculo(posicionUsuario, radioMetros))
            )
            FillLayer(
                id = "radio-cercania",
                source = radioSource,
                color = const(Color(0x223B82F6)),
                outlineColor = const(Color(0xFF1976D2))
            )
        }

        lugares.forEach { item ->
            key(item.lugar.id) {
                val source = rememberGeoJsonSource(
                    GeoJsonData.JsonString(geoJsonPunto(item.lugar))
                )
                CircleLayer(
                    id = "lugar-${item.lugar.id}",
                    source = source,
                    color = const(
                        if (item.lugar.id == masCercano?.id) {
                            Color(0xFF2E9D55)
                        } else {
                            Color(0xFF2583C5)
                        }
                    ),
                    radius = const(if (item.lugar.id == seleccionado) 10.dp else 8.dp),
                    strokeColor = const(Color.White),
                    strokeWidth = const(2.dp),
                    hitPadding = 10.dp,
                    onClick = {
                        onSeleccionarLugar(item.lugar.id)
                        org.maplibre.compose.interaction.ClickResult.Consume
                    }
                )
            }
        }

        if (permisoUbicacion && posicionUsuario != null) {
            LocationIndicatorLayer(id = "ubicacion-actual", location = posicionUsuario)
        }
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(estado) {
        val posicion = (estado as? UbicacionUi.Lista)?.posicion ?: return@LaunchedEffect
        mapState.animateCamera(
            CameraUpdate(
                center = Position(posicion.latitud, posicion.longitud),
                zoom = 16.0
            )
        )
    }

    LaunchedEffect(seleccionado, lugares) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
            ?: return@LaunchedEffect
        mapState.animateCamera(
            CameraUpdate(
                center = Position(lugar.latitud, lugar.longitud),
                zoom = 17.0
            )
        )
    }

    MaplibreMap(state = mapState, modifier = modifier) {
        include(MapOverlay.Full)

        val lugarSeleccionado = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
        if (lugarSeleccionado != null) {
            Column(
                modifier = Modifier
                    .placedAt(
                        Position(lugarSeleccionado.latitud, lugarSeleccionado.longitud),
                        alignment = Alignment.BottomCenter
                    )
                    .padding(bottom = 10.dp)
                    .clickable { onSeleccionarLugar(lugarSeleccionado.id) }
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                        Text(lugarSeleccionado.nombre, style = MaterialTheme.typography.labelMedium)
                        Text(
                            lugarSeleccionado.categoria,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        if (permisoUbicacion && posicionUsuario != null) {
            FilledIconButton(
                onClick = {
                    coroutineScope.launch {
                        mapState.animateCamera(
                            CameraUpdate(center = posicionUsuario, zoom = 16.0)
                        )
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            ) {
                Text("⌖")
            }
        }
    }
}

private fun geoJsonPunto(lugar: Lugar): String =
    """{"type":"Feature","properties":{"id":${lugar.id}},"geometry":{"type":"Point","coordinates":[${lugar.longitud},${lugar.latitud}]}}"""

private fun geoJsonCirculo(centro: Position, radioMetros: Int): String {
    val latitud = Math.toRadians(centro.latitude)
    val longitud = Math.toRadians(centro.longitude)
    val distanciaAngular = radioMetros / 6_371_000.0
    val puntos = (0..64).joinToString(",") { paso ->
        val rumbo = Math.toRadians(paso * 360.0 / 64.0)
        val latitudPunto = asin(
            sin(latitud) * cos(distanciaAngular) +
                cos(latitud) * sin(distanciaAngular) * cos(rumbo)
        )
        val longitudPunto = longitud + atan2(
            sin(rumbo) * sin(distanciaAngular) * cos(latitud),
            cos(distanciaAngular) - sin(latitud) * sin(latitudPunto)
        )
        String.format(
            Locale.US,
            "[%.6f,%.6f]",
            Math.toDegrees(longitudPunto),
            Math.toDegrees(latitudPunto)
        )
    }
    return """{"type":"Feature","properties":{},"geometry":{"type":"Polygon","coordinates":[[$puntos]]}}"""
}
