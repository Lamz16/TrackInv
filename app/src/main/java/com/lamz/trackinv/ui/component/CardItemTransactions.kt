package com.lamz.trackinv.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamz.trackinv.R
import com.lamz.trackinv.utils.convertStringToCalendar
import java.util.Calendar

@Composable
fun CardItemTransactions(
    type : String,
    nama : String,
    tipe : String,
    waktu : String,
    modifier: Modifier = Modifier,
) {

    val calendar = convertStringToCalendar(waktu)
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .size(100.dp)
        .clip(RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.lavender)
        )

    ){
        Column (
        modifier = modifier
    ) {
        Row (
            modifier = modifier
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_list_barang),
                contentDescription = null,
                modifier = modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 5.dp, top = 5.dp)
                    .background(
                        colorResource(id = R.color.white), shape = RoundedCornerShape(8.dp)
                    )
            )
            Text(
                text = type,
                fontSize = 15.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                color = Color.Blue,
                modifier = modifier
                    .padding(top = 10.dp)
            )
        }
        Divider(
            thickness = 1.dp, color = Color.Black
        )
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 25.dp, top = 10.dp)
        ){
            Text(
                text = nama
            )
            Text(
                text = tipe
            )
            Text(
                text = "$dayOfMonth-$month-$year"
            )
        }
    }

    }
}

@Preview(showBackground = true)
@Composable
fun CardtransactionsPreview(){
    CardItemTransactions("Keluar","John Doe","customer","2020-11-01T00:00:00.000Z")
}