package com.lamz.trackinv.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun TextItem(
    desc: String,
    modifier: Modifier = Modifier
) {

    Text(
        text = desc,
        modifier = Modifier
            .padding(start = 24.dp, top = 60.dp)
    )
}