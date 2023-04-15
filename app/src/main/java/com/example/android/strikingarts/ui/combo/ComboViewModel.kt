package com.example.android.strikingarts.ui.combo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.usecase.combo.DeleteComboUseCase
import com.example.android.strikingarts.domain.usecase.combo.RetrieveComboListUseCase
import com.example.android.strikingarts.domain.usecase.selection.SelectionUseCase
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.COMBO_SELECTION_MODE
import com.example.android.strikingarts.ui.technique.TechniqueViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComboViewModel @Inject constructor(
    retrieveComboListUseCase: RetrieveComboListUseCase,
    private val deleteComboUseCase: DeleteComboUseCase,
    private val selectionUseCase: SelectionUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val initialSelectionMode =
        savedStateHandle[SELECTION_MODE] ?: savedStateHandle[COMBO_SELECTION_MODE] ?: false

    val comboList = retrieveComboListUseCase.comboList.stateIn(
        viewModelScope, WhileSubscribed(5000), ImmutableList()
    )

    val selectedItemsIdList = selectionUseCase.selectedItemsIdList

    private val _selectionMode = MutableStateFlow(initialSelectionMode)
    private val _deleteDialogVisible = MutableStateFlow(false)

    val selectionMode = _selectionMode.asStateFlow()
    val deleteDialogVisible = _deleteDialogVisible.asStateFlow()

    private val itemId = MutableStateFlow(0L)

    init {
        _selectionMode.update { initialSelectionMode }
    }

    fun onItemSelectionChange(id: Long, newSelectedValue: Boolean) {
        selectionUseCase.onItemSelectionChange(id, newSelectedValue)
    }

    fun deselectItem(id: Long) {
        selectionUseCase.deselectItem(id)
    }

    fun onLongPress(id: Long) {
        if (_selectionMode.value) exitSelectionMode() else {
            selectionUseCase.deselectAllItems()
            _selectionMode.update { true }
            selectionUseCase.onItemSelectionChange(id, true)
        }

        savedStateHandle[TechniqueViewModel.SELECTION_MODE] = _selectionMode.value
    }

    fun selectAllItems() {
        selectionUseCase.selectAllItems(comboList.value.map { it.id })
    }

    fun deselectAllItems() {
        selectionUseCase.deselectAllItems()
    }

    fun exitSelectionMode() {
        _selectionMode.update { false }
    }

    fun setSelectedQuantity(id: Long, newQuantity: Int) {
        selectionUseCase.setSelectedQuantity(id, newQuantity)
    }

    fun setDeleteDialogVisibility(visible: Boolean) {
        _deleteDialogVisible.update { visible }
    }

    fun showDeleteDialogAndUpdateId(id: Long) {
        _deleteDialogVisible.update { true }
        itemId.update { id }
    }

    fun deleteItem() {
        viewModelScope.launch { deleteComboUseCase(itemId.value) }
        _deleteDialogVisible.update { false }
    }

    fun deleteSelectedItems() {
        viewModelScope.launch { deleteComboUseCase(selectedItemsIdList.value.toList()) }
        _deleteDialogVisible.update { false }
    }

    companion object {
        const val SELECTION_MODE = "combo_selection_mode"
    }
}