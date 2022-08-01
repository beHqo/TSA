package com.example.android.strikingarts.ui.techniquedetails

import androidx.compose.runtime.*
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.database.entity.TechniqueType

@Stable
data class TechniqueDetailsState(val technique: Technique) {
    var name by mutableStateOf(technique.name)
    var num by mutableStateOf(technique.num)
    var techniqueType by mutableStateOf(technique.techniqueType)
    var movementType by mutableStateOf(technique.movementType)
    var color by mutableStateOf(technique.color)
    val techniqueTypes = mutableStateListOf<TechniqueType>()
    var alertDialogVisible by mutableStateOf(false)
    var showColorPicker by mutableStateOf(false)
}