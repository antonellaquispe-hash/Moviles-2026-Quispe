package com.quispe.cafecampus.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import com.quispe.cafecampus.ui.components.ItemNavegacion

/**
 * El cliente navega entre 3 destinos (Menu, Pedidos, Perfil) y el
 * administrador entre 4 (Panel, Pedidos, Menu, Ajustes).
 */
val ITEMS_CLIENTE = listOf(
    ItemNavegacion(
        ruta = RutasCafe.MENU,
        etiqueta = "Menú",
        icono = Icons.Filled.Restaurant
    ),
    ItemNavegacion(
        ruta = RutasCafe.MIS_PEDIDOS,
        etiqueta = "Pedidos",
        icono = Icons.Filled.ReceiptLong
    ),
    ItemNavegacion(
        ruta = RutasCafe.CLIENTE_PERFIL,
        etiqueta = "Perfil",
        icono = Icons.Filled.Person
    )
)

val ITEMS_ADMIN = listOf(
    ItemNavegacion(
        ruta = RutasCafe.ADMIN,
        etiqueta = "Panel",
        icono = Icons.Filled.Dashboard
    ),
    ItemNavegacion(
        ruta = RutasCafe.ADMIN_PEDIDOS,
        etiqueta = "Pedidos",
        icono = Icons.Filled.ReceiptLong
    ),
    ItemNavegacion(
        ruta = RutasCafe.ADMIN_MENU,
        etiqueta = "Menú",
        icono = Icons.Filled.Restaurant
    ),
    ItemNavegacion(
        ruta = RutasCafe.ADMIN_AJUSTES,
        etiqueta = "Ajustes",
        icono = Icons.Filled.Settings
    )
)
