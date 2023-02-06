package com.example.android.strikingarts.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

const val heightSize = 48
const val tweenDuration = 48

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailsItemSwitch(
    initialValue: String,
    startingItemText: String,
    endingItemText: String,
    onSelectionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val swipeableState = rememberSwipeableState(
        initialValue = initialValue.ifEmpty { startingItemText }, animationSpec = tween(tweenDuration)
    )

    val selectionChange = { newValue: String ->
        coroutineScope.launch { swipeableState.animateTo(newValue) }
        onSelectionChange(newValue)
    }

    LaunchedEffect(key1 = swipeableState.isAnimationRunning) {
        if (swipeableState.targetValue != initialValue) onSelectionChange(swipeableState.targetValue)
    }

    val halfWidthPx = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.plus(1).div(2).dp.toPx()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(heightSize.dp)
            .swipeable(
                state = swipeableState,
                anchors = mapOf(0F to startingItemText, halfWidthPx to endingItemText),
                orientation = Orientation.Horizontal
            )
    ) {
        Box(modifier = Modifier
            .fillMaxWidth(0.5F)
            .height(heightSize.dp)
            .graphicsLayer { translationX = swipeableState.offset.value }
            .background(color = MaterialTheme.colors.primary))
        SwitchBox(
            startingItemText, Alignment.CenterStart, swipeableState.currentValue == startingItemText
        ) { selectionChange(startingItemText) }
        SwitchBox(
            endingItemText, Alignment.CenterEnd, swipeableState.currentValue == endingItemText
        ) { selectionChange(endingItemText) }
    }
}

@Composable
private fun BoxScope.SwitchBox(
    text: String, alignment: Alignment, selected: Boolean, onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickableWithNoIndication(onClick)
            .fillMaxWidth(0.5F)
            .fillMaxHeight()
            .align(alignment)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
