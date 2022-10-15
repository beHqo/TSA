package com.example.android.strikingarts.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

private fun NavHostController.onBottomNavigationItemClick(route: String) =
    navigate(route) {
        launchSingleTop = true
        popUpTo(graph.findStartDestination().id)
    }

private fun NavHostController.navigateToDetailsScreen(route: String) =
    navigate(route) { }

fun NavHostController.navigateToTechniqueScreen() =
    onBottomNavigationItemClick(Screen.Technique.route)

fun NavHostController.navigateToWorkoutScreen() =
    onBottomNavigationItemClick(Screen.Workout.route)

fun NavHostController.navigateToComboScreen() =
    onBottomNavigationItemClick(Screen.Combo.route)

fun NavHostController.navigateToTechniqueDetails(techniqueId: Long) =
    navigateToDetailsScreen(Screen.TechniqueDetails.createRoute("$techniqueId"))

fun NavHostController.navigateToWorkoutDetails(workoutId: Long) =
    navigateToDetailsScreen(Screen.WorkoutDetails.createRoute("$workoutId"))

fun NavHostController.navigateToComboDetails(comboId: Long) =
    navigateToDetailsScreen(Screen.ComboDetails.createRoute("$comboId"))

fun NavHostController.navigateFromComboToTechniqueScreen() =
    navigate(Screen.Technique.route)