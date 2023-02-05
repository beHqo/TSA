package com.example.android.strikingarts.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.android.strikingarts.ui.combo.ComboScreen
import com.example.android.strikingarts.ui.combodetails.ComboDetailsScreen
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.COMBO_ID
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.COMBO_SELECTION_MODE
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_ID
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_SELECTION_MODE
import com.example.android.strikingarts.ui.technique.TechniqueScreen
import com.example.android.strikingarts.ui.techniquedetails.TechniqueDetailsScreen
import com.example.android.strikingarts.ui.workout.WorkoutScreen
import com.example.android.strikingarts.ui.workoutdetails.WorkoutDetailsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    onSelectionModeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Technique.route
    ) {
        composable(
            route = Screen.Technique.route,
            arguments = listOf(navArgument(TECHNIQUE_SELECTION_MODE) {
                type = NavType.BoolType; defaultValue = false
            })
        ) {
            TechniqueScreen(
                onNavigateToTechniqueDetails = navController::navigateToTechniqueDetails,
                onNavigateToComboDetails = navController::navigateFromTechniqueToComboDetails,
                notifyBottomAppbarOnSelectionMode = onSelectionModeChange,
            )
        }
        composable(
            route = Screen.TechniqueDetails.route, arguments = listOf(navArgument(TECHNIQUE_ID) {
                type = NavType.LongType; defaultValue = 0L
            })
        ) {
            TechniqueDetailsScreen(navigateUp = navController::navigateUp)
        }
        composable(
            route = Screen.Combo.route, arguments = listOf(navArgument(COMBO_SELECTION_MODE) {
                type = NavType.BoolType; defaultValue = false
            })
        ) {
            ComboScreen(
                navigateToComboDetailsScreen = navController::navigateToComboDetails,
                notifyBottomNavBarOnSelectionMode = onSelectionModeChange
            )
        }
        composable(
            route = Screen.ComboDetails.route, arguments = listOf(
                navArgument(COMBO_ID) { type = NavType.LongType; defaultValue = 0L },
            )
        ) {
            ComboDetailsScreen(
                onNavigateToTechniqueScreen = {
                    navController.navigateFromComboDetailsToTechniqueScreen()
                    onSelectionModeChange(true)
                },
                onEnableSelectionMode = onSelectionModeChange,
                onNavigateUp = navController::navigateToComboScreen
            )
        }
        composable(route = Screen.Workout.route) {
            WorkoutScreen(
                navigateToWorkoutDetails = navController::navigateToWorkoutDetails,
                onSelectionModeChange = onSelectionModeChange
            )
        }
        composable(route = Screen.WorkoutDetails.route) {
            WorkoutDetailsScreen(
                navigateUp = navController::navigateUp,
                navigateToComboScreen = navController::navigateFromWorkoutDetailsToComboScreen
            )
        }
    }
}
