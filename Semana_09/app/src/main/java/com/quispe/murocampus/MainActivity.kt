package com.quispe.murocampus

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.quispe.murocampus.data.PostRepositoryRemote
import com.quispe.murocampus.data.remote.RetrofitClient
import com.quispe.murocampus.data.translation.TraductorMlKit
import com.quispe.murocampus.ui.PantallaDetalle
import com.quispe.murocampus.ui.PantallaCrear
import com.quispe.murocampus.ui.PantallaLista
import com.quispe.murocampus.ui.theme.MuroCampusTheme
import com.quispe.murocampus.viewmodel.PostsViewModel
import com.quispe.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    private val postsViewModel: PostsViewModel by lazy {
        ViewModelProvider(
            this,
            PostsViewModelFactory(
                PostRepositoryRemote(RetrofitClient.api, TraductorMlKit())
            )
        )[PostsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            MuroCampusTheme {
                MuroCampusScreen(postsViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuroCampusScreen(vm: PostsViewModel) {
    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    var creandoPublicacion by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(idSeleccionado) {
        idSeleccionado?.let(vm::abrir)
    }
    LaunchedEffect(creandoPublicacion) {
        if (creandoPublicacion) vm.limpiarResultadoPublicacion()
    }
    BackHandler(enabled = creandoPublicacion || idSeleccionado != null) {
        if (creandoPublicacion) {
            creandoPublicacion = false
        } else {
            idSeleccionado = null
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { MuroCampusTopBar() },
        floatingActionButton = {
            if (!creandoPublicacion && idSeleccionado == null) {
                FloatingActionButton(
                    onClick = {
                        vm.limpiarResultadoPublicacion()
                        creandoPublicacion = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Text("+")
                }
            }
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        val postId = idSeleccionado
        if (creandoPublicacion) {
            PantallaCrear(vm = vm, modifier = contentModifier)
        } else if (postId == null) {
            PantallaLista(
                vm = vm,
                modifier = contentModifier,
                onPostClick = { post -> idSeleccionado = post.id }
            )
        } else {
            PantallaDetalle(
                vm = vm,
                postId = postId,
                modifier = contentModifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MuroCampusTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "MuroCampus",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun MuroCampusScreenPreview() {
    MuroCampusTheme {
        Scaffold(topBar = { MuroCampusTopBar() }) { innerPadding ->
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}
