package com.quispe.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quispe.centrotareas.model.PrimosUi
import com.quispe.centrotareas.model.CargaUi
import com.quispe.centrotareas.viewmodel.TareasViewModel

private val AzulCentro = Color(0xFF1976B2)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModelFactory: ViewModelProvider.Factory) {
    val viewModel: TareasViewModel = viewModel(factory = viewModelFactory)
    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()
    val primos by viewModel.primos.collectAsState()
    val carga by viewModel.carga.collectAsState()
    val simularError by viewModel.simularError.collectAsState()
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
                            onClick = viewModel::tareaLarga,
                            enabled = !corriendo,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Iniciar")
                        }
                        OutlinedButton(
                            onClick = viewModel::cancelar,
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
                    Text("${(progreso * 100).toInt()} %", style = MaterialTheme.typography.bodySmall)
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
                            onClick = { hasta?.let(viewModel::contar) },
                            enabled = hasta != null && hasta >= 2 && primos != PrimosUi.Calculando,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Contar")
                        }
                        OutlinedButton(
                            onClick = viewModel::cancelarPrimos,
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
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE5F3E8)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Primos hasta $limitePrimos: ${estado.cantidad}",
                                    color = Color(0xFF256B38)
                                )
                                Text(
                                    "Tiempo: ${estado.milisegundos} ms",
                                    color = Color(0xFF256B38),
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = simularError,
                            onCheckedChange = viewModel::cambiarSimularError
                        )
                        Text("Simular error")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = viewModel::cargarUnaPorUna,
                            enabled = carga != CargaUi.Cargando,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Una por una")
                        }
                        Button(
                            onClick = viewModel::cargarALaVez,
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
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFCE4E4)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    estado.mensaje,
                                    color = Color(0xFFB3261E),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Button(
                                    onClick = viewModel::cargarALaVez,
                                    enabled = carga != CargaUi.Cargando
                                ) {
                                    Text("Reintentar")
                                }
                            }
                        }
                        is CargaUi.Exito -> Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE5F3E8)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Modo: ${estado.modo}", color = Color(0xFF256B38))
                                Text("Perfil: ${estado.perfil}", color = Color(0xFF256B38))
                                Text("Pedidos: ${estado.pedidos}", color = Color(0xFF256B38))
                                Text(
                                    "Tiempo: ${estado.milisegundos} ms",
                                    color = Color(0xFF256B38),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
