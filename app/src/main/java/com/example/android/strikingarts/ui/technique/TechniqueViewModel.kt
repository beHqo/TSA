package com.example.android.strikingarts.ui.technique

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.MovementType
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.database.entity.TechniqueType
import com.example.android.strikingarts.database.repository.TechniqueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class TechniqueUiState(
    val techniqueId: Long = 0,
    val tabIndex: Int = 0,
    val chipIndex: Int = Int.MAX_VALUE,
    val showDeleteDialog: Boolean = false,
    val visibleTechniques: List<Technique> = emptyList()
)

@HiltViewModel
class TechniqueViewModel @Inject constructor(private val repository: TechniqueRepository) :
    ViewModel() {

    private val allTechniques = MutableStateFlow<List<Technique>>(emptyList())

    private val _uiState = MutableStateFlow(TechniqueUiState())
    val uiState = _uiState.asStateFlow()

    init {
        displayAllTechniques()
    }

    private fun displayAllTechniques() {
        viewModelScope.launch {
            repository.techniqueList.collect {
                allTechniques.value = it
                displayTechniquesByMovementType()
            }
        }
    }

    private fun displayTechniquesByMovementType() {
        _uiState.value = _uiState.value.copy(visibleTechniques = allTechniques.value.filter {
            it.movementType ==
                    if (_uiState.value.tabIndex == 0) MovementType.Offense
                    else MovementType.Defense
        })
    }

    fun onTabClick(index: Int) {
        _uiState.value = _uiState.value.copy(chipIndex = Int.MAX_VALUE, tabIndex = index)
        displayTechniquesByMovementType()
    }

    fun onChipClick(techniqueType: TechniqueType, index: Int) {
        _uiState.value = _uiState.value.copy(chipIndex = index, visibleTechniques = emptyList())

        if (_uiState.value.chipIndex == Int.MAX_VALUE)
            displayTechniquesByMovementType()
        else
            _uiState.value = _uiState.value.copy(visibleTechniques = allTechniques.value.filter {
                it.techniqueType == techniqueType
            })
    }

    fun showDeleteDialog(id: Long) {
        _uiState.value = _uiState.value.copy(showDeleteDialog = true, techniqueId = id)
    }

    fun hideDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = false)
    }

    fun deleteTechnique() {
        viewModelScope.launch { repository.deleteTechnique(_uiState.value.techniqueId) }
        hideDeleteDialog()
    }
}