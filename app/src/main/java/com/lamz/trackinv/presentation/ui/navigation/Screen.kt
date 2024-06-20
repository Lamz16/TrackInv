package com.lamz.trackinv.presentation.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Inventory : Screen("inventory")
    data object Customer : Screen("customer")
    data object Out : Screen("customer/{idCustomer}"){
        fun createRoute(idCustomer: String) = "customer/$idCustomer"
    }

    data object Supplier : Screen("supplier")
    data object In : Screen("supplier/{idSupplier}"){
        fun createRoute(idSupplier: String) = "supplier/$idSupplier"
    }
    data object Transactions : Screen("transactions")
    data object Prediksi : Screen("prediksi")
    data object Add : Screen("Add")
    data object Membership : Screen("Membership")
    data object DetailInventory : Screen("inventory/{inventoryId}") {
        fun createRoute(inventoryId: String) = "inventory/$inventoryId"
    }
}