package com.lamz.trackinv.ui.navigation

sealed class Screen(val route: String) {
    object Splash: Screen("splash")
    object Home : Screen("home")
    object Inventory : Screen("inventory")
    object Customer : Screen("customer")
    object Out : Screen("customer/{idCustomer}"){
        fun createRoute(idCustomer: String) = "customer/$idCustomer"
    }

    object Supplier : Screen("supplier")
    object In : Screen("supplier/{idSupplier}"){
        fun createRoute(idSupplier: String) = "supplier/$idSupplier"
    }
    object Transactions : Screen("transactions")
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