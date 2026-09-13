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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    var textoTarea by remember { mutableStateOf("") }
    var contadorId by remember { mutableStateOf(1) }
    var listaTareas by remember { mutableStateOf(listOf<Tarea>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lista de tareas"
        )

        Spacer(
            modifier = Modifier.padding(8.dp)
        )

        OutlinedTextField(
            value = textoTarea,
            onValueChange = { textoTarea = it },
            label = {
                Text("Ingrese una tarea")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.padding(4.dp)
        )

        Button(
            onClick = {
                if (textoTarea.isNotBlank()) {
                    listaTareas = listaTareas + Tarea(
                        id = contadorId,
                        nombre = textoTarea
                    )
                    contadorId++
                    textoTarea = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar tarea")
        }

        Spacer(
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Total de tareas: ${listaTareas.size}"
        )

        Spacer(
            modifier = Modifier.padding(8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = listaTareas,
                key = { it.id }
            ) { tarea ->
                ItemTarea(
                    tarea = tarea,
                    onEliminar = {
                        listaTareas = listaTareas.filter {
                            it.id != tarea.id
                        }
                    },
                    onCambiarEstado = { completada ->
                        listaTareas = listaTareas.map {
                            if (it.id == tarea.id) {
                                it.copy(completada = completada)
                            } else {
                                it
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ItemTarea(
    tarea: Tarea,
    onEliminar: () -> Unit,
    onCambiarEstado: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = tarea.completada,
                    onCheckedChange = {
                        onCambiarEstado(it)
                    }
                )

                Text(
                    text = tarea.nombre,
                    modifier = Modifier.padding(top = 12.dp),
                    textDecoration = if (tarea.completada) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Button(
                onClick = onEliminar
            ) {
                Text("Eliminar")
            }
        }
    }
}