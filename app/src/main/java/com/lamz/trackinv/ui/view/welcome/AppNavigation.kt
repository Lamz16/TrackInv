package com.lamz.trackinv.ui.view.welcome

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lamz.trackinv.ui.navigation.Screen
import com.lamz.trackinv.ui.screen.login.LoginScreen
import com.lamz.trackinv.ui.screen.register.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
    ) {
        composable(Screen.Login.route) {
            // Your main composable function
            LoginScreen(navController = navController)
        }
        composable(Screen.Register.route) {
            // Composable function for registration screen
            RegisterScreen(navController = navController)

        }
    }
}