package com.example.android.strikingarts

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.ui.components.ConfirmDialog
import com.example.android.strikingarts.ui.components.DoubleButtonsRow
import com.example.android.strikingarts.ui.components.DoubleTextButtonRow
import com.example.android.strikingarts.ui.components.FadingAnimatedVisibility
import com.example.android.strikingarts.ui.components.SlideInAndOutVertically
import com.example.android.strikingarts.ui.components.clickableWithNoIndication

@Composable
fun DetailsLayout(
    bottomSheetVisible: Boolean,
    onDismissBottomSheet: (Boolean) -> Unit,
    saveButtonEnabled: Boolean,
    onSaveButtonClick: () -> Unit,
    onDiscardButtonClick: () -> Unit,
    bottomSheetContent: @Composable () -> Unit,
    columnContent: @Composable ColumnScope.() -> Unit,
) {
    val scrollState = rememberScrollState()

    var saveConfirmDialogVisible by rememberSaveable { mutableStateOf(false) }
    val setSaveConfirmDialogValue = { value: Boolean -> saveConfirmDialogVisible = value }

    var discardConfirmDialogVisible by rememberSaveable { mutableStateOf(false) }
    val setDiscardConfirmDialogValue = { value: Boolean -> discardConfirmDialogVisible = value }

    DetailsScreenConfirmDialog(
        saveConfirmDialogVisible = saveConfirmDialogVisible,
        setSaveConfirmDialogValue = setSaveConfirmDialogValue,
        discardConfirmDialogVisible = discardConfirmDialogVisible,
        setDiscardConfirmDialogValue = setDiscardConfirmDialogValue,
        onSaveButtonClick = onSaveButtonClick,
        onDiscardButtonClick = onDiscardButtonClick
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState), content = columnContent
        )
        ModalBottomSheetSlot(bottomSheetVisible, onDismissBottomSheet, bottomSheetContent)

        FadingAnimatedVisibility(
            visible = !bottomSheetVisible, modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            DoubleButtonsRow(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                leftButtonText = stringResource(R.string.all_discard),
                rightButtonText = stringResource(R.string.all_save),
                leftButtonEnabled = true,
                rightButtonEnabled = saveButtonEnabled,
                onLeftButtonClick = { setDiscardConfirmDialogValue(true) },
                onRightButtonClick = { setSaveConfirmDialogValue(true) })
        }
    }
}

@Composable
private fun DetailsScreenConfirmDialog(
    saveConfirmDialogVisible: Boolean,
    setSaveConfirmDialogValue: (Boolean) -> Unit,
    discardConfirmDialogVisible: Boolean,
    setDiscardConfirmDialogValue: (Boolean) -> Unit,
    onSaveButtonClick: () -> Unit,
    onDiscardButtonClick: () -> Unit
) {
    BackHandler { setDiscardConfirmDialogValue(true) }

    if (saveConfirmDialogVisible) ConfirmDialog(titleId = stringResource(R.string.all_save),
        textId = stringResource(R.string.all_confirm_dialog_save_text),
        confirmButtonText = stringResource(R.string.all_save),
        dismissButtonText = stringResource(R.string.all_cancel),
        onConfirm = { setSaveConfirmDialogValue(false); onSaveButtonClick() },
        onDismiss = { setSaveConfirmDialogValue(false) })

    if (discardConfirmDialogVisible) ConfirmDialog(titleId = stringResource(R.string.all_discard),
        textId = stringResource(R.string.all_confirm_dialog_discard_text),
        confirmButtonText = stringResource(R.string.all_discard),
        dismissButtonText = stringResource(R.string.all_cancel),
        onConfirm = { setDiscardConfirmDialogValue(false); onDiscardButtonClick() },
        onDismiss = { setDiscardConfirmDialogValue(false) })
}

@Composable
private fun BoxScope.ModalBottomSheetSlot(
    bottomSheetVisible: Boolean,
    onDismissBottomSheet: (Boolean) -> Unit,
    bottomSheetSlot: @Composable () -> Unit
) {
    BackHandler(bottomSheetVisible) { onDismissBottomSheet(false) }

    BackgroundDimmer(bottomSheetVisible, onDismissBottomSheet)

    SlideInAndOutVertically(
        visible = bottomSheetVisible, modifier = Modifier.align(Alignment.BottomCenter)
    ) { bottomSheetSlot() }
}

@Composable
fun BottomSheetBox(
    onDismissBottomSheet: (Boolean) -> Unit,
    saveButtonEnabled: Boolean,
    onSaveButtonClick: () -> Unit,
    SheetContent: @Composable (BoxScope.() -> Unit)
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5F)
            .background(color = MaterialTheme.colors.surface)
            .padding(8.dp)
    ) {
        SheetContent()
        DoubleTextButtonRow(modifier = Modifier
            .align(Alignment.BottomEnd)
            .fillMaxWidth(0.4F),
            leftButtonText = stringResource(R.string.all_cancel),
            rightButtonText = stringResource(R.string.all_save),
            leftButtonEnabled = true,
            rightButtonEnabled = saveButtonEnabled,
            onLeftButtonClick = { onDismissBottomSheet(false) },
            onRightButtonClick = { onDismissBottomSheet(false); onSaveButtonClick() })
    }
}

@Composable
private fun BackgroundDimmer(
    bottomSheetVisible: Boolean, onDismissBottomSheet: (Boolean) -> Unit
) {
    FadingAnimatedVisibility(visible = bottomSheetVisible) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5F)
            .background(color = Color.Transparent.copy(alpha = ContentAlpha.disabled))
            .clickableWithNoIndication { onDismissBottomSheet(false) })
    }
}
