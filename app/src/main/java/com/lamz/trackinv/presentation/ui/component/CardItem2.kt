package com.lamz.trackinv.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamz.trackinv.R

@Composable
fun CardItem2(
    image : Int,
    title: String,
    modifier : Modifier = Modifier
)
{
    Card(modifier = Modifier
        .padding(16.dp)
        .size(90.dp)
        .clip(RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.black40)
        )
    ){
        Column (modifier = modifier,
            verticalArrangement = Arrangement.Center

        ){
            Image(painter = painterResource(image),
                contentDescription = null,
                modifier= Modifier
                    .size(40.dp)
                    .padding(top = 10.dp, start = 10.dp)
                    .align(CenterHorizontally))

            Text(
                text = title,
                modifier = Modifier
                    .padding(10.dp),
                color = colorResource(id = R.color.white),
                fontSize = 10.sp
            )
        }
    }
}
