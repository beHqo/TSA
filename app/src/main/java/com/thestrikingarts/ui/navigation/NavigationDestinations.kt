package com.thestrikingarts.ui.navigation

import com.thestrikingarts.ui.navigation.Screen.Arguments.COMBO_ID
import com.thestrikingarts.ui.navigation.Screen.Arguments.COMBO_PRODUCTION_MODE
import com.thestrikingarts.ui.navigation.Screen.Arguments.LOSERS_WORKOUT_ID
import com.thestrikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_ID
import com.thestrikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_PRODUCTION_MODE
import com.thestrikingarts.ui.navigation.Screen.Arguments.TRAINING_WORKOUT_ID
import com.thestrikingarts.ui.navigation.Screen.Arguments.WINNERS_WORKOUT_ID
import com.thestrikingarts.ui.navigation.Screen.Arguments.WORKOUT_DETAILS_WORKOUT_ID
import com.thestrikingarts.ui.navigation.Screen.Arguments.WORKOUT_PREVIEW_WORKOUT_ID
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.ABOUT
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.CALENDAR
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.COMBO
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.COMBO_DETAILS
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.HELP
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.HOME
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.LOSERS
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.TECHNIQUE
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.TECHNIQUE_DETAILS
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.TRAINING
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.USER_PREFS
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.WINNERS
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.WORKOUT
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.WORKOUT_DETAILS
import com.thestrikingarts.ui.navigation.Screen.ScreenNames.WORKOUT_PREVIEW

sealed class Screen(val route: String) {
    object Technique : Screen(createRouteWithOneOptionalArg(TECHNIQUE, TECHNIQUE_PRODUCTION_MODE)) {
        fun createRoute() = TECHNIQUE
        fun createRoute(selectionMode: Boolean) =
            createRouteForOneOptionalArg(TECHNIQUE, TECHNIQUE_PRODUCTION_MODE, "$selectionMode")
    }

    object TechniqueDetails :
        Screen(createRouteWithOneOptionalArg(TECHNIQUE_DETAILS, TECHNIQUE_ID)) {
        fun createRoute() = TECHNIQUE_DETAILS
        fun createRoute(techniqueId: Long) =
            createRouteForOneOptionalArg(TECHNIQUE_DETAILS, TECHNIQUE_ID, "$techniqueId")
    }

    object Combo : Screen(createRouteWithOneOptionalArg(COMBO, COMBO_PRODUCTION_MODE)) {
        fun createRoute() = COMBO
        fun createRoute(selectionMode: Boolean) =
            createRouteForOneOptionalArg(COMBO, COMBO_PRODUCTION_MODE, "$selectionMode")
    }

    object ComboDetails : Screen(createRouteWithOneOptionalArg(COMBO_DETAILS, COMBO_ID)) {
        fun createRoute() = COMBO_DETAILS
        fun createRoute(comboId: Long) =
            createRouteForOneOptionalArg(COMBO_DETAILS, COMBO_ID, "$comboId")
    }

    object Workout : Screen(WORKOUT)

    object WorkoutDetails :
        Screen(createRouteWithOneOptionalArg(WORKOUT_DETAILS, WORKOUT_DETAILS_WORKOUT_ID)) {
        fun createRoute() = WORKOUT_DETAILS
        fun createRoute(workoutId: Long) =
            createRouteForOneOptionalArg(WORKOUT_DETAILS, WORKOUT_DETAILS_WORKOUT_ID, "$workoutId")
    }

    object WorkoutPreview :
        Screen(createRouteWithOneOptionalArg(WORKOUT_PREVIEW, WORKOUT_PREVIEW_WORKOUT_ID)) {
        fun createRoute(workoutId: Long) =
            createRouteForOneOptionalArg(WORKOUT_PREVIEW, WORKOUT_PREVIEW_WORKOUT_ID, "$workoutId")
    }

    object Training : Screen(createRouteWithOneOptionalArg(TRAINING, TRAINING_WORKOUT_ID)) {
        fun createRoute(workoutId: Long) =
            createRouteForOneOptionalArg(TRAINING, TRAINING_WORKOUT_ID, "$workoutId")
    }

    object Winners : Screen(createRouteWithOneOptionalArg(WINNERS, WINNERS_WORKOUT_ID)) {
        fun createRoute(workoutId: Long) =
            createRouteForOneOptionalArg(WINNERS, WINNERS_WORKOUT_ID, "$workoutId")
    }

    object Losers : Screen(createRouteWithOneOptionalArg(LOSERS, LOSERS_WORKOUT_ID)) {
        fun createRoute(workoutId: Long) =
            createRouteForOneOptionalArg(LOSERS, LOSERS_WORKOUT_ID, "$workoutId")
    }

    object Calendar : Screen(CALENDAR)

    object Home : Screen(HOME)

    object UserPreferences : Screen(USER_PREFS)

    object About : Screen(ABOUT)

    object Help : Screen(HELP)

    object ScreenNames {
        const val TECHNIQUE = "technique"
        const val TECHNIQUE_DETAILS = "technique_details"
        const val COMBO = "combo"
        const val COMBO_DETAILS = "combo_details"
        const val WORKOUT = "workout"
        const val WORKOUT_DETAILS = "workout_details"
        const val WORKOUT_PREVIEW = "workout_preview"
        const val TRAINING = "training"
        const val WINNERS = "winner"
        const val LOSERS = "loser"
        const val CALENDAR = "calendar"
        const val HOME = "home"
        const val USER_PREFS = "user_prefs"
        const val ABOUT = "about"
        const val HELP = "help"
    }

    object NavGraphNames {
        const val SESSION_NAV_GRAPH = "session_nav_graph"
    }

    object Arguments {
        const val TECHNIQUE_PRODUCTION_MODE = "technique_selection_mode"
        const val TECHNIQUE_ID = "technique_id"
        const val COMBO_ID = "combo_id"
        const val COMBO_PRODUCTION_MODE = "combo_selection_mode"
        const val WORKOUT_DETAILS_WORKOUT_ID = "workout_details_workout_id"
        const val WORKOUT_PREVIEW_WORKOUT_ID = "workout_preview_id"
        const val TRAINING_WORKOUT_ID = "training_workout_id"
        const val WINNERS_WORKOUT_ID = "finished_workout_id"
        const val LOSERS_WORKOUT_ID = "aborted_workout_id"
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