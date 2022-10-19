package com.example.android.strikingarts.ui.combo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.ComboWithTechniques
import com.example.android.strikingarts.database.repository.ComboRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ComboUiState(
    val comboId: Long = 0L,
    val showDeleteDialog: Boolean = false,
    val visibleCombos: List<ComboWithTechniques> = emptyList()
)

@HiltViewModel
class ComboViewModel @Inject constructor(
    private val comboRepository: ComboRepository
) : ViewModel() {

    private val allCombos = comboRepository.comboList.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _uiState = MutableStateFlow(ComboUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            allCombos.collect { comboList ->
                _uiState.update { state -> state.copy(visibleCombos = comboList) }
            }
        }
    }

    fun showDeleteDialog(id: Long) {
        _uiState.update { it.copy(showDeleteDialog = true, comboId = id) }
    }

    fun hideDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun deleteCombo() {
        viewModelScope.launch { comboRepository.deleteCombo(_uiState.value.comboId) }
        hideDeleteDialog()
    }
}