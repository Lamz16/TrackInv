package com.lamz.trackinv.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Inventory : Screen("Inventory")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Register: Screen("register")
    object DetailReward : Screen("home/{rewardId}") {
        fun createRoute(rewardId: Long) = "home/$rewardId"
    }
}