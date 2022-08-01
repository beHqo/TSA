package com.example.android.strikingarts.techniquedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.*
import com.example.android.strikingarts.database.repository.TechniqueRepository
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
    val state = TechniqueDetailsState(technique)

    private fun getTechniqueById(id: Long?): Technique {
        val technique: Technique = when (id) {
            null -> throw IllegalArgumentException("techniqueId is null")
            0L -> Technique()
            else -> runBlocking { repository.getTechnique(id)!! }
        }
        return technique
    }

    fun onNameChange(newName: String) {
        state.name = newName
    }

    fun onNumChange(newNum: String) {
        state.num = newNum
    }

    fun onDefenseButtonClick() {
        onMovementButtonClick(MovementType.Defense)
    }

    fun onOffenseButtonClick() {
        onMovementButtonClick(MovementType.Offense)
    }

    private fun onMovementButtonClick(movementType: MovementType) {
        state.movementType = movementType
        state.techniqueType = TechniqueType.NONE
        state.techniqueTypes.clear()
        if (movementType == MovementType.Offense) state.techniqueTypes.addAll(getOffenseTypes())
        else state.techniqueTypes.addAll(getDefenseTypes())
    }

    fun onTechniqueTypeChange(newTechniqueName: String) {
        state.techniqueType = getTechniqueType(newTechniqueName)
    }

    fun showColorPicker() {
        state.showColorPicker = true
    }

    fun hideColorPicker() {
        state.showColorPicker = false
    }

    fun onColorChange(color: String) {
        state.color = color
        hideColorPicker()
    }

    fun showAlertDialog() {
        state.alertDialogVisible = true
    }

    fun hideAlertDialog() {
        state.alertDialogVisible = false
    }

    fun onSaveButtonClick() {
        modifyTechnique()
        viewModelScope.launch {
            if (techniqueId == 0L) repository.insert(technique) else repository.update(technique)
        }
    }

    private fun modifyTechnique() {
        technique.name = state.name
        technique.num = state.num
        technique.techniqueType = state.techniqueType
        technique.movementType = state.movementType
        technique.color = state.color
    }
}