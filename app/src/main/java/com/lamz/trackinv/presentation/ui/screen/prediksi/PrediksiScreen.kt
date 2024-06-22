package com.lamz.trackinv.presentation.ui.screen.prediksi

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.rememberDatePickerState
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
import com.lamz.trackinv.presentation.ui.theme.yellow
import com.lamz.trackinv.presentation.ui.view.main.ui.theme.TrackInvTheme
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrediksiScreen(
    viewModel: InventoryViewModel = hiltViewModel(),
) {
    val selectedDateLabelFrom = remember { mutableStateOf("-") }
    val openDialogFrom = remember { mutableStateOf(false) }
    val selectedDateFrom = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)

    val selectedDateLabelTo = remember { mutableStateOf("-") }
    val openDialogTo = remember { mutableStateOf(false) }
    val selectedDateTo = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val totalStockState by viewModel.totalStockState.collectAsStateWithLifecycle()

    var selectedItem by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val predictionState by viewModel.predictionState.collectAsStateWithLifecycle()
    val mapeState by viewModel.mapeState.collectAsStateWithLifecycle()
    val mseState by viewModel.mseState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        item { TopBar() }

        item {
            DropDownMenu(
                viewModel = viewModel,
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                expanded = expanded,
                onExpandedChange = { expanded = it })

        }

        item {
            DatePickerRow(
                selectedDateLabelFrom = selectedDateLabelFrom,
                openDialogFrom = openDialogFrom,
                selectedDateLabelTo = selectedDateLabelTo,
                openDialogTo = openDialogTo
            )
        }

        item {
            GetFromDate(
                isOpen = openDialogFrom.value,
                onDismissRequest = { openDialogFrom.value = false },
                onDateSelected = {
                    selectedDateLabelFrom.value = it.convertMillisToDate()
                    openDialogFrom.value = false
                },
                selectedDate = selectedDateFrom
            )
        }

        item {
            GetToDate(
                isOpen = openDialogTo.value,
                onDismissRequest = { openDialogTo.value = false },
                onDateSelected = {
                    selectedDateLabelTo.value = it.convertMillisToDate()
                    openDialogTo.value = false
                },
                selectedDate = selectedDateTo
            )
        }

        item {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                PredictButton(
                    selectedItem = selectedItem,
                    selectedDateLabelFrom = selectedDateLabelFrom.value,
                    selectedDateLabelTo = selectedDateLabelTo.value,
                    onClick = {
                        val fromDate = selectedDateLabelFrom.value
                        val toDate = selectedDateLabelTo.value
                        viewModel.getTotalStock(fromDate, toDate, selectedItem ?: "")
                        viewModel.predictStockOut(fromDate, toDate, selectedItem ?: "")
                    },
                    onExpandedClick = { expanded = true },
                    onOpenDialogFromClick = { openDialogFrom.value = true },
                    onOpenDialogToClick = { openDialogTo.value = true },
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        }

        item {
            TotalStock(totalStockState = totalStockState)
        }



        when (val predict = predictionState) {
            is UiState.Loading -> {}

            is UiState.Success -> {
                val predictions = predict.data

                if (predictions.isNotEmpty()) {
                    item {

                        Text(
                            text = "Prediksi Stok Keluar untuk ${predictions.size} hari ke depan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = black40,
                            modifier = Modifier.padding(16.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .background(black40)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "Tanggal",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f),
                                    color = yellow
                                )
                                Text(
                                    text = "Prediksi Stok",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f),
                                    color = yellow
                                )
                            }
                            HorizontalDivider(thickness = 1.dp, color = black40)
                        }

                    }
                    items(predictions.size) { index ->
                        val prediction = predictions[index]
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = prediction.date,
                                    modifier = Modifier.weight(1f),
                                    color = black40
                                )
                                Text(
                                    text = "${prediction.stock.toFloat()}",
                                    modifier = Modifier.weight(1f),
                                    color = black40
                                )
                            }
                            if (index < predictions.size - 1) {
                                HorizontalDivider(thickness = 1.dp, color = black40)
                            }
                        }

                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "MAPE",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f),
                                    color = black40
                                )
                                Text(
                                    text = "MSE",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f),
                                    color = black40
                                )
                            }
                            HorizontalDivider(thickness = 1.dp, color = black40)
                        }
                                          }


                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                                .background(black40)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                when (val dataMape = mapeState) {
                                    is UiState.Success -> Text(
                                        "${String.format(Locale.US, "%.2f", dataMape.data)}%",
                                        modifier = Modifier.weight(1f),
                                        color = yellow
                                    )

                                    is UiState.Error -> Text("MAPE: Error")
                                    is UiState.Loading -> {}
                                }

                                when (val dataMSe = mseState) {
                                    is UiState.Success -> Text(
                                        String.format(Locale.US, "%.2f", dataMSe.data),
                                        modifier = Modifier.weight(1f),
                                        color = yellow
                                    )

                                    is UiState.Error -> Text("MSE: Error")
                                    is UiState.Loading -> {}
                                }
                            }

                        }

                    }

                } else {
                    item {
                        Text(
                            text = "Tidak ada data stok untuk periode ini.",
                            color = black40,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                }
            }

            is UiState.Error -> {
                item {
                    Text(
                        text = "Error: ${predict.errorMessage}",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Log.d("Pediksi Data", "PrediksiScreen: ${predict.errorMessage} ")
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