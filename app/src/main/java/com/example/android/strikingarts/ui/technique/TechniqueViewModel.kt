package com.example.android.strikingarts.ui.technique

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.MovementType
import com.example.android.strikingarts.domain.model.TechniqueType
import com.example.android.strikingarts.domain.usecase.selection.SelectionUseCase
import com.example.android.strikingarts.domain.usecase.technique.DeleteTechniqueUseCase
import com.example.android.strikingarts.domain.usecase.technique.FilterTechniquesUseCase
import com.example.android.strikingarts.ui.audioplayers.soundpool.SoundPoolWrapper
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_PRODUCTION_MODE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TechniqueViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val selectionUseCase: SelectionUseCase,
    private val filterTechniquesUseCase: FilterTechniquesUseCase,
    private val deleteTechniquesUseCase: DeleteTechniqueUseCase,
    private val soundPoolWrapper: SoundPoolWrapper,
) : ViewModel() {
    val productionMode = savedStateHandle[TECHNIQUE_PRODUCTION_MODE] ?: false
    private val initialSelectionMode = savedStateHandle[SELECTION_MODE] ?: productionMode

    val visibleTechniques = filterTechniquesUseCase.techniqueList.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ImmutableList()
    )

    val selectedItemsIdList = selectionUseCase.selectedItemsIdList
    val selectedItemsNames = selectedItemsIdList.map { list ->
        list.flatMap { id -> visibleTechniques.value.filter { technique -> technique.id == id } }
            .joinToString { technique -> technique.name }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    private val itemId = MutableStateFlow(0L)
    private val _selectionMode = MutableStateFlow(initialSelectionMode)
    private val _deleteDialogVisible = MutableStateFlow(false)
    private val _tabIndex = MutableStateFlow(0)
    private val _chipIndex = MutableStateFlow(CHIP_INDEX_ALL)

    val selectionMode = _selectionMode.asStateFlow()
    val deleteDialogVisible = _deleteDialogVisible.asStateFlow()
    val tabIndex = _tabIndex.asStateFlow()
    val chipIndex = _chipIndex.asStateFlow()

    init {
        _selectionMode.update { initialSelectionMode }
    }

    private fun filterTechniquesByMovementType() {
        _chipIndex.update { CHIP_INDEX_ALL }
        filterTechniquesUseCase.setTechniqueType(TechniqueType.UNSPECIFIED)
        filterTechniquesUseCase.setMovementType(
            if (_tabIndex.value == OFFENSE_TAB_INDEX) MovementType.OFFENSE else MovementType.DEFENSE
        )
    }

    fun onTabClick(index: Int) {
        _tabIndex.update { index }
        filterTechniquesByMovementType()
    }

    fun onChipClick(techniqueType: TechniqueType, index: Int) {
        if (index == CHIP_INDEX_ALL) filterTechniquesByMovementType() else {
            _chipIndex.update { index }
            filterTechniquesUseCase.setTechniqueType(techniqueType)
        }
    }

    fun onItemSelectionChange(id: Long, newSelectedValue: Boolean) {
        selectionUseCase.onItemSelectionChange(id, newSelectedValue)
    }

    fun deselectItem(id: Long) {
        selectionUseCase.deselectItem(id)
    }

    fun onLongPress(id: Long) {
        if (_selectionMode.value) exitSelectionMode() else {
            selectionUseCase.deselectAllItems()
            _selectionMode.update { true }
            selectionUseCase.onItemSelectionChange(id, true)
        }
    }

    fun selectAllItems() {
        viewModelScope.launch {
            selectionUseCase.selectAllItems(visibleTechniques.value.map { it.id })
        }
    }

    fun deselectAllItems() {
        selectionUseCase.deselectAllItems()
    }

    fun exitSelectionMode() {
        _selectionMode.update { false }
    }

    fun setSelectedQuantity(id: Long, newQuantity: Int) {
        selectionUseCase.setSelectedQuantity(id, newQuantity)
    }

    fun play(audioString: String) = viewModelScope.launch { soundPoolWrapper.play(audioString) }

    fun setDeleteDialogVisibility(visible: Boolean) {
        _deleteDialogVisible.update { visible }
    }

    fun showDeleteDialogAndUpdateId(id: Long) {
        _deleteDialogVisible.update { true }
        itemId.update { id }
    }

    suspend fun deleteItem(): Boolean = viewModelScope.async {
        val affectedRows = deleteTechniquesUseCase(itemId.value)

        val deleteOperationSuccessful = handleDeleteOperationResult(affectedRows)

        _deleteDialogVisible.update { false }

        return@async deleteOperationSuccessful
    }.await()

    suspend fun deleteSelectedItems(): Boolean = viewModelScope.async {
        val affectedRows = deleteTechniquesUseCase(selectedItemsIdList.value)

        val deleteOperationSuccessful = handleDeleteOperationResult(affectedRows)

        _deleteDialogVisible.update { false }

        return@async deleteOperationSuccessful
    }.await()

    private fun handleDeleteOperationResult(affectedRows: Long): Boolean = affectedRows != 0L

    fun surviveProcessDeath() {
        savedStateHandle[SELECTION_MODE] = _selectionMode.value
    }

    override fun onCleared() {
        soundPoolWrapper.release()
        super.onCleared()
    }

    companion object {
        private const val SELECTION_MODE = "technique_selection_mode"

        internal const val CHIP_INDEX_ALL = Int.MAX_VALUE
        internal const val OFFENSE_TAB_INDEX = 0
    }
}
