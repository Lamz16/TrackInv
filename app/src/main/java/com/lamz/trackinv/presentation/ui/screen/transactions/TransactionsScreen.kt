package com.lamz.trackinv.presentation.ui.screen.transactions

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.ContentAlpha
import com.lamz.trackinv.domain.model.TransaksiModel
import com.lamz.trackinv.presentation.model.transactions.TransactionViewModel
import com.lamz.trackinv.presentation.ui.component.CardItemTransactions
import com.lamz.trackinv.presentation.ui.component.SearchBar
import com.lamz.trackinv.presentation.ui.state.UiState
import kotlinx.coroutines.delay

@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    viewModel : TransactionViewModel = hiltViewModel()
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

        val allTransactionState by viewModel.getTransState.collectAsState()


        when(val state = allTransactionState){
            is UiState.Error -> {
                Text(text = state.errorMessage, modifier = Modifier.align(Alignment.Center))
            }
            UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                LaunchedEffect(key1 = true, block = {
                    delay(500L)
                    viewModel.getAllTransactions()

                })

            }
            is UiState.Success -> {
                TransactionsContent(modifier = Modifier.padding(16.dp), listTrans =  state.data)
            }
        }


    }
}

@Composable
fun TransactionsContent(
    modifier: Modifier,
    listTrans : List<TransaksiModel> = emptyList()
) {
    var query by remember { mutableStateOf(TextFieldValue()) }
    val isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(true) {
        if (isFocused) {
            focusRequester.requestFocus()
        }
    }

    var allTransactions by remember { mutableStateOf(emptyList<TransaksiModel>()) }
    var filteredTransactions by remember { mutableStateOf(emptyList<TransaksiModel>()) }

    allTransactions = listTrans
    filteredTransactions = allTransactions

    LaunchedEffect(query) {
        filteredTransactions = allTransactions.filter { transaction ->
            val searchText = query.text.lowercase()
            (transaction.namaPartner?.lowercase()?.contains(searchText) ?: false) or
                    (transaction.jenisTran?.lowercase()?.contains(searchText) ?: false)
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            state = rememberLazyListState(),
            contentPadding = PaddingValues(top = 80.dp, bottom = 80.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {

            if (filteredTransactions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Data barang kosong. Tambahkan terlebih dahulu.")
                    }
                }
            }else{
                items(filteredTransactions) { transactions ->
                    Log.d("Data Transaksi", "TransactionsContent: $transactions")
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

        SearchBar(
            value = query.text,
            onValueChange = { query = TextFieldValue(it) },
            placeholder = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Search...")
                }
            },
            onSearch = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTransactions() {
    TransactionsScreen()
}