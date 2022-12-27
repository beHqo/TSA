package com.example.android.strikingarts.domain.uilogic

import com.example.android.strikingarts.utils.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SelectionActions(
    private val selectionMode: MutableStateFlow<Boolean>,
    private val selectedItems: MutableStateFlow<List<Long>>
) {
    fun onItemSelectionChange(id: Long, newSelectedValue: Boolean) {
        selectedItems.update { if (newSelectedValue) it.plus(id) else it.minus(id) }
    }

    fun onLongPress(id: Long) {
        if (selectionMode.value) exitSelectionMode()
        else selectionMode.update { true }; selectedItems.update { listOf(id) }
    }

    fun selectAllItems(visibleItemsIds: ImmutableList<Long>) {
        selectedItems.update { visibleItemsIds }
    }

    fun deselectAllItems() {
        selectedItems.update { emptyList() }
    }

    fun exitSelectionMode() {
        selectionMode.update { false }; deselectAllItems()
    }
}
