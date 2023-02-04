package com.example.android.strikingarts.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize

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
fun SlideInAndOutVertically(
    modifier: Modifier = Modifier, visible: Boolean, content: @Composable () -> Unit
) = AnimatedVisibility(
    modifier = modifier,
    visible = visible,
    enter = slideInVertically(tween(TWEEN_DURATION), initialOffsetY = { height -> height }),
    exit = slideOutVertically(tween(TWEEN_DURATION), targetOffsetY = { height -> height }),
) { content() }

@Composable
fun FadingAnimatedVisibility(
    modifier: Modifier = Modifier, visible: Boolean, content: @Composable () -> Unit
) = AnimatedVisibility(
    modifier = modifier, visible = visible, enter = fadeIn(), exit = fadeOut()
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
fun ScalingAnimatedContent(
    modifier: Modifier = Modifier,
    selectionMode: Boolean,
    firstComponent: @Composable () -> Unit,
    secondComponent: @Composable () -> Unit
) = AnimatedContent(modifier = modifier,
    targetState = selectionMode,
    transitionSpec = { scalingAnimationContentTransform() })
{ inSelectionMode -> if (inSelectionMode) secondComponent() else firstComponent() }

@OptIn(ExperimentalAnimationApi::class)
private fun scalingAnimationContentTransform(): ContentTransform {
    return scaleIn(tween(TWEEN_DURATION, TWEEN_DELAY, LinearEasing)) with scaleOut(
        tween(TWEEN_DURATION, easing = LinearEasing)
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FadingAnimatedContent(
    modifier: Modifier = Modifier,
    currentState: Boolean,
    firstComposable: @Composable () -> Unit,
    secondComposable: @Composable () -> Unit
) = AnimatedContent(modifier = modifier,
    targetState = currentState,
    transitionSpec = { fadingAnimationContentTransform() })
{ state -> if (state) firstComposable() else secondComposable() }

@OptIn(ExperimentalAnimationApi::class)
private fun fadingAnimationContentTransform(): ContentTransform {
    return fadeIn(tween(TWEEN_DURATION, TWEEN_DELAY, LinearEasing)) with fadeOut(
        tween(TWEEN_DURATION, easing = LinearEasing)
    )
}

internal val enterTweenSpec: FiniteAnimationSpec<IntSize> = TweenSpec(
    durationMillis = TWEEN_DURATION, delay = TWEEN_DELAY, easing = FastOutSlowInEasing
)

internal val exitTweenSpec: FiniteAnimationSpec<IntSize> = TweenSpec(
    durationMillis = TWEEN_DURATION, easing = FastOutSlowInEasing
)

//@OptIn(ExperimentalAnimationApi::class) //CounterAnimation for "DELAY" in ComboDetailsScreen
//@Composable
//fun CounterAnimation(delay: Int, modifier: Modifier = Modifier) =
//    AnimatedContent(modifier = modifier, targetState = delay, transitionSpec = {
//        if (targetState > initialState) {
//            slideInVertically { height -> height } + fadeIn() with
//                    slideOutVertically { height -> -height } + fadeOut()
//        } else {
//            slideInVertically { height -> -height } + fadeIn() with
//                    slideOutVertically { height -> height } + fadeOut()
//        }.using(SizeTransform(clip = false))
//    }) { targetDelay ->
//        Text(quantityStringResource(R.plurals.all_second, targetDelay, targetDelay))
//    }

//@OptIn(ExperimentalAnimationApi::class) // This does the same thing as SlideInAndOutVertically except with AnimatedContent
//fun addAnimation(duration: Int = 300): ContentTransform {
//    return slideInVertically(animationSpec = tween(durationMillis = duration)) { width -> -width * 2 } + fadeIn(
//        animationSpec = tween(durationMillis = duration)
//    ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { width -> -width * 2 } + fadeOut(
//        animationSpec = tween(durationMillis = duration)
//    )
//}