package com.lamz.trackinv.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: TrackRepository): ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}