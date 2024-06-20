package com.lamz.trackinv.presentation.ui.screen.partner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ContentAlpha
import com.lamz.trackinv.R
import com.lamz.trackinv.domain.model.BarangModel
import com.lamz.trackinv.domain.model.TransaksiModel
import com.lamz.trackinv.presentation.model.partner.TransactionViewModel
import com.lamz.trackinv.presentation.ui.component.AlertDialogMessage
import com.lamz.trackinv.presentation.ui.component.CardLongItem
import com.lamz.trackinv.presentation.ui.component.SearchBar
import com.lamz.trackinv.presentation.ui.state.UiState
import com.lamz.trackinv.utils.FirebaseUtils
import com.lamz.trackinv.utils.getFormattedCurrentDate
import kotlinx.coroutines.delay

@Composable
fun OutGoingScreen(
    modifier: Modifier = Modifier,
    idCustomer: String,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        val allProductState by viewModel.getInventoryState.collectAsState()
        val updateStockState by viewModel.updateStockState.collectAsState()

        LaunchedEffect(updateStockState) {
            if (updateStockState is UiState.Success) {
                viewModel.getAllInventory()
            }
        }

        when (val state = allProductState) {
            is UiState.Error -> {
                Text(text = state.errorMessage, modifier = Modifier.align(Alignment.Center))
            }

            UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                LaunchedEffect(key1 = true, block = {
                    delay(1000L)
                        viewModel.getAllInventory()
                })

            }

            is UiState.Success -> {
                OutGoingContent(namaCustomer = idCustomer, listBarang = state.data, viewModel = viewModel)
            }
        }


    }
}

@Composable
fun OutGoingContent(
    modifier: Modifier = Modifier,
    namaCustomer: String,
    listBarang: List<BarangModel> = emptyList(),
    viewModel: TransactionViewModel,
) {

    var query by remember { mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }


    var showInputDialogOutgoing by remember { mutableStateOf(false) }
    var qty by remember { mutableStateOf("") }
    var isFocusDialog by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocusDialog }
    var selectedBarang by remember { mutableStateOf<BarangModel?>(null) }



    LaunchedEffect(true) {

        if (wasFocused) {
            focusRequester.requestFocus()
        }
    }

    var allProducts by remember { mutableStateOf(listBarang) }
    var filteredProducts by remember { mutableStateOf(allProducts) }

    LaunchedEffect(listBarang) {
        allProducts = listBarang
        filteredProducts = listBarang.filter { barang ->
            val searchText = query.text.lowercase()
            (barang.namaBarang?.lowercase()?.contains(searchText) ?: false) or
                    (barang.stokBarang?.lowercase()?.contains(searchText) ?: false) or
                    (barang.buy?.lowercase()?.contains(searchText) ?: false)
        }
    }

    LaunchedEffect(query) {
        filteredProducts = allProducts.filter { barang ->
            val searchText = query.text.lowercase()
            (barang.namaBarang?.lowercase()?.contains(searchText) ?: false) or
                    (barang.stokBarang?.lowercase()?.contains(searchText) ?: false) or
                    (barang.buy?.lowercase()?.contains(searchText) ?: false)
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
            items(filteredProducts) { inventory ->
                CardLongItem(
                    modifier = Modifier.clickable {
                        selectedBarang = inventory
                        showInputDialogOutgoing = true
                    },
                    namaItem = inventory.namaBarang ?: "",
                    pieces = inventory.stokBarang ?: "",
                    hargaJual = inventory.sell ?: "",
                    hargaBeli =  inventory.buy ?: "",
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

        if (showInputDialogOutgoing) {
            AlertDialog(
                onDismissRequest = {
                    // Handle dialog dismissal if needed
                    showInputDialogOutgoing = false
                },
                title = {
                    Text("Kurangi Stok Barang")
                },
                text = {
                    val containerColor = colorResource(id = R.color.lavender)
                    OutlinedTextField(
                        value = qty,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = containerColor,
                            unfocusedContainerColor = containerColor,
                            disabledContainerColor = containerColor,
                        ),
                        label = { Text(text = "Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        onValueChange = { newInput ->
                            qty = newInput
                        },
                        shape = RoundedCornerShape(size = 20.dp),
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                isFocusDialog = it.isFocused
                            },
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            selectedBarang?.let {barang ->

                                val idTransac = FirebaseUtils.dbTransaksi.push().key!!
                                val currentDate = getFormattedCurrentDate()

                                val qtyInt = qty.toInt()
                                val sellInt = barang.sell?.toInt()
                                val totalValue = qtyInt * sellInt!!
                                val namaBarang = barang.namaBarang
                                val keluar = "Keluar"

                                val itemTransac = TransaksiModel(
                                    idTransac,
                                    keluar,
                                    namaCustomer,
                                    namaBarang,
                                    totalValue.toString(),
                                    currentDate
                                )
                                viewModel.addTransactionStock(itemTransac)

                                val newStock = (barang.stokBarang?.toInt() ?: 0) - qtyInt
                                viewModel.updateStock(barang.idBarang!!, newStock)
                                viewModel.getAllInventory()
                                qty = ""
                            }
                            showInputDialogOutgoing = false
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
                            showInputDialogOutgoing = false
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

        val addTransactionState by viewModel.addTransactionState.collectAsStateWithLifecycle()

        when(val data = addTransactionState){
            is UiState.Error -> {
                AlertDialogMessage(modifier = modifier, title = "Error", desc = data.errorMessage)
            }
            is UiState.Success -> {
                viewModel.getAllInventory()
                AlertDialogMessage(modifier = modifier, title = "Sukses",  desc = "Sukses menambahkan data transaksi baru")
            }

            else -> {}
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