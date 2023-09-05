package com.example.android.strikingarts.domain.usecase.selection

import com.example.android.strikingarts.domain.model.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectionUseCase @Inject constructor() {
    private val _selectedItemsIdList = MutableStateFlow(ImmutableList<Long>())
    val selectedItemsIdList = _selectedItemsIdList.asStateFlow()

    fun updateSelectedItems(idList: List<Long>) {
        _selectedItemsIdList.update { ImmutableList(idList) }
    }

    fun onItemSelectionChange(id: Long, newSelectedValue: Boolean) {
        _selectedItemsIdList.update { list ->
            ImmutableList(if (newSelectedValue) list.plus(id) else list.minus(id))
        }
    }

    fun deselectItem(id: Long) {
        _selectedItemsIdList.update { list -> ImmutableList(list.filterNot { it == id }) }
    }

    fun deselectAllItems() {
        _selectedItemsIdList.update { ImmutableList() }
    }

    fun selectAllItems(idList: List<Long>) {
        _selectedItemsIdList.update { ImmutableList(idList) }
    }

    fun setSelectedQuantity(id: Long, newQuantity: Int) {
        _selectedItemsIdList.update { currentIdList ->
            ImmutableList(if (newQuantity == -1) currentIdList.minus(id) else currentIdList.plus(id))
        }
    }
}