package com.example.android.strikingarts.ui.techniquedetails

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.core.text.isDigitsOnly
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
import com.example.android.strikingarts.utils.combine6
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class TechniqueDetailsUiState(
    val name: String = "",
    val num: String = "",
    val techniqueType: String = "",
    val movementType: String = "",
    val color: String = Color.Transparent.value.toString(),
    val techniqueTypes: ImmutableSet<String> = ImmutableSet()
)

@HiltViewModel
class TechniqueDetailsViewModel @Inject constructor(
    private val repository: TechniqueRepository, private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val techniqueId = savedStateHandle.get<Long>(TECHNIQUE_ID) ?: 0

    private val technique = MutableStateFlow(Technique())

    private val name = MutableStateFlow("")
    private val num = MutableStateFlow("")
    private val techniqueType = MutableStateFlow("")
    private val movementType = MutableStateFlow("")
    private val color = MutableStateFlow(Color.Transparent.value.toString())
    private val techniqueTypes = MutableStateFlow(ImmutableSet<String>())

    val uiState = combine6(
        name, num, techniqueType, movementType, color, techniqueTypes
    ) { t1, t2, t3, t4, t5, t6 -> TechniqueDetailsUiState(t1, t2, t3, t4, t5, t6) }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), TechniqueDetailsUiState()
    )

    init {
        initializeTechniqueAndDisplayState()
    }

    private fun initializeTechniqueAndDisplayState() {
        if (techniqueId == 0L) return else viewModelScope.launch {
            repository.getTechnique(techniqueId).also { if (it != null) technique.value = it }
            initialUiUpdate()
        }
    }

    private fun initialUiUpdate() {
        name.update { savedStateHandle[NAME] ?: technique.value.name }
        num.update { savedStateHandle[NUM] ?: technique.value.num }
        techniqueType.update { savedStateHandle[TECHNIQUE_TYPE] ?: technique.value.techniqueType }
        movementType.update { savedStateHandle[MOVEMENT_TYPE] ?: technique.value.movementType }
        color.update { technique.value.color }
        techniqueTypes.update {
            ImmutableSet(
                if ((savedStateHandle[TECHNIQUE_TYPE]
                        ?: technique.value.movementType) == DEFENSE
                ) defenseTypes.keys
                else offenseTypes.keys
            )
        }
    }

    fun onNameChange(value: String) {
        if (value.length <= TEXTFIELD_NAME_MAX_CHARS + 1) {
            name.update { value }
            savedStateHandle[NAME] = value
        }
    }

    fun onNumChange(value: String) {
        if (value.isDigitsOnly()) {
            num.update { value }
            savedStateHandle[NUM] = value
        }
    }

    fun onMovementButtonClick(newMovementType: String) {
        movementType.update { newMovementType }
        techniqueType.update { "" }
        techniqueTypes.update { ImmutableSet(if (newMovementType == DEFENSE) defenseTypes.keys else offenseTypes.keys) }
        color.update { Color.Transparent.value.toString() }
        savedStateHandle[MOVEMENT_TYPE] = newMovementType
    }

    fun onTechniqueTypeChange(newTechniqueType: String) {
        techniqueType.update { newTechniqueType }
        savedStateHandle[TECHNIQUE_TYPE] = newTechniqueType
    }

    fun onColorChange(newColor: String) {
        color.update { newColor }
    }

    fun onSaveButtonClick() {
        viewModelScope.launch {
            if (techniqueId == 0L) repository.insert(technique.value) else repository.update(
                Technique(
                    techniqueId = technique.value.techniqueId,
                    name = name.value,
                    num = num.value,
                    techniqueType = techniqueType.value,
                    movementType = movementType.value,
                    color = color.value,
                    canBeBodyshot = movementType.value == OFFENSE,
                    canBeFaint = movementType.value == OFFENSE
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