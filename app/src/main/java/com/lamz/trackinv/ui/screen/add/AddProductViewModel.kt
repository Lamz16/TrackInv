package com.lamz.trackinv.ui.screen.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.data.model.BarangModel
import com.lamz.trackinv.data.pref.UserModel
import com.lamz.trackinv.helper.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddProductViewModel(private val repository: TrackRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Loading)
    val uiState: MutableStateFlow<UiState<Unit>> = _uiState

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun addProduct( barang : BarangModel) {

        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                _uiState.value = UiState.Success(repository.addProduct(barang))
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.toString())
            }
        }
    }

}