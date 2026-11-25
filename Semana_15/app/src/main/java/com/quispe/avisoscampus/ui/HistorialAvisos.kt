package com.quispe.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.quispe.avisoscampus.model.Aviso
import com.quispe.avisoscampus.model.Origen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Historial",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            val recientes = avisos.take(20)
            if (recientes.isEmpty()) {
                Text(
                    text = "Sin avisos todavía",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                recientes.forEach { aviso ->
                    FilaAviso(aviso)
                }
            }
        }
    }
}

@Composable
private fun FilaAviso(aviso: Aviso) {
    val (colorEtiqueta, colorTexto) = when (aviso.origen) {
        Origen.BOTON -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
        Origen.RECORDATORIO -> Color(0xFFFFC107) to Color(0xFF332700)
        Origen.MODO_AVION -> Color(0xFF7659C6) to Color.White
        Origen.CARGADOR -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onTertiary
    }
    val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        .format(Date(aviso.creadoEn))

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = colorEtiqueta
            ) {
                Text(
                    text = aviso.origen.texto,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = colorTexto,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = aviso.titulo,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = aviso.texto,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = hora,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1
            )
        }
    }
}
