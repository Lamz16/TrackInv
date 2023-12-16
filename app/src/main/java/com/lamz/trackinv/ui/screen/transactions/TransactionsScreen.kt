package com.lamz.trackinv.ui.screen.transactions

import android.content.Context
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.ContentAlpha
import com.lamz.trackinv.ViewModelFactory
import com.lamz.trackinv.data.di.Injection
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.transaksi.DataItemTransaction
import com.lamz.trackinv.response.transaksi.GetTransactionResponse
import com.lamz.trackinv.ui.component.CardItemTransactions
import com.lamz.trackinv.ui.component.SearchBar

@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        TransactionsContent(modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun TransactionsContent(
    modifier: Modifier,
    context: Context = LocalContext.current,
    viewModel: TransactionsViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context))
    ),
) {
    var query by remember { mutableStateOf(TextFieldValue()) }
    val isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val transactionState by viewModel.getTransaction.observeAsState()

    LaunchedEffect(true) {
        viewModel.getTransaction()
        if (isFocused) {
            focusRequester.requestFocus()
        }
    }

    var filteredProducts by remember(transactionState, query.text) {
        mutableStateOf<List<DataItemTransaction>>(emptyList())
    }

    DisposableEffect(transactionState, query.text) {
        when (transactionState) {
            is UiState.Success -> {
                val allProducts =
                    (transactionState as UiState.Success<GetTransactionResponse>).data.data
                filteredProducts = if (query.text.isNotEmpty()) {
                    allProducts.filter {
                        it.partner.name.contains(query.text, ignoreCase = true)
                        it.partner.type.contains(query.text, ignoreCase = true)
                    }
                } else {
                    allProducts
                }
            }

            else -> {}
        }
        onDispose { }
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
            when (transactionState) {
                is UiState.Success -> {

                    items(filteredProducts) { transactions ->
                        CardItemTransactions(
                            nama = transactions.partner.name,
                            waktu = transactions.createdAt,
                            harga = transactions.totalHarga.toString(),
                            type = transactions.type,
                            tipe = transactions.partner.type
                        )

                    }

                    if (filteredProducts.isEmpty()) {
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
                    }
                }

                else -> {}
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