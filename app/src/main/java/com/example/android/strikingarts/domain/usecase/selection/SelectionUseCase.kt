package com.example.android.strikingarts.domain.usecase.selection

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectionUseCase @Inject constructor() {
    private val _selectedItemsIdList = MutableStateFlow(emptyList<Long>())
    val selectedItemsIdList = _selectedItemsIdList.asStateFlow()

    fun updateSelectedItems(idList: List<Long>) {
        _selectedItemsIdList.update { idList }
    }

    fun onItemSelectionChange(id: Long, newSelectedValue: Boolean) {
        _selectedItemsIdList.update { list ->
            if (newSelectedValue) list.plus(id) else removeTheLastOccurrence(list, id)
        }
    }

    fun deselectItem(id: Long) {
        _selectedItemsIdList.update { list -> list.filterNot { it == id } }
    }

    fun deselectAllItems() {
        _selectedItemsIdList.update { emptyList() }
    }

    fun selectAllItems(idList: List<Long>) {
        _selectedItemsIdList.update { idList }
    }

    fun setSelectedQuantity(id: Long, newQuantity: Int) {
        _selectedItemsIdList.update { currentIdList ->
            if (newQuantity == -1) removeTheLastOccurrence(currentIdList, id)
            else currentIdList.plus(id)
        }
    }

    private fun removeTheLastOccurrence(currentIdList: List<Long>, id: Long): List<Long> {
        val n = currentIdList.size
        var i = n
        while (--i >= 0) if (currentIdList[i] == id) break

        return if (i == -1) currentIdList
        else currentIdList.subList(0, i) + currentIdList.subList(i + 1, n)
    }
}