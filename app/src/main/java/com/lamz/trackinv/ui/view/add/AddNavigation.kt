package com.lamz.trackinv.ui.view.add

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lamz.trackinv.ui.navigation.Screen
import com.lamz.trackinv.ui.screen.add.AddProductScreen
import com.lamz.trackinv.ui.screen.add.AddScreen
import com.lamz.trackinv.ui.screen.login.LoginScreen
import com.lamz.trackinv.ui.screen.register.RegisterScreen

@Composable
fun AddNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Add.route,
    ) {
        composable(Screen.Add.route) {
            // Your main composable function
            AddScreen(navController = navController,)
        }
        composable(Screen.AddProduct.route) {
            // Composable function for registration screen
            AddProductScreen(navController = navController)

        }
        // Add more destinations as needed
    }
}