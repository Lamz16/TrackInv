package com.lamz.trackinv.ui.screen.partner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.data.model.CustomerModel
import com.lamz.trackinv.data.model.SupplierModel
import com.lamz.trackinv.data.pref.UserModel
import com.lamz.trackinv.helper.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PartnerViewModel(private val repository: TrackRepository) : ViewModel() {

    private val _supplierState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Loading)
    val supplierState: MutableStateFlow<UiState<Unit>> = _supplierState

    private val _customerState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Loading)
    val customerState: MutableStateFlow<UiState<Unit>> = _customerState

    private val _getSupplierState = MutableStateFlow<UiState<List<SupplierModel>>>(UiState.Loading)
    val getSupplierState: StateFlow<UiState<List<SupplierModel>>> = _getSupplierState

    private val _getCustomerState = MutableStateFlow<UiState<List<CustomerModel>>>(UiState.Loading)
    val getCustomerState: StateFlow<UiState<List<CustomerModel>>> = _getCustomerState


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun addSupplier(supplier: SupplierModel) {
        viewModelScope.launch {
            _supplierState.value = UiState.Loading
            try {
                _supplierState.value = UiState.Success(repository.addSupplier(supplier))
            } catch (e: Exception) {
                _supplierState.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun getAllSupplier(idUser : String) {
        viewModelScope.launch {
            repository.getAllSupplier(idUser)
                .catch {
                    _getSupplierState.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _getSupplierState.value = UiState.Success(it)
                    println("Suppliers loaded: $it")
                }
        }
    }

    fun addCustomer(customer: CustomerModel) {

        viewModelScope.launch {
            _customerState.value = UiState.Loading

            try {
                _customerState.value = UiState.Success(repository.addCustomer(customer))
            } catch (e: Exception) {
                _customerState.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun getAllCustomer(idUser : String){
        viewModelScope.launch {
            repository.getAllCustomer(idUser)
                .catch {
                    _getCustomerState.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _getCustomerState.value = UiState.Success(it)
                }
        }
    }

    fun resetSupplierState() {
        _supplierState.value = UiState.Loading
    }

    fun resetCustomerState() {
        _customerState.value = UiState.Loading
    }

}