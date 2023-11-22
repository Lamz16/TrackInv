package com.lamz.trackinv.ui.screen.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.auth.RegisterResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: TrackRepository): ViewModel() {
    var namaToko by mutableStateOf("")
    var email by mutableStateOf("")
    var alamat by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")



    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _upload = MutableLiveData<UiState<RegisterResponse>>()
    val upload: LiveData<UiState<RegisterResponse>> = _upload

    fun uploadData(email: String, password: String, username: String, namaToko: String, alamat: String) {
        viewModelScope.launch {
            try {
                _isLoading.postValue(true) // Set loading to true

                repository.registerAccount(email, password, username, namaToko, alamat).asFlow().collect {
                    _upload.value = it
                }
            } finally {
                _isLoading.postValue(false) // Set loading to false
            }
        }
    }
}