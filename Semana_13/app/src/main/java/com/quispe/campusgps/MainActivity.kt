package com.quispe.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.quispe.campusgps.data.LugaresRepositoryLocal
import com.quispe.campusgps.data.UbicacionRepositoryGoogle
import com.quispe.campusgps.ui.PantallaMapa
import com.quispe.campusgps.ui.theme.CampusGPSTheme
import com.quispe.campusgps.viewmodel.CampusViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModelFactory = CampusViewModelFactory(
            ubicacionRepository = UbicacionRepositoryGoogle(applicationContext),
            lugaresRepository = LugaresRepositoryLocal()
        )

        setContent {
            CampusGPSTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaMapa(viewModelFactory = viewModelFactory)
                }
            }
        }
    }
}
