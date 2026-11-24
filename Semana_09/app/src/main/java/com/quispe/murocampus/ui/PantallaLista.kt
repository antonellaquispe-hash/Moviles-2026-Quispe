package com.quispe.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quispe.murocampus.model.Post
import com.quispe.murocampus.viewmodel.PostsViewModel
import com.quispe.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    modifier: Modifier = Modifier,
    onPostClick: (Post) -> Unit = {}
) {
    val estado by vm.lista.collectAsStateWithLifecycle()

    when (val actual = estado) {
        UiState.Cargando -> EstadoCargando(modifier)
        is UiState.Error -> EstadoError(
            mensaje = actual.mensaje,
            alReintentar = vm::cargar,
            modifier = modifier
        )
        is UiState.Exito -> LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(actual.datos, key = Post::id) { post ->
                TarjetaPost(post, onClick = { onPostClick(post) })
            }
        }
    }
}

@Composable
fun EstadoCargando(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(12.dp))
        Text("Cargando publicaciones…")
    }
}

@Composable
fun EstadoError(
    mensaje: String,
    alReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = alReintentar) {
                    Text("Reintentar")
                }
            }
        }
    }
}

@Composable
fun TarjetaPost(
    post: Post,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Usuario ${post.userId}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = post.body,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
