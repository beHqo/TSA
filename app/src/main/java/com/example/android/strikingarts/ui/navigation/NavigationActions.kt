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

fun NavHostController.navigateToTechniqueDetails(techniqueId: Long) =
    navigateToDetailsScreen(Screen.TechniqueDetails.createRoute(techniqueId))

fun NavHostController.navigateToWorkoutDetails(workoutId: Long) =
    navigateToDetailsScreen(Screen.WorkoutDetails.createRoute(workoutId))

fun NavHostController.navigateToComboDetails(comboId: Long) =
    navigateToDetailsScreen(Screen.ComboDetails.createRoute(comboId))

fun NavHostController.navigateFromComboDetailsToTechniqueScreen() =
    navigate(Screen.Technique.createRoute(true))

fun NavHostController.navigateFromWorkoutDetailsToComboScreen() =
    navigate(Screen.Combo.createRoute(true))

fun NavHostController.navigateFromTechniqueToComboDetails() {
    if (previousBackStackEntry?.destination?.route?.
        contains(Screen.ComboDetails.createRoute()) == true) navigateUp()
    else navigateToDetailsScreen(Screen.ComboDetails.createRoute())
}
