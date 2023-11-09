package com.lamz.trackinv.ui.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun InputTextItem(
    label : String,
    type : KeyboardOptions,
    modifier : Modifier = Modifier
){
    var input by remember { mutableStateOf("") }

    OutlinedTextField(
        value = input,
        label = { Text(text = label) },
        keyboardOptions = type,
        onValueChange = { newInput ->
            input = newInput
        },
    )
}