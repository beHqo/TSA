package com.example.android.strikingarts.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.TypographyManager
import kotlinx.coroutines.launch

private val heightSizeDp = 48.dp
private const val TWEEN_DURATION = 48

@OptIn(ExperimentalFoundationApi::class) //AnchoredDraggable
@Composable
fun DetailsItemSwitch(
    initialValue: String,
    startingItemText: String,
    endingItemText: String,
    onSelectionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val density = LocalDensity.current

    val halfWidthPx = with(density) {
        LocalConfiguration.current.screenWidthDp.plus(-1).div(2).dp.toPx()
    }

    val positionalThreshold = { distance: Float -> distance * 0.33F }
    val velocityThreshold = { with(density) { 56.dp.toPx() } }
    val animationSpec = tween<Float>(TWEEN_DURATION)

    val draggableState = rememberSaveable(
        saver = AnchoredDraggableState.Saver(
            animationSpec = animationSpec,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
        )
    ) {
        AnchoredDraggableState(
            initialValue = initialValue,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
            animationSpec = animationSpec
        )
    }

    draggableState.updateAnchors(newAnchors = DraggableAnchors { startingItemText at 0F; endingItemText at halfWidthPx })

    val selectionChange = { newValue: String ->
        coroutineScope.launch { draggableState.animateTo(newValue) }
        onSelectionChange(newValue)
    }

    LaunchedEffect(draggableState.isAnimationRunning) {
        if (draggableState.targetValue != initialValue) onSelectionChange(draggableState.targetValue)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(heightSizeDp)
            .anchoredDraggable(state = draggableState, orientation = Orientation.Horizontal)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth(0.5F)
            .height(heightSizeDp)
            .graphicsLayer { translationX = draggableState.offset }
            .background(ColorManager.primary))
        SwitchBox(
            startingItemText, Alignment.CenterStart, draggableState.currentValue == startingItemText
        ) { selectionChange(startingItemText) }
        SwitchBox(
            endingItemText, Alignment.CenterEnd, draggableState.currentValue == endingItemText
        ) { selectionChange(endingItemText) }
    }
}

@Composable
private fun BoxScope.SwitchBox(
    text: String, alignment: Alignment, selected: Boolean, onClick: () -> Unit
) = Box(
    modifier = Modifier
        .clickableWithNoIndication(onClick)
        .fillMaxWidth(0.5F)
        .fillMaxHeight()
        .align(alignment)
) {
    Text(
        text = text,
        style = TypographyManager.bodyLarge,
        color = if (selected) ColorManager.onPrimary else ColorManager.onSurface,
        modifier = Modifier.align(Alignment.Center)
    )
}