package com.lamz.trackinv.presentation.ui.screen.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.lamz.trackinv.R
import com.lamz.trackinv.domain.model.BarangModel
import com.lamz.trackinv.domain.model.TransaksiModel
import com.lamz.trackinv.presentation.model.home.HomeViewModel
import com.lamz.trackinv.presentation.ui.component.CardItem1
import com.lamz.trackinv.presentation.ui.component.CardItem2
import com.lamz.trackinv.presentation.ui.component.CardItemTransactions
import com.lamz.trackinv.presentation.ui.component.TextItem
import com.lamz.trackinv.presentation.ui.navigation.Screen
import com.lamz.trackinv.presentation.ui.state.UiState
import com.lamz.trackinv.utils.FirebaseUtils
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    Box(
        Modifier
            .fillMaxSize()
    ) {

        val allTransactionState by viewModel.getTransState.collectAsState()

        when(val state = allTransactionState){
            is UiState.Error -> {
                Text(text = state.errorMessage)
            }
            UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                LaunchedEffect(key1 = true, block = {
                    delay(500L)
                    viewModel.getAllUpdatedTransactions()

                })

            }
            is UiState.Success -> {
                HomeContent(navController = navController, listTransaksi = state.data)
            }
        }


    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    context: Context = LocalContext.current,
    listTransaksi : List<TransaksiModel> = emptyList(),
    navController: NavHostController,
) {

    val focusRequester = remember { FocusRequester() }
    val isFocused by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocused }

    val dbBarang = FirebaseUtils.dbBarang
    var allProducts by remember { mutableStateOf(emptyList<BarangModel>()) }
    var productTersedia by remember { mutableIntStateOf(0) }
    var productMenipis by remember { mutableIntStateOf(0) }
    var productHabis by remember { mutableIntStateOf(0) }

    var allTransactions by remember { mutableStateOf(emptyList<TransaksiModel>()) }
    allTransactions = listTransaksi

    LaunchedEffect(true) {
        if (wasFocused) {
            focusRequester.requestFocus()
        }

        dbBarang.orderByChild("stokBarang").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempTersediaList = mutableListOf<BarangModel>()
                val tempMenipisList = mutableListOf<BarangModel>()
                val tempHabisList = mutableListOf<BarangModel>()

                productTersedia = 0
                productMenipis = 0
                productHabis = 0

                for (barangSnapshot in snapshot.children) {
                    val barang = barangSnapshot.getValue(BarangModel::class.java)
                    if (barang != null) {
                        val stokBarang = barang.stokBarang?.toLong() ?: 0
                        when {
                            stokBarang > 49L -> {
                                tempTersediaList.add(barang)
                                productTersedia++
                            }
                            stokBarang in 1L..49L -> {
                                tempMenipisList.add(barang)
                                productMenipis++
                            }
                            stokBarang == 0L -> {
                                tempHabisList.add(barang)
                                productHabis++
                            }
                        }
                    }
                }
                allProducts = tempTersediaList + tempMenipisList + tempHabisList
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Gagal membaca data barang", Toast.LENGTH_SHORT).show()
            }
        })

    }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {

                Text(text = "Toko Sembako As-salam",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp,
                )

            },
            actions = {
            }
        )

        Column {
            Row(
                Modifier
                    .fillMaxWidth(),
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

        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier.padding(top = 8.dp)
        ) {

            if (allTransactions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Tidak ada update terbaru hari ini")
                    }
                }
            }else{
                items(allTransactions) { transactions ->
                    CardItemTransactions(
                        nama = transactions.namaBarang ?: "",
                        waktu = transactions.tglTran ?: "",
                        harga = transactions.nominal ?: "",
                        type = transactions.jenisTran ?: "",
                        tipe = transactions.namaPartner ?: ""
                    )

                }
            }
        }
    }




}


@Preview(showBackground = true)
@Composable
fun HomePreview() {

}