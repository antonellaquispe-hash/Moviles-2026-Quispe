package com.quispe.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quispe.murocampus.model.Comentario
import com.quispe.murocampus.viewmodel.PostsViewModel
import com.quispe.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    postId: Int,
    modifier: Modifier = Modifier
) {
    val estado by vm.detalle.collectAsStateWithLifecycle()

    when (val actual = estado) {
        UiState.Cargando -> EstadoCargando(modifier)
        is UiState.Error -> EstadoError(
            mensaje = actual.mensaje,
            alReintentar = { vm.abrir(postId) },
            modifier = modifier
        )
        is UiState.Exito -> LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "publicacion") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = actual.datos.post.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = actual.datos.post.body,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            item(key = "encabezado-comentarios") {
                Text(
                    text = "Comentarios (${actual.datos.comentarios.size})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
            items(actual.datos.comentarios, key = Comentario::id) { comentario ->
                TarjetaComentario(comentario)
            }
        }
    }
}

@Composable
private fun TarjetaComentario(comentario: Comentario) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = comentario.email,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
            Text(text = comentario.body)
        }
    }
}
