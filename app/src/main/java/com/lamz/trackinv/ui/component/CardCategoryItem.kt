package com.lamz.trackinv.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamz.trackinv.R

@Composable
fun CardCategoryItem(
    modifier: Modifier = Modifier,
    nameCategory: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .size(50.dp)
            .clip(RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.lavender)
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp)
        ) {
            Text(
                text = nameCategory,
                fontSize = 15.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                color = Color.Blue,
                modifier = modifier
                    .padding(top = 10.dp)
            )

            Image(
                imageVector = Icons.Default.ArrowForwardIos, contentDescription = null,
                alignment = Alignment.CenterEnd,
                colorFilter = ColorFilter.tint(Color.Blue),
                modifier = modifier
                    .padding(top = 10.dp, start = 10.dp)
            )
        }
    }
}