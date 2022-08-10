package com.example.android.strikingarts.ui.combo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.repository.ComboRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComboViewModel @Inject constructor(
    private val comboRepository: ComboRepository
) : ViewModel() {

    val comboList = comboRepository.comboList
    var comboId by mutableStateOf(0L)
        private set
    var showDeleteDialog by mutableStateOf(false)
        private set

    fun showDeleteDialog(id: Long) {
        showDeleteDialog = true
        comboId = id
    }

    fun hideDeleteDialog() {
        showDeleteDialog = false
    }

    fun deleteCombo() {
        viewModelScope.launch { comboRepository.deleteCombo(comboId) }
        hideDeleteDialog()
    }
}