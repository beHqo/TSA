package com.example.android.strikingarts.ui.techniquedetails

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.database.repository.TechniqueRepository
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_ID
import com.example.android.strikingarts.utils.ImmutableSet
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
    private val repository: TechniqueRepository, private val savedStateHandle: SavedStateHandle
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
                name = savedStateHandle[NAME] ?: technique.value.name,
                num = savedStateHandle[NUM] ?: technique.value.num,
                techniqueType = savedStateHandle[TECHNIQUE_TYPE] ?: technique.value.techniqueType,
                movementType = savedStateHandle[MOVEMENT_TYPE] ?: technique.value.movementType,
                color = technique.value.color,
                techniqueTypes = ImmutableSet(
                    if ((savedStateHandle[TECHNIQUE_TYPE]
                            ?: technique.value.movementType) == DEFENSE
                    ) defenseTypes.keys
                    else offenseTypes.keys
                )
            )
        }
    }

    fun onNameChange(value: String) {
        if (value.length <= TEXTFIELD_NAME_MAX_CHARS + 1) {
            _uiState.update { it.copy(name = value) }
            savedStateHandle[NAME] = value
        }
    }

    fun onNumChange(value: String) {
        _uiState.update { it.copy(num = value) }
        savedStateHandle[NUM] = value
    }

    fun onDefenseButtonClick() {
        _uiState.update {
            it.copy(
                movementType = DEFENSE,
                techniqueType = "",
                techniqueTypes = ImmutableSet(defenseTypes.keys)
            )
        }
        savedStateHandle[MOVEMENT_TYPE] = DEFENSE
    }

    fun onOffenseButtonClick() {
        _uiState.update {
            it.copy(
                movementType = OFFENSE, techniqueType = "",
                color = "18446744069414584320", techniqueTypes = ImmutableSet(offenseTypes.keys)
            )
        }
        savedStateHandle[MOVEMENT_TYPE] = OFFENSE
    }

    fun onTechniqueTypeChange(techniqueType: String) {
        _uiState.update { it.copy(techniqueType = techniqueType) }
        savedStateHandle[TECHNIQUE_TYPE] = techniqueType
    }

    fun showColorPicker() {
        _uiState.update { it.copy(showColorPicker = true) }
    }

    fun hideColorPicker() {
        _uiState.update { it.copy(showColorPicker = false) }
    }

    fun onColorChange(newColor: String) {
        _uiState.update { it.copy(color = newColor, showColorPicker = false) }
    }

    fun showAlertDialog() {
        _uiState.update { it.copy(alertDialogVisible = true) }
    }

    fun hideAlertDialog() {
        _uiState.update { it.copy(alertDialogVisible = false) }
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

    companion object {
        const val NAME = "name"
        const val NUM = "num"
        const val TECHNIQUE_TYPE = "technique_type"
        const val MOVEMENT_TYPE = "movement_type"
    }
}