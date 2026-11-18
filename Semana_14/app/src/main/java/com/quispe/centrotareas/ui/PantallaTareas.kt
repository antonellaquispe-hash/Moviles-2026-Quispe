package com.quispe.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quispe.centrotareas.model.CargaUi
import com.quispe.centrotareas.model.PrimosUi
import com.quispe.centrotareas.viewmodel.TareasViewModel

private val AzulCentro = Color(0xFF1976B2)
private val VerdeTexto = Color(0xFF256B38)
private val VerdeFondo = Color(0xFFE5F3E8)
private val RojoTexto = Color(0xFFB3261E)
private val RojoFondo = Color(0xFFFCE4E4)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModelFactory: ViewModelProvider.Factory) {
    val viewModel: TareasViewModel = viewModel(factory = viewModelFactory)
    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()
    val primos by viewModel.primos.collectAsState()
    val carga by viewModel.carga.collectAsState()
    val simularError by viewModel.simularError.collectAsState()
    val comparacion by viewModel.comparacion.collectAsState()
    var toques by remember { mutableIntStateOf(0) }
    var limitePrimos by remember { mutableStateOf("500000") }
    val hasta = limitePrimos.toIntOrNull()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulCentro,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Tarea con progreso",
                        modifier = Modifier.fillMaxWidth(),
                        color = AzulCentro,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Button(onClick = { toques++ }) {
                        Text("Tócame: $toques")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.tareaLarga() },
                            enabled = !corriendo,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Iniciar")
                        }
                        OutlinedButton(
                            onClick = { viewModel.cancelar() },
                            enabled = corriendo,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }
                    }
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        "${(progreso * 100).toInt()} %",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(mensaje, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Contar números primos",
                        color = AzulCentro,
                        style = MaterialTheme.typography.titleSmall
                    )
                    OutlinedTextField(
                        value = limitePrimos,
                        onValueChange = { limitePrimos = it },
                        label = { Text("Hasta el número") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        enabled = primos != PrimosUi.Calculando,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { if (hasta != null) viewModel.contar(hasta) },
                            enabled = hasta != null && hasta >= 2 && primos != PrimosUi.Calculando,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Contar")
                        }
                        OutlinedButton(
                            onClick = { viewModel.cancelarPrimos() },
                            enabled = primos == PrimosUi.Calculando,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }
                    }
                    when (val estado = primos) {
                        PrimosUi.Inicial -> Unit
                        PrimosUi.Calculando -> Text("Calculando...")
                        PrimosUi.Cancelado -> Text("Cancelado")
                        is PrimosUi.Listo -> Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = VerdeFondo)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Primos hasta $limitePrimos: ${estado.cantidad}",
                                    color = VerdeTexto
                                )
                                Text(
                                    "Tiempo: ${estado.milisegundos} ms",
                                    color = VerdeTexto,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Dos cargas",
                        modifier = Modifier.fillMaxWidth(),
                        color = AzulCentro,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Switch(
                            checked = simularError,
                            onCheckedChange = { viewModel.cambiarSimularError(it) }
                        )
                        Text("Simular error")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.cargarUnaPorUna() },
                            enabled = carga != CargaUi.Cargando,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Una por una")
                        }
                        Button(
                            onClick = { viewModel.cargarALaVez() },
                            enabled = carga != CargaUi.Cargando,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("A la vez")
                        }
                    }
                    when (val estado = carga) {
                        CargaUi.Inicial -> Unit
                        CargaUi.Cargando -> CircularProgressIndicator()
                        is CargaUi.Error -> Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = RojoFondo)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    estado.mensaje,
                                    color = RojoTexto,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                OutlinedButton(onClick = { viewModel.cargarALaVez() }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                        is CargaUi.Exito -> Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = VerdeFondo)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Modo: ${estado.modo}", color = VerdeTexto)
                                Text("Perfil: ${estado.perfil}", color = VerdeTexto)
                                Text("Pedidos: ${estado.pedidos}", color = VerdeTexto)
                                Text(
                                    "Tiempo: ${estado.milisegundos} ms",
                                    color = VerdeTexto,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        "Comparación de tiempos",
                        color = AzulCentro,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CeldaTabla("Modo", 1.4f, negrita = true)
                        CeldaTabla("Veces", 0.8f, negrita = true)
                        CeldaTabla("Último", 1f, negrita = true)
                        CeldaTabla("Promedio", 1.1f, negrita = true)
                    }
                    FilaComparacion(
                        modo = "Una por una",
                        tiempos = comparacion.unaPorUna,
                        promedio = comparacion.promedioUnaPorUna
                    )
                    FilaComparacion(
                        modo = "A la vez",
                        tiempos = comparacion.aLaVez,
                        promedio = comparacion.promedioALaVez
                    )
                    val ahorro = comparacion.ahorroMs
                    Text(
                        text = if (ahorro == null) {
                            "Ejecuta ambos modos para ver la diferencia"
                        } else {
                            "«A la vez» es $ahorro ms más rápido en promedio"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (ahorro == null) Color.Gray else VerdeTexto
                    )
                    OutlinedButton(
                        onClick = { viewModel.limpiarComparacion() },
                        enabled = comparacion.unaPorUna.isNotEmpty() ||
                                comparacion.aLaVez.isNotEmpty()
                    ) {
                        Text("Limpiar tabla")
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.CeldaTabla(texto: String, peso: Float, negrita: Boolean = false) {
    Text(
        text = texto,
        modifier = Modifier.weight(peso),
        fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun FilaComparacion(modo: String, tiempos: List<Long>, promedio: Long?) {
    Row(modifier = Modifier.fillMaxWidth()) {
        CeldaTabla(modo, 1.4f)
        CeldaTabla("${tiempos.size}", 0.8f)
        CeldaTabla(tiempos.lastOrNull()?.let { "$it ms" } ?: "—", 1f)
        CeldaTabla(promedio?.let { "$it ms" } ?: "—", 1.1f)
    }
}