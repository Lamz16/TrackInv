package com.lamz.trackinv.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Inventory : Screen("inventory")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Add : Screen("Add")
    object AddProduct : Screen("Add/{categoryId}"){
        fun createRoute(categoryId: String) = "add/$categoryId"
    }
    object Register: Screen("register")
    object DetailInventory : Screen("inventory/{inventoryId}") {
        fun createRoute(inventoryId: String) = "inventory/$inventoryId"
    }
}