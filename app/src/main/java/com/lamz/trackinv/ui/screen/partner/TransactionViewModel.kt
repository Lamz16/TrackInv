package com.lamz.trackinv.ui.screen.partner

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

class TransactionViewModel(private val repository: TrackRepository) : ViewModel() {

    private val _getInventoryState = MutableStateFlow<UiState<List<BarangModel>>>(UiState.Loading)
    val getInventoryState: StateFlow<UiState<List<BarangModel>>> = _getInventoryState

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
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

}