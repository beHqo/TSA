package com.example.android.strikingarts.ui.technique

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.database.repository.TechniqueRepository
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_SELECTION_MODE
import com.example.android.strikingarts.utils.DEFENSE
import com.example.android.strikingarts.utils.OFFENSE
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
class TechniqueViewModel @Inject constructor(
    private val repository: TechniqueRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialSelectionModeValue =
        savedStateHandle.get<Boolean>(TECHNIQUE_SELECTION_MODE) ?: false

    private val allTechniques: StateFlow<List<Technique>> = repository.techniqueList.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _uiState = MutableStateFlow(
        TechniqueUiState(selectedTechniques = unSelectAllTechniques())
    )
    val uiState = _uiState.asStateFlow()

    init {
        displayAllTechniques()
    }

    private fun displayAllTechniques() {
        viewModelScope.launch {
            allTechniques.collect { techniqueList ->
                _uiState.update { state ->
                    state.copy(
                        tabIndex = 0,
                        chipIndex = Int.MAX_VALUE,
                        visibleTechniques = techniqueList.filter { technique ->
                            technique.movementType == OFFENSE
                        },
                        selectionMode = initialSelectionModeValue,
                        selectedTechniques = unSelectAllTechniques()
                    )
                }
            }
        }
    }

    private fun displayTechniquesByMovementType() {
        viewModelScope.launch {
            allTechniques.collectLatest { techniqueList ->
                _uiState.update { state ->
                    state.copy(chipIndex = Int.MAX_VALUE,
                        visibleTechniques = techniqueList.filter { technique ->
                            technique.movementType == if (state.tabIndex == 0) OFFENSE else DEFENSE
                        }
                    )
                }
            }
        }
    }

    fun onTabClick(index: Int) {
        viewModelScope.launch {
            allTechniques.collectLatest { techniqueList ->
                _uiState.update { state ->
                    state.copy(chipIndex = Int.MAX_VALUE,
                        tabIndex = index,
                        visibleTechniques = techniqueList.filter { technique ->
                            technique.movementType == if (index == 0) OFFENSE else DEFENSE
                        }
                    )
                }
            }
        }
    }

    fun onChipClick(techniqueType: String, index: Int) {
        if (index == Int.MAX_VALUE) displayTechniquesByMovementType()
        else viewModelScope.launch {
            allTechniques.collectLatest { techniqueList ->
                _uiState.update { state ->
                    state.copy(chipIndex = index,
                        visibleTechniques = techniqueList.filter { technique ->
                            technique.techniqueType == techniqueType
                        }
                    )
                }
            }
        }
    }

    fun onItemSelectionChange(id: Long, selected: Boolean) {
        _uiState.update {
            it.copy(selectedTechniques = getSelectedTechniques().also { map -> map[id] = !selected }
            )
        }
    }

    fun onLongPress(id: Long, currentSelectionMode: Boolean) {
        _uiState.update {
            it.copy(selectionMode = !currentSelectionMode,
                selectedTechniques =
                if (currentSelectionMode) unSelectAllTechniques() else getSelectedTechniques()
                    .also { map -> map[id] = true }
            )
        }
    }

    private fun getSelectedTechniques() = _uiState.value.selectedTechniques.toMutableMap()

    private fun unSelectAllTechniques(): Map<Long, Boolean> {
        val map: MutableMap<Long, Boolean> = mutableMapOf()
        viewModelScope.launch {
            allTechniques.collectLatest { techniqueList ->
                map.putAll(techniqueList.associate { it.techniqueId to false })
            }
        }

        return map
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
}