package com.lamz.trackinv.presentation.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.lamz.trackinv.R

@Composable
fun AlertDialogMessage(
    modifier: Modifier,
    title : String,
    desc : String,
){
    var showInputDialogOutgoing by remember { mutableStateOf(true)}
    if (showInputDialogOutgoing){
        AlertDialog(
            onDismissRequest = {
                showInputDialogOutgoing = false
            },
            title = {
                Text(title)
            },
            text = {
                val containerColor = colorResource(id = R.color.lavender)
                Text(desc, color = Color.Black)
            },
            confirmButton = {
                Button(
                    onClick = {

                        showInputDialogOutgoing = false
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = colorResource(id = R.color.Yellow)
                    )
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showInputDialogOutgoing = false
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.Black
                    )
                ) {
                    Text("No")
                }
            }
        )
    }

}