package com.lamz.trackinv.ui.screen.add

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
import com.lamz.trackinv.response.category.AddCategoryResponse
import com.lamz.trackinv.response.category.GetAllCategoryResponse
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddViewModel(private val repository: TrackRepository): ViewModel() {

    var addCategory by mutableStateOf("")


    private val _upload = MutableLiveData<UiState<AddCategoryResponse>>()
    val upload: LiveData<UiState<AddCategoryResponse>> = _upload

    private val _getCategory = MutableLiveData<UiState<GetAllCategoryResponse>>()
    val getCategory: LiveData<UiState<GetAllCategoryResponse>> = _getCategory

    fun addCategory(category : String) {
        viewModelScope.launch {
            repository.addCategory(category).asFlow().collect {
                _upload.value = it
            }
        }
    }

    fun getCategory(){
        viewModelScope.launch {
            repository.getCategory().asFlow().collect(){
                _getCategory.value = it
            }
        }
    }

}