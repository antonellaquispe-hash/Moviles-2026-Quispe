package com.quispe.manejoestadosconia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskScreen()
        }
    }
}

@Composable
fun TaskScreen() {
    var tarea by remember { mutableStateOf("") }
    var tareas by remember { mutableStateOf(listOf<Tarea>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(text = "Lista de Tareas")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = tarea,
            onValueChange = { tarea = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Escribe una tarea")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (tarea.isNotBlank()) {
                    tareas = tareas + Tarea(tarea)
                    tarea = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar tarea")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Cantidad de tareas: ${tareas.size}")

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tareas.forEachIndexed { index, item ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Checkbox(
                            checked = item.completada,
                            onCheckedChange = { completada ->
                                tareas = tareas.toMutableList().also {
                                    it[index] = item.copy(completada = completada)
                                }
                            }
                        )

                        Text(
                            text = item.nombre,
                            modifier = Modifier.padding(top = 12.dp),
                            textDecoration = if (item.completada) {
                                TextDecoration.LineThrough
                            } else {
                                TextDecoration.None
                            }
                        )
                    }
                }
            }
        }
    }
}