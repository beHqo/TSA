package com.thestrikingarts.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.thestrikingarts.ui.about.AboutScreen
import com.thestrikingarts.ui.calendar.CalendarScreen
import com.thestrikingarts.ui.combo.ComboScreen
import com.thestrikingarts.ui.combodetails.ComboDetailsScreen
import com.thestrikingarts.ui.help.HelpScreen
import com.thestrikingarts.ui.home.HomeScreen
import com.thestrikingarts.ui.navigation.Screen.Arguments.COMBO_ID
import com.thestrikingarts.ui.navigation.Screen.Arguments.COMBO_PRODUCTION_MODE
import com.thestrikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_ID
import com.thestrikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_PRODUCTION_MODE
import com.thestrikingarts.ui.navigation.Screen.Arguments.WORKOUT_DETAILS_WORKOUT_ID
import com.thestrikingarts.ui.technique.TechniqueScreen
import com.thestrikingarts.ui.techniquedetails.TechniqueDetailsScreen
import com.thestrikingarts.ui.userpreferences.UserPreferencesScreen
import com.thestrikingarts.ui.workout.WorkoutScreen
import com.thestrikingarts.ui.workoutdetails.WorkoutDetailsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    showSnackbar: (message: String) -> Unit,
    modifier: Modifier = Modifier,
) = NavHost(
    modifier = modifier, navController = navController, startDestination = Screen.Home.route
) {
    composable(
        route = Screen.Technique.route, arguments = listOf(navArgument(TECHNIQUE_PRODUCTION_MODE) {
            type = NavType.BoolType; defaultValue = false
        })
    ) {
        TechniqueScreen(
            showSnackbar = showSnackbar,
            navigateToTechniqueDetails = navController::navigateToTechniqueDetails,
            navigateToComboDetails = navController::navigateFromTechniqueToComboDetails,
            setSelectionModeValueGlobally = setSelectionModeValueGlobally,
        )
    }

    composable(
        route = Screen.TechniqueDetails.route, arguments = listOf(navArgument(TECHNIQUE_ID) {
            type = NavType.LongType; defaultValue = 0L
        })
    ) {
        TechniqueDetailsScreen(navigateUp = navController::navigateUp, showSnackbar = showSnackbar)
    }

    composable(
        route = Screen.Combo.route, arguments = listOf(navArgument(COMBO_PRODUCTION_MODE) {
            type = NavType.BoolType; defaultValue = false
        })
    ) {
        ComboScreen(
            showSnackbar = showSnackbar,
            navigateToComboDetails = navController::navigateToComboDetails,
            navigateToWorkoutDetails = navController::navigateFromComboToWorkoutDetails,
            setSelectionModeValueGlobally = setSelectionModeValueGlobally
        )
    }

    composable(
        route = Screen.ComboDetails.route,
        arguments = listOf(navArgument(COMBO_ID) { type = NavType.LongType; defaultValue = 0L })
    ) {
        ComboDetailsScreen(
            navigateUp = navController::navigateToComboScreen,
            navigateToTechniqueScreen = navController::navigateFromComboDetailsToTechniqueScreen,
            showSnackbar = showSnackbar,
            setSelectionModeValueGlobally = setSelectionModeValueGlobally
        )
    }

    composable(route = Screen.Workout.route) {
        WorkoutScreen(
            showSnackbar = showSnackbar,
            navigateToWorkoutDetails = navController::navigateToWorkoutDetails,
            navigateToWorkoutPreviewScreen = navController::navigateToWorkoutPreviewScreen,
            setSelectionModeValueGlobally = setSelectionModeValueGlobally
        )
    }

    composable(
        route = Screen.WorkoutDetails.route,
        arguments = listOf(navArgument(WORKOUT_DETAILS_WORKOUT_ID) {
            type = NavType.LongType; defaultValue = 0L
        })
    ) {
        WorkoutDetailsScreen(
            navigateUp = navController::navigateUp,
            navigateToComboScreen = navController::navigateFromWorkoutDetailsToComboScreen,
            showSnackbar = showSnackbar,
            setSelectionModeValueGlobally = setSelectionModeValueGlobally
        )
    }

    composable(route = Screen.Calendar.route) {
        CalendarScreen(navigateToWorkoutPreview = navController::navigateToWorkoutPreviewScreen)
    }

    composable(route = Screen.Home.route) {
        HomeScreen(
            navigateToUserPreferencesScreen = navController::navigateToUserPreferencesScreen,
            navigateToAboutScreen = navController::navigateToAboutScreen,
            navigateToHelpScreen = navController::navigateToHelpScreen,
            navigateToWorkoutPreviewScreen = navController::navigateToWorkoutPreviewScreen
        )
    }

    composable(route = Screen.UserPreferences.route) {
        UserPreferencesScreen(navigateUp = navController::navigateUp)
    }

    composable(route = Screen.About.route) { AboutScreen(navigateUp = navController::navigateUp) }

    composable(route = Screen.Help.route) { HelpScreen(navigateUp = navController::navigateUp) }

    sessionNavGraph(navController)
}