package com.example.android.strikingarts.ui.technique

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.database.repository.SelectedItemsRepository
import com.example.android.strikingarts.database.repository.TechniqueRepository
import com.example.android.strikingarts.ui.parentlayouts.ListScreenViewModel
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_SELECTION_MODE
import com.example.android.strikingarts.utils.ImmutableList
import com.example.android.strikingarts.utils.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.utils.TechniqueCategory.OFFENSE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val CHIP_INDEX_ALL = Int.MAX_VALUE
const val OFFENSE_TAB_INDEX = 0

@HiltViewModel
class TechniqueViewModel @Inject constructor(
    private val techniqueRepository: TechniqueRepository,
    private val selectedItemRepository: SelectedItemsRepository,
    private val savedStateHandle: SavedStateHandle
) : ListScreenViewModel() {
    override val initialSelectionMode =
        savedStateHandle[SELECTION_MODE] ?: savedStateHandle[TECHNIQUE_SELECTION_MODE] ?: false

    private val techniqueList = techniqueRepository.techniqueList.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList()
    )

    private val _visibleTechniques = MutableStateFlow(ImmutableList<Technique>())
    private val _tabIndex = MutableStateFlow(0)
    private val _chipIndex = MutableStateFlow(CHIP_INDEX_ALL)

    val visibleTechniques = _visibleTechniques.asStateFlow()
    val tabIndex = _tabIndex.asStateFlow()
    val chipIndex = _chipIndex.asStateFlow()

    init {
        viewModelScope.launch { retrieveComboListAndUpdateState() }
    }

    private suspend fun retrieveComboListAndUpdateState() {
        techniqueList.collectLatest { listOfTechniques -> initialUiUpdate(listOfTechniques) }
    }

    private fun initialUiUpdate(techniqueList: List<Technique>) {
        _chipIndex.update { CHIP_INDEX_ALL }
        _visibleTechniques.update {
            ImmutableList(techniqueList.filter {
                it.movementType == (if (_tabIndex.value == OFFENSE_TAB_INDEX) OFFENSE else DEFENSE)
            })
        }

        mSelectionMode.update { initialSelectionMode }
        mLoadingScreen.update { false }
    }

    private fun updateVisibleTechniques() {
        _visibleTechniques.update {
            ImmutableList(techniqueList.value.filter {
                it.movementType == if (_tabIndex.value == OFFENSE_TAB_INDEX) OFFENSE else DEFENSE
            })
        }
    }

    private fun displayTechniquesByMovementType() {
        _chipIndex.update { CHIP_INDEX_ALL }
        updateVisibleTechniques()
    }

    fun onTabClick(index: Int) {
        _chipIndex.update { CHIP_INDEX_ALL }
        _tabIndex.update { index }
        updateVisibleTechniques()
    }

    fun onChipClick(techniqueType: String, index: Int) {
        if (index == CHIP_INDEX_ALL) displayTechniquesByMovementType() else {
            _chipIndex.update { index }
            _visibleTechniques.update {
                ImmutableList(techniqueList.value.filter { it.techniqueType == techniqueType })
            }
        }
    }

    fun updateSelectedItemIds() {
        selectedItemRepository.selectedIds.value = mSelectedItemsIdList.value
    }

    override fun onLongPress(id: Long) {
        super.onLongPress(id)
        savedStateHandle[SELECTION_MODE] = mSelectionMode.value
    }

    override fun deleteItem() {
        viewModelScope.launch { techniqueRepository.deleteTechnique(itemId.value) }
        super.deleteItem()
    }

    override fun deleteSelectedItems() {
        viewModelScope.launch { techniqueRepository.deleteTechniques(mSelectedItemsIdList.value) }
        super.deleteSelectedItems()
    }

    companion object {
        const val SELECTION_MODE = "technique_selection_mode"
    }
}
