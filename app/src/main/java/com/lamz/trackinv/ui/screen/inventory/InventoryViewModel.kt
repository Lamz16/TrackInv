package com.lamz.trackinv.ui.screen.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.data.model.BarangModel
import com.lamz.trackinv.data.pref.UserModel
import com.lamz.trackinv.helper.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class InventoryViewModel(private val repository: TrackRepository) : ViewModel() {
    private val _getInventoryState = MutableStateFlow<UiState<List<BarangModel>>>(UiState.Loading)
    val getInventoryState: StateFlow<UiState<List<BarangModel>>> = _getInventoryState

    private val _getInventoryIdState = MutableStateFlow<UiState<BarangModel>>(UiState.Loading)
    val getInventoryIdState: StateFlow<UiState<BarangModel>> = _getInventoryIdState

    private val _deleteProductState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Loading)
    val deleteProductState: MutableStateFlow<UiState<Unit>> = _deleteProductState

    private val _updateProductState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Loading)
    val updateProductState: MutableStateFlow<UiState<Unit>> = _updateProductState

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getAllInventory(idUser: String) {
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

    fun getInventoryId(idBarang: String) {
        viewModelScope.launch {
            repository.getProductId(idBarang).catch {
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
                _deleteProductState.value = UiState.Success(repository.deleteProduct(idbarang))
            } catch (e: Exception) {
                _deleteProductState.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun updateProduct(idbarang : String, barang : BarangModel) {
        viewModelScope.launch {
            _updateProductState.value = UiState.Loading

            try {
                _updateProductState.value = UiState.Success(repository.updateProduct(idbarang, barang))
            } catch (e: Exception) {
                _updateProductState.value = UiState.Error(e.message.toString())
            }
        }
    }


}