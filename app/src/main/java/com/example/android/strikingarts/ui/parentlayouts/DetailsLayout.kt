package com.example.android.strikingarts.ui.parentlayouts

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.components.ConfirmDialog
import com.example.android.strikingarts.ui.components.DoubleButtonsRow
import com.example.android.strikingarts.ui.components.DoubleTextButtonRow
import com.example.android.strikingarts.ui.components.FadingAnimatedVisibility
import com.example.android.strikingarts.ui.components.ModalBottomSheet
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager

@Composable
fun DetailsLayout(
    bottomSheetVisible: Boolean,
    setBottomSheetVisibility: (Boolean) -> Unit,
    saveButtonEnabled: Boolean,
    onSaveButtonClick: () -> Unit,
    onDiscardButtonClick: () -> Unit,
    bottomSheetContent: @Composable () -> Unit,
    columnContent: @Composable ColumnScope.() -> Unit,
) {
    val scrollState = rememberScrollState()

    var saveConfirmDialogVisible by rememberSaveable { mutableStateOf(false) }
    val setSaveConfirmDialogVisibility = { value: Boolean -> saveConfirmDialogVisible = value }

    var discardConfirmDialogVisible by rememberSaveable { mutableStateOf(false) }
    val setDiscardConfirmDialogVisibility =
        { value: Boolean -> discardConfirmDialogVisible = value }

    DetailsScreenConfirmDialog(
        saveConfirmDialogVisible = saveConfirmDialogVisible,
        setSaveConfirmDialogVisibility = setSaveConfirmDialogVisibility,
        discardConfirmDialogVisible = discardConfirmDialogVisible,
        setDiscardConfirmDialogValue = setDiscardConfirmDialogVisibility,
        onSaveButtonClick = onSaveButtonClick,
        onDiscardButtonClick = onDiscardButtonClick
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        columnContent()

        Spacer(Modifier.weight(1F))

        FadingAnimatedVisibility(
            visible = !bottomSheetVisible, modifier = Modifier.align(Alignment.End)
        ) {
            DoubleButtonsRow(modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingManager.Large),
                leftButtonText = stringResource(R.string.all_discard),
                rightButtonText = stringResource(R.string.all_save),
                leftButtonEnabled = true,
                rightButtonEnabled = saveButtonEnabled,
                onLeftButtonClick = { setDiscardConfirmDialogVisibility(true) },
                onRightButtonClick = { setSaveConfirmDialogVisibility(true) })
        }
    }

    ModalBottomSheet(
        bottomSheetVisible = bottomSheetVisible,
        setBottomSheetVisibility = setBottomSheetVisibility,
        bottomSheetSlot = bottomSheetContent
    )
}

@Composable
private fun DetailsScreenConfirmDialog(
    saveConfirmDialogVisible: Boolean,
    setSaveConfirmDialogVisibility: (Boolean) -> Unit,
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
        onConfirm = { setSaveConfirmDialogVisibility(false); onSaveButtonClick() },
        onDismiss = { setSaveConfirmDialogVisibility(false) })

    if (discardConfirmDialogVisible) ConfirmDialog(titleId = stringResource(R.string.all_discard),
        textId = stringResource(R.string.all_confirm_dialog_discard_text),
        confirmButtonText = stringResource(R.string.all_discard),
        dismissButtonText = stringResource(R.string.all_cancel),
        onConfirm = { setDiscardConfirmDialogValue(false); onDiscardButtonClick() },
        onDismiss = { setDiscardConfirmDialogValue(false) })
}

@Composable
fun BottomSheetBox( //todo: Move to ModalBottomSheet
    setBottomSheetVisibility: (Boolean) -> Unit,
    saveButtonEnabled: Boolean = true,
    onSaveButtonClick: () -> Unit = {},
    onDiscardButtonClick: () -> Unit = {},
    sheetContent: @Composable ColumnScope.() -> Unit
) = Column {
    sheetContent()
    DoubleTextButtonRow(modifier = Modifier
        .align(Alignment.End)
        .fillMaxWidth()
        .padding(top = PaddingManager.Medium),
        leftButtonText = stringResource(R.string.all_cancel),
        rightButtonText = stringResource(R.string.all_save),
        leftButtonEnabled = true,
        rightButtonEnabled = saveButtonEnabled,
        onLeftButtonClick = { setBottomSheetVisibility(false); onDiscardButtonClick() },
        onRightButtonClick = { setBottomSheetVisibility(false); onSaveButtonClick() })
}