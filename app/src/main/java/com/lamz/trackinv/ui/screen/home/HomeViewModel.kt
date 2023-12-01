package com.lamz.trackinv.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.ItemsProduct
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.data.pref.UserModel
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.product.DataItem
import com.lamz.trackinv.response.product.GetProductResponse
import com.lamz.trackinv.response.transaksi.OutgoingResponse
import kotlinx.coroutines.launch


class HomeViewModel(private val repository: TrackRepository) : ViewModel() {


    private val _getProduct = MutableLiveData<UiState<GetProductResponse>>()
    val getProduct: LiveData<UiState<GetProductResponse>> = _getProduct

    private val _stokMenipis = MutableLiveData<List<DataItem>>(emptyList())
    val stokMenipis: LiveData<List<DataItem>> = _stokMenipis

    private val _stokTersedia = MutableLiveData<List<DataItem>>(emptyList())
    val stokTersedia: LiveData<List<DataItem>> = _stokTersedia

    private val _stokHabis = MutableLiveData<List<DataItem>>(emptyList())
    val stokhabis: LiveData<List<DataItem>> = _stokHabis

    private val _upload = MutableLiveData<UiState<OutgoingResponse>>()
    val upload: LiveData<UiState<OutgoingResponse>> = _upload

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }


    fun getAllProductsMenipis() {
        viewModelScope.launch() {
            repository.getProduct().asFlow().collect {
                when (it) {
                    is UiState.Success -> {
                        it.data.data.forEach { data ->
                            if (data.stok > 50) {
                                _stokTersedia.value = _stokTersedia.value?.plus(data) ?: listOf(data)
                            } else if (data.stok < 50) {
                                _stokMenipis.value = _stokMenipis.value?.plus(data) ?: listOf(data)
                            } else if (data.stok == 0) {
                                _stokHabis.value = _stokHabis.value?.plus(data) ?: listOf(data)
                            }
                        }
                        _getProduct.value = it
                    }
                    else -> _getProduct.value = it
                }

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