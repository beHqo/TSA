package com.example.android.strikingarts.ui.technique

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.MovementType
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.database.entity.TechniqueType
import com.example.android.strikingarts.database.repository.TechniqueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
    val selectedTechniques: Map<Long, Boolean> = mapOf()
)

@HiltViewModel
class TechniqueViewModel @Inject constructor(private val repository: TechniqueRepository) :
    ViewModel() {

    private val allTechniques = MutableStateFlow<List<Technique>>(emptyList())

    private val _uiState = MutableStateFlow(
        TechniqueUiState(selectedTechniques = unSelectAllTechniques())
    )
    val uiState = _uiState.asStateFlow()

    init {
        displayAllTechniques()
    }

    private fun displayAllTechniques() {
        viewModelScope.launch {
            repository.techniqueList.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            ).collect {
                allTechniques.value = it
                _uiState.update { state ->
                    state.copy(
                        tabIndex = 0,
                        chipIndex = Int.MAX_VALUE,
                        visibleTechniques = allTechniques.value.filter { technique ->
                            technique.movementType == MovementType.Offense
                        },
                        selectedTechniques = unSelectAllTechniques()
                    )
                }
            }
        }
    }

    private fun displayTechniquesByMovementType() {
        _uiState.update {
            it.copy(
                chipIndex = Int.MAX_VALUE,
                visibleTechniques = allTechniques.value.filter { technique ->
                    technique.movementType ==
                            if (_uiState.value.tabIndex == 0) MovementType.Offense
                            else MovementType.Defense
                })
        }
    }

    fun onItemSelectionChange(id: Long, selected: Boolean) {
        _uiState.update {
            it.copy(
                selectedTechniques = getSelectedTechniques().also { map -> map[id] = !selected })
        }
    }

    fun onLongPress(id: Long) {
        val selectionMode = _uiState.value.selectionMode

        _uiState.update {
            it.copy(
                selectionMode = !selectionMode,
                selectedTechniques = if (selectionMode) unSelectAllTechniques() else
                    getSelectedTechniques().also { map -> map[id] = true })
        }
    }

    // _uiState needs to get updated twice for visibleTechniques to get recomposed onTabClick
    fun onTabClick(index: Int) { // Don't know why...
        _uiState.update { it.copy(tabIndex = index) }
        displayTechniquesByMovementType()
    }

    fun onChipClick(techniqueType: TechniqueType, index: Int) {
        if (index == Int.MAX_VALUE) displayTechniquesByMovementType()
        else _uiState.update {
            it.copy(
                chipIndex = index, visibleTechniques = allTechniques.value.filter { technique ->
                    technique.techniqueType == techniqueType
                })
        }
    }

    fun showDeleteDialog(id: Long) {
        _uiState.update { it.copy(showDeleteDialog = true, techniqueId = id) }
    }

    fun hideDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun deleteTechnique() {
        viewModelScope.launch { repository.deleteTechnique(_uiState.value.techniqueId) }
        hideDeleteDialog()
    }

    private fun getSelectedTechniques() =
        _uiState.value.selectedTechniques.toMutableMap()

    private fun unSelectAllTechniques(): Map<Long, Boolean> =
        allTechniques.value.associate { it.techniqueId to false }
}