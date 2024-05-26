package com.lamz.trackinv.ui.screen.partner

import android.content.Context
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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import com.lamz.trackinv.R
import com.lamz.trackinv.data.model.BarangModel
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.ui.component.CardLongItem
import com.lamz.trackinv.ui.component.SearchBar
import com.lamz.trackinv.ui.screen.inventory.InventoryContent
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun IncomingScreen(
    modifier: Modifier = Modifier,
    idCustomer: String,
    viewModel: TransactionViewModel = koinViewModel()
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        val allProductState by viewModel.getInventoryState.collectAsState()
        when (val state = allProductState) {
            is UiState.Error -> {
                Text(text = state.errorMessage, modifier = Modifier.align(Alignment.Center))
            }

            UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                val idUser by viewModel.getSession().observeAsState()
                LaunchedEffect(key1 = true, block = {
                    delay(1000L)
                    idUser?.let {
                        viewModel.getAllInventory(it.idUser)
                    }
                })

            }

            is UiState.Success -> {
                IncomingContent(idCustomer = idCustomer, listBarang = state.data)
            }
        }
    }
}

@Composable
fun IncomingContent(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    idCustomer: String,
    listBarang: List<BarangModel> = emptyList(),
) {

    var query by remember { mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }

//    val productState by viewModel.getProduct.observeAsState()
//    val uploadState by viewModel.upload.observeAsState()
    var showOutgoing by remember { mutableStateOf(false) }
    var qty by remember { mutableStateOf("") }
    var isFocusDialog by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocusDialog }




    LaunchedEffect(true) {
        if (wasFocused) {
            focusRequester.requestFocus()
        }
    }

//    when (uploadState) {
//        is UiState.Loading -> {
//
//        }
//
//        is UiState.Success -> {
//            val intent = Intent(context, MainActivity::class.java)
//            intent.flags =
//                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            context.startActivity(intent)
//            (context as? ComponentActivity)?.finish()
//        }
//
//        is UiState.Error -> {
//
//        }
//
//        else -> {}
//    }
    var allProducts by remember { mutableStateOf(emptyList<BarangModel>()) }
    var filteredProducts by remember { mutableStateOf(emptyList<BarangModel>()) }
    allProducts = listBarang
    filteredProducts = allProducts

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

                        showOutgoing = true
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

        if (showOutgoing) {
            AlertDialog(
                onDismissRequest = {
                    // Handle dialog dismissal if needed
                    showOutgoing = false
                },
                title = {
                    Text("Tambah Stok Barang")
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
//                            val itemsList = listOf(ItemsProduct(viewModel.outId, qty.toInt()))
//                            viewModel.incomingTran(idCustomer, itemsList)
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
                            showOutgoing = false
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