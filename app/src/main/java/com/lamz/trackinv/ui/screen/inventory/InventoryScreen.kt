package com.lamz.trackinv.ui.screen.inventory

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lamz.trackinv.R
import com.lamz.trackinv.ViewModelFactory
import com.lamz.trackinv.data.di.Injection
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.product.GetProductResponse
import com.lamz.trackinv.ui.component.CardLongItem
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
        InventoryContent(navController = navController, navigateToDetail = navigateToDetail,)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
    val listState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocused }

    var showDialog by remember { mutableStateOf(false) }
    val productState by viewModel.getProduct.observeAsState()



    LaunchedEffect(true) {
        viewModel.getAllProduct()
        if (wasFocused) {
            focusRequester.requestFocus()
        }
    }

    Box {
        TopAppBar(
            title = {
                Text(
                    stringResource(id = R.string.daftar_barang),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                )
            },
            actions = {
                IconButton(onClick = {
                    showDialog = false
                    navController.navigate(Screen.Add.route)
                }) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Logout",
                        tint = colorResource(id = R.color.Yellow),
                        modifier = Modifier
                            .size(36.dp)
                    )
                }
            }
        )

        LazyColumn(
            state = listState, contentPadding = PaddingValues(bottom = 80.dp),
            modifier = Modifier.padding(top = 48.dp)
        ) {
            when (productState) {
                is UiState.Loading -> {

                }

                is UiState.Success -> {

                    val products = (productState as UiState.Success<GetProductResponse>).data.data

                    items(products) { inventory ->
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

                }

                is UiState.Error -> {

                }

                else -> {}
            }
        }
    }
}