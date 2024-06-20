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

        Text(
            text = "Pilih Periode : ",
            color = black40,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp)
        )

        Row {
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

        getFromDate(
            isOpen = openDialogFrom.value,
            onDismissRequest = { openDialogFrom.value = false },
            onDateSelected = {
                selectedDateLabelFrom.value = it.convertMillisToDate()
                openDialogFrom.value = false
                showAlert.value = false
            },
            selectedDate = selectedDateFrom
        )

        getToDate(
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun getFromDate(
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
private fun getToDate(
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
private fun dropDownMenu(viewModel: InventoryViewModel) {
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
        Column {
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