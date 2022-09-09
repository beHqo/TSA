package com.example.android.strikingarts.ui.workout

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class WorkoutUiState(
    val textFieldValue1: String = "",
    val textFieldValue2: String = "",

)

@HiltViewModel
class WorkoutViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(WorkoutUiState())
    val uiState = _uiState.asStateFlow()

    fun onTextField1ValueChange(value: String) {
        _uiState.value = _uiState.value.copy(textFieldValue1 = value)
    }

    fun onTextField2ValueChange(value: String) {
        _uiState.value = _uiState.value.copy(textFieldValue2 = value)
    }
}