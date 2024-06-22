package com.lamz.trackinv.presentation.ui.screen.prediksi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.Text
import com.lamz.trackinv.R
import com.lamz.trackinv.presentation.model.inventory.InventoryViewModel
import com.lamz.trackinv.presentation.ui.state.UiState
import com.lamz.trackinv.presentation.ui.theme.black40
import com.lamz.trackinv.presentation.ui.theme.red
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBar() {
    CenterAlignedTopAppBar(title = {
        Text(
            text = "Prediksi Stok",
            color = black40,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp
        )
    })
    HorizontalDivider(color = black40)
}

@Composable
internal fun DatePickerRow(
    selectedDateLabelFrom: MutableState<String>,
    openDialogFrom: MutableState<Boolean>,
    selectedDateLabelTo: MutableState<String>,
    openDialogTo: MutableState<Boolean>
) {
    Row {
        Text(
            text = "Pilih Periode : ",
            color = black40,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 16.dp, end = 8.dp)
        )

        Text(
            text = selectedDateLabelFrom.value,
            color = black40,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp)
                .clickable { openDialogFrom.value = true }
        )

        Text(
            text = "to",
            color = black40,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
        )

        Text(
            text = selectedDateLabelTo.value,
            color = black40,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp)
                .clickable { openDialogTo.value = true }
        )
    }
}

@Composable
internal fun PredictButton(
    selectedItem: String?,
    selectedDateLabelFrom: String,
    selectedDateLabelTo: String,
    onClick: () -> Unit,
    onExpandedClick: () -> Unit,
    onOpenDialogFromClick: () -> Unit,
    onOpenDialogToClick: () -> Unit,
    modifier: Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(top = 8.dp),
        enabled = selectedItem != null && selectedDateLabelTo != "-" && selectedDateLabelFrom != "-",
        colors = ButtonDefaults.buttonColors(black40)
    ) {
        when {
            selectedItem == null -> Text(
                text = "Pilih item dahulu",
                color = red,
                modifier = modifier.clickable { onExpandedClick() }
            )

            selectedDateLabelFrom == "-" -> Text(
                text = "Pilih dari tanggal dahulu",
                color = red,
                modifier = modifier.clickable { onOpenDialogFromClick() }
            )

            selectedDateLabelTo == "-" -> Text(
                text = "Pilih hingga tanggal dahulu",
                color = red,
                modifier = modifier.clickable { onOpenDialogToClick() }
            )

            else -> Text(
                text = "Hitung Total Stok Keluar",
                color = colorResource(id = R.color.Yellow)
            )
        }
    }
}


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