package com.lamz.trackinv.ui.screen.inventory

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ContentAlpha
import com.lamz.trackinv.R
import com.lamz.trackinv.ViewModelFactory
import com.lamz.trackinv.data.di.Injection
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.product.DataItem
import com.lamz.trackinv.response.product.GetProductResponse
import com.lamz.trackinv.ui.component.CardLongItem
import com.lamz.trackinv.ui.component.SearchBar
import com.lamz.trackinv.ui.navigation.Screen


@Composable
fun InventoryScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigateToDetail: (String) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        InventoryContent(navController = navController, navigateToDetail = navigateToDetail)
    }
}


@Composable
fun InventoryContent(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    viewModel: InventoryViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context))
    ),
    navController: NavHostController,
    navigateToDetail: (String) -> Unit,
) {
    var query by remember { mutableStateOf(TextFieldValue()) }
    val isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var showDialog by remember { mutableStateOf(false) }
    val productState by viewModel.getProduct.observeAsState()

    LaunchedEffect(true) {
        viewModel.getAllProduct()
        if (isFocused) {
            focusRequester.requestFocus()
        }
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
                                navigateToDetail(inventory.id)
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

        FloatingActionButton(
            shape = CircleShape,
            containerColor = colorResource(id = R.color.black40),
            onClick = {
                showDialog = false
                navController.navigate(Screen.Add.route)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Add",
                tint = colorResource(id = R.color.Yellow),
                modifier = Modifier.size(36.dp)
            )
        }
    }
}
