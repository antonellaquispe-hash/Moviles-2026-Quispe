package com.quispe.cafecampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProductoIcono(
    icono: String,
    modifier: Modifier = Modifier,
    tamano: Dp = 72.dp,
    conFondo: Boolean = false
) {
    Box(
        modifier = modifier
            .size(tamano)
            .then(
                if (conFondo) {
                    Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icono,
            fontSize = (tamano.value * 0.58f).sp,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
