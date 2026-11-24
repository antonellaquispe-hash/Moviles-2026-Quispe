package com.quispe.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.quispe.murocampus.data.PostRepositoryRemote
import com.quispe.murocampus.data.remote.RetrofitClient
import com.quispe.murocampus.ui.PantallaLista
import com.quispe.murocampus.ui.theme.MuroCampusTheme
import com.quispe.murocampus.viewmodel.PostsViewModel
import com.quispe.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    private val postsViewModel: PostsViewModel by lazy {
        ViewModelProvider(
            this,
            PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
        )[PostsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MuroCampusTopBar() }
    ) { innerPadding ->
        PantallaLista(
            vm = vm,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MuroCampusTopBar() {
    TopAppBar(
        title = { Text("MuroCampus") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF1565C0),
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
