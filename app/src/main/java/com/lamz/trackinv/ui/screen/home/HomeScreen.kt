package com.lamz.trackinv.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lamz.trackinv.R
import com.lamz.trackinv.ui.component.CardItem1
import com.lamz.trackinv.ui.component.CardItem2
import com.lamz.trackinv.ui.component.CardLongItem
import com.lamz.trackinv.ui.component.TextItem

@Composable
fun HomeScreen(){
HomeContent()
}



@Composable
fun HomeContent(){
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            CardItem1(R.drawable.ic_stok_tersedia, stringResource(id = R.string.tersedia))
            CardItem1(R.drawable.ic_menipis, stringResource(id = R.string.menipis))
            CardItem1(R.drawable.ic_stok_habis, stringResource(id = R.string.habis))
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            CardItem2(R.drawable.ic_stok_masuk, stringResource(id = R.string.stok_masuk))
            CardItem2(R.drawable.ic_stok_keluar, stringResource(id = R.string.stok_keluar))
        }

        TextItem(desc = stringResource(id = R.string.last_update))

        CardLongItem(namaItem = "Gula", pieces = 200, category = "Sembako", id = 39210237L)
    }


}


@Preview(showBackground = true)
@Composable
fun HomePreview(){
HomeContent()
}