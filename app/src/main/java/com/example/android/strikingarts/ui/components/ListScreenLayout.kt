package com.example.android.strikingarts.ui.components

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListScreenLayout(
    modifier: Modifier = Modifier,
    selectionMode: Boolean,
    exitSelectionMode: () -> Unit,
    showDeleteDialog: Boolean,
    dismissDeleteDialog: () -> Unit,
    onDeleteItem: () -> Unit,
    onDeleteMultipleItems: () -> Unit,
    TopSlot: (@Composable LazyItemScope.() -> Unit)? = null,
    lazyColumnContent: LazyListScope.() -> Unit,
    BottomSlot: (@Composable BoxScope.() -> Unit)? = null
) {
    BackHandler(selectionMode, exitSelectionMode)

    if (showDeleteDialog) ConfirmDialog(
        titleId = stringResource(R.string.all_delete),
        textId = if (selectionMode) stringResource(R.string.all_confirm_dialog_delete_multiple)
        else stringResource(R.string.all_confirm_dialog_delete_singular),
        confirmButtonText = stringResource(R.string.all_delete),
        dismissButtonText = stringResource(R.string.all_cancel),
        onConfirm = {
            exitSelectionMode()
            if (selectionMode) onDeleteMultipleItems() else onDeleteItem()
        },
        onDismiss = dismissDeleteDialog
    )

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            TopSlot?.let { stickyHeader(contentType = { "stickyHeader" }, content = it) }
            lazyColumnContent()
        }
        BottomSlot?.let { it() }
    }
}
