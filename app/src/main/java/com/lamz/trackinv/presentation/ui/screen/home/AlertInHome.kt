package com.lamz.trackinv.presentation.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.lamz.trackinv.R
import com.lamz.trackinv.presentation.model.home.HomeViewModel
import com.lamz.trackinv.presentation.ui.component.CardItem1
import com.lamz.trackinv.presentation.ui.component.StockDialog
import com.lamz.trackinv.presentation.ui.state.UiState

@Composable
internal fun StockTersediaAlert(
    modifier: Modifier,
    viewModel: HomeViewModel,
    navController: NavHostController,
) {

    val barangTersedia by viewModel.getInventoryStateMoreThan.collectAsStateWithLifecycle()

    var showStockTersedia by remember { mutableStateOf(false) }

    fun showPopupTersedia() {
        showStockTersedia = true
    }

    fun closePopupTersedia() {
        showStockTersedia = false
    }

    when (val stokTersedia = barangTersedia) {
        is UiState.Error -> {
            if (showStockTersedia) {
                StockDialog(
                    modifier = modifier,
                    onDismissRequest = { closePopupTersedia() },
                    navHostController = navController,
                    listBarang = emptyList(),
                    textError = stokTersedia.errorMessage,
                )
            }
        }

        UiState.Loading -> {}
        is UiState.Success -> {
            CardItem1(
                R.drawable.ic_stok_tersedia,
                stokTersedia.data.size.toString(),
                stringResource(id = R.string.tersedia),
                modifier = Modifier.clickable {
                    showPopupTersedia()
                }
            )
            if (showStockTersedia) {
                StockDialog(
                    modifier = modifier
                        .padding(vertical = 60.dp),
                    onDismissRequest = { closePopupTersedia() },
                    navHostController = navController,
                    listBarang = stokTersedia.data,
                    textEmpty = stringResource(R.string.anda_kekurangan_stok)
                )
            }
        }
    }
}

@Composable
internal fun StockMenipisALert(
    modifier: Modifier,
    viewModel: HomeViewModel,
    navController: NavHostController,
) {
    val barangMenipis by viewModel.getInventoryStateThin.collectAsStateWithLifecycle()

    var showStockMenipis by remember { mutableStateOf(false) }

    fun showPopUpMenipis() {
        showStockMenipis = true
    }

    fun closePopupMenipis() {
        showStockMenipis = false
    }

    when (val produkMenipis = barangMenipis) {
        is UiState.Error -> {
            if (showStockMenipis) {
                StockDialog(
                    modifier = modifier,
                    onDismissRequest = { closePopupMenipis() },
                    navHostController = navController,
                    listBarang = emptyList(),
                    textError = produkMenipis.errorMessage,

                )
            }
        }

        UiState.Loading -> {}
        is UiState.Success -> {
            CardItem1(
                R.drawable.ic_menipis,
                produkMenipis.data.size.toString(),
                stringResource(id = R.string.menipis),
                modifier = Modifier.clickable {
                    showPopUpMenipis()
                }
            )
            if (showStockMenipis) {
                StockDialog(
                    modifier = modifier
                        .padding(vertical = 60.dp),
                    onDismissRequest = { closePopupMenipis() },
                    navHostController = navController,
                    listBarang = produkMenipis.data,
                    textEmpty = stringResource(R.string.tidal_ada_produk_yang_menipis)
                )
            }
        }
    }
}

@Composable
internal fun StockHabisAlert(
    modifier: Modifier,
    viewModel: HomeViewModel,
    navController: NavHostController,
) {
    val barangHabis by viewModel.getInventoryStateOut.collectAsStateWithLifecycle()

    var showStockHabis by remember { mutableStateOf(false) }

    fun showPopUpMenipis() {
        showStockHabis = true
    }

    fun closePopupMenipis() {
        showStockHabis = false
    }

    when (val produkHabis = barangHabis) {
        is UiState.Error -> {
            if (showStockHabis) {
                StockDialog(
                    modifier = modifier,
                    onDismissRequest = { closePopupMenipis() },
                    navHostController = navController,
                    listBarang = emptyList(),
                    textError = produkHabis.errorMessage
                )
            }
        }

        UiState.Loading -> {}
        is UiState.Success -> {

            CardItem1(
                R.drawable.ic_stok_habis,
                produkHabis.data.size.toString(),
                stringResource(id = R.string.habis),
                modifier = Modifier.clickable {
                    showPopUpMenipis()
                }
            )
            if (showStockHabis) {
                StockDialog(
                    modifier = modifier
                        .padding(vertical = 60.dp),
                    onDismissRequest = { closePopupMenipis() },
                    navHostController = navController,
                    listBarang = produkHabis.data,
                    textEmpty = stringResource(R.string.tidak_ada_stok_yang_habis)
                )
            }
        }
    }
}