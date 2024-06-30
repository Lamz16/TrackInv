package com.lamz.trackinv.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamz.trackinv.R
import com.lamz.trackinv.domain.model.BarangModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CardLongInventory(
    inventory : BarangModel,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit,
){
    val sell = inventory.sell?.toInt()
    val buy = inventory.buy?.toInt()
    val formattedPriceJual = NumberFormat.getNumberInstance(Locale("id", "ID")).format(sell)
    val formattedPriceBeli = NumberFormat.getNumberInstance(Locale("id", "ID")).format(buy)
    var showDetail by remember { mutableStateOf(false) }

    fun showPopupDetail() {
        showDetail = true
    }

    fun closePopupDetail() {
        showDetail = false
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .size(100.dp)
        .clip(RoundedCornerShape(10.dp))
        .clickable { showPopupDetail() },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.lavender)
        )

    ){
        Column (
        modifier = modifier
    ) {

        if (showDetail) {
            InventoryDialog(
                barang = inventory,
                navigateToDetail =  navigateToDetail,
                onDismissRequest = { closePopupDetail() })
        }

        Row (
            modifier = modifier
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_list_barang),
                contentDescription = null,
                modifier = modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .background(
                        colorResource(id = R.color.white), shape = RoundedCornerShape(8.dp)
                    )
            )
            Text(
                text = inventory.namaBarang ?: "",
                fontSize = 15.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                color = Color.Blue,
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )
        }
        HorizontalDivider(
            thickness = 1.dp, color = Color.Black
        )
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 25.dp)
        ){
            Text(
                text = stringResource(id = R.string.stok)
            )
            Text(
                text = stringResource(id = R.string.harga_jual)
            )
            Text(
                text = stringResource(id = R.string.harga_beli)
            )
        }
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 20.dp, top = 10.dp)
        ){
            Text(
                text = inventory.stokBarang.toString()
            )
            Text(
                text = stringResource(id = R.string.id_inventory, formattedPriceJual)
            )
            Text(
                text = stringResource(id = R.string.id_inventory, formattedPriceBeli)
            )
        }
    }

    }
}