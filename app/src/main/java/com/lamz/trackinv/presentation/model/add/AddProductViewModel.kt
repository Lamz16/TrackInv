package com.lamz.trackinv.presentation.model.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamz.trackinv.domain.model.BarangModel
import com.lamz.trackinv.domain.usecase.TrackInvUseCase
import com.lamz.trackinv.presentation.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(private val useCase: TrackInvUseCase) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Loading)
    val uiState: MutableStateFlow<UiState<Unit>> = _uiState

    fun addProduct( barang : BarangModel) {

        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                _uiState.value = UiState.Success(useCase.addProduct(barang))
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.toString())
            }
        }
    }

}