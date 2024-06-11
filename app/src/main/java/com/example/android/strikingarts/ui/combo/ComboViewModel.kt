package com.example.android.strikingarts.ui.combo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.interfaces.ComboAudioPlayer
import com.example.android.strikingarts.domain.mapper.getAudioStringList
import com.example.android.strikingarts.domain.model.Combo
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.usecase.combo.DeleteComboUseCase
import com.example.android.strikingarts.domain.usecase.combo.RetrieveComboListUseCase
import com.example.android.strikingarts.domain.usecase.selection.SelectionUseCase
import com.example.android.strikingarts.domain.usecase.training.ComboVisualPlayerUseCase
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.COMBO_PRODUCTION_MODE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComboViewModel @Inject constructor(
    retrieveComboListUseCase: RetrieveComboListUseCase,
    private val deleteComboUseCase: DeleteComboUseCase,
    private val selectionUseCase: SelectionUseCase,
    private val comboAudioPlayer: ComboAudioPlayer,
    private val comboVisualPlayerUseCase: ComboVisualPlayerUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val productionMode = savedStateHandle[COMBO_PRODUCTION_MODE] ?: false
    private val initialSelectionMode = savedStateHandle[SELECTION_MODE] ?: productionMode

    val comboList = retrieveComboListUseCase.comboList.stateIn(
        viewModelScope, WhileSubscribed(5000), ImmutableList()
    )

    val selectedItemsIdList = selectionUseCase.selectedItemsIdList
    val selectedItemsNames = selectedItemsIdList.map { list ->
        list.flatMap { id -> comboList.value.filter { combo -> combo.id == id } }
            .joinToString { combo -> combo.name }
    }.stateIn(viewModelScope, WhileSubscribed(5000), "")

    private val _selectionMode = MutableStateFlow(initialSelectionMode)
    private val _deleteDialogVisible = MutableStateFlow(false)
    private val _comboPreviewDialogVisible = MutableStateFlow(false)
    private val _currentCombo = MutableStateFlow(Combo())
    private val _itemId = MutableStateFlow(0L)
    val techniqueColorString = comboVisualPlayerUseCase.currentColorString

    val selectionMode = _selectionMode.asStateFlow()
    val currentCombo = _currentCombo.asStateFlow()
    val deleteDialogVisible = _deleteDialogVisible.asStateFlow()
    val comboPreviewDialogVisible = _comboPreviewDialogVisible.asStateFlow()

    init {
        _selectionMode.update { initialSelectionMode }
    }

    fun playCombo() {
        viewModelScope.launch {
            comboAudioPlayer.play(_currentCombo.value.getAudioStringList())
            comboVisualPlayerUseCase.display(_currentCombo.value)
        }
    }

    private fun pauseCombo() {
        viewModelScope.launch {
            comboAudioPlayer.pause()
            comboVisualPlayerUseCase.pause()
        }
    }

    fun dismissComboPreviewDialog() {
        _comboPreviewDialogVisible.update { false }
        pauseCombo()
    }

    fun onComboClick(playableCombo: Combo) {
        _currentCombo.update { playableCombo }

        _comboPreviewDialogVisible.update { true }

        playCombo()
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

        savedStateHandle[SELECTION_MODE] = _selectionMode.value
    }

    fun selectAllItems() {
        selectionUseCase.selectAllItems(comboList.value.map { it.id })
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

    fun setDeleteDialogVisibility(visible: Boolean) {
        _deleteDialogVisible.update { visible }
    }

    fun showDeleteDialogAndUpdateId(id: Long) {
        _deleteDialogVisible.update { true }
        _itemId.update { id }
    }

    fun deleteItem() {
        viewModelScope.launch { deleteComboUseCase(_itemId.value) }
        _deleteDialogVisible.update { false }
    }

    fun deleteSelectedItems() {
        viewModelScope.launch { deleteComboUseCase(selectedItemsIdList.value.toList()) }
        _deleteDialogVisible.update { false }
    }

    override fun onCleared() {
        comboAudioPlayer.release()
        comboVisualPlayerUseCase.release()
        super.onCleared()
    }

    companion object {
        private const val SELECTION_MODE = "combo_selection_mode"
    }
}