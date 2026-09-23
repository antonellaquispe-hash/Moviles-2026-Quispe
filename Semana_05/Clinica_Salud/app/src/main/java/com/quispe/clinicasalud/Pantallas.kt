package com.quispe.clinicasalud

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    destinoActual: String,
    onNavegar: (String) -> Unit,
    contenido: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Clínica Salud+",
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    NavigationDrawerItem(
                        label = {
                            Text("Inicio")
                        },
                        selected = destinoActual == "inicio",
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            onNavegar("inicio")
                        }
                    )

                    NavigationDrawerItem(
                        label = {
                            Text("Mis citas")
                        },
                        selected = destinoActual == "citas",
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            onNavegar("citas")
                        }
                    )

                    NavigationDrawerItem(
                        label = {
                            Text("Historial médico")
                        },
                        selected = destinoActual == "historial",
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            onNavegar("historial")
                        }
                    )
                }
            }
        }
    ) {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("☰ Menú")
                }

                contenido()
            }
        }
    }
}

@Composable
fun Inicio(
    onMedicoSeleccionado: (String, String, String) -> Unit
) {
    val especialidades = listOf(
        "Todas",
        "Cardiología",
        "Pediatría",
        "Dermatología",
        "Odontología"
    )

    val medicos = remember {
        listOf(
            Medico("Dra. María López", "Cardiología", "4.9"),
            Medico("Dr. Carlos Pérez", "Pediatría", "4.8"),
            Medico("Dra. Ana Torres", "Dermatología", "4.7"),
            Medico("Dr. Luis Ramírez", "Odontología", "4.9")
        )
    }

    var busqueda by remember {
        mutableStateOf("")
    }

    var especialidadSeleccionada by remember {
        mutableStateOf("Todas")
    }

    val medicosFiltrados = medicos.filter { medico ->
        val coincideBusqueda =
            medico.nombre.contains(busqueda, ignoreCase = true) ||
                    medico.especialidad.contains(busqueda, ignoreCase = true)

        val coincideEspecialidad =
            especialidadSeleccionada == "Todas" ||
                    medico.especialidad == especialidadSeleccionada

        coincideBusqueda && coincideEspecialidad
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Clínica Salud+"
        )

        OutlinedTextField(
            value = busqueda,
            onValueChange = {
                busqueda = it
            },
            label = {
                Text("Buscar médico")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        Text(
            text = "Especialidades",
            modifier = Modifier.padding(
                top = 20.dp,
                bottom = 10.dp
            )
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(especialidades) { especialidad ->
                Card(
                    modifier = Modifier
                        .clickable {
                            especialidadSeleccionada = especialidad
                        }
                        .background(Color.Transparent)
                        .padding(2.dp)
                ) {
                    Text(
                        text = especialidad,
                        modifier = Modifier.padding(
                            horizontal = 20.dp,
                            vertical = 16.dp
                        )
                    )
                }
            }
        }

        Text(
            text = "Médicos disponibles",
            modifier = Modifier.padding(
                top = 20.dp,
                bottom = 10.dp
            )
        )

        if (medicosFiltrados.isEmpty()) {
            Text(
                text = "No se encontraron médicos"
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(medicosFiltrados) { medico ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onMedicoSeleccionado(
                                    medico.nombre,
                                    medico.especialidad,
                                    medico.valoracion
                                )
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = medico.nombre
                            )

                            Text(
                                text = medico.especialidad,
                                modifier = Modifier.padding(top = 6.dp)
                            )

                            Text(
                                text = "⭐ ${medico.valoracion}",
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Perfil(
    nombre: String,
    especialidad: String,
    valoracion: String,
    onAgendar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Perfil médico"
        )

        Text(
            text = nombre,
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Especialidad: $especialidad",
            modifier = Modifier.padding(top = 12.dp)
        )

        Text(
            text = "Valoración: ⭐ $valoracion",
            modifier = Modifier.padding(top = 12.dp)
        )

        Button(
            onClick = onAgendar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Agendar cita")
        }
    }
}

@Composable
fun AgendarCita(
    nombre: String,
    especialidad: String,
    onConfirmar: (String, String) -> Unit
) {
    val fechas = listOf(
        "Lunes 28",
        "Martes 29",
        "Miércoles 30"
    )

    val horarios = listOf(
        "09:00",
        "11:00",
        "15:00"
    )

    var fechaSeleccionada by remember {
        mutableStateOf("")
    }

    var horarioSeleccionado by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Agendar cita"
        )

        Text(
            text = nombre,
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            text = especialidad,
            modifier = Modifier.padding(top = 6.dp)
        )

        Text(
            text = "Selecciona una fecha",
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 8.dp
            )
        )

        fechas.forEach { fecha ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        fechaSeleccionada = fecha
                    }
                    .padding(vertical = 6.dp)
            ) {
                RadioButton(
                    selected = fechaSeleccionada == fecha,
                    onClick = {
                        fechaSeleccionada = fecha
                    }
                )

                Text(
                    text = fecha,
                    modifier = Modifier.padding(
                        top = 12.dp,
                        start = 8.dp
                    )
                )
            }
        }

        Text(
            text = "Selecciona un horario",
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 8.dp
            )
        )

        horarios.forEach { horario ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        horarioSeleccionado = horario
                    }
                    .padding(vertical = 6.dp)
            ) {
                RadioButton(
                    selected = horarioSeleccionado == horario,
                    onClick = {
                        horarioSeleccionado = horario
                    }
                )

                Text(
                    text = horario,
                    modifier = Modifier.padding(
                        top = 12.dp,
                        start = 8.dp
                    )
                )
            }
        }

        Button(
            onClick = {
                onConfirmar(
                    fechaSeleccionada,
                    horarioSeleccionado
                )
            },
            enabled = fechaSeleccionada.isNotEmpty() &&
                    horarioSeleccionado.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text("Confirmar cita")
        }
    }
}

@Composable
fun Confirmacion(
    nombre: String,
    especialidad: String,
    fecha: String,
    horario: String,
    onFinalizar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Cita confirmada"
        )

        Text(
            text = "Médico: $nombre",
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Especialidad: $especialidad",
            modifier = Modifier.padding(top = 12.dp)
        )

        Text(
            text = "Fecha: $fecha",
            modifier = Modifier.padding(top = 12.dp)
        )

        Text(
            text = "Horario: $horario",
            modifier = Modifier.padding(top = 12.dp)
        )

        Button(
            onClick = onFinalizar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Volver al inicio")
        }
    }
}

@Composable
fun MisCitas() {
    val citas = remember {
        listOf(
            Cita(
                "Dra. María López",
                "Lunes 28 - 09:00",
                "Confirmada"
            ),
            Cita(
                "Dr. Carlos Pérez",
                "Martes 29 - 11:00",
                "Completada"
            ),
            Cita(
                "Dra. Ana Torres",
                "Miércoles 30 - 15:00",
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
            text = "Mis citas",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(citas) { cita ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = cita.medico
                        )

                        Text(
                            text = cita.detalle,
                            modifier = Modifier.padding(top = 6.dp)
                        )

                        Text(
                            text = cita.estado,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .background(
                                    if (cita.estado == "Confirmada") {
                                        Color(0xFFDFF5E1)
                                    } else {
                                        Color(0xFFE0E0E0)
                                    }
                                )
                                .padding(
                                    horizontal = 10.dp,
                                    vertical = 6.dp
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistorialMedico() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Historial médico"
        )

        Text(
            text = "Consulta general - 15/08/2026",
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Control preventivo - 02/07/2026",
            modifier = Modifier.padding(top = 14.dp)
        )

        Text(
            text = "Análisis de rutina - 18/05/2026",
            modifier = Modifier.padding(top = 14.dp)
        )
    }
}