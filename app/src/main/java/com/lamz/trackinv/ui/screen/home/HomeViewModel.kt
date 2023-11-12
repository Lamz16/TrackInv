package com.lamz.trackinv.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.data.pref.UserModel
import kotlinx.coroutines.launch


class HomeViewModel(private val repository: TrackRepository): ViewModel() {

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}