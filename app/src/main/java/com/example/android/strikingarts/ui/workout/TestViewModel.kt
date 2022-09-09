package com.example.android.strikingarts.ui.workout

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@Immutable
data class ImmutableList<T>(val list: List<T>)

data class TestUiState(
    val textFieldValue1: String = "",
    val textFieldValue2: String = "",
    val optionName: String = "",
    val checkBoxValue: Boolean = false,
//    val visibleOptions: ImmutableList<String> = ImmutableList(emptyList())
)

@HiltViewModel
class TestViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        TestUiState(
//            visibleOptions = ImmutableList(listOf("Option 1", "Option 2"))
        )
    )
    val uiState = _uiState.asStateFlow()
    val visibleOptions = ImmutableList(listOf("Option 1", "Option 2"))

    fun onTextField1ValueChange(value: String) {
        _uiState.value = _uiState.value.copy(textFieldValue1 = value)
    }

    fun onTextField2ValueChange(value: String) {
        _uiState.value = _uiState.value.copy(textFieldValue2 = value)
    }

    fun onCheckBoxValueChange(value: Boolean) {
        _uiState.value = _uiState.value.copy(checkBoxValue = value)
        differentOptions()
    }

    private fun differentOptions() {
        visibleOptions.copy(
            list = if (_uiState.value.checkBoxValue) listOf("Option 3", "Option 4")
            else listOf("Option 1", "Option 2")
        )
    }

    //    private fun differentOptions() {
//        _uiState.value = _uiState.value.copy(visibleOptions = ImmutableList(
//            if (_uiState.value.checkBoxValue) listOf("Option 3", "Option 4")
//            else listOf("Option 1", "Option 2")
//        ))
//    }
}