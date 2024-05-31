package com.lamz.trackinv.presentation.ui.screen.add

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.material.icons.filled.Cancel
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.lamz.trackinv.R
import com.lamz.trackinv.domain.model.BarangModel
import com.lamz.trackinv.presentation.model.add.AddProductViewModel
import com.lamz.trackinv.presentation.ui.component.OutLinedTextItem
import com.lamz.trackinv.presentation.ui.component.TextItem
import com.lamz.trackinv.presentation.ui.navigation.Screen
import com.lamz.trackinv.presentation.ui.state.UiState
import com.lamz.trackinv.presentation.ui.view.main.MainActivity
import com.lamz.trackinv.utils.FirebaseUtils.dbBarang
import com.lamz.trackinv.utils.formatToCurrency


@Composable
fun AddProductScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()

    ) {
        AddProductContent(navController = navController)
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductContent(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    navController: NavHostController,
    viewModel: AddProductViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val isFocused by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocused }

    var namaBarang by remember { mutableStateOf("") }
    var stokBarang by remember { mutableStateOf("") }
    var hargaBeli by remember { mutableStateOf("") }
    var hargaJual by remember { mutableStateOf("") }

    val addProductState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        if (wasFocused) {
            focusRequester.requestFocus()
        }
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
                        showDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
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

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Handle dialog dismissal if needed
                showDialog = false
            },
            title = {
                Text("Batal Tambah")
            },
            text = {
                Text("Yakin ingin Batal ?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                        (context as? ComponentActivity)?.finish()

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
                        showDialog = false
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


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        TextItem(
            desc = "Tambah Barang",
            fontWeight = FontWeight.SemiBold,
            fontSize = 36.sp,
        )


        val containerColor = colorResource(id = R.color.lavender)


        /**       Using component view for set input column */

        OutLinedTextItem(
            namaBarang,
            text = "Nama Barang",
            containerColor = containerColor,
            keyboardType = KeyboardType.Text,
            onValueChange = { namaBarang = it })
        OutLinedTextItem(
            stokBarang,
            text = "Stok Barang",
            containerColor = containerColor,
            keyboardType = KeyboardType.Number,
            onValueChange = { stokBarang = it })
        OutLinedTextItem(
            formatToCurrency(hargaBeli),
            text = "Harga Beli",
            containerColor = containerColor,
            keyboardType = KeyboardType.Number,
            onValueChange = { hargaBeli = it.replace(".", "") })
        OutLinedTextItem(
            formatToCurrency(hargaJual),
            text = "Harga Jual",
            containerColor = containerColor,
            keyboardType = KeyboardType.Number,
            onValueChange = { hargaJual = it.replace(".", "") })

        ElevatedButton(
            onClick = {
                showLoading = true
                // set action firebase
                    val idBarang = dbBarang.push().key!!
                    val barang = BarangModel(
                        idBarang,
                        namaBarang,
                        stokBarang,
                        hargaBeli,
                        hargaJual
                    )
                    viewModel.addProduct(barang)


            },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = colorResource(id = R.color.Yellow)
            ),
        ) {
            when(addProductState){
                is UiState.Error -> { }
                UiState.Loading -> {
                    if (showLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Gray
                        )
                    } else {
                        Text("Tambah")
                    }
                }
                is UiState.Success -> {
                    showLoading = false
                    Toast.makeText(context, "Berhasil menambah barang", Toast.LENGTH_SHORT)
                        .show()
                    navController.navigate(Screen.Inventory.route) {
                        popUpTo(0)
                    }
                }
            }


        }
    }
}