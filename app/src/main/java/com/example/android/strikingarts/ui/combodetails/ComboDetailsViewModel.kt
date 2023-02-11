package com.example.android.strikingarts.ui.combodetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.Combo
import com.example.android.strikingarts.database.entity.ComboWithTechniques
import com.example.android.strikingarts.database.repository.ComboRepository
import com.example.android.strikingarts.database.repository.SelectedItemsRepository
import com.example.android.strikingarts.ui.components.TEXTFIELD_DESC_MAX_CHARS
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.COMBO_ID
import com.example.android.strikingarts.utils.ImmutableList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComboDetailsViewModel @Inject constructor(
    private val comboRepository: ComboRepository,
    private val selectedItemRepository: SelectedItemsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val comboId = savedStateHandle[COMBO_ID] ?: 0L

    private val comboWithTechniques = MutableStateFlow(ComboWithTechniques(Combo(), emptyList()))

    private val _loadingScreen = MutableStateFlow(true)
    private val _name = MutableStateFlow("")
    private val _desc = MutableStateFlow("")
    private val _delay = MutableStateFlow(1)
    private val _selectedItemIds = MutableStateFlow(ImmutableList<Long>())

    val name = _name.asStateFlow()
    val desc = _desc.asStateFlow()
    val delay = _delay.asStateFlow()
    val selectedItemIds = _selectedItemIds.asStateFlow()
    val loadingScreen = _loadingScreen.asStateFlow()

    init {
        viewModelScope.launch { retrieveComboWithTechniques() }
    }

    private suspend fun retrieveComboWithTechniques() {
        if (comboId != 0L) {
            comboRepository.getCombo(comboId).also { if (it != null) comboWithTechniques.value = it }
            selectedItemRepository.selectedIds.update {
                comboWithTechniques.value.techniques.map { it.techniqueId }
            }
        }
        initialUiUpdate()
    }

    private suspend fun initialUiUpdate() {
        selectedItemRepository.selectedIds.collectLatest { selectedItems ->
            _name.update { savedStateHandle[NAME] ?: comboWithTechniques.value.combo.name }
            _desc.update { savedStateHandle[DESC] ?: comboWithTechniques.value.combo.description }
            _delay.update { savedStateHandle[DELAY] ?: comboWithTechniques.value.combo.delay }
            _selectedItemIds.update { ImmutableList(selectedItems) }

            _loadingScreen.update { false }
        }
    }

    fun onNameChange(value: String) {
        if (value.length <= TEXTFIELD_NAME_MAX_CHARS + 1) _name.update { value }
    }

    fun onDescChange(value: String) {
        if (value.length <= TEXTFIELD_DESC_MAX_CHARS + 1) _desc.update { value }
    }

    fun onDelayChange(value: Int) {
        _delay.update { value }
        savedStateHandle[DELAY] = value
    }

    fun clearSelectedItemsId() {
        selectedItemRepository.selectedIds.value = emptyList()
    }

    fun insertOrUpdateItem() {
        viewModelScope.launch {
            comboRepository.insertOrUpdateComboWithTechniques(
                Combo(comboId, _name.value, _desc.value, _delay.value), _selectedItemIds.value
            )
        }
    }

    companion object {
        const val NAME = "name"
        const val DESC = "desc"
        const val DELAY = "delay"
        const val ITEM_ID_LIST = "selected_items_id"
    }
}