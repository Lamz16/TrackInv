package com.lamz.trackinv.ui.screen.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lamz.trackinv.data.TrackRepository

class RegisterViewModel(private val repository: TrackRepository): ViewModel() {
    var name by mutableStateOf("")
    var phone by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
}