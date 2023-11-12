package com.lamz.trackinv.ui.screen.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lamz.trackinv.data.TrackRepository

class AddViewModel(private val repository: TrackRepository): ViewModel() {
    var namaBarang by mutableStateOf("")
    var stokBarang by mutableStateOf("")
    var hargaBeli by mutableStateOf("")
    var hargaJual by mutableStateOf("")
}