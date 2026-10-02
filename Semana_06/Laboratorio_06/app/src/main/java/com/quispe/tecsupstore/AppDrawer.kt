package com.quispe.tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {

    ModalDrawerSheet {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primary
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "AQ",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 12.dp)
                ) {

                    Text(
                        text = "Antonella Quispe",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "TECSUP Store",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            HorizontalDivider()

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            NavigationDrawerItem(
                label = {
                    Text("Inicio")
                },
                selected = selectedItem == "Inicio",
                onClick = {
                    onItemSelected("Inicio")
                }
            )

            NavigationDrawerItem(
                label = {
                    Text("Mis pedidos")
                },
                selected = selectedItem == "Mis pedidos",
                onClick = {
                    onItemSelected("Mis pedidos")
                }
            )

            NavigationDrawerItem(
                label = {
                    Text("Favoritos")
                },
                selected = selectedItem == "Favoritos",
                onClick = {
                    onItemSelected("Favoritos")
                }
            )

            NavigationDrawerItem(
                label = {
                    Text("Perfil")
                },
                selected = selectedItem == "Perfil",
                onClick = {
                    onItemSelected("Perfil")
                }
            )
        }
    }
}