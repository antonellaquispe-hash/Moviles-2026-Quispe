package com.quispe.registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

data class Curso(
    val nombre: String,
    val peso: Int
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                RegistroNotasApp()
            }
        }
    }
}

@Composable
fun RegistroNotasApp() {

    val cursos = listOf(
        Curso("Fundamentos de Programación", 20),
        Curso("Programación Orientada a Objetos", 25),
        Curso("Programación en Móviles", 30),
        Curso("Base de Datos", 25)
    )

    var nota1 by remember { mutableFloatStateOf(0f) }
    var nota2 by remember { mutableFloatStateOf(0f) }
    var nota3 by remember { mutableFloatStateOf(0f) }
    var nota4 by remember { mutableFloatStateOf(0f) }

    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    var calcular by remember { mutableStateOf(false) }

    val notas = listOf(nota1, nota2, nota3, nota4)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        ) {
            Text(
                text = "Registro de Notas",
                modifier = Modifier.padding(16.dp),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cursos) { curso ->

                val indice = cursos.indexOf(curso)

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "${curso.nombre} (${curso.peso}%)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Slider(
                                value = notas[indice],
                                onValueChange = { valor ->
                                    when (indice) {
                                        0 -> nota1 = valor
                                        1 -> nota2 = valor
                                        2 -> nota3 = valor
                                        3 -> nota4 = valor
                                    }
                                },
                                valueRange = 0f..20f,
                                steps = 19,
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = notas[indice].toInt().toString(),
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    ),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Redondear promedio final",
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )

                        Switch(
                            checked = redondear,
                            onCheckedChange = {
                                redondear = it
                            }
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = confirmado,
                        onCheckedChange = {
                            confirmado = it
                        }
                    )

                    Text(
                        text = "Confirmo que las notas son correctas",
                        fontSize = 15.sp
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        calcular = true
                    },
                    enabled = confirmado,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "CALCULAR PROMEDIO",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                if (!calcular) {
                    Text(
                        text = "Asigna las notas y confirma para calcular",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                } else {
                    val promedioPonderado =
                        nota1 * 0.20 +
                                nota2 * 0.25 +
                                nota3 * 0.30 +
                                nota4 * 0.25

                    val promedioFinal = if (redondear) {
                        promedioPonderado.roundToInt().toDouble()
                    } else {
                        promedioPonderado
                    }

                    val observacion = when {
                        promedioFinal >= 17 -> "EXCELENTE"
                        promedioFinal >= 13 -> "APROBADO"
                        promedioFinal >= 10 -> "EN RECUPERACIÓN"
                        else -> "DESAPROBADO"
                    }

                    val chipColor = when {
                        promedioFinal >= 17 -> Color(0xFF2E7D32)
                        promedioFinal >= 13 -> Color(0xFF43A047)
                        promedioFinal >= 10 -> Color(0xFFFFA000)
                        else -> Color(0xFFD32F2F)
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Resultados",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Promedio ponderado: %.2f".format(promedioPonderado),
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (redondear) {
                                    "Promedio final: ${promedioFinal.toInt()} (redondeado)"
                                } else {
                                    "Promedio final: %.2f".format(promedioFinal)
                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            FilterChip(
                                selected = true,
                                onClick = {},
                                label = {
                                    Text(
                                        text = observacion,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = chipColor,
                                    selectedLabelColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "✓ Las notas han sido confirmadas correctamente.",
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = "Desarrollado por: Antonella Quispe",
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            fontSize = 13.sp,
            color = Color.Gray
        )
    }
}