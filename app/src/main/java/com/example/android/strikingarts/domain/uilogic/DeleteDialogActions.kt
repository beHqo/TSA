package com.example.android.strikingarts.domain.uilogic

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class DeleteDialogActions(
    private val showDeleteDialog: MutableStateFlow<Boolean>,
    private val itemId: MutableStateFlow<Long>
) {
    fun showDeleteDialog() {
        showDeleteDialog.update { true }
    }

    fun showDeleteDialogAndUpdateId(id: Long) {
        showDeleteDialog.update { true }
        itemId.update { id }
    }

    fun hideDeleteDialog() {
        showDeleteDialog.update { false }
    }
}