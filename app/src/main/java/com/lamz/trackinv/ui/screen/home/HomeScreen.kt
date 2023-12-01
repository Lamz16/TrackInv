package com.lamz.trackinv.ui.screen.home

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lamz.trackinv.R
import com.lamz.trackinv.ViewModelFactory
import com.lamz.trackinv.data.ItemsProduct
import com.lamz.trackinv.data.di.Injection
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.ui.component.CardItem1
import com.lamz.trackinv.ui.component.CardItem2
import com.lamz.trackinv.ui.component.CardLongItem
import com.lamz.trackinv.ui.component.TextItem
import com.lamz.trackinv.ui.view.main.MainActivity

@Composable
fun HomeScreen() {
    HomeContent()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    context: Context = LocalContext.current,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context))
    ),
) {

    val sessionData by viewModel.getSession().observeAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showIncoming by remember { mutableStateOf(false) }
    var showOutcoming by remember { mutableStateOf(false) }
    var qty by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val fakeIdbarang = "8fdaf6cf-f6d6-446f-9000-e51937994c23"
    val fakeIdCustomer = "afc9a85d-fe69-4f30-a95d-1af879795754"
    val wasFocused = remember { isFocused }
    val uploadState by viewModel.upload.observeAsState()

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

    LaunchedEffect(true) {
        viewModel.getAllProductsMenipis()
        if (wasFocused) {
            focusRequester.requestFocus()
        }
    }

    TopAppBar(
        title = {
            sessionData?.let {
                Text(
                    it.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp,
                )
            }
        },
        actions = {
            IconButton(onClick = {
                showDialog = true
            }) {
                Icon(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.ic_sign_out),
                    contentDescription = "Logout",
                    tint = colorResource(id = R.color.Yellow)
                )
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
                Text("Logout")
            },
            text = {
                Text("Yakin ingin logout ?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.logout()
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

    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 58.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            val productTersedia by viewModel.stokTersedia.observeAsState(emptyList())
            val productMenipis by viewModel.stokMenipis.observeAsState(emptyList())
            val productHabis by viewModel.stokhabis.observeAsState(emptyList())

            CardItem1(R.drawable.ic_stok_tersedia,productTersedia.size.toString(), stringResource(id = R.string.tersedia))
            CardItem1(R.drawable.ic_menipis,productMenipis.size.toString() , stringResource(id = R.string.menipis))
            CardItem1(R.drawable.ic_stok_habis, productHabis.size.toString() ,stringResource(id = R.string.habis))
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CardItem2(R.drawable.ic_stok_masuk, stringResource(id = R.string.stok_masuk), modifier = Modifier.clickable {
                showIncoming = true
            })

            if (showIncoming) {
                AlertDialog(
                    onDismissRequest = {
                        // Handle dialog dismissal if needed
                        showIncoming = false
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
                                    isFocused = it.isFocused
                                },
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val itemsList = listOf(ItemsProduct(fakeIdbarang, qty.toInt()))
                                viewModel.outgoingTran(fakeIdCustomer, itemsList)
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
                                showIncoming = false
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

            CardItem2(R.drawable.ic_stok_keluar, stringResource(id = R.string.stok_keluar), modifier = Modifier.clickable {
                showOutcoming = true
            })

            if (showOutcoming) {
                AlertDialog(
                    onDismissRequest = {
                        // Handle dialog dismissal if needed
                        showIncoming = false
                    },
                    title = {
                        Text("Tambah Data Kategori")
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
                            label = { Text(text = "Nama Kategori") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            singleLine = true,
                            onValueChange = { newInput ->
                                qty = newInput
                            },
                            shape = RoundedCornerShape(size = 20.dp),
                            modifier = Modifier
                                .padding(bottom = 24.dp)
                                .focusRequester(focusRequester)
                                .onFocusChanged {
                                    isFocused = it.isFocused
                                },
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {

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
                                showIncoming = false
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

        TextItem(
            desc = stringResource(id = R.string.last_update), modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
        )

        CardLongItem(namaItem = "Gula", pieces = "200", category = "Sembako", id = "15000")
    }


}


@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeContent()
}