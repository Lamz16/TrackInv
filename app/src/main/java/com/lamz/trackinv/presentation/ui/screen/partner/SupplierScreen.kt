package com.lamz.trackinv.presentation.ui.screen.partner

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.lamz.trackinv.R
import com.lamz.trackinv.domain.model.SupplierModel
import com.lamz.trackinv.presentation.model.partner.PartnerViewModel
import com.lamz.trackinv.presentation.ui.component.CardCategoryItem
import com.lamz.trackinv.presentation.ui.navigation.Screen
import com.lamz.trackinv.presentation.ui.state.UiState
import com.lamz.trackinv.presentation.ui.view.main.MainActivity
import com.lamz.trackinv.utils.FirebaseUtils
import kotlinx.coroutines.delay

@Composable
fun SupplierScreen(
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit,
    navController : NavHostController,
    viewModel: PartnerViewModel = hiltViewModel(),
) {

    val context = LocalContext.current
    val activity = context as? ComponentActivity

    val (backPressed, setBackPressed) = remember { mutableStateOf(false) }


    DisposableEffect(activity) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setBackPressed(true)
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }

    if (backPressed) {
        navController.navigate(Screen.Home.route){
            popUpTo(0)
            restoreState = true
        }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()

    ) {
        val getAllSupplier by viewModel.getSupplierState.collectAsState()
        when (val state = getAllSupplier) {
            is UiState.Error -> {
                Text(text = "Error: ${state.errorMessage}")
            }

            UiState.Loading -> {

                CircularProgressIndicator(modifier.align(Alignment.Center))
                LaunchedEffect(key1 = true, block = {
                    delay(1200L)
                        viewModel.getAllSupplier()

                })

            }

            is UiState.Success -> {

                SupplierContent(
                    navigateToDetail = navigateToDetail,
                    listSupplier = state.data
                )
            }
        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierContent(
    context: Context = LocalContext.current,
    navigateToDetail: (String) -> Unit,
    viewModel: PartnerViewModel = hiltViewModel(),
    listSupplier: List<SupplierModel> = emptyList(),
) {
    var showDialog by remember { mutableStateOf(false) }
    var addDialog by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocused }
    var addSupplier by remember { mutableStateOf("") }



    val supplierState by viewModel.supplierState.collectAsState()
    val listState = rememberLazyListState()


    LaunchedEffect(true) {
        if (wasFocused) {
            focusRequester.requestFocus()
        }
    }


    Box {

        TopAppBar(
            title = {
                Text(
                    stringResource(id = R.string.pilih_supplier),
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
                            addDialog = true
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

        if (addDialog) {
            AlertDialog(
                onDismissRequest = {
                    // Handle dialog dismissal if needed
                    addDialog = false
                },
                title = {
                    Text("Tambahkan partner")
                },
                text = {
                    val containerColor = colorResource(id = R.color.lavender)
                    OutlinedTextField(
                        value = addSupplier,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = containerColor,
                            unfocusedContainerColor = containerColor,
                            disabledContainerColor = containerColor,
                        ),
                        label = { Text(text = "Nama Supplier") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        onValueChange = { newInput ->
                            addSupplier = newInput
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
                            val idSupplier = FirebaseUtils.dbSupplier.push().key!!
                            if (addSupplier.isNotEmpty()){
                                    viewModel.addSupplier(
                                        SupplierModel(
                                            idSupplier,
                                            addSupplier
                                        )
                                    )

                                showLoading = true
                            }

                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = colorResource(id = R.color.Yellow)
                        )
                    ) {

                        when (val state = supplierState) {
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

                            is UiState.Error -> {
                                Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            is UiState.Success -> {
                                showLoading = false
                                addDialog = false
                                addSupplier = ""
                                LaunchedEffect(true) {
                                        viewModel.getAllSupplier()

                                    viewModel.resetSupplierState()
                                }
                            }
                        }


                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            addDialog = false
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

        if (listSupplier.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Data partner masih kosong. Tambahkan dulu.")
            }

        } else {
            LazyColumn(
                state = listState, contentPadding = PaddingValues(bottom = 120.dp),
                modifier = Modifier.padding(top = 48.dp)
            ) {

                items(listSupplier) { supplier ->
                    supplier.namaSupp?.let {
                        CardCategoryItem(
                            nameCategory = it,
                            modifier = Modifier.clickable {
                                navigateToDetail(supplier.namaSupp)
                            })
                    }
                }


            }
        }
    }
}