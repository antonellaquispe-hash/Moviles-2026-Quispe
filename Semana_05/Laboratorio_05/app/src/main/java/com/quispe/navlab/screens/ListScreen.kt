package com.quispe.navlab.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.quispe.navlab.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    val items = listOf(
        "Producto 1",
        "Producto 2",
        "Producto 3",
        "Producto 4",
        "Producto 5"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Lista de elementos")
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            items(items.indices.toList()) { index ->
                ListItem(
                    headlineContent = {
                        Text(items[index])
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                "detail/${index + 1}"
                            )
                        }
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}