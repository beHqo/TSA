package com.github.tsa.ui.combodetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tsa.domain.combo.RetrieveComboUseCase
import com.github.tsa.domain.combo.UpsertComboUseCase
import com.github.tsa.domain.mapper.toMilliSeconds
import com.github.tsa.domain.mapper.toSeconds
import com.github.tsa.domain.model.Combo
import com.github.tsa.domain.selection.RetrieveSelectedItemsIdList
import com.github.tsa.domain.selection.UpdateSelectedItemsIdList
import com.github.tsa.ui.components.TEXTFIELD_DESC_MAX_CHARS
import com.github.tsa.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.github.tsa.ui.navigation.Screen.Arguments.COMBO_ID
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
    private val upsertComboUseCase: UpsertComboUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val comboId = savedStateHandle[COMBO_ID] ?: 0L

    private lateinit var combo: Combo
    var isComboNew = true; private set

    val selectedItemsIdList = retrieveSelectedItemsIdList()

    private val _loadingScreen = MutableStateFlow(true)
    private val _name = MutableStateFlow("")
    private val _desc = MutableStateFlow("")
    private val _delaySeconds = MutableStateFlow(3)

    val name = _name.asStateFlow()
    val desc = _desc.asStateFlow()
    val delaySeconds = _delaySeconds.asStateFlow()
    val loadingScreen = _loadingScreen.asStateFlow()

    init {
        viewModelScope.launch { initialUiUpdate() }
    }

    private suspend fun initialUiUpdate() {
        if (comboId != 0L) {
            combo = retrieveComboUseCase(comboId)
            isComboNew = false

            updateSelectedItemsIdList(combo.techniqueList.map { it.id })
        } else {
            combo = Combo()
            updateSelectedItemsIdList(listOf())
        }

        _name.update { savedStateHandle[NAME] ?: combo.name }
        _desc.update { savedStateHandle[DESC] ?: combo.desc }
        _delaySeconds.update {
            savedStateHandle[DELAY_SECONDS] ?: combo.delayMillis.toSeconds()
        }

        _loadingScreen.update { false }
    }

    fun onNameChange(value: String) {
        if (value.length <= TEXTFIELD_NAME_MAX_CHARS + 1) _name.update { value.trim() }
    }

    fun onDescChange(value: String) {
        if (value.length <= TEXTFIELD_DESC_MAX_CHARS + 1) _desc.update { value.trim() }
    }

    fun onDelayChange(value: Int) {
        _delaySeconds.update { value }
    }

    fun insertOrUpdateItem() {
        viewModelScope.launch {
            upsertComboUseCase(
                Combo(
                    id = comboId,
                    name = _name.value,
                    desc = _desc.value,
                    delayMillis = _delaySeconds.value.toMilliSeconds()
                ), techniqueIdList = selectedItemsIdList.value
            )
        }
    }

    fun surviveProcessDeath() {
        savedStateHandle[NAME] = _name.value
        savedStateHandle[DESC] = _desc.value
        savedStateHandle[DELAY_SECONDS] = _delaySeconds.value
    }

    private companion object {
        const val NAME = "name"
        const val DESC = "desc"
        const val DELAY_SECONDS = "delay"
    }
}