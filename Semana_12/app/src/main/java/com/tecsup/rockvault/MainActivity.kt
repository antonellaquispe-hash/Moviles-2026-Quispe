package com.tecsup.rockvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.tecsup.rockvault.ui.lista.ListaScreen
import com.tecsup.rockvault.ui.lista.ListaViewModel
import com.tecsup.rockvault.ui.theme.RockVaultTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[ListaViewModel::class.java]
        setContent {
            RockVaultTheme {
                val estado by viewModel.estado.collectAsState()
                ListaScreen(
                    estado = estado,
                    onReintentar = viewModel::cargarDatosPrueba
                )
            }
        }
    }
}
