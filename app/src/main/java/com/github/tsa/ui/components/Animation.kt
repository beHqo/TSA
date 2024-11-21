package com.github.tsa.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
) = AnimatedVisibility(
    modifier = modifier,
    visible = visible,
    enter = slideInVertically(tween(ANIMATION_DURATION, animationDelay)) { height -> height },
    exit = slideOutVertically(tween(ANIMATION_DURATION)) { height -> height }) { content() }

@Composable
fun FadingAnimatedVisibility(
    modifier: Modifier = Modifier, visible: Boolean, content: @Composable () -> Unit
) = AnimatedVisibility(
    modifier = modifier, visible = visible, enter = fadeIn(), exit = fadeOut()
) { content() }

@Composable
fun SlideVerticallyAnimatedContent(
    modifier: Modifier = Modifier,
    targetState: Boolean,
    currentStateComponent: @Composable () -> Unit,
    targetStateComponent: @Composable () -> Unit
) = AnimatedContent(
    modifier = modifier, targetState = targetState, transitionSpec = {
        slideIntoContainer(
            SlideDirection.Up, tween(ANIMATION_DURATION, ANIMATION_DELAY)
        ) togetherWith slideOutOfContainer(SlideDirection.Down, tween(ANIMATION_DURATION))
    }, label = "SlideVerticallyAnimatedContent"
) { isTargetState -> if (isTargetState) targetStateComponent() else currentStateComponent() }

@Composable
fun CounterAnimation(quantity: Int, modifier: Modifier = Modifier) =
    AnimatedContent(modifier = modifier, targetState = quantity, transitionSpec = {
        if (targetState > initialState) {
            slideInVertically { height -> height } + fadeIn() togetherWith slideOutVertically { height -> -height } + fadeOut()
        } else {
            slideInVertically { height -> -height } + fadeIn() togetherWith slideOutVertically { height -> height } + fadeOut()
        } using (SizeTransform(clip = false))
    }, label = "CounterAnimation") { targetQuantity -> Text(targetQuantity.toString()) }

internal val enterTweenSpec: FiniteAnimationSpec<IntSize> = TweenSpec(
    durationMillis = ANIMATION_DURATION, delay = ANIMATION_DELAY, easing = FastOutSlowInEasing
)

internal val exitTweenSpec: FiniteAnimationSpec<IntSize> = TweenSpec(
    durationMillis = ANIMATION_DURATION, easing = FastOutSlowInEasing
)

//@Composable
//fun FadingAnimatedContent(
//    modifier: Modifier = Modifier,
//    targetState: Boolean,
//    currentStateComponent: @Composable () -> Unit,
//    targetStateComponent: @Composable () -> Unit
//) = AnimatedContent(
//    modifier = modifier, targetState = targetState, transitionSpec = {
//        fadeIn(tween(ANIMATION_DURATION, ANIMATION_DELAY, LinearEasing)) togetherWith fadeOut(
//            tween(ANIMATION_DURATION, easing = LinearEasing)
//        ) using (SizeTransform(false) { initialSize, _ ->
//            keyframes {
//                durationMillis = ANIMATION_DURATION
//                IntSize(initialSize.width, initialSize.height) at ANIMATION_DURATION
//            }
//        })
//    }, label = "FadingAnimatedContent"
//) { isTargetState -> if (isTargetState) targetStateComponent() else currentStateComponent() }
//
//@Composable
//fun CounterAnimation(quantity: String, modifier: Modifier = Modifier) =
//    AnimatedContent(modifier = modifier, targetState = quantity, transitionSpec = {
//        if (targetState > initialState) {
//            slideInVertically { height -> height } + fadeIn() togetherWith slideOutVertically { height -> -height } + fadeOut()
//        } else {
//            slideInVertically { height -> -height } + fadeIn() togetherWith slideOutVertically { height -> height } + fadeOut()
//        } using (SizeTransform(clip = false))
//    }, label = "CounterAnimation") { targetQuantity -> Text(targetQuantity) }
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