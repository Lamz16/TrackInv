package com.lamz.trackinv.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.lamz.trackinv.R

@Composable
fun CardLongItem(modifier: Modifier = Modifier
   

){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .size(100.dp)
        .clip(RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.lavender)
        )
        
    ){
        
    }
}