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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ComboDetailsUiState(
    val name: String = "",
    val desc: String = "",
    val delay: Int = 1,
    val selectedItemIds: List<Long> = emptyList()
)

@HiltViewModel
class ComboDetailsViewModel @Inject constructor(
    private val comboRepository: ComboRepository,
    private val selectedItemRepository: SelectedItemsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val comboId = savedStateHandle[COMBO_ID] ?: 0L

    private val comboStateFlow = MutableStateFlow(ComboWithTechniques(Combo(), emptyList()))

    private val name = MutableStateFlow("")
    private val desc = MutableStateFlow("")
    private val delay = MutableStateFlow(1)
    private val selectedItemIds = MutableStateFlow(emptyList<Long>())

    val uiState = combine(name, desc, delay, selectedItemIds) { t1, t2, t3, t4 ->
        ComboDetailsUiState(t1, t2, t3, t4)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ComboDetailsUiState())

    init {
        viewModelScope.launch {
            retrieveComboWithTechniques()
            initialUiUpdate()
        }
    }

    private suspend fun retrieveComboWithTechniques() {
        if (comboId != 0L) {
            comboRepository.getCombo(comboId).also { if (it != null) comboStateFlow.value = it }
            selectedItemRepository.selectedIds.update {
                comboStateFlow.value.techniques.map { it.techniqueId }
            }
        }
    }

    private suspend fun initialUiUpdate() {
        selectedItemRepository.selectedIds.collectLatest { selectedItems ->
            name.update { savedStateHandle[NAME] ?: comboStateFlow.value.combo.name }
            desc.update { savedStateHandle[DESC] ?: comboStateFlow.value.combo.description }
            delay.update { savedStateHandle[DELAY] ?: comboStateFlow.value.combo.delay }
            selectedItemIds.update { selectedItems }
        }
    }

    fun onNameChange(value: String) {
        if (value.length <= TEXTFIELD_NAME_MAX_CHARS + 1) name.update { value }
    }

    fun onDescChange(value: String) {
        if (value.length <= TEXTFIELD_DESC_MAX_CHARS + 1) desc.update { value }
    }

    fun onDelayChange(value: Int) {
        delay.update { value }
        savedStateHandle[DELAY] = value
    }

    fun clearSelectedItemsId() {
        selectedItemRepository.selectedIds.value = emptyList()
    }

    fun insertOrUpdateItem() {
        viewModelScope.launch {
            comboRepository.insertOrUpdateComboWithTechniques(
                Combo(comboId, name.value, desc.value, delay.value), selectedItemIds.value
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