package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material.icons.sharp.Deselect
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material.icons.sharp.Remove
import androidx.compose.material.icons.sharp.SelectAll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ContentAlphaManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ElevationManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager


@Composable
fun BoxScope.SelectionModeBottomSheet(
    modifier: Modifier = Modifier,
    visible: Boolean,
    itemsSelectedText: String,
    previewText: String,
    buttonText: String,
    buttonsEnabled: Boolean,
    onButtonClick: () -> Unit,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onDelete: () -> Unit
) {
    val containerColor = ColorManager.surfaceColorAtElevation(ElevationManager.Level2)

    var expanded by rememberSaveable { mutableStateOf(false) }
    val setExpandedValue = { value: Boolean -> expanded = value }

    BackgroundDimmer(expanded && visible, setExpandedValue)

    CompositionLocalProvider(LocalContentColor provides contentColorFor(containerColor)) {
        VerticalSlideAnimatedVisibility(
            visible = visible,
            animationDelay = ANIMATION_DELAY,
            modifier = modifier.align(Alignment.BottomStart)
        ) {
            SlideVerticallyAnimatedContent(targetState = expanded, currentStateComponent = {
                BottomSheetShrunkState(
                    containerColor = containerColor,
                    buttonText = buttonText,
                    buttonsEnabled = buttonsEnabled,
                    onButtonClick = onButtonClick,
                    setExpandedValue = setExpandedValue,
                    onSelectAll = onSelectAll,
                    onDeselectAll = onDeselectAll,
                    onDelete = onDelete
                )
            }, targetStateComponent = {
                BottomSheetExpandedState(
                    containerColor = containerColor,
                    itemsSelectedText = itemsSelectedText,
                    previewText = previewText,
                    buttonText = buttonText,
                    buttonsEnabled = buttonsEnabled,
                    onButtonClick = onButtonClick,
                    setExpandedValue = setExpandedValue,
                    deSelectLastItem = onDeselectAll
                )
            })
        }
    }
}

@Composable
private fun BottomSheetShrunkState(
    containerColor: Color,
    buttonText: String,
    buttonsEnabled: Boolean,
    onButtonClick: () -> Unit,
    setExpandedValue: (Boolean) -> Unit,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onDelete: () -> Unit
) = Row(verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
        .fillMaxWidth()
        .height(SelectionModeBottomSheetShrunkStateHeightDp)
        .outerShadow(containerColor)
        .background(containerColor)
        .pointerInput(Unit) {}) { // TODO: to be changed with swipeable
    TextButtonOnElevatedSurface(
        text = buttonText,
        onClick = onButtonClick,
        enabled = buttonsEnabled,
        elevation = ElevationManager.Level2
    )
    Divider(
        Modifier
            .width(1.dp)
            .fillMaxHeight(0.77F)
    )
    IconButton(onClick = onSelectAll, enabled = true) {
        Icon(
            Icons.Sharp.SelectAll,
            stringResource(R.string.all_selection_mode_bottom_sheet_select_all)
        )
    }
    IconButton(onClick = onDeselectAll, enabled = buttonsEnabled) {
        Icon(
            Icons.Sharp.Deselect,
            stringResource(R.string.all_selection_mode_bottom_sheet_deselect_all)
        )
    }
    IconButton(onClick = onDelete, enabled = buttonsEnabled) {
        Icon(
            Icons.Sharp.Delete, stringResource(R.string.all_selection_mode_bottom_sheet_delete)
        )
    }
    Spacer(modifier = Modifier.weight(1F))
    IconButton(
        onClick = { setExpandedValue(true) },
        enabled = true,
        modifier = Modifier.padding(end = PaddingManager.Medium)
    ) {
        Icon(
            Icons.Sharp.KeyboardArrowUp,
            stringResource(R.string.all_selection_mode_bottom_sheet_expand)
        )
    }
}

@Composable
fun BottomSheetExpandedState(
    containerColor: Color,
    itemsSelectedText: String,
    previewText: String,
    buttonText: String,
    buttonsEnabled: Boolean,
    onButtonClick: () -> Unit,
    setExpandedValue: (Boolean) -> Unit,
    deSelectLastItem: () -> Unit
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .background(containerColor)
        .padding(PaddingManager.Medium)
        .pointerInput(Unit) {}) { // TODO: to be changed with swipeable
    SummaryBox(itemsSelectedText, containerColor, deSelectLastItem)

    PreviewBox(previewText)

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        TextButtonOnElevatedSurface(
            text = buttonText,
            onClick = onButtonClick,
            enabled = buttonsEnabled,
            elevation = ElevationManager.Level2,
        )

        IconButton(onClick = { setExpandedValue(false) }) {
            Icon(
                Icons.Sharp.KeyboardArrowDown, "Shrink "
            )
        }
    }
}

@Composable
private fun SummaryBox(
    itemsSelectedText: String, backgroundColor: Color, deSelectLastItem: () -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 48.dp)
        .background(backgroundColor)
        .shadow(1.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    PrimaryText(
        text = itemsSelectedText,
        maxLines = 4,
        color = LocalContentColor.current,
        modifier = Modifier.padding(PaddingManager.Large)
    )

    IconButton(onClick = deSelectLastItem) {
        Icon(
            Icons.Sharp.Remove,
            stringResource(R.string.all_selection_mode_bottom_sheet_deselect_last_item)
        )
    }
}

@Composable
private fun PreviewBox(previewText: String) = PrimaryText(
    text = previewText, maxLines = 3, modifier = Modifier.padding(
        top = PaddingManager.Large, start = PaddingManager.Medium, end = PaddingManager.Medium
    )
)

@Composable
private fun BoxScope.BackgroundDimmer(visible: Boolean, setExpandedValue: (Boolean) -> Unit) =
    FadingAnimatedVisibility(visible = visible) {
        Box(
            Modifier
                .fillMaxSize()
                .align(Alignment.TopStart)
                .background(Color.Transparent.copy(ContentAlphaManager.disabled))
                .clickableWithNoIndication { setExpandedValue(false) })
    }

@Composable
fun BoxScope.SelectionModeBottomSheet(
    visible: Boolean,
    buttonsEnabled: Boolean,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = ColorManager.surfaceColorAtElevation(ElevationManager.Level2)

    VerticalSlideAnimatedVisibility(
        visible = visible,
        animationDelay = ANIMATION_DELAY,
        modifier = modifier.align(Alignment.BottomStart)
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColorFor(containerColor)) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SelectionModeBottomSheetShrunkStateHeightDp)
                    .outerShadow(containerColor)
                    .background(containerColor)
                    .pointerInput(Unit) {}) {
                IconButton(onClick = onSelectAll, enabled = true) {
                    Icon(
                        Icons.Sharp.SelectAll,
                        stringResource(R.string.all_selection_mode_bottom_sheet_select_all)
                    )
                }
                IconButton(onClick = onDeselectAll, enabled = buttonsEnabled) {
                    Icon(
                        Icons.Sharp.Deselect,
                        stringResource(R.string.all_selection_mode_bottom_sheet_deselect_all)
                    )
                }
                IconButton(onClick = onDelete, enabled = buttonsEnabled) {
                    Icon(
                        Icons.Sharp.Delete,
                        stringResource(R.string.all_selection_mode_bottom_sheet_delete)
                    )
                }
            }
        }
    }
}

val SelectionModeBottomSheetShrunkStateHeightDp = 56.dp