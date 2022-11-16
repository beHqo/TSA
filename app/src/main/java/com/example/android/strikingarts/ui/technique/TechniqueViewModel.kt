package com.example.android.strikingarts.ui.technique

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.database.repository.SelectedItemsRepository
import com.example.android.strikingarts.database.repository.TechniqueRepository
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_SELECTION_MODE
import com.example.android.strikingarts.utils.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.utils.TechniqueCategory.OFFENSE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
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
    val numberOfSelectedItems: Int = 0
)

@HiltViewModel
class TechniqueViewModel @Inject constructor(
    private val techniqueRepository: TechniqueRepository,
    private val selectedItemRepository: SelectedItemsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialSelectionModeValue = savedStateHandle[TECHNIQUE_SELECTION_MODE] ?: false

    private val techniqueList = MutableStateFlow(emptyList<Technique>())

    private val _uiState = MutableStateFlow(TechniqueUiState())
    val uiState = _uiState.asStateFlow()

    init {
        displayAllTechniques()
    }

    private fun displayAllTechniques() {
        viewModelScope.launch {
            techniqueRepository.techniqueList.stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
            ).collectLatest { techniques ->
                techniqueList.update { techniques }

                _uiState.update { state ->
                    state.copy(
                        tabIndex = 0,
                        chipIndex = Int.MAX_VALUE,
                        visibleTechniques = techniques.filter { it.movementType == OFFENSE },
                        selectionMode = initialSelectionModeValue
                    )
                }
            }
        }
    }

    private fun displayTechniquesByMovementType() {
        _uiState.update { state ->
            state.copy(chipIndex = Int.MAX_VALUE,
                visibleTechniques = techniqueList.value.filter { technique ->
                    technique.movementType == if (state.tabIndex == 0) OFFENSE else DEFENSE
                })
        }
    }

    fun onTabClick(index: Int) {
        _uiState.update { state ->
            state.copy(chipIndex = Int.MAX_VALUE,
                tabIndex = index,
                visibleTechniques = techniqueList.value.filter { technique ->
                    technique.movementType == if (index == 0) OFFENSE else DEFENSE
                })
        }
    }

    fun onChipClick(techniqueType: String, index: Int) {
        if (index == Int.MAX_VALUE) displayTechniquesByMovementType()
        else _uiState.update { state ->
            state.copy(
                chipIndex = index,
                visibleTechniques = techniqueList.value.filter { technique ->
                    technique.techniqueType == techniqueType
                })
        }
    }

    fun onItemSelectionChange(id: Long, newSelectedValue: Boolean) {
        _uiState.update { state ->
            state.copy(
                selectedItems = state.selectedItems.toMutableList()
                    .also { list -> if (newSelectedValue) list.add(id) else list.remove(id) },
                numberOfSelectedItems = state.numberOfSelectedItems.plus(if (newSelectedValue) 1 else -1)
            )
        }
    }

    fun onLongPress(id: Long, newSelectionModeValue: Boolean) {
        if (_uiState.value.selectionMode) exitSelectionMode() else _uiState.update { state ->
            state.copy(
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
                selectedItems = _uiState.value.visibleTechniques.map { it.techniqueId },
                numberOfSelectedItems = state.selectedItems.size
            )
        }
    }

    fun updateSelectedItemIds() {
        selectedItemRepository.selectedIds.value = _uiState.value.selectedItems
    }

    fun showDeleteDialogAndUpdateId(id: Long) {
        _uiState.update { it.copy(showDeleteDialog = true, techniqueId = id) }
    }

    fun hideDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun deleteItem() {
        viewModelScope.launch { techniqueRepository.deleteTechnique(_uiState.value.techniqueId) }
        hideDeleteDialog()
    }

    fun showDeleteDialog() = _uiState.update { it.copy(showDeleteDialog = true) }

    fun deleteSelectedItems() {
        viewModelScope.launch {
            techniqueRepository.deleteTechniques(_uiState.value.selectedItems)
        }
        hideDeleteDialog()
    }
}