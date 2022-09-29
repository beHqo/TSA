package com.example.android.strikingarts.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android.strikingarts.ui.combo.ComboScreen
import com.example.android.strikingarts.ui.combodetails.ComboDetailsScreen
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.COMBO_ID
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_ID
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.WORKOUT_ID
import com.example.android.strikingarts.ui.technique.TechniqueListScreen
import com.example.android.strikingarts.ui.techniquedetails.TechniqueDetailsScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {

    NavHost(
        navController = navController,
        startDestination = Screen.Combo.route
    ) {
        composable(route = Screen.Technique.route) {
            TechniqueListScreen(onNavigateToTechniqueDetails = { techniqueId ->
                navigateTo(
                    navController, Screen.TechniqueDetails.createRoute("$techniqueId")
                )
            }
            )
        }
        composable(
            route = Screen.TechniqueDetails.route,
            arguments = listOf(navArgument(TECHNIQUE_ID) { type = NavType.LongType })
        ) {
            TechniqueDetailsScreen(
                onNavigationRequest = { navigateTo(navController, Screen.Technique.route, true) }
            )
        }
        composable(route = Screen.Combo.route) {
            ComboScreen(onNavigationRequest = { comboId ->
                navigateTo(navController, Screen.ComboDetails.createRoute("$comboId"))
            }
            )
        }
        composable(route = Screen.ComboDetails.route) {
            ComboDetailsScreen(
                onNavigateToTechniqueScreen = { navigateTo(navController, Screen.Technique.route) },
                onNavigateToComboScreen = { navigateTo(navController, Screen.Combo.route) }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Technique : Screen("technique")
    object TechniqueDetails : Screen("technique_details/{$TECHNIQUE_ID}") {
        fun createRoute(techniqueId: String) = "technique_details/$techniqueId"
    }

    object Combo : Screen("combo")
    object ComboDetails : Screen("combo_details/{$COMBO_ID}") {
        fun createRoute(comboId: String) = "combo_details/$comboId"
    }

    object Workout : Screen("workout")
    object WorkoutDetails : Screen("workout/{$WORKOUT_ID}") {
        fun createRoute(workoutId: String) = "workout/$workoutId"
    }

    object Arguments {
        const val TECHNIQUE_ID = "techniqueId"
        const val COMBO_ID = "comboId"
        const val WORKOUT_ID = "workoutId"
    }
}

private fun navigateTo(
    navController: NavHostController,
    route: String,
    popUpToStartDestination: Boolean = false
) {
    navController.navigate(route) {
        if (popUpToStartDestination) popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}