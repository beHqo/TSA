package com.example.android.strikingarts.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@Composable
fun SelectionModeBottomSheet(
    modifier: Modifier = Modifier,
    shrunkStateText: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onDelete: () -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = !expanded, enter = expandHorizontally(
                animationSpec = tween(
                    durationMillis = 700, delayMillis = 500, easing = FastOutSlowInEasing
                )
            ), exit = shrinkHorizontally(
                animationSpec = tween(
                    durationMillis = 700, easing = FastOutSlowInEasing
                )
            ), label = "SelectionModeBottomSheet AnimatedVisibility Shrunk State"
        ) {
            Row(modifier = Modifier
                .clip(CutCornerShape(topStart = 16.dp))
                .background(MaterialTheme.colors.primary)
                .clickable { expanded = true }
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Sharp.KeyboardArrowLeft,
                    contentDescription = "Expand",
                    modifier = Modifier.padding(start = 0.dp)
                )
                Text(text = shrunkStateText, modifier = Modifier.padding(4.dp))
            }
        }
        AnimatedVisibility(
            visible = expanded, enter = expandHorizontally(
                animationSpec = tween(
                    durationMillis = 700, delayMillis = 500, easing = FastOutSlowInEasing
                )
            ), exit = shrinkHorizontally(
                animationSpec = tween(
                    durationMillis = 700, easing = FastOutSlowInEasing
                )
            ), label = "SelectionModeBottomSheet AnimatedVisibility Expanded State"
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .background(MaterialTheme.colors.primary)
                    .padding((0.2).dp), // To make shrunkState and expandedState have the same height
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButtonOnPrimary(text = buttonText, onClick = onButtonClick)
                Divider(
                    Modifier
                        .width(1.dp)
                        .fillMaxHeight(0.77F)
                )
                IconButton(onClick = onSelectAll) {
                    Icon(Icons.Sharp.SelectAll, "Select All")
                }
                IconButton(onClick = onDeselectAll) {
                    Icon(Icons.Sharp.Deselect, "Deselect All")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Sharp.Delete, "Delete")
                }
                Spacer(modifier = Modifier.weight(1F))
                IconButton(onClick = { expanded = false }) {
                    Icon(Icons.Sharp.KeyboardArrowRight, "Shrink the Bar")
                }
            }
        }
    }
}
