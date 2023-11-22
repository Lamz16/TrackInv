package com.lamz.trackinv.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Inventory : Screen("Inventory")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Add : Screen("add")
    object AddProduct : Screen("product")
    object Register: Screen("register")
    object DetailInventory : Screen("home/{inventoryId}") {
        fun createRoute(inventoryId: Long) = "home/$inventoryId"
    }
}