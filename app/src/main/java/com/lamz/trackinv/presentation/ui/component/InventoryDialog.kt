package com.lamz.trackinv.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.lamz.trackinv.domain.model.BarangModel
import com.lamz.trackinv.presentation.ui.theme.black40
import com.lamz.trackinv.presentation.ui.theme.yellow
import java.text.NumberFormat
import java.util.Locale

@Composable
fun InventoryDialog(
    modifier: Modifier = Modifier,
    barang: BarangModel? = null,
    navigateToDetail: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier
                .clip(RoundedCornerShape(16.dp))
                .background(colorResource(id = R.color.lavender))
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
                    text = "Detail Barang",
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
                modifier.padding(
                    horizontal = 16.dp,
                )
            ) {
                Column {
                    Text(
                        text = "Nama Barang",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )

                    Text(
                        text = "Stok Barang",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )

                    Text(
                        text = "Harga Beli",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                    Text(
                        text = "Harga Jual",
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
                }
                Column {
                    Text(
                        text = barang?.namaBarang ?: "",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                    Text(
                        text = "${barang?.stokBarang}",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                    val buy = barang?.buy?.toInt()
                    val formattedBuy =
                        NumberFormat.getNumberInstance(Locale("id", "ID")).format(buy)

                    Text(
                        text = formattedBuy,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                    val sell = barang?.sell?.toInt()
                    val formattedSell =
                        NumberFormat.getNumberInstance(Locale("id", "ID")).format(sell)
                    Text(
                        text = formattedSell ?: "",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        modifier = modifier
                            .padding(end = 10.dp)
                    )
                }
            }

            Row {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        onDismissRequest()
                        navigateToDetail(barang?.idBarang ?: "")
                    },
                    colors = ButtonDefaults.buttonColors(black40),
                    shape = RoundedCornerShape(4.dp),
                    modifier = modifier.size(72.dp, height = 36.dp)
                ) {
                    Text(text = "Edit", color = yellow, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {

    InventoryDialog(
        navigateToDetail = {},
        onDismissRequest = {},
        barang = BarangModel(namaBarang = "Beras", stokBarang = 5, buy = "5000", sell = "5000")
    )
}