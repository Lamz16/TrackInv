package com.lamz.trackinv.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.data.model.AuthModel
import com.lamz.trackinv.helper.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: TrackRepository) : ViewModel() {

    private val _uiState : MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Loading)
    val uiState : MutableStateFlow<UiState<Unit>> = _uiState


    fun register(register : AuthModel) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                _uiState.value = UiState.Success(repository.register(register))
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }


}