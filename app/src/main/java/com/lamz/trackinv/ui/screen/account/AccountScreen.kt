package com.lamz.trackinv.ui.screen.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lamz.trackinv.R
import com.lamz.trackinv.data.pref.UserModel

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AccountContent(modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun AccountContent(
    modifier: Modifier
){

    Column {
        Text(text = "Sukardi")

        ElevatedButton(
            onClick = {
                // Set showDialog to true when the button is clicked

            },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = colorResource(id = R.color.Yellow)
            ),
        ) {
            Text("Logout")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun previewAccount(){
AccountScreen()
}