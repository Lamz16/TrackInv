package com.lamz.trackinv.presentation.ui.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable () -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = placeholder,
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = {
                onSearch()
                isFocused = false
            },
            onSearch = {
                onSearch()
                isFocused = false
            }
        ),
        modifier = modifier
            .onFocusChanged {
                isFocused = it.isFocused
            }
    )
}
