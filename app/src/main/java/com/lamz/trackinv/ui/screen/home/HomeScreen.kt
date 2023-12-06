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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lamz.trackinv.R
import com.lamz.trackinv.ViewModelFactory
import com.lamz.trackinv.data.di.Injection
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.transaksi.GetTransactionResponse
import com.lamz.trackinv.ui.component.CardItem1
import com.lamz.trackinv.ui.component.CardItem2
import com.lamz.trackinv.ui.component.CardItemTransactions
import com.lamz.trackinv.ui.component.TextItem
import com.lamz.trackinv.ui.navigation.Screen
import com.lamz.trackinv.ui.view.main.MainActivity

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    HomeContent(navController = navController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    context: Context = LocalContext.current,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context))
    ),
    navController: NavHostController,
) {

    val sessionData by viewModel.getSession().observeAsState()

    var showDialog by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocused }
    val uploadState by viewModel.upload.observeAsState()
    val transactionState by viewModel.getTransaction.observeAsState()



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
        viewModel.getTransaction()
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
                navController.navigate(Screen.Supplier.route)
            })


            CardItem2(R.drawable.ic_stok_keluar, stringResource(id = R.string.stok_keluar), modifier = Modifier.clickable {
                navController.navigate(Screen.Customer.route)
            })


        }

        TextItem(
            desc = stringResource(id = R.string.last_update), modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
        )

        LazyColumn(state = rememberLazyListState()){
            when (transactionState) {
                is UiState.Success -> {
                    val transactions = (transactionState as UiState.Success<GetTransactionResponse>).data.data
                    items(transactions){ tran ->
                        CardItemTransactions(type = tran.type, nama = tran.partner.name, tipe = tran.partner.type, waktu = tran.createdAt)
                    }
                }
                else -> {}
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun HomePreview() {

}