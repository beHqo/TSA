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
    object Technique : Screen(createRouteWithOptionalArg(TECHNIQUE, TECHNIQUE_SELECTION_MODE)) {
        fun createRoute(selectionMode: Boolean) =
            createRouteForOptionalArg(TECHNIQUE, TECHNIQUE_SELECTION_MODE, "$selectionMode")
    }

    object TechniqueDetails : Screen(createRouteWithOptionalArg(TECHNIQUE_DETAILS, TECHNIQUE_ID)) {
        fun createRoute(techniqueId: Long) =
            createRouteForOptionalArg(TECHNIQUE_DETAILS, TECHNIQUE_ID, "$techniqueId")
    }

    object Combo : Screen(createRouteWithOptionalArg(COMBO, COMBO_SELECTION_MODE)) {
        fun createRoute(selectionMode: Boolean) =
            createRouteForOptionalArg(COMBO, COMBO_SELECTION_MODE, "$selectionMode")
    }

    object ComboDetails : Screen(createRouteWithOptionalArg(COMBO_DETAILS, COMBO_ID)) {
        fun createRoute(comboId: Long) =
            createRouteForOptionalArg(COMBO_DETAILS, COMBO_ID, "$comboId")
    }

    object Workout : Screen(WORKOUT)

    object WorkoutDetails : Screen(createRouteWithOptionalArg(WORKOUT_DETAILS, WORKOUT_ID)) {
        fun createRoute(workoutId: Long) =
            createRouteForOptionalArg(WORKOUT_DETAILS, WORKOUT_ID, "$workoutId")
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

private fun createRouteWithOptionalArg(route: String, arg: String) = "$route?$arg={$arg}"
private fun createRouteForOptionalArg(route: String, argId: String, arg: String) =
    "$route?$argId=$arg"
