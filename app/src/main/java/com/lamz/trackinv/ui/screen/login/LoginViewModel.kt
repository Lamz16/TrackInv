package com.lamz.trackinv.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.data.model.AuthModel
import com.lamz.trackinv.data.pref.UserModel
import com.lamz.trackinv.helper.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: TrackRepository): ViewModel() {

    private val _uiState : MutableStateFlow<UiState<AuthModel>> = MutableStateFlow(UiState.Loading)
    val uiState : MutableStateFlow<UiState<AuthModel>> = _uiState

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email : String, password : String){
        viewModelScope.launch {
            repository.login(email, password).collect{
                _uiState.value = it
            }
        }
    }
}