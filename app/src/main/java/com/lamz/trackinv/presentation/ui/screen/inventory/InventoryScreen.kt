package com.lamz.trackinv.presentation.ui.screen.inventory

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ContentAlpha
import com.lamz.trackinv.R
import com.lamz.trackinv.domain.model.BarangModel
import com.lamz.trackinv.presentation.model.inventory.InventoryViewModel
import com.lamz.trackinv.presentation.ui.component.CardLongItem
import com.lamz.trackinv.presentation.ui.component.SearchBar
import com.lamz.trackinv.presentation.ui.navigation.Screen
import com.lamz.trackinv.presentation.ui.state.UiState
import kotlinx.coroutines.delay


@Composable
fun InventoryScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigateToDetail: (String) -> Unit,
    viewModel: InventoryViewModel = hiltViewModel(),
) {



    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        val allProductState by viewModel.getInventoryState.collectAsState()


        when(val state = allProductState){
            is UiState.Error -> {
                Text(text = state.errorMessage, modifier = Modifier.align(Alignment.Center))
            }
            UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                LaunchedEffect(key1 = true, block = {
                    delay(500L)
                        viewModel.getAllInventory()

                })

            }
            is UiState.Success -> {
                InventoryContent(navController = navController, navigateToDetail = navigateToDetail, listBarang = state.data)
            }
        }


    }

}


@Composable
fun InventoryContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigateToDetail: (String) -> Unit,
    listBarang: List<BarangModel> = emptyList(),
) {
    var query by remember { mutableStateOf(TextFieldValue()) }
    val isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var showDialog by remember { mutableStateOf(false) }


    LaunchedEffect(true) {
        if (isFocused) {
            focusRequester.requestFocus()
        }
    }
    var allProducts by remember { mutableStateOf(emptyList<BarangModel>()) }
    var filteredProducts by remember { mutableStateOf(emptyList<BarangModel>()) }

    allProducts = listBarang
    filteredProducts = allProducts

    LaunchedEffect(query) {
        filteredProducts = allProducts.filter { barang ->
            val searchText = query.text.lowercase()
            (barang.namaBarang?.lowercase()?.contains(searchText) ?: false) or
                    (barang.stokBarang?.toString()?.contains(searchText) ?: false) or
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
                        navigateToDetail(inventory.idBarang!!)
                    },
                    namaItem = inventory.namaBarang!!,
                    pieces = inventory.stokBarang!!,
                    hargaJual = inventory.sell!!,
                    hargaBeli = inventory.buy!!
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
