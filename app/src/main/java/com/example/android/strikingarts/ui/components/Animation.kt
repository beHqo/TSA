package com.example.android.strikingarts.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize

const val ANIMATION_DURATION = 200
const val ANIMATION_DELAY = 200

@Composable
fun VerticalExpandAnimatedVisibility(
    modifier: Modifier = Modifier, visible: Boolean, content: @Composable () -> Unit
) = AnimatedVisibility(
    modifier = modifier,
    visible = visible,
    enter = expandVertically(animationSpec = enterTweenSpec),
    exit = shrinkVertically(animationSpec = exitTweenSpec),
) { content() }

@Composable
fun VerticalSlideAnimatedVisibility(
    modifier: Modifier = Modifier,
    animationDelay: Int = 0,
    visible: Boolean,
    content: @Composable () -> Unit
) = AnimatedVisibility(modifier = modifier,
    visible = visible,
    enter = slideInVertically(tween(ANIMATION_DURATION, animationDelay)) { height -> height },
    exit = slideOutVertically(tween(ANIMATION_DURATION)) { height -> height }) { content() }

@Composable
fun FadingAnimatedVisibility(
    modifier: Modifier = Modifier, visible: Boolean, content: @Composable () -> Unit
) = AnimatedVisibility(
    modifier = modifier, visible = visible, enter = fadeIn(), exit = fadeOut()
) { content() }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FadingAnimatedContent(
    modifier: Modifier = Modifier,
    targetState: Boolean,
    currentStateComponent: @Composable () -> Unit,
    targetStateComponent: @Composable () -> Unit
) = AnimatedContent(modifier = modifier, targetState = targetState, transitionSpec = {
    fadeIn(tween(ANIMATION_DURATION, ANIMATION_DELAY, LinearEasing)) with fadeOut(
        tween(ANIMATION_DURATION, easing = LinearEasing)
    ) using (SizeTransform(false) { initialSize, _ ->
        keyframes {
            durationMillis = ANIMATION_DURATION
            IntSize(initialSize.width, initialSize.height) at ANIMATION_DURATION
        }
    })
}) { isTargetState -> if (isTargetState) targetStateComponent() else currentStateComponent() }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SlideVerticallyAnimatedContent(
    modifier: Modifier = Modifier,
    targetState: Boolean,
    currentStateComponent: @Composable () -> Unit,
    targetStateComponent: @Composable () -> Unit
) = AnimatedContent(modifier = modifier, targetState = targetState, transitionSpec = {
    slideIntoContainer(
        AnimatedContentScope.SlideDirection.Up, tween(ANIMATION_DURATION, ANIMATION_DELAY)
    ) with slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, tween(ANIMATION_DURATION))
}) { isTargetState -> if (isTargetState) targetStateComponent() else currentStateComponent() }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CounterAnimation(quantity: Int, modifier: Modifier = Modifier) =
    AnimatedContent(modifier = modifier, targetState = quantity, transitionSpec = {
        if (targetState > initialState) {
            slideInVertically { height -> height } + fadeIn() with slideOutVertically { height -> -height } + fadeOut()
        } else {
            slideInVertically { height -> -height } + fadeIn() with slideOutVertically { height -> height } + fadeOut()
        } using (SizeTransform(clip = false))
    }) { targetQuantity -> Text(targetQuantity.toString()) }

internal val enterTweenSpec: FiniteAnimationSpec<IntSize> = TweenSpec(
    durationMillis = ANIMATION_DURATION, delay = ANIMATION_DELAY, easing = FastOutSlowInEasing
)

internal val exitTweenSpec: FiniteAnimationSpec<IntSize> = TweenSpec(
    durationMillis = ANIMATION_DURATION, easing = FastOutSlowInEasing
)

//@OptIn(ExperimentalAnimationApi::class)
//@Composable
//fun ScalingAnimatedContent(
//    modifier: Modifier = Modifier,
//    targetState: Boolean,
//    currentStateComponent: @Composable () -> Unit,
//    targetStateComponent: @Composable () -> Unit
//) = AnimatedContent(modifier = modifier, targetState = targetState, transitionSpec = {
//    scaleIn(tween(TWEEN_DURATION, TWEEN_DELAY)) with scaleOut(tween(TWEEN_DURATION))
//}) { isTargetState -> if (isTargetState) targetStateComponent() else currentStateComponent() }

//@Composable
//fun ExpandHorizontallyAnimatedVisibility(
//    modifier: Modifier = Modifier, expanded: Boolean, content: @Composable () -> Unit
//) = AnimatedVisibility(
//    modifier = modifier,
//    visible = expanded,
//    enter = expandHorizontally(animationSpec = enterTweenSpec),
//    exit = shrinkHorizontally(animationSpec = exitTweenSpec),
//) { content() }