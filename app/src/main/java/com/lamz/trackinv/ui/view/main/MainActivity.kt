package com.lamz.trackinv.ui.view.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lamz.trackinv.R
import com.lamz.trackinv.ViewModelFactory
import com.lamz.trackinv.ui.theme.TrackInvTheme
import com.lamz.trackinv.ui.view.welcome.AppNavigation

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getSession().observe(this) { user ->

            setContent {
                val systemUiController = rememberSystemUiController()
                val colors = colorResource(id = R.color.black)
                val useDarkIcons = !isSystemInDarkTheme()
                val colorsYellow = colorResource(id = R.color.Yellow)
                DisposableEffect(systemUiController, useDarkIcons) {

                    systemUiController.setStatusBarColor(
                        color = colorsYellow,
                        darkIcons = useDarkIcons
                    )

                    systemUiController.setNavigationBarColor(
                        color = colors,
                        darkIcons = useDarkIcons
                    )
                    onDispose {}
                }
                TrackInvTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        if (!user.isLogin) {
                            AppNavigation()
                        } else {
                            TrackInvApp()
                        }
                    }
                }
            }
        }
    }
}