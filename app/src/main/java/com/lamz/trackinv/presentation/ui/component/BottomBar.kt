package com.lamz.trackinv.presentation.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lamz.trackinv.R
import com.lamz.trackinv.presentation.ui.navigation.NavigationItem
import com.lamz.trackinv.presentation.ui.navigation.Screen

@Composable
fun BottomBar(
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
                title = stringResource(R.string.menu_prediction),
                icon = ImageBitmap.imageResource(id = R.drawable.ic_predictive),
                screen = Screen.Prediksi
            ),

            )
        navigationItems.map { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        bitmap = item.icon,
                        contentDescription = item.title,
                        tint = colorResource(id = R.color.Yellow),
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(item.title, color = colorResource(id = R.color.Yellow)) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = false
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }

    }

}