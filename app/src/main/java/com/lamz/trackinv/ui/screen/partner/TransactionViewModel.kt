package com.lamz.trackinv.ui.screen.partner

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.data.model.BarangModel
import com.lamz.trackinv.data.model.SupplierModel
import com.lamz.trackinv.data.model.TransaksiModel
import com.lamz.trackinv.data.pref.UserModel
import com.lamz.trackinv.helper.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TrackRepository) : ViewModel() {

    private val _getSupplierState = MutableStateFlow<UiState<SupplierModel>>(UiState.Loading)
    val getSupplierState: StateFlow<UiState<SupplierModel>> = _getSupplierState

    private val _getInventoryState = MutableStateFlow<UiState<List<BarangModel>>>(UiState.Loading)
    val getInventoryState: StateFlow<UiState<List<BarangModel>>> = _getInventoryState


    private val _addTransactionState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Loading)
    val addTransactionState: MutableStateFlow<UiState<Unit>> = _addTransactionState

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getSupplierId(idSupplier : String) {
        viewModelScope.launch {
            repository.getSupplierById(idSupplier)
                .catch {
                    _getSupplierState.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _getSupplierState.value = UiState.Success(it)
                    println("Suppliers loaded: $it")
                }
        }
    }

    fun getAllInventory(idUser : String) {
        viewModelScope.launch {
            repository.getAllProduct(idUser)
                .catch {
                    _getInventoryState.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _getInventoryState.value = UiState.Success(it)
                    println("Suppliers loaded: $it")
                }
        }
    }


    fun addTransactionStock(transaksi : TransaksiModel){
        viewModelScope.launch {
            _addTransactionState.value = UiState.Loading
            try {
                _addTransactionState.value = UiState.Success(repository.addTransactionStock(transaksi))
            } catch (e: Exception) {
                _addTransactionState.value = UiState.Error(e.message.toString())
            }
        }
    }

}