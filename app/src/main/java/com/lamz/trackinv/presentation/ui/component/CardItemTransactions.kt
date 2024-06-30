package com.lamz.trackinv.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamz.trackinv.R
import com.lamz.trackinv.domain.model.TransaksiModel
import com.lamz.trackinv.presentation.ui.theme.green
import java.text.NumberFormat
import java.util.Locale


@Composable
fun CardItemTransactions(
    modifier: Modifier = Modifier,
    transaksi: TransaksiModel? = null,
) {
    var showDetail by remember { mutableStateOf(false) }

    fun showPopupDetail() {
        showDetail = true
    }

    fun closePopupDetail() {
        showDetail = false
    }

    val nominal = transaksi?.nominal?.toInt()
    val formattedNominal = NumberFormat.getNumberInstance(Locale("id", "ID")).format(nominal)
    val color = if (transaksi?.jenisTran?.lowercase() == "masuk") {
        Color.Red
    }else{
        green
    }
    val jenisTrans = if (transaksi?.jenisTran?.lowercase() == "masuk") {
        stringResource(id = R.string.tran_masuk, formattedNominal)
    }else{
        stringResource(id = R.string.tran_keluar, formattedNominal)
    }

    if (showDetail){
        transaksi?.let {
            TransactionsDialog(
                transactions = it,
                onDismissRequest = { closePopupDetail() })
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .size(100.dp)
            .clickable { showPopupDetail() }
            .clip(RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.lavender)
        )

    ) {
        Column(
            modifier = modifier
        ) {
            Row(
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
                        .size(24.dp)
                )
                Text(
                    text = transaksi?.jenisTran.toString(),
                    fontSize = 15.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    modifier = modifier
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = jenisTrans,
                    fontSize = 15.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    modifier = modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 10.dp)
                )

            }
            HorizontalDivider(
                thickness = 1.dp, color = Color.Black
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 25.dp, top = 10.dp)
            ) {
                Text(
                    text = transaksi?.namaBarang ?: "",
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.weight(1f)
                )
                Text(
                    text = transaksi?.namaPartner ?: "",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.weight(1f)
                )
                Text(
                    text = transaksi?.tglTran ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.weight(1f)
                )
            }
        }

    }
}

