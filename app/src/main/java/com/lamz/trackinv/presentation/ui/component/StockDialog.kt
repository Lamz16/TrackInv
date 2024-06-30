package com.lamz.trackinv.presentation.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.lamz.trackinv.domain.model.BarangModel
import com.lamz.trackinv.presentation.ui.navigation.Screen

@Composable
fun StockDialog(
    modifier: Modifier =Modifier,
    listBarang: List<BarangModel>,
    onDismissRequest: () -> Unit,
    navHostController: NavHostController,
    textError : String = "",
    textEmpty : String = "",
){
    when{
         listBarang.isEmpty() -> {
             Dialog(onDismissRequest = {onDismissRequest()}){
                 Card{
                     TextItem(
                         desc = textEmpty,
                         modifier = Modifier.padding(16.dp),
                         fontWeight = FontWeight.SemiBold,
                         fontSize = 24.sp,
                     )
                 }
             }
         }

        textError != "" -> {
            Dialog(onDismissRequest = {onDismissRequest()}){
                Card(
                    modifier
                        .fillMaxSize()
                ){
                    TextItem(
                        desc = textError,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                    )
                }
            }
        }

        else -> {
            Dialog(onDismissRequest = {onDismissRequest()}) {
                Box(modifier = modifier){
                    LazyColumn {
                        items(listBarang){ inventory ->
                            CardLongItem(
                                onClick = {
                                    navHostController.navigate(Screen.DetailInventory.createRoute(inventory.idBarang!!)){
                                        popUpTo(Screen.Home.route)
                                    }
                                },
                                inventory = inventory
                            )
                        }
                    }
                }
            }
        }
    }

}