package com.lamz.trackinv.ui.screen.partner

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.ContentAlpha
import com.lamz.trackinv.R
import com.lamz.trackinv.ViewModelFactory
import com.lamz.trackinv.data.ItemsProduct
import com.lamz.trackinv.data.di.Injection
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.product.DataItem
import com.lamz.trackinv.response.product.GetProductResponse
import com.lamz.trackinv.ui.component.CardLongItem
import com.lamz.trackinv.ui.component.SearchBar
import com.lamz.trackinv.ui.view.main.MainActivity

@Composable
fun IncomingScreen(
    modifier: Modifier = Modifier,
    idCustomer: String
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        IncomingContent(idCustomer = idCustomer)
    }
}

@Composable
fun IncomingContent(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    viewModel: PartnerViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context))
    ),
    idCustomer: String,
) {

    var query by remember { mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }

    val productState by viewModel.getProduct.observeAsState()
    val uploadState by viewModel.upload.observeAsState()
    var showOutgoing by remember { mutableStateOf(false) }
    var qty by remember { mutableStateOf("") }
    var isFocusDialog by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocusDialog }




    LaunchedEffect(true) {
        viewModel.getAllProduct()
        if (wasFocused) {
            focusRequester.requestFocus()
        }
    }

    when (uploadState) {
        is UiState.Loading -> {

        }

        is UiState.Success -> {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            (context as? ComponentActivity)?.finish()
        }

        is UiState.Error -> {

        }

        else -> {}
    }

    var filteredProducts by remember(productState, query.text) {
        mutableStateOf<List<DataItem>>(emptyList())
    }

    DisposableEffect(productState, query.text) {
        when (productState) {
            is UiState.Success -> {
                val allProducts = (productState as UiState.Success<GetProductResponse>).data.data
                filteredProducts = if (query.text.isNotEmpty()) {
                    allProducts.filter {
                        it.name.contains(query.text, ignoreCase = true) ||
                                it.category.name.contains(query.text, ignoreCase = true) ||
                                it.hargaBeli.toString().contains(query.text, ignoreCase = true)
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
            when (productState) {
                is UiState.Success -> {
                    items(filteredProducts) { inventory ->
                        CardLongItem(
                            modifier = Modifier.clickable {
                                viewModel.outId = inventory.id
                                showOutgoing = true
                            },
                            namaItem = inventory.name,
                            pieces = inventory.stok.toString(),
                            category = inventory.category.name,
                            id = inventory.hargaBeli.toString()
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
                            val itemsList = listOf(ItemsProduct(viewModel.outId, qty.toInt()))
                            viewModel.incomingTran(idCustomer, itemsList)
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