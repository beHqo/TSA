package com.example.android.strikingarts.ui.techniquedetails

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.*
import com.example.android.strikingarts.database.repository.TechniqueRepository
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@Immutable
data class TechniqueDetailsUiState(
    val name: String = "",
    val num: String = "",
    val techniqueType: TechniqueType = TechniqueType.NONE,
    val movementType: MovementType = MovementType.NONE,
    val color: String = "18446744069414584320",
    val alertDialogVisible: Boolean = false,
    val showColorPicker: Boolean = false,
    val techniqueTypes: List<TechniqueType> = emptyList()
)

@HiltViewModel
class TechniqueDetailsViewModel @Inject constructor(
    private val repository: TechniqueRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val techniqueId = savedStateHandle.get<Long>("techniqueId")
    private val technique = getTechniqueById(techniqueId)

    private val _uiState = MutableStateFlow(
        TechniqueDetailsUiState(
            name = technique.name,
            num = technique.num,
            techniqueType = technique.techniqueType,
            movementType = technique.movementType,
            color = technique.color,
            techniqueTypes =
            if (technique.movementType == MovementType.Defense) getDefenseTypes()
            else getOffenseTypes()
        )
    )
    val uiState = _uiState.asStateFlow()

    private fun getTechniqueById(id: Long?): Technique {
        val technique: Technique = when (id) {
            null -> throw IllegalArgumentException("techniqueId is null")
            0L -> Technique()
            else -> runBlocking { repository.getTechnique(id)!! }
        }
        return technique
    }

    fun onNameChange(value: String) {
        if (value.length <= TEXTFIELD_NAME_MAX_CHARS + 1) _uiState.value =
            _uiState.value.copy(name = value)
    }

    fun onNumChange(value: String) {
        _uiState.value = _uiState.value.copy(num = value)
    }

    fun onDefenseButtonClick() {
        _uiState.value = _uiState.value.copy(
            movementType = MovementType.Defense,
            techniqueType = TechniqueType.NONE,
            techniqueTypes = getDefenseTypes()
        )
    }

    fun onOffenseButtonClick() {
        _uiState.value = _uiState.value.copy(
            movementType = MovementType.Offense,
            techniqueType = TechniqueType.NONE,
            techniqueTypes = getOffenseTypes()
        )
    }

    fun onTechniqueTypeChange(newTechniqueName: String) {
        _uiState.value = _uiState.value.copy(techniqueType = getTechniqueType(newTechniqueName))
    }

    fun showColorPicker() {
        _uiState.value = _uiState.value.copy(showColorPicker = true)
    }

    fun hideColorPicker() {
        _uiState.value = _uiState.value.copy(showColorPicker = false)
    }

    fun onColorChange(newColor: String) {
        _uiState.value = _uiState.value.copy(color = newColor, showColorPicker = false)
    }

    fun showAlertDialog() {
        _uiState.value = _uiState.value.copy(alertDialogVisible = true)
    }

    fun hideAlertDialog() {
        _uiState.value = _uiState.value.copy(alertDialogVisible = false)
    }

    fun onSaveButtonClick() {
        viewModelScope.launch {
            if (techniqueId == 0L) repository.insert(technique)
            else repository.update(
                Technique(
                    techniqueId = technique.techniqueId,
                    name = _uiState.value.name,
                    num = _uiState.value.num,
                    techniqueType = _uiState.value.techniqueType,
                    movementType = _uiState.value.movementType,
                    color = _uiState.value.color
                )
            )
        }
    }
}