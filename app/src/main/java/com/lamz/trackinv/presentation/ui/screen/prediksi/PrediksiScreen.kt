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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.Text
import com.lamz.trackinv.R
import com.lamz.trackinv.presentation.model.inventory.InventoryViewModel
import com.lamz.trackinv.presentation.ui.state.UiState
import com.lamz.trackinv.presentation.ui.theme.black40
import com.lamz.trackinv.presentation.ui.theme.red
import com.lamz.trackinv.presentation.ui.view.main.ui.theme.TrackInvTheme
import java.text.SimpleDateFormat
import java.util.Calendar
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

    val showAlert = remember { mutableStateOf(false) }
    val totalStockState by viewModel.totalStockState.collectAsStateWithLifecycle()

    var selectedItem by remember { mutableStateOf<String?>(null) }
    var qty by remember { mutableIntStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

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

        DropDownMenu(
            viewModel = viewModel,
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it },
            expanded = expanded,
            onExpandedChange = { expanded = it })


        Text(
            text = "Pilih Periode : ",
            color = black40,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = selectedDateLabelFrom.value,
                color = black40,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { openDialogFrom.value = true }
            )

            Text(
                text = "Hingga",
                color = black40,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = selectedDateLabelTo.value,
                color = black40,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { openDialogTo.value = true }
            )
        }

        if (showAlert.value) {
            Text(
                text = "Jarak antara tanggal From dan To maksimal 7 hari.",
                color = Color.Red,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        GetFromDate(
            isOpen = openDialogFrom.value,
            onDismissRequest = { openDialogFrom.value = false },
            onDateSelected = {
                selectedDateLabelFrom.value = it.convertMillisToDate()
                openDialogFrom.value = false
                showAlert.value = false
            },
            selectedDate = selectedDateFrom
        )

        GetToDate(
            isOpen = openDialogTo.value,
            onDismissRequest = { openDialogTo.value = false },
            onDateSelected = {
                val fromDateMillis = selectedDateFrom.selectedDateMillis
                val toDateMillis = it
                if (fromDateMillis != null && toDateMillis - fromDateMillis > 7 * 24 * 60 * 60 * 1000) {
                    showAlert.value = true
                } else {
                    selectedDateLabelTo.value = it.convertMillisToDate()
                    showAlert.value = false
                }
                openDialogTo.value = false
            },
            selectedDate = selectedDateTo
        )

        Button(
            onClick = {
                val fromDate = selectedDateLabelFrom.value
                val toDate = selectedDateLabelTo.value
                viewModel.getTotalStock(fromDate, toDate, selectedItem ?: "")
            }, modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = selectedItem != null && selectedDateLabelTo.value != "-" && selectedDateLabelFrom.value != "-",
            colors = ButtonDefaults.buttonColors(black40)
        ) {

            when {
                selectedItem == null -> Text(text = "Pilih item dahulu", color = red,
                    modifier = Modifier.clickable { expanded = true })

                selectedDateLabelFrom.value == "-" -> Text(
                    text = "Pilih dari tanggal dahulu",
                    color = red,
                    modifier = Modifier.clickable { openDialogFrom.value = true }
                )

                selectedDateLabelTo.value == "-" -> Text(
                    text = "Pilih hingga tanggal dahulu",
                    color = red,
                    modifier = Modifier.clickable { openDialogTo.value = true }
                )

                else -> Text(
                    text = "Hitung Total Stok Keluar",
                    color = colorResource(id = R.color.Yellow)
                )
            }
        }

        when (val state = totalStockState) {
            is UiState.Loading -> {}

            is UiState.Success -> {
                qty = state.data
                Text(
                    text = "Total Stok Keluar: ${state.data}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = black40,
                    modifier = Modifier.padding(16.dp)
                )
            }

            is UiState.Error -> {
                Text(
                    text = "Error: ${state.errorMessage}",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GetFromDate(
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
private fun GetToDate(
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
private fun DropDownMenu(
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

fun Long.convertMillisToDate(): String {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@convertMillisToDate
    }
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    return sdf.format(calendar.time)
}


@Composable
@Preview
private fun Preview() {
    TrackInvTheme {
        PrediksiScreen()
    }
}