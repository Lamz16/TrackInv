package com.lamz.trackinv.ui.screen.partner

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.ItemsProduct
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.partner.AddPartnerResponse
import com.lamz.trackinv.response.partner.GetCustomerResponse
import com.lamz.trackinv.response.partner.GetSupplierResponse
import com.lamz.trackinv.response.product.GetProductResponse
import com.lamz.trackinv.response.transaksi.OutgoingResponse
import kotlinx.coroutines.launch

class PartnerViewModel(private val repository: TrackRepository): ViewModel() {
    private val _getCustomer = MutableLiveData<UiState<GetCustomerResponse>>()
    val getCustomer: LiveData<UiState<GetCustomerResponse>> = _getCustomer

    private val _uploadCustomer = MutableLiveData<UiState<AddPartnerResponse>>()
    val uploadCustomer: LiveData<UiState<AddPartnerResponse>> = _uploadCustomer


    private val _getSupplier = MutableLiveData<UiState<GetSupplierResponse>>()
    val getSupplier: LiveData<UiState<GetSupplierResponse>> = _getSupplier

    private val _uploadSupplier = MutableLiveData<UiState<AddPartnerResponse>>()
    val uploadSupplier: LiveData<UiState<AddPartnerResponse>> = _uploadSupplier

    var outId by mutableStateOf("")

    private val _upload = MutableLiveData<UiState<OutgoingResponse>>()
    val upload: LiveData<UiState<OutgoingResponse>> = _upload


    fun addCustomer(name : String) {
        viewModelScope.launch {
            repository.addCustomer(name).asFlow().collect {
                _uploadCustomer.value = it
            }
        }
    }

    fun getCustomer(){
        viewModelScope.launch {
            repository.getCustomer().asFlow().collect(){
                _getCustomer.value = it
            }
        }
    }


    fun addSupplier(name : String) {
        viewModelScope.launch {
            repository.addSupplier(name).asFlow().collect {
                _uploadSupplier.value = it
            }
        }
    }

    fun getSupplier(){
        viewModelScope.launch {
            repository.getSupplier().asFlow().collect(){
                _getSupplier.value = it
            }
        }
    }

    private val _getProduct = MutableLiveData<UiState<GetProductResponse>>()
    val getProduct: LiveData<UiState<GetProductResponse>> = _getProduct

    fun getAllProduct(){
        viewModelScope.launch {
            repository.getProduct().asFlow().collect{
                _getProduct.value = it
            }
        }
    }

    fun outgoingTran(partnerId : String, items : List<ItemsProduct>) {
        viewModelScope.launch {
            repository.outgoingTran(partnerId, items).asFlow().collect {
                _upload.value = it
            }
        }
    }

    fun incomingTran(partnerId : String, items : List<ItemsProduct>) {
        viewModelScope.launch {
            repository.incomingTran(partnerId, items).asFlow().collect {
                _upload.value = it
            }
        }
    }

}