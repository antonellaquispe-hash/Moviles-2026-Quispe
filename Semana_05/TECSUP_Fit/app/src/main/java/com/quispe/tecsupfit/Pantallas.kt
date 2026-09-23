package com.quispe.tecsupfit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BarraNavegacion(
    destinoActual: String,
    onNavegar: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = destinoActual == "inicio",
            onClick = {
                onNavegar("inicio")
            },
            icon = {
                Text("⌂")
            },
            label = {
                Text("Inicio")
            }
        )

        NavigationBarItem(
            selected = destinoActual == "reservas",
            onClick = {
                onNavegar("reservas")
            },
            icon = {
                Text("✓")
            },
            label = {
                Text("Reservas")
            }
        )

        NavigationBarItem(
            selected = destinoActual == "rutinas",
            onClick = {
                onNavegar("rutinas")
            },
            icon = {
                Text("★")
            },
            label = {
                Text("Rutinas")
            }
        )

        NavigationBarItem(
            selected = destinoActual == "perfil",
            onClick = {
                onNavegar("perfil")
            },
            icon = {
                Text("●")
            },
            label = {
                Text("Perfil")
            }
        )
    }
}

@Composable
fun Inicio(
    onClaseSeleccionada: (String, String) -> Unit
) {
    val filtros = listOf(
        "Hoy",
        "Esta semana"
    )

    val clases = remember {
        listOf(
            Clase("Entrenamiento funcional", "08:00"),
            Clase("Spinning", "10:00"),
            Clase("Yoga", "18:00"),
            Clase("Cross training", "19:30")
        )
    }

    var filtroSeleccionado by remember {
        mutableStateOf("Hoy")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "TECSUP Fit"
        )

        Text(
            text = "Filtrar clases",
            modifier = Modifier.padding(
                top = 20.dp,
                bottom = 10.dp
            )
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(filtros) { filtro ->
                Card(
                    modifier = Modifier.clickable {
                        filtroSeleccionado = filtro
                    }
                ) {
                    Text(
                        text = filtro,
                        modifier = Modifier.padding(
                            horizontal = 24.dp,
                            vertical = 16.dp
                        )
                    )
                }
            }
        }

        Text(
            text = "Filtro seleccionado: $filtroSeleccionado",
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            text = "Clases disponibles",
            modifier = Modifier.padding(
                top = 20.dp,
                bottom = 10.dp
            )
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(clases) { clase ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClaseSeleccionada(
                                clase.nombre,
                                clase.horario
                            )
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = clase.nombre
                        )

                        Text(
                            text = "Horario: ${clase.horario}",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetalleClase(
    nombre: String,
    horario: String,
    onReservar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Detalle de clase"
        )

        Text(
            text = nombre,
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Horario: $horario",
            modifier = Modifier.padding(top = 12.dp)
        )

        Button(
            onClick = onReservar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Reservar cupo")
        }
    }
}

@Composable
fun Confirmacion(
    nombre: String,
    horario: String,
    onVerReservas: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Reserva confirmada"
        )

        Text(
            text = "Clase: $nombre",
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Horario: $horario",
            modifier = Modifier.padding(top = 12.dp)
        )

        Button(
            onClick = onVerReservas,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Ver reservas")
        }
    }
}

@Composable
fun Reservas() {
    val reservas = remember {
        listOf(
            Reserva(
                "Entrenamiento funcional",
                "08:00",
                "Confirmada"
            ),
            Reserva(
                "Spinning",
                "10:00",
                "Completada"
            ),
            Reserva(
                "Yoga",
                "18:00",
                "Confirmada"
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis reservas",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reservas) { reserva ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = reserva.clase
                        )

                        Text(
                            text = "Horario: ${reserva.horario}",
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Text(
                            text = reserva.estado,
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .then(
                                    Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Rutinas() {
    val rutinas = listOf(
        "Rutina de fuerza",
        "Rutina de cardio",
        "Rutina de movilidad"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Rutinas",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(rutinas) { rutina ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = rutina,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PerfilUsuario() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mi perfil"
        )

        Text(
            text = "Antonella Quispe",
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Estudiante TECSUP",
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Estadísticas",
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 12.dp
            )
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Clases tomadas: 12")

                Text(
                    text = "Racha de asistencia: 5 días",
                    modifier = Modifier.padding(top = 12.dp)
                )

                Text(
                    text = "Horas entrenadas: 18",
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}