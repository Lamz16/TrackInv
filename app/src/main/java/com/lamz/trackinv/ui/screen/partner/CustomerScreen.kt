package com.lamz.trackinv.ui.screen.partner

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
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
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lamz.trackinv.R
import com.lamz.trackinv.ViewModelFactory
import com.lamz.trackinv.data.di.Injection
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.partner.GetCustomerResponse
import com.lamz.trackinv.ui.component.CardCategoryItem
import com.lamz.trackinv.ui.navigation.Screen
import com.lamz.trackinv.ui.view.main.MainActivity

@Composable
fun CustomerScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigateToDetail: (String) -> Unit,
) {

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()

    ) {
        CustomerContent(navController = navController, navigateToDetail = navigateToDetail)

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerContent(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    viewModel: PartnerViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context))
    ),
    navController: NavHostController,
    navigateToDetail: (String) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var showCategory by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocused }
    var addCustomer by remember { mutableStateOf("") }


    val uploadState by viewModel.uploadCustomer.observeAsState()
    val customerState by viewModel.getCustomer.observeAsState()
    val listState = rememberLazyListState()

    when (uploadState) {
        is UiState.Loading -> {

        }

        is UiState.Success -> {
            showCategory = false
            Toast.makeText(context, "Berhasil menambah kategori", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.Customer.route) {
                popUpTo(Screen.Home.route)
            }
        }

        is UiState.Error -> {
            Toast.makeText(context, "Tingkatkan limit mu", Toast.LENGTH_SHORT).show()
        }

        else -> {}
    }

    LaunchedEffect(true) {
        viewModel.getCustomer()
        if (wasFocused) {
            focusRequester.requestFocus()
        }
    }

    Box {

        TopAppBar(
            title = {
                Text(
                    stringResource(id = R.string.pilih_customer),
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
                            showCategory = true
                        }
                    ) {
                        Icon(
                            bitmap = ImageBitmap.imageResource(id = R.drawable.ic_partner),
                            contentDescription = "Add partner",
                            tint = colorResource(id = R.color.Yellow),
                            modifier = Modifier
                                .size(36.dp)
                                .padding(end = 16.dp)
                        )
                    }

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

        if (showCategory) {
            AlertDialog(
                onDismissRequest = {
                    // Handle dialog dismissal if needed
                    showCategory = false
                },
                title = {
                    Text("Tambahkan partner")
                },
                text = {
                    val containerColor = colorResource(id = R.color.lavender)
                    OutlinedTextField(
                        value = addCustomer,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = containerColor,
                            unfocusedContainerColor = containerColor,
                            disabledContainerColor = containerColor,
                        ),
                        label = { Text(text = "Nama Kategori") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        onValueChange = { newInput ->
                            addCustomer = newInput
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
                            viewModel.addCustomer(addCustomer)
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
                            showCategory = false
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


        LazyColumn(
            state = listState, contentPadding = PaddingValues(bottom = 120.dp),
            modifier = Modifier.padding(top = 48.dp)
        ) {
            when (customerState) {
                is UiState.Loading -> {
                    // Display loading indicator if needed
                }

                is UiState.Success -> {
                    val categories =
                        (customerState as UiState.Success<GetCustomerResponse>).data.data

                    // Display each category in LazyColumn
                    items(categories) { customer ->
                        CardCategoryItem(
                            nameCategory = customer.name,
                            modifier = Modifier.clickable {
                                navigateToDetail(customer.id)
                            })
                    }

                    if (categories.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Data partner masih kosong. Tambahkan dulu.")
                            }
                        }
                    }

                }

                is UiState.Error -> {
                    // Handle error state if needed
                }

                else -> {}
            }


        }
    }
}