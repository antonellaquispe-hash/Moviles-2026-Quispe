package com.quispe.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.quispe.centrotareas.data.TareasRepositoryLocal
import com.quispe.centrotareas.ui.PantallaTareas
import com.quispe.centrotareas.ui.theme.CentroTareasTheme
import com.quispe.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CentroTareasTheme {
                PantallaTareas(
                    viewModelFactory = TareasViewModelFactory(TareasRepositoryLocal())
                )
            }
        }
    }
}
