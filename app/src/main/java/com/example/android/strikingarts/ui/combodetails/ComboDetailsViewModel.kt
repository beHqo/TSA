package com.example.android.strikingarts.ui.combodetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.android.strikingarts.ui.components.TEXTFIELD_DESC_MAX_CHARS
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ComboDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var name by mutableStateOf("")
        private set
    var desc by mutableStateOf("")
        private set
    var delay by mutableStateOf(1F)
        private set

    fun onNameChange(value: String) {
        if (value.length <= TEXTFIELD_NAME_MAX_CHARS + 1) name = value
    }

    fun onDescChange(value: String) {
        if (value.length <= TEXTFIELD_DESC_MAX_CHARS + 1) desc = value
    }

    fun onDelayChange(value: Float) {
        delay = value
    }
}