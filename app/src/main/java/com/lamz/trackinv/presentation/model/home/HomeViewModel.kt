package com.lamz.trackinv.presentation.model.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.domain.model.BarangModel
import com.lamz.trackinv.domain.model.TransaksiModel
import com.lamz.trackinv.domain.usecase.TrackInvUseCase
import com.lamz.trackinv.presentation.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: TrackInvUseCase) : ViewModel() {

    private val _getInventoryStateMoreThan = MutableStateFlow<UiState<List<BarangModel>>>(UiState.Loading)
    val getInventoryStateMoreThan: StateFlow<UiState<List<BarangModel>>> = _getInventoryStateMoreThan

    private val _getInventoryStateThin = MutableStateFlow<UiState<List<BarangModel>>>(UiState.Loading)
    val getInventoryStateThin: StateFlow<UiState<List<BarangModel>>> = _getInventoryStateThin

    private val _getInventoryStateOut = MutableStateFlow<UiState<List<BarangModel>>>(UiState.Loading)
    val getInventoryStateOut: StateFlow<UiState<List<BarangModel>>> = _getInventoryStateOut

    init {
        getAllInventoryMoreThan()
        getAllInventoryThin()
        getAllInventoryOut()
    }
    private fun getAllInventoryMoreThan() {
        viewModelScope.launch {
            useCase.getAllProductMoreThan()
                .catch {
                    _getInventoryStateMoreThan.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _getInventoryStateMoreThan.value = UiState.Success(it)
                    println("Barang lebih dari 50: $it")
                }
        }
    }

    private fun getAllInventoryThin(){
        viewModelScope.launch {
            useCase.getAllProductThin()
                .catch {
                    _getInventoryStateThin.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _getInventoryStateThin.value = UiState.Success(it)
                    println("Barang kurang dari 50: $it")
                }
        }
    }

    private fun getAllInventoryOut(){
        viewModelScope.launch {
            useCase.getALlProductOut()
                .catch {
                    _getInventoryStateOut.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _getInventoryStateOut.value = UiState.Success(it)
                    println("Barang habis: $it")
                }
        }
    }

    private val _getTransState = MutableStateFlow<UiState<List<TransaksiModel>>>(UiState.Loading)
    val getTransState: StateFlow<UiState<List<TransaksiModel>>> = _getTransState

    fun getAllUpdatedTransactions() {
        viewModelScope.launch {
            useCase.getAllUpdatedTransaction()
                .catch {
                    _getTransState.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _getTransState.value = it
                }
        }
    }

}