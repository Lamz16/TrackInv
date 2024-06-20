package com.lamz.trackinv.presentation.ui.screen.prediksi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.Text
import com.lamz.trackinv.presentation.model.inventory.InventoryViewModel
import com.lamz.trackinv.presentation.ui.state.UiState
import com.lamz.trackinv.presentation.ui.theme.black40
import com.lamz.trackinv.presentation.ui.view.main.ui.theme.TrackInvTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrediksiScreen(
    viewModel: InventoryViewModel = hiltViewModel(),
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        CenterAlignedTopAppBar(title = {
            Text(
                text = "Prediksi Stok",
                color = black40,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp
            )
        })

        HorizontalDivider(color = black40)

        dropDownMenu(viewModel = viewModel)

        Card {

        }

    }

}

@Composable
private fun dropDownMenu(viewModel: InventoryViewModel){

    val uiState by viewModel.getInventoryState.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<String?>(null) }
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = selectedItem ?: "Pilih item",
            color = black40,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier
                .weight(1f)
                .height(36.dp)
                .clickable { expanded = true }
        )
        Column{
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown arrow",
                tint = Color.Black,
                modifier = Modifier
                    .clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .heightIn(max = 250.dp)
                    .widthIn(max = 250.dp)
            ) {
                when (val items = uiState) {
                    is UiState.Error -> {}
                    UiState.Loading -> {}
                    is UiState.Success -> {
                        items.data.forEach { item ->
                            DropdownMenuItem(onClick = {
                                selectedItem = item.namaBarang
                                expanded = false
                            }, text = {
                                Text(text = item.namaBarang ?: "", color = black40)
                            })
                        }
                    }
                }

            }
        }

    }
}

@Composable
@Preview
private fun Preview() {
    TrackInvTheme {
        PrediksiScreen()
    }
}