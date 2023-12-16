package com.lamz.trackinv.ui.view.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lamz.trackinv.ui.component.BottomBar
import com.lamz.trackinv.ui.navigation.Screen
import com.lamz.trackinv.ui.screen.add.AddProductScreen
import com.lamz.trackinv.ui.screen.add.AddScreen
import com.lamz.trackinv.ui.screen.home.HomeScreen
import com.lamz.trackinv.ui.screen.inventory.InventoryScreen
import com.lamz.trackinv.ui.screen.inventory.detail.InvDetailScreen
import com.lamz.trackinv.ui.screen.membership.MembershipScreen
import com.lamz.trackinv.ui.screen.partner.CustomerScreen
import com.lamz.trackinv.ui.screen.partner.IncomingScreen
import com.lamz.trackinv.ui.screen.partner.SupplierScreen
import com.lamz.trackinv.ui.screen.partner.OutGoingScreen
import com.lamz.trackinv.ui.screen.transactions.TransactionsScreen
import com.lamz.trackinv.ui.theme.TrackInvTheme

@Composable
fun TrackInvApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.DetailInventory.route &&
                currentRoute != Screen.Add.route &&
                currentRoute != Screen.AddProduct.route &&
                currentRoute != Screen.Membership.route &&
                currentRoute != Screen.Customer.route &&
                currentRoute != Screen.In.route &&
                currentRoute != Screen.Supplier.route &&
                currentRoute != Screen.Out.route
            ) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }
            composable(Screen.Membership.route) {
                MembershipScreen()
            }
            composable(Screen.Inventory.route) {
                InventoryScreen(navController = navController,
                    navigateToDetail = { inventoryId ->
                        navController.navigate(Screen.DetailInventory.createRoute(inventoryId))
                    })
            }
            composable(
                route = Screen.DetailInventory.route,
                arguments = listOf(navArgument("inventoryId") { type = NavType.StringType })
            ) {
                // Composable function for registration screen
                val inventoryId = it.arguments?.getString("inventoryId") ?: " "
                InvDetailScreen(
                    inventoryId = inventoryId,
                    navController = navController
                )

            }

            composable(Screen.Transactions.route) {
                TransactionsScreen()
            }

            composable(Screen.Add.route) {
                // Your main composable function
                AddScreen(navController = navController,
                    navigateToDetail = { categoryId ->
                        navController.navigate(Screen.AddProduct.createRoute(categoryId))
                    })
            }
            composable(
                route = Screen.AddProduct.route,
                arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
            ) {
                // Composable function for registration screen
                val categoryId = it.arguments?.getString("categoryId") ?: "sembako"
                AddProductScreen(
                    categoryId = categoryId,
                    navController = navController
                )

            }
            composable(route = Screen.Customer.route){
                CustomerScreen(navController = navController,
                    navigateToDetail = { idCustomer ->
                        navController.navigate(Screen.Out.createRoute(idCustomer))
                    })
            }

            composable(route = Screen.Out.route,
                arguments = listOf(navArgument("idCustomer") { type = NavType.StringType })){
                val idCustomer = it.arguments?.getString("idCustomer") ?: "s"
                OutGoingScreen(
                    idCustomer = idCustomer
                )
            }

            composable(route = Screen.Supplier.route){
                SupplierScreen(navController = navController,
                    navigateToDetail = { idSupplier ->
                        navController.navigate(Screen.In.createRoute(idSupplier))
                    })
            }

            composable(route = Screen.In.route,
                arguments = listOf(navArgument("idSupplier") { type = NavType.StringType })){
                val idSupplier = it.arguments?.getString("idSupplier") ?: "s"
                IncomingScreen(
                    idCustomer = idSupplier
                )
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun TrackInvAppPreview() {
    TrackInvTheme {
        TrackInvApp()
    }
}