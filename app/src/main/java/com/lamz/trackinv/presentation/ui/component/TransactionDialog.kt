package com.lamz.trackinv.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.window.Dialog
import com.lamz.trackinv.R
import com.lamz.trackinv.domain.model.TransaksiModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TransactionsDialog(
    modifier: Modifier = Modifier,
    transactions: TransaksiModel,
    onDismissRequest: () -> Unit,
){
    Dialog(onDismissRequest = {onDismissRequest()}) {
        Column(
            modifier
                .clip(RoundedCornerShape(16.dp))
                .background(colorResource(id = R.color.lavender))
                .wrapContentSize()
                .padding(16.dp)
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

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Detail Transaksi",
                    fontSize = 15.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 10.dp)
                )
                Spacer(modifier = Modifier.weight(1f))

            }
            HorizontalDivider(
                thickness = 1.dp, color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(
                    horizontal = 8.dp,
                )
            ) {
               Column {
                   Text(
                       text = "Jenis Transaksi",
                       fontSize = 12.sp,
                       fontStyle = FontStyle.Normal,
                       modifier = modifier
                           .padding(end = 10.dp)
                   )

                   Text(
                       text = "Nama Partner",
                       fontSize = 12.sp,
                       fontStyle = FontStyle.Normal,
                       modifier = modifier
                           .padding(end = 10.dp)
                   )

                   Text(
                       text = "Nama Barang",
                       fontSize = 12.sp,
                       fontStyle = FontStyle.Normal,
                       modifier = modifier
                           .padding(end = 10.dp)
                   )
                   Text(
                       text = "Jumlah",
                       fontSize = 12.sp,
                       fontStyle = FontStyle.Normal,
                       modifier = modifier
                           .padding(end = 10.dp)
                   )
                   Text(
                       text = "Nominal",
                       fontSize = 12.sp,
                       fontStyle = FontStyle.Normal,
                       modifier = modifier
                           .padding(end = 10.dp)
                   )
                   Text(
                       text = "Tanggal Transaksi",
                       fontSize = 12.sp,
                       fontStyle = FontStyle.Normal,
                       modifier = modifier
                           .padding(end = 10.dp)
                   )

               }
                Column {
                    Text(
                        text = ":",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                    Text(
                        text = ":",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                    Text(
                        text = ":",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                    Text(
                        text = ":",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                    Text(
                        text = ":",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                    Text(
                        text = ":",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                }
                Column{
                    Text(
                        text = transactions.jenisTran ?: "",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                    Text(
                        text = transactions.namaPartner ?: "",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                    Text(
                        text = transactions.namaBarang ?: "",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )

                    Text(
                        text = transactions.jumlah ?: "",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                    val nominal = transactions.nominal?.toInt()
                    val formattedNominal = NumberFormat.getNumberInstance(Locale("id", "ID")).format(nominal)

                    Text(
                        text = formattedNominal,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )

                    Text(
                        text = transactions.tglTran ?: "",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                }
            }

        }
    }

}


@Composable
@Preview
private fun Preview(){
    TransactionsDialog(transactions = TransaksiModel(
        "1",
        "Masuk",
        "Martik",
        "Beras",
        "2",
        "32000",
        "27-04-2024",

        ), onDismissRequest = {})
}