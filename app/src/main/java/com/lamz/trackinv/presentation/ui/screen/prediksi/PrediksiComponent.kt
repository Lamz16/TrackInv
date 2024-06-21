package com.lamz.trackinv.presentation.ui.screen.prediksi

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.Text
import com.lamz.trackinv.domain.model.StockData
import com.lamz.trackinv.presentation.model.inventory.InventoryViewModel
import com.lamz.trackinv.presentation.ui.state.UiState
import com.lamz.trackinv.presentation.ui.theme.black40
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
internal fun TotalStock(
    totalStockState : UiState<Int>,
){
    when (totalStockState) {
        is UiState.Loading -> {}

        is UiState.Success -> {
            Text(
                text = "Total Stok Keluar: ${totalStockState.data}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = black40,
                modifier = Modifier.padding(16.dp)
            )
        }

        is UiState.Error -> {
            Text(
                text = "Error: ${totalStockState.errorMessage}",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

        }
    }
}

@Composable
internal fun PredictionResult(
    predictionState: UiState<List<StockData>>,
    mapeState: UiState<Double>,
    mseState: UiState<Double>
) {
    when (predictionState) {
        is UiState.Loading -> {}

        is UiState.Success -> {
            val predictions = predictionState.data

            if (predictions.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        Text(
                            text = "Prediksi Stok Keluar untuk 7 hari ke depan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = black40,
                            modifier = Modifier.padding(16.dp)
                        )
                        // Header Table
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Tanggal",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                                color = black40
                            )
                            Text(
                                text = "Prediksi Stok",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                                color = black40
                            )
                        }
                        HorizontalDivider(thickness = 1.dp, color = black40)
                    }
                    items(predictions.size) { index ->
                        val prediction = predictions[index]
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

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
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


                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            when (mapeState) {
                                is UiState.Success -> Text(
                                    "${String.format(Locale.US, "%.2f", mapeState.data)}%",
                                    modifier = Modifier.weight(1f),
                                    color = black40
                                )

                                is UiState.Error -> Text("MAPE: Error")
                                is UiState.Loading -> {}
                            }

                            when (mseState) {
                                is UiState.Success -> Text(
                                    String.format(Locale.US, "%.2f", mseState.data),
                                    modifier = Modifier.weight(1f),
                                    color = black40
                                )

                                is UiState.Error -> Text("MSE: Error")
                                is UiState.Loading -> {}
                            }
                        }
                    }

                }
            } else {
                Text(
                    text = "Tidak ada data stok untuk periode ini.",
                    color = black40,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        is UiState.Error -> {
            Text(
                text = "Error: ${predictionState.errorMessage}",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Log.d("Pediksi Data", "PrediksiScreen: ${predictionState.errorMessage} ")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GetFromDate(
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onDateSelected: (Long) -> Unit,
    selectedDate: DatePickerState
) {
    if (isOpen) {
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = selectedDate.selectedDateMillis
                        if (selectedDateMillis != null) {
                            onDateSelected(selectedDateMillis)
                        }
                    }
                ) {
                    Text("OK", color = black40)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel", color = black40)
                }
            }
        ) {
            DatePicker(state = selectedDate)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GetToDate(
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onDateSelected: (Long) -> Unit,
    selectedDate: DatePickerState
) {
    if (isOpen) {
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = selectedDate.selectedDateMillis
                        if (selectedDateMillis != null) {
                            onDateSelected(selectedDateMillis)
                        }
                    }
                ) {
                    Text("OK", color = black40)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel", color = black40)
                }
            }
        ) {
            DatePicker(state = selectedDate)
        }
    }
}


@Composable
internal fun DropDownMenu(
    viewModel: InventoryViewModel,
    selectedItem: String?,
    onItemSelected: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    val uiState by viewModel.getInventoryState.collectAsStateWithLifecycle()

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
                .clickable { onExpandedChange(true) }
        )
        Column {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown arrow",
                tint = Color.Black,
                modifier = Modifier
                    .clickable { onExpandedChange(true) }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
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
                                onItemSelected(item.namaBarang ?: "")
                                onExpandedChange(false)
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

internal fun Long.convertMillisToDate(): String {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@convertMillisToDate
    }
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    return sdf.format(calendar.time)
}