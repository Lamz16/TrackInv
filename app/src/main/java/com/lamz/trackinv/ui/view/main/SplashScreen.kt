package com.lamz.trackinv.ui.view.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lamz.trackinv.R
import com.lamz.trackinv.ui.view.main.ui.theme.TrackInvTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackInvTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaunchedEffect(Unit) {
                        delay(3000)
                        navigateToNextScreen()
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Logo
                            Image(
                                painter = painterResource(id = R.drawable.ic_splash),
                                contentDescription = "Track Inventory Logo",
                                modifier = Modifier.size(200.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            // Title
                            Text(
                                text = "TRACK INVENTORY",
                                style = MaterialTheme.typography.headlineMedium,
                                color = colorResource(id = R.color.Yellow)
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            // Subtitle
                            Text(
                                text = "Atur stok barang tanpa ribet!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorResource(id = R.color.Yellow)
                            )

                        }
                    }

                }
            }
        }
    }

    private fun navigateToNextScreen() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

