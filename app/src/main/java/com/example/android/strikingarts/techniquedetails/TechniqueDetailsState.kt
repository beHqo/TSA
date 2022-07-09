package com.example.android.strikingarts.techniquedetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.database.entity.TechniqueType

class TechniqueDetailsState (technique: Technique) {
    var name by mutableStateOf(technique.name)
    var num by mutableStateOf(technique.num)
    var techniqueType by mutableStateOf(technique.techniqueType)
    var movementType by mutableStateOf(technique.movementType)
    var color by mutableStateOf(technique.color)
    val techniqueTypes = mutableStateListOf<TechniqueType>()
    var alertDialogVisible by mutableStateOf(false)
    var showColorPicker by mutableStateOf(false)
}