package com.example.android.strikingarts.domain.uilogic

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class DeleteDialogActions(
    private val deleteDialogVisible: MutableStateFlow<Boolean>,
    private val itemId: MutableStateFlow<Long>
) {
    fun setDeleteDialogVisibility(visible: Boolean) {
        deleteDialogVisible.update { visible }
    }

    fun showDeleteDialogAndUpdateId(id: Long) {
        deleteDialogVisible.update { true }
        itemId.update { id }
    }
}