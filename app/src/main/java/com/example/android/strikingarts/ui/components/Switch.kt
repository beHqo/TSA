package com.example.android.strikingarts.ui.components

import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val animationDuration = 150
const val heightSize = 48

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailsItemSwitch(
    startingItemText: String, endingItemText: String, coroutineScope: CoroutineScope
) {
    val swipeableState = rememberSwipeableState(
        initialValue = true, animationSpec = TweenSpec(animationDuration)
    )

    val onSelectionChange = { newValue: Boolean ->
        coroutineScope.launch { swipeableState.animateTo(newValue) }
    }

    val halfWidthPx = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.plus(1).div(2).dp.toPx()
    }

    Box(modifier = Modifier
        .fillMaxWidth(0.5F)
        .height(heightSize.dp)
        .graphicsLayer { translationX = swipeableState.offset.value }
        .background(color = MaterialTheme.colors.primary))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightSize.dp)
            .swipeable(
                state = swipeableState,
                anchors = mapOf(0F to true, halfWidthPx to false),
                orientation = Orientation.Horizontal
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        SwitchBox(startingItemText, swipeableState.currentValue) { onSelectionChange(true) }
        SwitchBox(endingItemText, !swipeableState.currentValue) { onSelectionChange(false) }
    }
}

@Composable
private fun RowScope.SwitchBox(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .weight(1F)
            .clickableWithNoIndication(onClick)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
