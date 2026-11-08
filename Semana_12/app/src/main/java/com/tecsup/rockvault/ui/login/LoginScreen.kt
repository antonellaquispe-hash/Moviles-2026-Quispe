package com.tecsup.rockvault.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.tecsup.rockvault.ui.UiState

@Composable
fun LoginScreen(
    estado: UiState<*>,
    onIniciarSesion: (String, String) -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    val cargando = estado is UiState.Cargando

    Box(
        modifier = modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary.copy(alpha = .18f), MaterialTheme.colorScheme.background))
        ).padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.size(88.dp).clip(RoundedCornerShape(28.dp)).background(
                        Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary))
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("♫", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(Modifier.height(18.dp))
                Text("RockVault", style = MaterialTheme.typography.headlineLarge)
                Text("Entra a tu colección musical", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(24.dp))
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !cargando,
                    shape = RoundedCornerShape(18.dp)
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = clave,
                    onValueChange = { clave = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !cargando,
                    shape = RoundedCornerShape(18.dp)
                )
                when (estado) {
                    is UiState.Error -> {
                        Spacer(Modifier.height(12.dp))
                        Text(estado.mensaje, color = MaterialTheme.colorScheme.error)
                        if (estado.mensaje.startsWith("No se pudo verificar")) {
                            Button(onClick = onReintentar) { Text("Reintentar") }
                        }
                    }
                    else -> Unit
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { onIniciarSesion(correo, clave) },
                    enabled = !cargando,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    if (cargando) CircularProgressIndicator() else Text("Iniciar sesión")
                }
            }
        }
    }
}
