package com.lamz.trackinv.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.data.pref.UserModel
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.auth.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: TrackRepository): ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")


    private val _upload = MutableLiveData<UiState<LoginResponse>>()
    val upload: LiveData<UiState<LoginResponse>> = _upload

    fun login(email: String, password: String,) {
        viewModelScope.launch {
                repository.login(email, password).asFlow().collect {
                    _upload.value = it
                }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}