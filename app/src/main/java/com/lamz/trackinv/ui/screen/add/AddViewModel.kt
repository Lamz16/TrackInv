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
import com.lamz.trackinv.response.category.GetCategoryIdResponse
import com.lamz.trackinv.response.membership.MembershipResponse
import com.lamz.trackinv.response.product.AddProductResponse
import kotlinx.coroutines.launch

class AddViewModel(private val repository: TrackRepository): ViewModel() {
    var addCategory by mutableStateOf("")
    var namaBarang by mutableStateOf("")
    var stokBarang by mutableStateOf("")
    var hargaBeli by mutableStateOf("0")
    var hargaJual by mutableStateOf("0")
    var categoryId by mutableStateOf("")



    private val _upload = MutableLiveData<UiState<AddCategoryResponse>>()
    val upload: LiveData<UiState<AddCategoryResponse>> = _upload

    private val _getCategory = MutableLiveData<UiState<GetAllCategoryResponse>>()
    val getCategory: LiveData<UiState<GetAllCategoryResponse>> = _getCategory

    private val _getCategoryId = MutableLiveData<UiState<GetCategoryIdResponse>>()
    val getCategoryId: LiveData<UiState<GetCategoryIdResponse>> = _getCategoryId

    private val _delete= MutableLiveData<UiState<GetCategoryIdResponse>>()
    val delete: LiveData<UiState<GetCategoryIdResponse>> = _delete

    private val _updateCategory = MutableLiveData<UiState<GetCategoryIdResponse>>()
    val updateCategory: LiveData<UiState<GetCategoryIdResponse>> = _updateCategory

    private val _uploadProduct = MutableLiveData<UiState<AddProductResponse>>()
    val uploadProduct: LiveData<UiState<AddProductResponse>> = _uploadProduct

    private val _uploadMembership = MutableLiveData<UiState<MembershipResponse>>()
    val uploadMembership: LiveData<UiState<MembershipResponse>> = _uploadMembership

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

    fun getCategoryId(id : String){
        viewModelScope.launch {
            repository.getCategoryId(id).asFlow().collect(){
                _getCategoryId.value = it
            }
        }
    }

    fun editCategory( id : String, name : String){
        viewModelScope.launch {
            repository.updateCategory(id, name).asFlow().collect(){
                _updateCategory.value = it
            }
        }
    }

    fun deleteCategory( id : String){
        viewModelScope.launch {
            repository.deleteCategory(id).asFlow().collect(){
                _delete.value = it
            }
        }
    }

    fun addProduct( name : String,stock : String,category : String,hargabeli : Int,hargaJual : Int,){
        viewModelScope.launch {
            repository.addProduct(name, stock, category, hargabeli, hargaJual).asFlow().collect(){
                _uploadProduct.value = it
            }
        }
    }



    fun membership() {
        viewModelScope.launch {
            repository.membership().asFlow().collect {
                _uploadMembership.value = it
            }
        }
    }

    fun membershipTahun() {
        viewModelScope.launch {
            repository.membershipTahun().asFlow().collect {
                _uploadMembership.value = it
            }
        }
    }

}