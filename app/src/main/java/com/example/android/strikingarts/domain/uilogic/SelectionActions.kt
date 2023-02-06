package com.example.android.strikingarts.domain.uilogic

import com.example.android.strikingarts.utils.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SelectionActions(
    private val selectionMode: MutableStateFlow<Boolean>,
    private val selectedItemsIdList: MutableStateFlow<ImmutableList<Long>>
) {
    fun onItemSelectionChange(id: Long, newSelectedValue: Boolean) {
        selectedItemsIdList.update {
            if (newSelectedValue) ImmutableList(it.plus(id)) else ImmutableList(it.minus(id))
        }
    }

    fun onLongPress(id: Long) {
        if (selectionMode.value) exitSelectionMode()
        else selectionMode.update { true }; selectedItemsIdList.update { ImmutableList(listOf(id)) }
    }

    fun selectAllItems(visibleItemsIds: ImmutableList<Long>) {
        selectedItemsIdList.update { visibleItemsIds }
    }

    fun deselectAllItems() {
        selectedItemsIdList.update { ImmutableList() }
    }

    fun exitSelectionMode() {
        selectionMode.update { false }; deselectAllItems()
    }
}
