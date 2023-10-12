package com.example.android.strikingarts.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager

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

