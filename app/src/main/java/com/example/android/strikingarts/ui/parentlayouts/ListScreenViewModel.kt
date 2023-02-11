package com.example.android.strikingarts.ui.parentlayouts

import androidx.lifecycle.ViewModel
import com.example.android.strikingarts.utils.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class ListScreenViewModel : ViewModel() {
    protected abstract val initialSelectionMode: Boolean

    protected val itemId = MutableStateFlow(0L)
    protected val mLoadingScreen = MutableStateFlow(true)
    protected val mSelectedItemsIdList = MutableStateFlow(ImmutableList<Long>())
    protected val mSelectionMode = MutableStateFlow(initialSelectionMode)
    private val mDeleteDialogVisible = MutableStateFlow(false)

    val loadingScreen = mLoadingScreen.asStateFlow()
    val selectedItemsIdList = mSelectedItemsIdList.asStateFlow()
    val selectionMode = mSelectionMode.asStateFlow()
    val deleteDialogVisible = mDeleteDialogVisible.asStateFlow()

    fun onItemSelectionChange(id: Long, newSelectedValue: Boolean) {
        mSelectedItemsIdList.update {
            ImmutableList(if (newSelectedValue) it.plus(id) else it.minus(id))
        }
    }

    open fun onLongPress(id: Long) {
        if (mSelectionMode.value) exitSelectionMode() else {
            mSelectionMode.update { true }
            mSelectedItemsIdList.update { ImmutableList(listOf(id)) }
        }
    }

    fun selectAllItems(visibleItemsIds: ImmutableList<Long>) {
        mSelectedItemsIdList.update { visibleItemsIds }
    }

    fun deselectAllItems() {
        mSelectedItemsIdList.update { ImmutableList() }
    }

    fun exitSelectionMode() {
        mSelectionMode.update { false }
        deselectAllItems()
    }

    fun setDeleteDialogVisibility(visible: Boolean) {
        mDeleteDialogVisible.update { visible }
    }

    fun showDeleteDialogAndUpdateId(id: Long) {
        mDeleteDialogVisible.update { true }
        itemId.update { id }
    }

    open fun deleteItem() {
        mDeleteDialogVisible.update { false }
    }

    open fun deleteSelectedItems() {
        mDeleteDialogVisible.update { false }
    }
}
