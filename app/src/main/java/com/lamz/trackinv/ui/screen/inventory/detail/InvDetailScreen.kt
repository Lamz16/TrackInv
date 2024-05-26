package com.lamz.trackinv.ui.screen.inventory.detail

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.lamz.trackinv.R
import com.lamz.trackinv.data.model.BarangModel
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.ui.component.OutLinedTextItem
import com.lamz.trackinv.ui.component.TextItem
import com.lamz.trackinv.ui.navigation.Screen
import com.lamz.trackinv.ui.screen.inventory.InventoryViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun InvDetailScreen(
    modifier: Modifier = Modifier,
    inventoryId: String,
    navController: NavHostController,
    viewModel: InventoryViewModel = koinViewModel(),
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()

    ) {

        val allProductState by viewModel.getInventoryIdState.collectAsState()
        when (val state = allProductState) {
            is UiState.Error -> {
                Text(text = state.errorMessage, modifier = Modifier.align(Alignment.Center))
            }

            UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                LaunchedEffect(key1 = true, block = {
                    delay(500L)
                    viewModel.getInventoryId(inventoryId)
                })

            }

            is UiState.Success -> {
                InvDetailContent(inventory = state.data, navController = navController)
                println(" ini idUser barang ${ state.data.idUser }")
            }
        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvDetailContent(
    modifier: Modifier = Modifier,
    inventory: BarangModel,
    navController: NavHostController,
    viewModel: InventoryViewModel = koinViewModel(),
) {
    val focusRequester = remember { FocusRequester() }
    val isFocused by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocused }
    var showDelete by remember { mutableStateOf(false) }
    var showLoadingUpdate by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }
    var editedNamaBarang by remember { mutableStateOf(inventory.namaBarang ?: "") }
    var editedstokBarang by remember { mutableStateOf(inventory.stokBarang ?: "") }
    var editedhargaBeli by remember{ mutableStateOf(inventory.buy ?: "") }
    var editedhargaJual by remember { mutableStateOf(inventory.sell ?: "") }
    val deleteproduct by viewModel.deleteProductState.collectAsState()
    val updateproduct by viewModel.updateProductState.collectAsState()


    LaunchedEffect(true) {

        if (wasFocused) {
            focusRequester.requestFocus()
        }
    }

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
            onValueChange = { editedNamaBarang = it })
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
                showLoadingUpdate = true
                val updatedBarang = BarangModel(
                    idBarang = inventory.idBarang,
                    idUser = inventory.idUser,
                    namaBarang = editedNamaBarang,
                    stokBarang = editedstokBarang,
                    buy = editedhargaBeli,
                    sell = editedhargaJual
                )
                viewModel.updateProduct(inventory.idBarang ?: "", updatedBarang)

            },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = colorResource(id = R.color.Yellow)
            ),
        ) {
            when (val state = updateproduct) {
                is UiState.Error -> {
                    Text(text = state.errorMessage)
                }

                UiState.Loading -> {
                    if (showLoadingUpdate) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Gray
                        )
                    } else {
                        Text("Update")
                    }
                }

                is UiState.Success -> {
                    navController.navigate(Screen.Inventory.route) {
                        popUpTo(0)
                    }
                }
            }

        }


    }




    TopAppBar(
        title = {
            Text(
                stringResource(id = R.string.detail_barang, editedNamaBarang),
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
                        viewModel.deleteProduct(inventory.idBarang ?: "")
                        showLoading = true
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = colorResource(id = R.color.Yellow)
                    )
                ) {
                    when (val state = deleteproduct) {
                        is UiState.Error -> {
                            Text(text = state.errorMessage)
                        }

                        UiState.Loading -> {
                            if (showLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.Gray
                                )
                            } else {
                                Text("Yes")
                            }
                        }

                        is UiState.Success -> {
                            navController.navigate(Screen.Inventory.route) {
                                popUpTo(0)
                            }
                        }
                    }

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