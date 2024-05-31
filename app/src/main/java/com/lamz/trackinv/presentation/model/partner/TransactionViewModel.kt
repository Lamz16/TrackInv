package com.lamz.trackinv.presentation.model.partner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.domain.model.BarangModel
import com.lamz.trackinv.domain.model.TransaksiModel
import com.lamz.trackinv.domain.usecase.TrackInvUseCase
import com.lamz.trackinv.presentation.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val useCase: TrackInvUseCase) : ViewModel() {

    private val _getInventoryState = MutableStateFlow<UiState<List<BarangModel>>>(UiState.Loading)
    val getInventoryState: StateFlow<UiState<List<BarangModel>>> = _getInventoryState


    private val _addTransactionState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Loading)
    val addTransactionState: MutableStateFlow<UiState<Unit>> = _addTransactionState

    private val _updateStockState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val updateStockState: StateFlow<UiState<Boolean>> = _updateStockState




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

    fun updateStock(idBarang: String, newStock: Int) {
        viewModelScope.launch {
            _updateStockState.value = UiState.Loading
            try {
                useCase.updateStock(idBarang, newStock)
                _updateStockState.value = UiState.Success(true)
            } catch (e: Exception) {
                _updateStockState.value = UiState.Error(e.message.toString())
            }
        }
    }


    fun addTransactionStock(transaksi : TransaksiModel){
        viewModelScope.launch {
            _addTransactionState.value = UiState.Loading
            try {
                _addTransactionState.value = UiState.Success(useCase.addTransactionStock(transaksi))
            } catch (e: Exception) {
                _addTransactionState.value = UiState.Error(e.message.toString())
            }
        }
    }

}