package com.lamz.trackinv.ui.screen.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.lamz.trackinv.R
import com.lamz.trackinv.data.model.BarangModel
import com.lamz.trackinv.ui.component.CardItem1
import com.lamz.trackinv.ui.component.CardItem2
import com.lamz.trackinv.ui.component.TextItem
import com.lamz.trackinv.ui.navigation.Screen
import com.lamz.trackinv.utils.FirebaseUtils
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    HomeContent(navController = navController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    context: Context = LocalContext.current,
    viewModel: HomeViewModel = koinViewModel(),
    navController: NavHostController,
) {

    val sessionData by viewModel.getSession().observeAsState()

    var showDialog by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val isFocused by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocused }

    val dbBarang = FirebaseUtils.dbBarang
    var allProducts by remember { mutableStateOf(emptyList<BarangModel>()) }
    var productTersedia by remember { mutableStateOf(0) }
    var productMenipis by remember { mutableStateOf(0) }
    var productHabis by remember { mutableStateOf(0) }


    LaunchedEffect(true) {
        if (wasFocused) {
            focusRequester.requestFocus()
        }

        val query = dbBarang.orderByChild("stokBarang").startAt("50")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<BarangModel>()
                productTersedia = 0

                for (barangSnapshot in snapshot.children) {
                    val barang = barangSnapshot.getValue(BarangModel::class.java)
                    if (barang != null) {
                        tempList.add(barang)
                        productTersedia++
                    }
                }
                allProducts = tempList
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Gagal membaca data barang", Toast.LENGTH_SHORT).show()
            }
        })

        dbBarang.orderByChild("stokBarang").startAt("0").endAt("49")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tempList = mutableListOf<BarangModel>()
                    productMenipis = 0 // Reset productMenipis before update

                    for (barangSnapshot in snapshot.children) {
                        val barang = barangSnapshot.getValue(BarangModel::class.java)
                        if (barang != null) {
                            tempList.add(barang)
                            if ((barang.stokBarang?.toInt() ?: 0) < 50) {
                                productMenipis++ // Increment if stock is less than 50
                            }
                        }
                    }
                    allProducts = tempList
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Gagal membaca data barang", Toast.LENGTH_SHORT).show()
                }
            })

        val query2 = dbBarang.orderByChild("stokBarang").equalTo("0")
        query2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<BarangModel>()
                productHabis = 0 // Reset productHabis before update

                for (barangSnapshot in snapshot.children) {
                    val barang = barangSnapshot.getValue(BarangModel::class.java)
                    if (barang != null) {
                        tempList.add(barang)
                        productHabis++ // Increment if stock is 0
                    }
                }
                allProducts = tempList
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Gagal membaca data barang", Toast.LENGTH_SHORT).show()
            }
        })

    }

    TopAppBar(
        title = {
            sessionData?.let {
                Text(
                    it.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp,
                )
            }
        },
        actions = {
            IconButton(onClick = {
                showDialog = true
            }) {
                Icon(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.ic_sign_out),
                    contentDescription = "Logout",
                    tint = colorResource(id = R.color.Yellow)
                )
            }
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Handle dialog dismissal if needed
                showDialog = false
            },
            title = {
                Text("Logout")
            },
            text = {
                Text("Yakin ingin logout ?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.logout()
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = colorResource(id = R.color.Yellow)
                    )
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.Black
                    )
                ) {
                    Text("No")
                }
            }
        )
    }

    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 58.dp),
            horizontalArrangement = Arrangement.Center
        ) {


            CardItem1(
                R.drawable.ic_stok_tersedia,
                productTersedia.toString(),
                stringResource(id = R.string.tersedia)
            )
            CardItem1(
                R.drawable.ic_menipis,
                productMenipis.toString(),
                stringResource(id = R.string.menipis)
            )
            CardItem1(
                R.drawable.ic_stok_habis,
                productHabis.toString(),
                stringResource(id = R.string.habis)
            )
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CardItem2(
                R.drawable.ic_stok_masuk,
                stringResource(id = R.string.stok_masuk),
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Supplier.route)
                })


            CardItem2(
                R.drawable.ic_stok_keluar,
                stringResource(id = R.string.stok_keluar),
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Customer.route)
                })


        }

        TextItem(
            desc = stringResource(id = R.string.last_update), modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
        )
    }


}


@Preview(showBackground = true)
@Composable
fun HomePreview() {

}