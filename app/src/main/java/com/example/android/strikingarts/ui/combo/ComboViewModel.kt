package com.example.android.strikingarts.ui.combo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.ComboWithTechniques
import com.example.android.strikingarts.database.repository.ComboRepository
import com.example.android.strikingarts.domain.uilogic.DeleteDialogActions
import com.example.android.strikingarts.domain.uilogic.SelectionActions
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.COMBO_SELECTION_MODE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ComboUiState(
    val comboId: Long = 0L,
    val showDeleteDialog: Boolean = false,
    val visibleCombos: List<ComboWithTechniques> = emptyList(),
    val selectionMode: Boolean = false,
    val selectedItems: List<Long> = emptyList(),
)

@HiltViewModel
class ComboViewModel @Inject constructor(
    private val comboRepository: ComboRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val initialSelectionMode = savedStateHandle.get<Boolean>(COMBO_SELECTION_MODE) ?: false

    private val comboList =
        comboRepository.comboList.stateIn(viewModelScope, WhileSubscribed(5_000), emptyList())

    private val comboId = MutableStateFlow(0L)
    private val showDeleteDialog = MutableStateFlow(false)
    private val visibleCombos = MutableStateFlow(emptyList<ComboWithTechniques>())
    private val selectedItems = MutableStateFlow(emptyList<Long>())
    private val selectionMode = MutableStateFlow(false)

    val uiState = combine(
        comboId, showDeleteDialog, visibleCombos, selectionMode, selectedItems
    ) { t1, t2, t3, t4, t5 -> ComboUiState(t1, t2, t3, t4, t5) }.stateIn(
            viewModelScope, WhileSubscribed(5000L), ComboUiState())

    val selectionActions = SelectionActions(selectionMode, selectedItems)
    val deleteDialogActions = DeleteDialogActions(showDeleteDialog, comboId)

    init {
        viewModelScope.launch { retrieveComboListAndUpdateState() }
    }

    private suspend fun retrieveComboListAndUpdateState() {
        comboList.collectLatest { resetUiState() }
    }

    private fun resetUiState() {
        visibleCombos.update { comboList.value }
        selectionMode.update { initialSelectionMode }
    }

    fun deleteItem() {
        viewModelScope.launch { comboRepository.deleteCombo(comboId.value) }
        deleteDialogActions.hideDeleteDialog()
    }

    fun deleteSelectedItems() {
        viewModelScope.launch { comboRepository.deleteCombos(selectedItems.value) }
        deleteDialogActions.hideDeleteDialog()
    }
}