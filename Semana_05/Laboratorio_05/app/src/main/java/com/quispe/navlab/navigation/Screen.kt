package com.quispe.navlab.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object List : Screen("list")
    object Detail : Screen("detail/{itemId}")
    object Profile : Screen("profile")
}