package com.example.android.strikingarts.ui.technique

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.model.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.domain.model.TechniqueCategory.OFFENSE
import com.example.android.strikingarts.domain.usecase.selection.SelectionUseCase
import com.example.android.strikingarts.domain.usecase.technique.DeleteTechniqueUseCase
import com.example.android.strikingarts.domain.usecase.technique.FilterTechniquesUseCase
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_SELECTION_MODE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TechniqueViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val selectionUseCase: SelectionUseCase,
    private val filterTechniquesUseCase: FilterTechniquesUseCase,
    private val deleteTechniquesUseCase: DeleteTechniqueUseCase
) : ViewModel() {
    private val initialSelectionMode =
        savedStateHandle[SELECTION_MODE] ?: savedStateHandle[TECHNIQUE_SELECTION_MODE] ?: false

    val visibleTechniques = filterTechniquesUseCase.techniqueList.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ImmutableList()
    )

    val selectedItemsIdList = selectionUseCase.selectedItemsIdList

    private val itemId = MutableStateFlow(0L)
    private val _selectionMode = MutableStateFlow(initialSelectionMode)
    private val _deleteDialogVisible = MutableStateFlow(false)
    private val _tabIndex = MutableStateFlow(0)
    private val _chipIndex = MutableStateFlow(CHIP_INDEX_ALL)

    val selectionMode = _selectionMode.asStateFlow()
    val deleteDialogVisible = _deleteDialogVisible.asStateFlow()
    val tabIndex = _tabIndex.asStateFlow()
    val chipIndex = _chipIndex.asStateFlow()

    init {
        _selectionMode.update { initialSelectionMode }
    }

    private fun filterTechniquesByMovementType() {
        _chipIndex.update { CHIP_INDEX_ALL }
        filterTechniquesUseCase.setTechniqueType("")
        filterTechniquesUseCase.setMovementType(
            if (_tabIndex.value == OFFENSE_TAB_INDEX) OFFENSE else DEFENSE
        )
    }

    fun onTabClick(index: Int) {
        _tabIndex.update { index }
        filterTechniquesByMovementType()
    }

    fun onChipClick(techniqueType: String, index: Int) {
        if (index == CHIP_INDEX_ALL) filterTechniquesByMovementType() else {
            _chipIndex.update { index }
            filterTechniquesUseCase.setTechniqueType(techniqueType)
        }
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

        savedStateHandle[SELECTION_MODE] = _selectionMode.value
    }

    fun selectAllItems() {
        viewModelScope.launch {
            selectionUseCase.selectAllItems(visibleTechniques.value.map { it.id })
        }
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
        viewModelScope.launch { deleteTechniquesUseCase(itemId.value) }
        _deleteDialogVisible.update { false }
    }

    fun deleteSelectedItems() {
        viewModelScope.launch { deleteTechniquesUseCase(selectedItemsIdList.value) }
        _deleteDialogVisible.update { false }
    }

    companion object {
        private const val SELECTION_MODE = "technique_selection_mode"

        internal const val CHIP_INDEX_ALL = Int.MAX_VALUE
        internal const val OFFENSE_TAB_INDEX = 0
    }
}
