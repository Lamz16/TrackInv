package com.lamz.trackinv.ui.screen.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.ItemsProduct
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.product.GetProductResponse
import com.lamz.trackinv.response.transaksi.GetTransactionResponse
import com.lamz.trackinv.response.transaksi.OutgoingResponse
import kotlinx.coroutines.launch

class TransactionsViewModel(val repository: TrackRepository) : ViewModel() {

    private val _upload = MutableLiveData<UiState<OutgoingResponse>>()
    val upload: LiveData<UiState<OutgoingResponse>> = _upload


    private val _getTransaction = MutableLiveData<UiState<GetTransactionResponse>>()
    val getTransaction: LiveData<UiState<GetTransactionResponse>> = _getTransaction

    fun getTransaction(){
        viewModelScope.launch {
            repository.getTransaction().asFlow().collect{
                _getTransaction.value = it
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