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
    val selectedItems: List<Long> = emptyList(),
    val numberOfSelectedItems: Int = 0
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
            allCombos.collectLatest { comboList ->
                _uiState.update { state ->
                    state.copy(visibleCombos = comboList, selectionMode = initialSelectionMode)
                }
            }
        }
    }

    fun onItemSelectionChange(id: Long, newSelectedValue: Boolean) {
        _uiState.update {
            it.copy(
                selectedItems = it.selectedItems.toMutableList()
                    .also { list -> if (newSelectedValue) list.add(id) else list.remove(id) },
                numberOfSelectedItems = it.numberOfSelectedItems.plus(if (newSelectedValue) 1 else -1)
            )
        }
    }

    fun onLongPress(id: Long, newSelectionModeValue: Boolean) {
        if (_uiState.value.selectionMode) exitSelectionMode() else _uiState.update {
            it.copy(
                selectionMode = newSelectionModeValue,
                selectedItems = listOf(id),
                numberOfSelectedItems = 1
            )
        }
    }

    fun exitSelectionMode() {
        _uiState.update {
            it.copy(
                selectedItems = emptyList(), selectionMode = false, numberOfSelectedItems = 0
            )
        }
    }

    fun deselectAllItems() {
        _uiState.update {
            it.copy(selectedItems = emptyList(), numberOfSelectedItems = 0)
        }
    }

    fun selectAllItems() {
        _uiState.update { state ->
            state.copy(
                selectedItems = _uiState.value.visibleCombos.map { it.combo.comboId },
                numberOfSelectedItems = state.selectedItems.size
            )
        }
    }

    fun showDeleteDialogAndUpdateId(id: Long) {
        _uiState.update { it.copy(showDeleteDialog = true, comboId = id) }
    }

    fun hideDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun deleteItem() {
        viewModelScope.launch { comboRepository.deleteCombo(_uiState.value.comboId) }
        hideDeleteDialog()
    }

    fun showDeleteDialog() = _uiState.update { it.copy(showDeleteDialog = true) }

    fun deleteSelectedItems() {
        viewModelScope.launch {
            comboRepository.deleteCombos(_uiState.value.selectedItems)
        }
        hideDeleteDialog()
    }}