package com.quispe.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.quispe.avisoscampus.ui.PantallaAvisos
import com.quispe.avisoscampus.ui.theme.AvisosCampusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AvisosCampusTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaAvisos()
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PantallaAvisosPreview() {
    AvisosCampusTheme {
        PantallaAvisos(vm = com.quispe.avisoscampus.viewmodel.AvisosViewModel())
    }
}
