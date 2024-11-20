package com.github.tsa.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun SurviveProcessDeath(
    onStop: () -> Unit = {},
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val currentOnStop by rememberUpdatedState(onStop)

    DisposableEffect(lifecycleOwner) {
        val lifecycleEventObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) currentOnStop()
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleEventObserver)

        onDispose { lifecycleOwner.lifecycle.removeObserver(lifecycleEventObserver) }
    }
}

//@Composable
//fun DisposableEffectWithLifecycle(
//    onCreate: () -> Unit = {},
//    onStart: () -> Unit = {},
//    onResume: () -> Unit = {},
//    onPause: () -> Unit = {},
//    onStop: () -> Unit = {},
//    onDestroy: () -> Unit = {},
//    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
//) {
//    val currentOnCreate by rememberUpdatedState(onCreate)
//    val currentOnStart by rememberUpdatedState(onStart)
//    val currentOnResume by rememberUpdatedState(onResume)
//    val currentOnPause by rememberUpdatedState(onPause)
//    val currentOnStop by rememberUpdatedState(onStop)
//    val currentOnDestroy by rememberUpdatedState(onDestroy)
//
//    DisposableEffect(lifecycleOwner) {
//        val lifecycleEventObserver = LifecycleEventObserver { _, event ->
//            when (event) {
//                Lifecycle.Event.ON_CREATE -> currentOnCreate()
//                Lifecycle.Event.ON_START -> currentOnStart()
//                Lifecycle.Event.ON_PAUSE -> currentOnPause()
//                Lifecycle.Event.ON_RESUME -> currentOnResume()
//                Lifecycle.Event.ON_STOP -> currentOnStop()
//                Lifecycle.Event.ON_DESTROY -> currentOnDestroy()
//                else -> {}
//            }
//        }
//        lifecycleOwner.lifecycle.addObserver(lifecycleEventObserver)
//
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(lifecycleEventObserver)
//        }
//    }
//}