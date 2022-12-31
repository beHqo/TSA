package com.example.android.strikingarts.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import com.example.android.strikingarts.R
import com.example.android.strikingarts.utils.quantityStringResource

const val TWEEN_DURATION = 200
const val TWEEN_DELAY = 200

@Composable
fun ExpandOrShrinkVertically(
    modifier: Modifier = Modifier, visible: Boolean, content: @Composable () -> Unit
) = AnimatedVisibility(
    modifier = modifier,
    visible = visible,
    enter = expandVertically(animationSpec = enterTweenSpec),
    exit = shrinkVertically(animationSpec = exitTweenSpec),
) { content() }

@Composable
fun ExpandOrShrinkHorizontally(
    modifier: Modifier = Modifier, expanded: Boolean, content: @Composable () -> Unit
) = AnimatedVisibility(
    modifier = modifier,
    visible = expanded,
    enter = expandHorizontally(animationSpec = enterTweenSpec),
    exit = shrinkHorizontally(animationSpec = exitTweenSpec),
) { content() }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MoreVertCheckBoxAnimation(
    modifier: Modifier = Modifier,
    selectionMode: Boolean,
    moreVertComponent: @Composable () -> Unit,
    checkBoxComponent: @Composable () -> Unit
) = AnimatedContent(modifier = modifier, targetState = selectionMode, transitionSpec = {
    scaleIn(tween(TWEEN_DURATION, TWEEN_DELAY, LinearEasing)) with
            scaleOut(tween(TWEEN_DURATION, easing = LinearEasing)) using
            (SizeTransform(clip = false))
}) { inSelectionMode -> if (inSelectionMode) checkBoxComponent() else moreVertComponent() }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CounterAnimation(delay: Int, modifier: Modifier = Modifier) =
    AnimatedContent(modifier = modifier, targetState = delay, transitionSpec = {
        if (targetState > initialState) {
            slideInVertically { height -> height } + fadeIn() with
                    slideOutVertically { height -> -height } + fadeOut()
        } else {
            slideInVertically { height -> -height } + fadeIn() with
                    slideOutVertically { height -> height } + fadeOut()
        }.using(SizeTransform(clip = false))
    }) { targetDelay ->
        Text(quantityStringResource(R.plurals.all_second, targetDelay, targetDelay))
    }

internal val enterTweenSpec: FiniteAnimationSpec<IntSize> = TweenSpec(
    durationMillis = TWEEN_DURATION, delay = TWEEN_DELAY, easing = FastOutSlowInEasing
)

internal val exitTweenSpec: FiniteAnimationSpec<IntSize> = TweenSpec(
    durationMillis = TWEEN_DURATION, easing = FastOutSlowInEasing
)
