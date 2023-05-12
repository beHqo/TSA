package com.example.android.strikingarts.ui.navigation

import com.example.android.strikingarts.ui.navigation.Screen.Arguments.COMBO_ID
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.COMBO_SELECTION_MODE
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_ID
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_SELECTION_MODE
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.WORKOUT_ID
import com.example.android.strikingarts.ui.navigation.Screen.ScreenNames.COMBO
import com.example.android.strikingarts.ui.navigation.Screen.ScreenNames.COMBO_DETAILS
import com.example.android.strikingarts.ui.navigation.Screen.ScreenNames.TECHNIQUE
import com.example.android.strikingarts.ui.navigation.Screen.ScreenNames.TECHNIQUE_DETAILS
import com.example.android.strikingarts.ui.navigation.Screen.ScreenNames.WORKOUT
import com.example.android.strikingarts.ui.navigation.Screen.ScreenNames.WORKOUT_DETAILS

sealed class Screen(val route: String) {
    object Technique : Screen(createRouteWithOneOptionalArg(TECHNIQUE, TECHNIQUE_SELECTION_MODE)) {
        fun createRoute() = TECHNIQUE
        fun createRoute(selectionMode: Boolean) =
            createRouteForOneOptionalArg(TECHNIQUE, TECHNIQUE_SELECTION_MODE, "$selectionMode")
    }

    object TechniqueDetails :
        Screen(createRouteWithOneOptionalArg(TECHNIQUE_DETAILS, TECHNIQUE_ID)) {
        fun createRoute() = TECHNIQUE_DETAILS
        fun createRoute(techniqueId: Long) =
            createRouteForOneOptionalArg(TECHNIQUE_DETAILS, TECHNIQUE_ID, "$techniqueId")
    }

    object Combo : Screen(createRouteWithOneOptionalArg(COMBO, COMBO_SELECTION_MODE)) {
        fun createRoute() = COMBO
        fun createRoute(selectionMode: Boolean) =
            createRouteForOneOptionalArg(COMBO, COMBO_SELECTION_MODE, "$selectionMode")
    }

    object ComboDetails : Screen(createRouteWithOneOptionalArg(COMBO_DETAILS, COMBO_ID)) {
        fun createRoute() = COMBO_DETAILS
        fun createRoute(comboId: Long) =
            createRouteForOneOptionalArg(COMBO_DETAILS, COMBO_ID, "$comboId")
    }

    object Workout : Screen(WORKOUT)

    object WorkoutDetails : Screen(createRouteWithOneOptionalArg(WORKOUT_DETAILS, WORKOUT_ID)) {
        fun createRoute() = WORKOUT_DETAILS
        fun createRoute(workoutId: Long) =
            createRouteForOneOptionalArg(WORKOUT_DETAILS, WORKOUT_ID, "$workoutId")
    }

    object ScreenNames {
        const val TECHNIQUE = "technique"
        const val TECHNIQUE_DETAILS = "technique_details"
        const val COMBO = "combo"
        const val COMBO_DETAILS = "combo_details"
        const val WORKOUT = "workout"
        const val WORKOUT_DETAILS = "workout_details"
    }

    object Arguments {
        const val TECHNIQUE_SELECTION_MODE = "techniqueSelectionMode"
        const val TECHNIQUE_ID = "techniqueId"
        const val COMBO_ID = "comboId"
        const val COMBO_SELECTION_MODE = "comboSelectionMode"
        const val WORKOUT_ID = "workoutId"
    }
}

private fun createRouteWithOneOptionalArg(route: String, arg: String) = "$route?$arg={$arg}"
private fun createRouteForOneOptionalArg(route: String, argId: String, arg: String) =
    "$route?$argId=$arg"
//
//private fun createRouteWithTwoOptionalArgs(route: String, arg1: String, arg2: String) =
//    "$route?$arg1={$arg1}&$arg2={$arg2}"
//
//private fun createRouteForTwoOptionalArgs(
//    route: String, argId1: String, arg1: String, argId2: String, arg2: String
//) = "$route?$argId1=$arg1&$argId2=$arg2"