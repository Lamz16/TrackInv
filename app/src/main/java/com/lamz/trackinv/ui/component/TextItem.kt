package com.lamz.trackinv.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit


@Composable
fun TextItem(
    modifier: Modifier = Modifier,
    desc: String,
    fontWeight: FontWeight? = null,
    fontSize : TextUnit = TextUnit.Unspecified,
) {

    Text(
        fontSize = fontSize,
        fontWeight = fontWeight,
        text = desc,
        modifier = modifier
    )
}