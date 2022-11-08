package com.example.android.strikingarts.ui.techniquedetails

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.database.repository.TechniqueRepository
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_ID
import com.example.android.strikingarts.utils.*
import com.example.android.strikingarts.utils.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.utils.TechniqueCategory.OFFENSE
import com.example.android.strikingarts.utils.TechniqueCategory.defenseTypes
import com.example.android.strikingarts.utils.TechniqueCategory.offenseTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class TechniqueDetailsUiState(
    val name: String = "",
    val num: String = "",
    val techniqueType: String = "",
    val movementType: String = "",
    val color: String = "18446744069414584320",
    val alertDialogVisible: Boolean = false,
    val showColorPicker: Boolean = false,
    val techniqueTypes: ImmutableSet<String> = ImmutableSet()
)

@HiltViewModel
class TechniqueDetailsViewModel @Inject constructor(
    private val repository: TechniqueRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val techniqueId = savedStateHandle.get<Long>(TECHNIQUE_ID) ?: 0
    private val technique = MutableStateFlow(Technique())
    private val _uiState = MutableStateFlow(TechniqueDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initializeTechniqueAndDisplayState()
    }

    private fun initializeTechniqueAndDisplayState() {
        if (techniqueId == 0L) return
        else viewModelScope.launch {
            repository.getTechnique(techniqueId).also { if (it != null) technique.value = it }
            initialUiUpdate()
        }
    }

    private fun initialUiUpdate() {
        _uiState.update {
            it.copy(
                name = technique.value.name,
                num = technique.value.num,
                techniqueType = technique.value.techniqueType,
                movementType = technique.value.movementType,
                color = technique.value.color,
                techniqueTypes = ImmutableSet(
                    if (technique.value.movementType == DEFENSE) defenseTypes.keys
                    else offenseTypes.keys
                )
            )
        }
    }

    fun onNameChange(value: String) {
        if (value.length <= TEXTFIELD_NAME_MAX_CHARS + 1) _uiState.update { it.copy(name = value) }
    }

    fun onNumChange(value: String) {
        _uiState.update { it.copy(num = value) }
    }

    fun onDefenseButtonClick() {
        _uiState.update {
            it.copy(
                movementType = DEFENSE,
                techniqueType = "",
                techniqueTypes = ImmutableSet(defenseTypes.keys)
            )
        }
    }

    fun onOffenseButtonClick() {
        _uiState.update {
            it.copy(
                movementType = OFFENSE,
                techniqueType = "",
                color = "18446744069414584320",
                techniqueTypes = ImmutableSet(offenseTypes.keys)
            )
        }
    }

    fun onTechniqueTypeChange(techniqueType: String) {
        _uiState.update { _uiState.value.copy(techniqueType = techniqueType) }
    }

    fun showColorPicker() {
        _uiState.update { _uiState.value.copy(showColorPicker = true) }
    }

    fun hideColorPicker() {
        _uiState.update { _uiState.value.copy(showColorPicker = false) }
    }

    fun onColorChange(newColor: String) {
        _uiState.update { _uiState.value.copy(color = newColor, showColorPicker = false) }
    }

    fun showAlertDialog() {
        _uiState.update { _uiState.value.copy(alertDialogVisible = true) }
    }

    fun hideAlertDialog() {
        _uiState.update { _uiState.value.copy(alertDialogVisible = false) }
    }

    fun onSaveButtonClick() {
        viewModelScope.launch {
            if (techniqueId == 0L) repository.insert(technique.value)
            else repository.update(
                Technique(
                    techniqueId = technique.value.techniqueId,
                    name = _uiState.value.name,
                    num = _uiState.value.num,
                    techniqueType = _uiState.value.techniqueType,
                    movementType = _uiState.value.movementType,
                    color = _uiState.value.color,
                    canBeBodyshot = _uiState.value.movementType == OFFENSE,
                    canBeFaint = _uiState.value.movementType == OFFENSE
                )
            )
        }
    }
}