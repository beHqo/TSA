package com.example.android.strikingarts.ui.combodetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.domain.usecase.combo.RetrieveComboUseCase
import com.example.android.strikingarts.domain.usecase.combo.UpsertComboListItemUseCase
import com.example.android.strikingarts.domain.usecase.selection.RetrieveSelectedItemsIdList
import com.example.android.strikingarts.domain.usecase.selection.UpdateSelectedItemsIdList
import com.example.android.strikingarts.ui.components.TEXTFIELD_DESC_MAX_CHARS
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.COMBO_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComboDetailsViewModel @Inject constructor(
    retrieveSelectedItemsIdList: RetrieveSelectedItemsIdList,
    private val retrieveComboUseCase: RetrieveComboUseCase,
    private val updateSelectedItemsIdList: UpdateSelectedItemsIdList,
    private val upsertComboListItemUseCase: UpsertComboListItemUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val comboId = savedStateHandle[COMBO_ID] ?: 0L

    private val comboListItem = MutableStateFlow(ComboListItem())

    val selectedItemsIdList = retrieveSelectedItemsIdList()

    private val _loadingScreen = MutableStateFlow(true)
    private val _name = MutableStateFlow("")
    private val _desc = MutableStateFlow("")
    private val _delay = MutableStateFlow(1)

    val name = _name.asStateFlow()
    val desc = _desc.asStateFlow()
    val delay = _delay.asStateFlow()
    val loadingScreen = _loadingScreen.asStateFlow()

    init {
        viewModelScope.launch { initialUiUpdate() }
    }

    private suspend fun initialUiUpdate() {
        if (comboId != 0L) {
            comboListItem.update { retrieveComboUseCase(comboId) }
            updateSelectedItemsIdList(comboListItem.value.techniqueList.map { it.id })
        }
        _name.update { savedStateHandle[NAME] ?: comboListItem.value.name }
        _desc.update { savedStateHandle[DESC] ?: comboListItem.value.desc }
        _delay.update { savedStateHandle[DELAY] ?: comboListItem.value.delay }

        _loadingScreen.update { false }
    }

    fun onNameChange(value: String) {
        if (value.length <= TEXTFIELD_NAME_MAX_CHARS + 1)
            _name.update { value }; savedStateHandle[NAME] = value
    }

    fun onDescChange(value: String) {
        if (value.length <= TEXTFIELD_DESC_MAX_CHARS + 1)
            _desc.update { value }; savedStateHandle[DESC] = value
    }

    fun onDelayChange(value: Int) {
        _delay.update { value }
        savedStateHandle[DELAY] = value
    }

    fun insertOrUpdateItem() {
        viewModelScope.launch {
            upsertComboListItemUseCase(
                ComboListItem(comboId, _name.value, _desc.value, _delay.value),
                selectedItemsIdList.value
            )
        }
    }

    private companion object {
        const val NAME = "name"
        const val DESC = "desc"
        const val DELAY = "delay"
    }
}