package com.lamz.trackinv.ui.view.add

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            AddScreen(navController = navController,
                navigateToDetail = {categoryId ->
                    navController.navigate(Screen.AddProduct.createRoute(categoryId))
                })
        }
        composable(
            route = Screen.AddProduct.route,
            arguments = listOf(navArgument("categoryId") {type = NavType.StringType })
            ) {
            // Composable function for registration screen
            val categoryId = it.arguments?.getString("categoryId") ?: "sembako"
            AddProductScreen(
                categoryId = categoryId,
                navController = navController)

        }
        // Add more destinations as needed
    }
}