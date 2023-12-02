package com.lamz.trackinv.ui.view.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lamz.trackinv.R
import com.lamz.trackinv.ui.navigation.NavigationItem
import com.lamz.trackinv.ui.navigation.Screen
import com.lamz.trackinv.ui.screen.account.AccountScreen
import com.lamz.trackinv.ui.screen.add.AddProductScreen
import com.lamz.trackinv.ui.screen.add.AddScreen
import com.lamz.trackinv.ui.screen.home.HomeScreen
import com.lamz.trackinv.ui.screen.inventory.InventoryScreen
import com.lamz.trackinv.ui.screen.inventory.detail.InvDetailScreen
import com.lamz.trackinv.ui.screen.transactions.TransactionsScreen
import com.lamz.trackinv.ui.theme.TrackInvTheme

@Composable
private fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = colorResource(id = R.color.black40)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = ImageBitmap.imageResource(id = R.drawable.ic_home),
                screen = Screen.Home,
            ),
            NavigationItem(
                title = stringResource(R.string.menu_inventory),
                icon = ImageBitmap.imageResource(id = R.drawable.ic_inventory),
                screen = Screen.Inventory
            ),
            NavigationItem(
                title = stringResource(R.string.menu_transactions),
                icon = ImageBitmap.imageResource(id = R.drawable.ic_transactions),
                screen = Screen.Transactions
            ),
            NavigationItem(
                title = stringResource(R.string.menu_profile),
                icon = ImageBitmap.imageResource(id = R.drawable.ic_profile),
                screen = Screen.Profile
            ),

            )
        navigationItems.map { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        bitmap = item.icon,
                        contentDescription = item.title,
                        tint = colorResource(id = R.color.Yellow)
                    )
                },
                label = { Text(item.title, color = colorResource(id = R.color.Yellow)) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }

    }

}


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
                currentRoute != Screen.AddProduct.route
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
                HomeScreen()
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

            composable(Screen.Profile.route) {
                AccountScreen()
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