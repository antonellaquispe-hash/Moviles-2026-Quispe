package com.quispe.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
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
    val estado by vm.listaVisible.collectAsStateWithLifecycle()
    val usuarioFiltro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()

    when (val actual = estado) {
        UiState.Cargando -> EstadoCargando(modifier)
        is UiState.Error -> EstadoError(
            mensaje = actual.mensaje,
            alReintentar = vm::cargar,
            modifier = modifier
        )
        is UiState.Exito -> Column(modifier = modifier.fillMaxSize()) {
            FilaFiltros(usuarioSeleccionado = usuarioFiltro, onSeleccionar = vm::filtrar)
            OutlinedTextField(
                value = busqueda,
                onValueChange = vm::buscar,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                label = { Text("Buscar por título", fontSize = 12.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            val contenidoModifier = Modifier
                .fillMaxWidth()
                .weight(1f)

            if (actual.datos.isEmpty()) {
                Box(
                    modifier = contenidoModifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (busqueda.isBlank()) "Sin publicaciones"
                        else "No se encontraron publicaciones"
                    )
                }
            } else {
                LazyColumn(
                    modifier = contenidoModifier,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(actual.datos, key = Post::id) { post ->
                        TarjetaPost(post, onClick = { onPostClick(post) })
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaFiltros(usuarioSeleccionado: Int?, onSeleccionar: (Int?) -> Unit) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(key = "todos") {
            ChipUsuario(
                texto = "Todos",
                seleccionado = usuarioSeleccionado == null,
                onClick = { onSeleccionar(null) }
            )
        }
        items(count = 10, key = { it + 1 }) { index ->
            val userId = index + 1
            ChipUsuario(
                texto = "Usuario $userId",
                seleccionado = usuarioSeleccionado == userId,
                onClick = { onSeleccionar(userId) }
            )
        }
    }
}

@Composable
private fun ChipUsuario(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = { Text(texto) }
    )
}

@Composable
fun EstadoCargando(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(32.dp), strokeWidth = 3.dp)
                Text("Cargando publicaciones…", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
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
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = alReintentar,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
                ) {
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
        .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = if (post.userId == 2) androidx.compose.ui.graphics.Color(0xFFFFB800)
                else MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = "Usuario ${post.userId}",
                    color = androidx.compose.ui.graphics.Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                )
            }
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = post.body,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
