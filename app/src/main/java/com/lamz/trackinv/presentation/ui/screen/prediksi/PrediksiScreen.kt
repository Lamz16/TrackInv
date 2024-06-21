package com.lamz.trackinv.presentation.ui.screen.prediksi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import com.lamz.trackinv.presentation.ui.theme.black40
import com.lamz.trackinv.presentation.ui.theme.red
import com.lamz.trackinv.presentation.ui.view.main.ui.theme.TrackInvTheme


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
    var expanded by remember { mutableStateOf(false) }

    val predictionState by viewModel.predictionState.collectAsStateWithLifecycle()
    val mapeState by viewModel.mapeState.collectAsStateWithLifecycle()
    val mseState by viewModel.mseState.collectAsStateWithLifecycle()

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
                    showAlert.value = !showAlert.value
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
                viewModel.predictStockOut(fromDate, toDate, selectedItem ?: "")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
            enabled = selectedItem != null && selectedDateLabelTo.value != "-" && selectedDateLabelFrom.value != "-" && !showAlert.value,
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

        //Tampilkan total stock keluar
        TotalStock(totalStockState = totalStockState)

        // Tampilkan hasil prediksi
        PredictionResult(
            predictionState = predictionState,
            mapeState = mapeState,
            mseState = mseState
        )
    }

}




@Composable
@Preview
private fun Preview() {
    TrackInvTheme {
        PrediksiScreen()
    }
}