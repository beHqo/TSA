package com.example.android.strikingarts.ui.combo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.entity.ComboWithTechniques
import com.example.android.strikingarts.database.repository.ComboRepository
import com.example.android.strikingarts.ui.parentlayouts.ListScreenViewModel
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.COMBO_SELECTION_MODE
import com.example.android.strikingarts.utils.ImmutableList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComboViewModel @Inject constructor(
    private val comboRepository: ComboRepository, private val savedStateHandle: SavedStateHandle
) : ListScreenViewModel() {
    override val initialSelectionMode =
        savedStateHandle[SELECTION_MODE] ?: savedStateHandle[COMBO_SELECTION_MODE] ?: false

    private val comboList = comboRepository.comboList.stateIn(
        viewModelScope, WhileSubscribed(5_000), emptyList()
    )

    private val _visibleCombos = MutableStateFlow(ImmutableList<ComboWithTechniques>())

    val visibleCombos = _visibleCombos.asStateFlow()

    init {
        viewModelScope.launch { retrieveComboListAndUpdateState() }
    }

    private suspend fun retrieveComboListAndUpdateState() {
        comboList.collectLatest { listOfCombos -> initialUiUpdate(listOfCombos) }
    }

    private fun initialUiUpdate(comboList: List<ComboWithTechniques>) {
        mSelectionMode.update { initialSelectionMode }
        _visibleCombos.update { ImmutableList(comboList) }
        mLoadingScreen.update { false }
    }

    override fun onLongPress(id: Long) {
        super.onLongPress(id)
        savedStateHandle[SELECTION_MODE] = mSelectionMode.value
    }

    override fun deleteItem() {
        viewModelScope.launch { comboRepository.deleteCombo(itemId.value) }
        super.deleteItem()
    }

    override fun deleteSelectedItems() {
        viewModelScope.launch { comboRepository.deleteCombos(mSelectedItemsIdList.value) }
        super.deleteSelectedItems()
    }

    companion object {
        const val SELECTION_MODE = "combo_selection_mode"
    }
}
