package com.lamz.trackinv.presentation.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@SuppressLint("NotConstructor")
@Composable
fun OutLinedTextItem(
    input: String,
    onValueChange : (String) -> Unit,
    text: String,
    containerColor: Color,
    keyboardType: KeyboardType
) {

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }


    OutlinedTextField(
        value = input,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
        ),
        label = { Text(text = text) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(size = 20.dp),
        modifier = Modifier
            .padding(bottom = 24.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
            },
    )


}
