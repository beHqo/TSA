package com.example.android.strikingarts.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
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
import com.example.android.strikingarts.ui.theme.designsystemmanager.SizeManager.SwitchHeight
import com.example.android.strikingarts.ui.theme.designsystemmanager.SizeManager.SwitchVelocity
import com.example.android.strikingarts.ui.theme.designsystemmanager.TypographyManager
import kotlinx.coroutines.launch

private const val TWEEN_DURATION = 40

@OptIn(ExperimentalFoundationApi::class) //AnchoredDraggable
@Composable
fun <T : Any> DetailsItemSwitch(
    initialValue: T,
    startingItem: T,
    endingItem: T,
    startingText: String,
    endingText: String,
    onSelectionChange: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val density = LocalDensity.current

    val halfWidthPx = with(density) {
        LocalConfiguration.current.screenWidthDp.plus(-1).div(2).dp.toPx()
    }

    val positionalThreshold = { distance: Float -> distance * 0.33F }
    val velocityThreshold = { with(density) { SwitchVelocity.toPx() } }
    val animationSpec = tween<Float>(TWEEN_DURATION)
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()

    val draggableState = rememberSaveable(
        saver = AnchoredDraggableState.Saver(
            snapAnimationSpec = animationSpec,
            decayAnimationSpec = decayAnimationSpec,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
        )
    ) {
        AnchoredDraggableState(
            initialValue = initialValue,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
            snapAnimationSpec = animationSpec,
            decayAnimationSpec = decayAnimationSpec
        )
    }

    draggableState.updateAnchors(newAnchors = DraggableAnchors { startingItem at 0F; endingItem at halfWidthPx })

    val selectionChange = { newValue: T ->
        coroutineScope.launch { draggableState.animateTo(newValue) }
        onSelectionChange(newValue)
    }

    LaunchedEffect(draggableState.isAnimationRunning) {
        if (draggableState.targetValue != initialValue) onSelectionChange(draggableState.targetValue)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(SwitchHeight)
            .anchoredDraggable(state = draggableState, orientation = Orientation.Horizontal)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth(0.5F)
            .height(SwitchHeight)
            .graphicsLayer { translationX = draggableState.offset }
            .background(ColorManager.primary))
        SwitchBox(
            startingText, Alignment.CenterStart, draggableState.currentValue == startingItem
        ) { selectionChange(startingItem) }
        SwitchBox(
            endingText, Alignment.CenterEnd, draggableState.currentValue == endingItem
        ) { selectionChange(endingItem) }
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