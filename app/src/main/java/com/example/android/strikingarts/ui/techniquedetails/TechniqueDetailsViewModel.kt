package com.example.android.strikingarts.ui.techniquedetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.*
import com.example.android.strikingarts.database.repository.TechniqueRepository
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class TechniqueDetailsViewModel @Inject constructor(
    private val repository: TechniqueRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val techniqueId = savedStateHandle.get<Long>("techniqueId")
    private val technique = getTechniqueById(techniqueId)

    var name by mutableStateOf(technique.name)
        private set
    var num by mutableStateOf(technique.num)
        private set
    var techniqueType by mutableStateOf(technique.techniqueType)
        private set
    var movementType by mutableStateOf(technique.movementType)
        private set
    var color by mutableStateOf(technique.color)
        private set
    var alertDialogVisible by mutableStateOf(false)
        private set
    var showColorPicker by mutableStateOf(false)
        private set
    val techniqueTypes = mutableStateListOf<TechniqueType>()

    private fun getTechniqueById(id: Long?): Technique {
        val technique: Technique = when (id) {
            null -> throw IllegalArgumentException("techniqueId is null")
            0L -> Technique()
            else -> runBlocking { repository.getTechnique(id)!! }
        }
        return technique
    }

    fun onNameChange(value: String) {
        if (value.length <= TEXTFIELD_NAME_MAX_CHARS + 1) name = value
    }

    fun onNumChange(value: String) {
        num = value
    }

    fun onDefenseButtonClick() {
        onMovementButtonClick(MovementType.Defense)
    }

    fun onOffenseButtonClick() {
        onMovementButtonClick(MovementType.Offense)
    }

    private fun onMovementButtonClick(newMovementType: MovementType) {
        movementType = newMovementType
        techniqueType = TechniqueType.NONE
        techniqueTypes.clear()
        if (newMovementType == MovementType.Offense) techniqueTypes.addAll(getOffenseTypes())
        else techniqueTypes.addAll(getDefenseTypes())
    }

    fun onTechniqueTypeChange(newTechniqueName: String) {
        techniqueType = getTechniqueType(newTechniqueName)
    }

    fun showColorPicker() {
        showColorPicker = true
    }

    fun hideColorPicker() {
        showColorPicker = false
    }

    fun onColorChange(newColor: String) {
        color = newColor
        hideColorPicker()
    }

    fun showAlertDialog() {
        alertDialogVisible = true
    }

    fun hideAlertDialog() {
        alertDialogVisible = false
    }

    fun onSaveButtonClick() {
        viewModelScope.launch {
            if (techniqueId == 0L) repository.insert(technique)
            else repository.update(
                Technique(
                    techniqueId = technique.techniqueId,
                    name = name,
                    num = num,
                    techniqueType = techniqueType,
                    movementType = movementType,
                    color = color
                )
            )
        }
    }
}