package com.example.android.strikingarts.ui.technique

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.MovementType
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.database.entity.TechniqueType
import com.example.android.strikingarts.database.repository.TechniqueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TechniqueViewModel @Inject constructor(private val repository: TechniqueRepository) :
    ViewModel() {

    private val allTechniques = MutableStateFlow<List<Technique>>(emptyList())
    val techniqueList = mutableStateListOf<Technique>()

    var techniqueId by mutableStateOf(0L)
        private set
    var tabIndex by mutableStateOf(0)
        private set
    var chipIndex: Int? by mutableStateOf(null)
        private set
    var showDeleteDialog by mutableStateOf(false)
        private set

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
        techniqueList.clear()
        techniqueList.addAll(allTechniques.value.filter {
            it.movementType == if (tabIndex == 0) MovementType.Offense else MovementType.Defense
        })
    }

    fun onTabClick(index: Int) {
        chipIndex = null
        tabIndex = index
        displayTechniquesByMovementType()
    }

    fun onChipClick(techniqueType: TechniqueType, index: Int?) {
        chipIndex = index
        techniqueList.clear()

        if (chipIndex == null)
            displayTechniquesByMovementType()
        else
            allTechniques.value.filter { it.techniqueType == techniqueType }
                .also { techniqueList.addAll(it) }
    }

    fun showDeleteDialog(id: Long) {
        showDeleteDialog = true
        techniqueId = id
    }

    fun hideDeleteDialog() {
        showDeleteDialog = false
    }

    fun deleteTechnique() {
        viewModelScope.launch { repository.deleteTechnique(techniqueId) }
        hideDeleteDialog()
    }
}