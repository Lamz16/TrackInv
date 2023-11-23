package com.lamz.trackinv.ui.screen.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.category.GetAllCategoryResponse
import com.lamz.trackinv.response.product.GetProductResponse
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class InventoryViewModel(private val repository: TrackRepository): ViewModel(){

    private val _getProduct = MutableLiveData<UiState<GetProductResponse>>()
    val getProduct: LiveData<UiState<GetProductResponse>> = _getProduct

    fun getAllProduct(){
        viewModelScope.launch {
            repository.getProduct().asFlow().collect{
                _getProduct.value = it
            }
        }
    }

}