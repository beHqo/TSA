package com.thestrikingarts.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import com.thestrikingarts.R
import com.thestrikingarts.ui.theme.designsystemmanager.ColorManager
import com.thestrikingarts.ui.theme.designsystemmanager.PaddingManager

@OptIn(ExperimentalLayoutApi::class) //For imeNestedScroll
@Composable
fun ModalBottomSheet(
    modifier: Modifier = Modifier,
    bottomSheetVisible: Boolean,
    setBottomSheetVisibility: (Boolean) -> Unit,
    bottomSheetSlot: @Composable () -> Unit
) {
    BackHandler(bottomSheetVisible) { setBottomSheetVisibility(false) }

    BackgroundDimmer(visible = bottomSheetVisible, setVisibility = setBottomSheetVisibility)

    Box(modifier.fillMaxSize()) {
        VerticalSlideAnimatedVisibility(visible = bottomSheetVisible,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(ColorManager.surface)
                .imePadding()
                .imeNestedScroll()
                .padding(PaddingManager.Large)
                .pointerInput(Unit) {} //to disable clickability
        ) { bottomSheetSlot() }
    }
}

@Composable
fun SingleButtonBottomSheetBox(
    setBottomSheetVisibility: (Boolean) -> Unit,
    doneButtonEnabled: Boolean = true,
    onDoneButtonClick: () -> Unit = {},
    sheetContent: @Composable () -> Unit
) = Column {
    sheetContent()
    DoneTextButton(
        setBottomSheetVisibility = setBottomSheetVisibility,
        onButtonClick = onDoneButtonClick,
        buttonEnabled = doneButtonEnabled,
        modifier = Modifier
            .padding(top = PaddingManager.Medium)
            .align(Alignment.End)
    )
}

@Composable
fun DoubleButtonBottomSheetBox(
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