package com.lamz.trackinv.ui.screen.inventory.detail

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lamz.trackinv.R
import com.lamz.trackinv.ViewModelFactory
import com.lamz.trackinv.data.di.Injection
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.product.GetProductByIdResponse
import com.lamz.trackinv.ui.component.OutLinedTextItem
import com.lamz.trackinv.ui.component.TextItem
import com.lamz.trackinv.ui.navigation.Screen

@Composable
fun InvDetailScreen(
    modifier: Modifier = Modifier,
    inventoryId: String,
    navController: NavHostController,
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()

    ) {
        InvDetailContent(inventoryId = inventoryId, navController = navController)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvDetailContent(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    viewModel: InvDetailViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context))
    ),
    inventoryId: String,
    navController: NavHostController,
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocused }
    var showDelete by remember { mutableStateOf(false) }
    val deleteState by viewModel.delete.observeAsState()
    val dataState by viewModel.getProductId.observeAsState()
    val updateState by viewModel.updateproduct.observeAsState()

    when (deleteState) {
        is UiState.Success -> {
            showDelete = false
            navController.navigate(Screen.Inventory.route) {
                popUpTo(0)
            }
        }

        else -> {}
    }

    when (updateState) {
        is UiState.Success -> {
            navController.navigate(Screen.Inventory.route) {
                popUpTo(0)
            }
        }

        else -> {}
    }

    LaunchedEffect(true) {
        viewModel.getProductId(inventoryId)
        viewModel.productId = inventoryId

        if (wasFocused) {
            focusRequester.requestFocus()
        }
    }

    when (dataState) {
        is UiState.Success -> {
            val product = (dataState as UiState.Success<GetProductByIdResponse>).data

            viewModel.namaBarang = product.name
            viewModel.stokBarang = product.stok.toString()
            viewModel.hargaBeli = product.hargaBeli.toString()
            viewModel.hargaJual = product.hargaJual.toString()
            viewModel.categoryId = product.category.id

            var editedNamaBarang by remember { mutableStateOf(viewModel.namaBarang) }
            var editedstokBarang by remember { mutableStateOf(viewModel.stokBarang) }
            var editedhargaBeli by remember { mutableStateOf(viewModel.hargaBeli) }
            var editedhargaJual by remember { mutableStateOf(viewModel.hargaJual) }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                TextItem(
                    desc = "Update Barang",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 36.sp,
                )


                val containerColor = colorResource(id = R.color.lavender)

                OutLinedTextItem(
                    editedNamaBarang,
                    text = "Nama Barang",
                    containerColor = containerColor,
                    keyboardType = KeyboardType.Text,
                    onValueChange = {  editedNamaBarang = it })
                OutLinedTextItem(
                    editedstokBarang,
                    text = "Stok Barang",
                    containerColor = containerColor,
                    keyboardType = KeyboardType.Number,
                    onValueChange = { editedstokBarang = it })
                OutLinedTextItem(
                    editedhargaBeli,
                    text = "Harga Beli",
                    containerColor = containerColor,
                    keyboardType = KeyboardType.Number,
                    onValueChange = { editedhargaBeli = it })
                OutLinedTextItem(
                    editedhargaJual,
                    text = "Harga Jual",
                    containerColor = containerColor,
                    keyboardType = KeyboardType.Number,
                    onValueChange = { editedhargaJual = it })

                ElevatedButton(
                    onClick = {
                        viewModel.updateProduct(
                            inventoryId,
                            editedNamaBarang,
                            editedstokBarang,
                            viewModel.categoryId,
                            editedhargaBeli.toInt(),
                            editedhargaJual.toInt()
                        )
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = colorResource(id = R.color.Yellow)
                    ),
                ) {
                    Text("Update")
                }
            }

        }

        is UiState.Error -> {

        }

        else -> {}
    }

    TopAppBar(
        title = {
            Text(
                stringResource(id = R.string.tambah_barang),
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
            )
        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight()
            ) {


                IconButton(
                    onClick = {
                        showDelete = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Cancel",
                        tint = colorResource(id = R.color.Yellow),
                        modifier = Modifier
                            .size(36.dp)
                            .padding(end = 16.dp)
                    )
                }
            }
        }
    )

    if (showDelete) {
        AlertDialog(
            onDismissRequest = {
                // Handle dialog dismissal if needed
                showDelete = false
            },
            title = {
                Text("Hapus Data")
            },
            text = {
                Text("Yakin ingin hapus ?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteProd(viewModel.productId)
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
                        showDelete = false
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
}