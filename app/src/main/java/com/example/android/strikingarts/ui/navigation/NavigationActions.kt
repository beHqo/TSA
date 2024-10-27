package com.example.android.strikingarts.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

private fun NavHostController.onBottomNavigationItemClick(route: String) =
    navigate(route) { launchSingleTop = true; popUpTo(graph.findStartDestination().id) }

private fun NavHostController.navigateToDetailsScreen(route: String) =
    navigate(route) { launchSingleTop = true }

fun NavHostController.navigateToTechniqueScreen() =
    onBottomNavigationItemClick(Screen.Technique.createRoute())

fun NavHostController.navigateToWorkoutScreen() = onBottomNavigationItemClick(Screen.Workout.route)

fun NavHostController.navigateToComboScreen() =
    onBottomNavigationItemClick(Screen.Combo.createRoute())

fun NavHostController.navigateToTechniqueDetails(techniqueId: Long?) = navigateToDetailsScreen(
    if (techniqueId != null) Screen.TechniqueDetails.createRoute(techniqueId)
    else Screen.TechniqueDetails.createRoute()
)

fun NavHostController.navigateToWorkoutDetails(workoutId: Long?) = navigateToDetailsScreen(
    if (workoutId != null) Screen.WorkoutDetails.createRoute(workoutId)
    else Screen.WorkoutDetails.createRoute()
)

fun NavHostController.navigateToComboDetails(comboId: Long?) = navigateToDetailsScreen(
    if (comboId != null) Screen.ComboDetails.createRoute(comboId)
    else Screen.ComboDetails.createRoute()
)

fun NavHostController.navigateFromComboDetailsToTechniqueScreen() =
    navigate(Screen.Technique.createRoute(true))

fun NavHostController.navigateFromWorkoutDetailsToComboScreen() =
    navigate(Screen.Combo.createRoute(true))

fun NavHostController.navigateFromTechniqueToComboDetails() {
    if (previousBackStackEntry?.destination?.route?.contains(Screen.ComboDetails.createRoute()) == true) navigateUp()
    else navigateToDetailsScreen(Screen.ComboDetails.createRoute())
}

fun NavHostController.navigateFromComboToWorkoutDetails() {
    if (previousBackStackEntry?.destination?.route?.contains(Screen.WorkoutDetails.createRoute()) == true) navigateUp()
    else navigateToDetailsScreen(Screen.WorkoutDetails.createRoute())
}

fun NavHostController.navigateToWorkoutPreviewScreen(workoutId: Long) =
    navigate(Screen.WorkoutPreview.createRoute(workoutId))

fun NavHostController.navigateToTrainingScreen(workoutId: Long) =
    navigate(Screen.Training.createRoute(workoutId))

fun NavHostController.navigateToWinnersScreen(workoutId: Long) =
    navigate(Screen.Winners.createRoute(workoutId))

fun NavHostController.navigateToLosersScreen(workoutId: Long) =
    navigate(Screen.Losers.createRoute(workoutId))

fun NavHostController.navigateToUserPreferencesScreen() = navigate(Screen.UserPreferences.route)

fun NavHostController.navigateToHomeScreen() = navigate(Screen.Home.route)

fun NavHostController.navigateToCalendarScreen() = navigate(Screen.Calendar.route)

fun NavHostController.navigateToAboutScreen() = navigate(Screen.About.route)

fun NavHostController.navigateToHelpScreen() = navigate(Screen.Help.route)