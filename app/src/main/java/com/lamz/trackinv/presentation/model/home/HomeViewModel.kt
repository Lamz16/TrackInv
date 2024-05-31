package com.lamz.trackinv.presentation.model.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _getTransState = MutableStateFlow<UiState<List<TransaksiModel>>>(UiState.Loading)
    val getTransState: StateFlow<UiState<List<TransaksiModel>>> = _getTransState

    fun getAllUpdatedTransactions() {
        viewModelScope.launch {
            useCase.getAllUpdatedTransaction()
                .catch {
                    _getTransState.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _getTransState.value = UiState.Success(it)
                }
        }
    }

}