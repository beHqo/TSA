package com.example.android.strikingarts.ui.technique

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.database.repository.SelectedItemsRepository
import com.example.android.strikingarts.database.repository.TechniqueRepository
import com.example.android.strikingarts.domain.uilogic.DeleteDialogActions
import com.example.android.strikingarts.domain.uilogic.SelectionActions
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_SELECTION_MODE
import com.example.android.strikingarts.utils.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.utils.TechniqueCategory.OFFENSE
import com.example.android.strikingarts.utils.combine7
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class TechniqueUiState(
    val techniqueId: Long = 0,
    val tabIndex: Int = 0,
    val chipIndex: Int = Int.MAX_VALUE,
    val showDeleteDialog: Boolean = false,
    val selectionMode: Boolean = false,
    val visibleTechniques: List<Technique> = emptyList(),
    val selectedItems: List<Long> = emptyList(),
)

const val CHIP_INDEX_ALL = Int.MAX_VALUE

@HiltViewModel
class TechniqueViewModel @Inject constructor(
    private val techniqueRepository: TechniqueRepository,
    private val selectedItemRepository: SelectedItemsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialSelectionModeValue = savedStateHandle[TECHNIQUE_SELECTION_MODE] ?: false

    private val techniqueList = techniqueRepository.techniqueList
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private val techniqueId = MutableStateFlow(0L)
    private val tabIndex = MutableStateFlow(0)
    private val chipIndex = MutableStateFlow(CHIP_INDEX_ALL)
    private val showDeleteDialog = MutableStateFlow(false)
    private val selectionMode = MutableStateFlow(initialSelectionModeValue)
    private val visibleTechniques = MutableStateFlow(emptyList<Technique>())
    private val selectedItems = MutableStateFlow(emptyList<Long>())

    val uiState = combine7(
        techniqueId,
        tabIndex,
        chipIndex,
        showDeleteDialog,
        selectionMode,
        visibleTechniques,
        selectedItems,
    ) { t1, t2, t3, t4, t5, t6, t7 ->
        TechniqueUiState(t1, t2, t3, t4, t5, t6, t7)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), TechniqueUiState())

    val selectionActions = SelectionActions(selectionMode, selectedItems)
    val deleteDialogActions = DeleteDialogActions(showDeleteDialog, techniqueId)

    init {
        viewModelScope.launch { retrieveTechniqueListAndUpdateUiState() }
    }

    private fun resetUiState() {
        selectionMode.update { initialSelectionModeValue } //TODO: is this needed?
        tabIndex.update { 0 }
        chipIndex.update { CHIP_INDEX_ALL }
        visibleTechniques.update { techniqueList.value.filter { it.movementType == OFFENSE } }
    }

    private suspend fun retrieveTechniqueListAndUpdateUiState() {
        techniqueList.collectLatest { resetUiState() }
    }

    private fun updateVisibleTechniques() {
        visibleTechniques.update {
            techniqueList.value.filter {
                it.movementType == if (tabIndex.value == 0) OFFENSE else DEFENSE
            }
        }
    }

    private fun displayTechniquesByMovementType() {
        chipIndex.update { CHIP_INDEX_ALL }
        updateVisibleTechniques()
    }

    fun onTabClick(index: Int) {
        chipIndex.update { CHIP_INDEX_ALL }
        tabIndex.update { index }
        updateVisibleTechniques()
    }

    fun onChipClick(techniqueType: String, index: Int) {
        if (index == CHIP_INDEX_ALL) displayTechniquesByMovementType() else {
            chipIndex.update { index }
            visibleTechniques.update {
                techniqueList.value.filter { it.techniqueType == techniqueType }
            }
        }
    }

    fun updateSelectedItemIds() {
        selectedItemRepository.selectedIds.value = selectedItems.value
    }

    fun deleteItem() {
        deleteDialogActions.hideDeleteDialog()
        viewModelScope.launch { techniqueRepository.deleteTechnique(techniqueId.value) }
    }

    fun deleteSelectedItems() {
        deleteDialogActions.hideDeleteDialog()
        viewModelScope.launch { techniqueRepository.deleteTechniques(selectedItems.value) }
    }
}
