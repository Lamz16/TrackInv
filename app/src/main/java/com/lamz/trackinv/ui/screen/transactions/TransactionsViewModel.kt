package com.lamz.trackinv.ui.screen.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.transaksi.GetTransactionResponse
import kotlinx.coroutines.launch

class TransactionsViewModel(val repository: TrackRepository) : ViewModel() {




    private val _getTransaction = MutableLiveData<UiState<GetTransactionResponse>>()
    val getTransaction: LiveData<UiState<GetTransactionResponse>> = _getTransaction

    fun getTransaction(){
        viewModelScope.launch {
            repository.getTransaction().asFlow().collect{
                _getTransaction.value = it
            }
        }
    }




}