package com.lamz.trackinv.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.lamz.trackinv.domain.model.BarangModel
import com.lamz.trackinv.presentation.ui.navigation.Screen

@Composable
fun StockDialog(
    modifier: Modifier =Modifier,
    listBarang: List<BarangModel> = emptyList(),
    onDismissRequest: () -> Unit,
    navHostController: NavHostController,
    textError : String = "",
){
    if(textError != "" && listBarang.isEmpty()){
        Dialog(onDismissRequest = {onDismissRequest()}){
            Text(textError)
        }
    }else{
        Dialog(onDismissRequest = {onDismissRequest()}) {
            Box(modifier = modifier){
                LazyColumn {
                    items(listBarang){ inventory ->
                        CardLongItem(
                            modifier = Modifier.clickable {
                                navHostController.navigate(Screen.DetailInventory.createRoute(inventory.idBarang!!)){
                                    popUpTo(Screen.Home.route)
                                }
                            },
                            namaItem = inventory.namaBarang!!,
                            pieces = inventory.stokBarang!!,
                            hargaJual = inventory.sell!!,
                            hargaBeli = inventory.buy!!
                        )
                    }
                }
            }
        }
    }

}