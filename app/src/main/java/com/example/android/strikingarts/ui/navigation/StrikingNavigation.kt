package com.example.android.strikingarts.ui.navigation

import com.example.android.strikingarts.ui.navigation.Screen.Arguments.COMBO_ID
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.SELECTION_MODE
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_ID
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.WORKOUT_ID

sealed class Screen(val route: String) {
    object Technique : Screen("technique?$SELECTION_MODE={$SELECTION_MODE}") {
        fun createRoute(selectionMode: Boolean) = "technique?$SELECTION_MODE=$selectionMode"
    }

    object TechniqueDetails : Screen("technique_details?$TECHNIQUE_ID={$TECHNIQUE_ID}") {
        fun createRoute(techniqueId: Long) = "technique_details?$TECHNIQUE_ID=$techniqueId"
    }

    object Combo : Screen("combo")

    object ComboDetails : Screen("combo_details/{$COMBO_ID}") {
        fun createRoute(comboId: Long) = "combo_details/$comboId"
    }

    object Workout : Screen("workout")

    object WorkoutDetails : Screen("workout/{$WORKOUT_ID}") {
        fun createRoute(workoutId: Long) = "workout/$workoutId"
    }

    object Arguments {
        const val SELECTION_MODE = "selectionMode"
        const val TECHNIQUE_ID = "techniqueId"
        const val COMBO_ID = "comboId"
        const val WORKOUT_ID = "workoutId"
    }
}
