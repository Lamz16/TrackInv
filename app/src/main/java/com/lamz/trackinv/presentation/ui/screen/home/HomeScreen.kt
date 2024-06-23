package com.lamz.trackinv.presentation.ui.screen.home

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.lamz.trackinv.R
import com.lamz.trackinv.domain.model.TransaksiModel
import com.lamz.trackinv.presentation.model.home.HomeViewModel
import com.lamz.trackinv.presentation.ui.component.CardItem2
import com.lamz.trackinv.presentation.ui.component.CardItemTransactionsUpdate
import com.lamz.trackinv.presentation.ui.component.TextItem
import com.lamz.trackinv.presentation.ui.navigation.Screen
import com.lamz.trackinv.presentation.ui.state.UiState
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

        when (val state = allTransactionState) {
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
                HomeContent(
                    navController = navController,
                    listTransaksi = state.data,
                    viewModel = viewModel,
                    modifier = Modifier
                )
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    modifier: Modifier,
    listTransaksi: List<TransaksiModel> = emptyList(),
    navController: NavHostController,
    viewModel: HomeViewModel
) {

    val focusRequester = remember { FocusRequester() }
    val isFocused by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocused }

    var allTransactions by remember { mutableStateOf(emptyList<TransaksiModel>()) }
    allTransactions = listTransaksi


    LaunchedEffect(true) {
        if (wasFocused) {
            focusRequester.requestFocus()
        }
    }


    Column(
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Toko Sembako As-salam",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp,
                )
            },
            actions = {}
        )

        Column {
            AlertStock(modifier = modifier, viewModel = viewModel, navController = navController)
            AddTransaction(navController = navController)

            TextItem(
                desc = stringResource(id = R.string.last_update),
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
            )
        }
        ListUpdatedTrans(allTransactions = allTransactions)
    }
}

@Composable
private fun AlertStock(
    modifier: Modifier,
    viewModel: HomeViewModel,
    navController: NavHostController
) {
    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        StockTersediaAlert(
            modifier = modifier,
            viewModel = viewModel,
            navController = navController
        )
        StockMenipisALert(
            modifier = modifier,
            viewModel = viewModel,
            navController = navController
        )
        StockHabisAlert(
            modifier = modifier,
            viewModel = viewModel,
            navController = navController
        )

    }
}


@Composable
private fun AddTransaction(
    navController: NavHostController
) {
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
}


@Composable
private fun ListUpdatedTrans(
    allTransactions: List<TransaksiModel>
) {
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
        } else {
            items(allTransactions) { transactions ->
                CardItemTransactionsUpdate(
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


