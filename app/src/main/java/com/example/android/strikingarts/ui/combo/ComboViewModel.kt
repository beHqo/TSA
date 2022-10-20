package com.example.android.strikingarts.ui.combo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.ComboWithTechniques
import com.example.android.strikingarts.database.repository.ComboRepository
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.COMBO_SELECTION_MODE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ComboUiState(
    val comboId: Long = 0L,
    val showDeleteDialog: Boolean = false,
    val visibleCombos: List<ComboWithTechniques> = emptyList(),
    val selectionMode: Boolean = false,
    val selectedCombos: Map<Long, Boolean> = emptyMap()
)

@HiltViewModel
class ComboViewModel @Inject constructor(
    private val comboRepository: ComboRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val initialSelectionMode = savedStateHandle.get<Boolean>(COMBO_SELECTION_MODE) ?: false
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
                _uiState.update { state ->
                    state.copy(
                        visibleCombos = comboList,
                        selectionMode = initialSelectionMode,
                        selectedCombos = unSelectAllCombos()
                    )
                }
            }
        }
    }

    fun onItemSelectionChange(id: Long, selected: Boolean) {
        _uiState.update {
            it.copy(selectedCombos = getSelectedCombos().also { map -> map[id] = !selected })
        }
    }

    fun onLongPress(id: Long, currentSelectionMode: Boolean
    ) {
        _uiState.update {
            it.copy(selectionMode = !currentSelectionMode,
                selectedCombos =
                if (currentSelectionMode) unSelectAllCombos() else getSelectedCombos().also { map ->
                    map[id] = true
                }
            )
        }
    }

    private fun getSelectedCombos() = _uiState.value.selectedCombos.toMutableMap()

    private fun unSelectAllCombos(): Map<Long, Boolean> {
        val map: MutableMap<Long, Boolean> = mutableMapOf()
        viewModelScope.launch {
            allCombos.collect { comboList ->
                map.putAll(comboList.associate { it.combo.comboId to false })
            }
        }

        return map
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