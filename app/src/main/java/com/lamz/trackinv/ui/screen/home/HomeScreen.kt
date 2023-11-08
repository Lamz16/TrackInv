package com.lamz.trackinv.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lamz.trackinv.R

@Composable
fun HomeScreen(){

}


@Composable
fun HomeContent(){
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 48.dp)
        ) {
            Card(modifier = Modifier
                .padding(16.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.black40)
                )

            ){

            }

            Card(modifier = Modifier
                .padding(16.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.black40)
                )

            ){

            }

            Card(modifier = Modifier
                .padding(16.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.black40)
                )

            ){

            }
        }

        Row (Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center){
            Card(modifier = Modifier
                .padding(16.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.black40)
                )

            ){

            }

            Card(modifier = Modifier
                .padding(16.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.black40)
                )

            ){

            }

        }

        Text(text = stringResource(id = R.string.last_update),
            modifier = Modifier
                .padding( start = 24.dp,
                    top = 60.dp)
        )

        Column {
            Card(modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.lavender)
                )

            ){

            }
        }

    }


}


@Preview(showBackground = true)
@Composable
fun HomePreview(){
HomeContent()
}