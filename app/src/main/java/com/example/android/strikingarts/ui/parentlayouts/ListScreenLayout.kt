package com.example.android.strikingarts.ui.parentlayouts

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.components.ConfirmDialog

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListScreenLayout(
    modifier: Modifier = Modifier,
    selectionMode: Boolean,
    exitSelectionMode: () -> Unit,
    deleteDialogVisible: Boolean,
    dismissDeleteDialog: (Boolean) -> Unit,
    onDeleteItem: () -> Unit,
    onDeleteMultipleItems: () -> Unit,
    topSlot: (@Composable LazyItemScope.() -> Unit)? = null,
    lazyColumnContent: LazyListScope.() -> Unit,
    bottomSlot: (@Composable BoxScope.() -> Unit)? = null
) {
    BackHandler(selectionMode, exitSelectionMode)

    if (deleteDialogVisible) ConfirmDialog(
        titleId = stringResource(R.string.all_delete),
        textId = if (selectionMode) stringResource(R.string.all_confirm_dialog_delete_multiple)
        else stringResource(R.string.all_confirm_dialog_delete_singular),
        confirmButtonText = stringResource(R.string.all_delete),
        dismissButtonText = stringResource(R.string.all_cancel),
        onConfirm = {
            if (selectionMode) onDeleteMultipleItems() else onDeleteItem()
            exitSelectionMode()
        },
        onDismiss = { dismissDeleteDialog(false) }
    )

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            topSlot?.let { stickyHeader(contentType = { "stickyHeader" }, content = it) }
            lazyColumnContent()
        }
        bottomSlot?.let { it() }
    }
}
