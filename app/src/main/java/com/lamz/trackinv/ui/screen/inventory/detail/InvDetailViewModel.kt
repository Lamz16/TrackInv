package com.lamz.trackinv.ui.screen.inventory.detail

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
import com.lamz.trackinv.response.product.AddProductResponse
import com.lamz.trackinv.response.product.DeleteProductResponse
import com.lamz.trackinv.response.product.GetProductByIdResponse
import com.lamz.trackinv.response.product.UpdateProductResponse
import kotlinx.coroutines.launch

class InvDetailViewModel(private val repository: TrackRepository): ViewModel(){
    var namaBarang by mutableStateOf("")
    var stokBarang by mutableStateOf("")
    var hargaBeli by mutableStateOf("")
    var hargaJual by mutableStateOf("")
    var categoryId by mutableStateOf("")
    var productId by mutableStateOf("")

    private val _getProductId = MutableLiveData<UiState<GetProductByIdResponse>>()
    val getProductId: LiveData<UiState<GetProductByIdResponse>> = _getProductId

    private val _delete= MutableLiveData<UiState<DeleteProductResponse>>()
    val delete: LiveData<UiState<DeleteProductResponse>> = _delete

    private val _updateProduct = MutableLiveData<UiState<UpdateProductResponse>>()
    val updateproduct: LiveData<UiState<UpdateProductResponse>> = _updateProduct

    fun getProductId(id : String){
        viewModelScope.launch {
            repository.getProductId(id).asFlow().collect(){
                _getProductId.value = it
            }
        }
    }

    fun deleteProd(id : String){
        viewModelScope.launch {
            repository.deleteProducts(id).asFlow().collect(){
                _delete.value = it
            }
        }
    }

    fun updateProduct( id: String, name : String,stock : String,category : String,hargabeli : Int,hargaJual : Int,){
        viewModelScope.launch {
            repository.updateProduct(id, name, stock, category, hargabeli, hargaJual).asFlow().collect(){
                _updateProduct.value = it
            }
        }
    }

}