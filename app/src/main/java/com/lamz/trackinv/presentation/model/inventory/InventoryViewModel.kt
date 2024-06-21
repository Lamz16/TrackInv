package com.lamz.trackinv.presentation.model.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.domain.model.BarangModel
import com.lamz.trackinv.domain.model.StockData
import com.lamz.trackinv.domain.usecase.TrackInvUseCase
import com.lamz.trackinv.presentation.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(private val useCase: TrackInvUseCase) : ViewModel() {
    private val _getInventoryState = MutableStateFlow<UiState<List<BarangModel>>>(UiState.Loading)
    val getInventoryState: StateFlow<UiState<List<BarangModel>>> = _getInventoryState

    private val _getInventoryIdState = MutableStateFlow<UiState<BarangModel>>(UiState.Loading)
    val getInventoryIdState: StateFlow<UiState<BarangModel>> = _getInventoryIdState

    private val _deleteProductState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Loading)
    val deleteProductState: MutableStateFlow<UiState<Unit>> = _deleteProductState

    private val _updateProductState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Loading)
    val updateProductState: MutableStateFlow<UiState<Unit>> = _updateProductState


    init {
        getAllInventory()
    }
    fun getAllInventory() {
        viewModelScope.launch {
            useCase.getAllProduct()
                .catch {
                    _getInventoryState.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _getInventoryState.value = UiState.Success(it)
                    println("Suppliers loaded: $it")
                }
        }
    }

    fun getInventoryId(idBarang: String) {
        viewModelScope.launch {
            useCase.getProductId(idBarang).catch {
                _getInventoryIdState.value = UiState.Error(it.message.toString())
            }
                .collect {
                    _getInventoryIdState.value = UiState.Success(it)
                }
        }
    }

    fun deleteProduct(idbarang : String) {
        viewModelScope.launch {
            _deleteProductState.value = UiState.Loading

            try {
                _deleteProductState.value = UiState.Success(useCase.deleteProduct(idbarang))
            } catch (e: Exception) {
                _deleteProductState.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun updateProduct(idbarang : String, barang : BarangModel) {
        viewModelScope.launch {
            _updateProductState.value = UiState.Loading

            try {
                _updateProductState.value = UiState.Success(useCase.updateProduct(idbarang, barang))
            } catch (e: Exception) {
                _updateProductState.value = UiState.Error(e.message.toString())
            }
        }
    }

    private val _totalStockState = MutableStateFlow<UiState<Int>>(UiState.Loading)
    val totalStockState: StateFlow<UiState<Int>> = _totalStockState

    fun getTotalStock(fromDate: String, toDate: String, namaBarang: String) {
        _totalStockState.value = UiState.Loading
        viewModelScope.launch {
            useCase.getTransactionsByDateRange(fromDate, toDate, namaBarang)
                .catch {
                    _totalStockState.value = UiState.Error(it.message.toString())
                }
                .collect { transactions ->
                    val totalStock = transactions.filter { it.jenisTran == "Keluar" }
                        .sumOf { it.jumlah?.toIntOrNull() ?: 0 }
                    _totalStockState.value = UiState.Success(totalStock)
                }
        }
    }


    private val _predictionState = MutableStateFlow<UiState<List<StockData>>>(UiState.Loading)
    val predictionState: StateFlow<UiState<List<StockData>>> = _predictionState

    private val _mapeState = MutableStateFlow<UiState<Double>>(UiState.Loading)
    val mapeState: StateFlow<UiState<Double>> = _mapeState

    private val _mseState = MutableStateFlow<UiState<Double>>(UiState.Loading)
    val mseState: StateFlow<UiState<Double>> = _mseState

    fun predictStockOut(fromDate: String, toDate: String, namaBarang: String) {
        _predictionState.value = UiState.Loading
        viewModelScope.launch {
            useCase.predictStockOut(fromDate, toDate, namaBarang)
                .catch {
                    _predictionState.value = UiState.Error(it.message.toString())
                }
                .collect { (prediction, mape, mse) ->
                    _predictionState.value = UiState.Success(prediction)
                    _mapeState.value = UiState.Success(mape)
                    _mseState.value = UiState.Success(mse)
                }
        }
    }


}