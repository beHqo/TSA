package com.example.android.strikingarts.ui.combodetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.android.strikingarts.ui.components.TEXTFIELD_DESC_MAX_CHARS
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ComboDetailsViewModel @Inject constructor() : ViewModel() {

    private val _selectedIndices = mutableListOf<Int>()
    val selectedIndices: List<Int>
        get() = _selectedIndices

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

    fun generateErrorMessageForDelayTextfield(value: String) : String? {
        return if (value.isNotEmpty() && !value.isDigitsOnly()) "You can only use numbers here!"
        else if (value.isNotEmpty() && value.isDigitsOnly() && value.toInt() > 15) "You can only rest 15 seconds between each combo"
        else null
    }
}