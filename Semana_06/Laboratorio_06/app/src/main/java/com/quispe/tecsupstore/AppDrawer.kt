package com.quispe.tecsupstore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

            Text(
                text = "TECSUP Store"
            )

            Text(
                text = "Antonella Quispe"
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            HorizontalDivider()

            Spacer(
                modifier = Modifier.height(16.dp)
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