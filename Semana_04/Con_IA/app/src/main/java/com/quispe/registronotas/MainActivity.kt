package com.quispe.registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        com.quispe.registronotas.model.Curso(
            "Fundamentos de Programación",
            20
        ),
        com.quispe.registronotas.model.Curso(
            "Programación Orientada a Objetos",
            25
        ),
        com.quispe.registronotas.model.Curso(
            "Programación en Móviles",
            30
        ),
        com.quispe.registronotas.model.Curso(
            "Base de Datos",
            25
        )
    )

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

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            cursos.forEach { curso ->
                Text(
                    text = "${curso.nombre} (${curso.peso}%)",
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 16.sp
                )
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