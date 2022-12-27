package com.example.android.strikingarts.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
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
import com.example.android.strikingarts.ui.technique.TechniqueListScreen
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
        techniqueScreen(navController, onSelectionModeChange)
        techniqueDetailsScreen(navController)
        comboScreen(navController, onSelectionModeChange)
        comboDetailsScreen(navController, onSelectionModeChange)
        workoutScreen(navController, onSelectionModeChange)
        workoutDetailsScreen(navController)
    }
}

private fun NavGraphBuilder.techniqueScreen(
    navController: NavHostController, onSelectionModeChange: (Boolean) -> Unit
) {
    composable(
        route = Screen.Technique.route, arguments = listOf(navArgument(TECHNIQUE_SELECTION_MODE) {
            type = NavType.BoolType; defaultValue = false
        })
    ) {
        TechniqueListScreen(
            onNavigateToTechniqueDetails = navController::navigateToTechniqueDetails,
            onNavigateToComboDetails = navController::navigateFromTechniqueToComboDetails,
            notifyBottomAppbarOnSelectionMode = onSelectionModeChange,
        )
    }
}

private fun NavGraphBuilder.techniqueDetailsScreen(navController: NavHostController) {
    composable(
        route = Screen.TechniqueDetails.route, arguments = listOf(navArgument(TECHNIQUE_ID) {
            type = NavType.LongType; defaultValue = 0L
        })
    ) { TechniqueDetailsScreen(navigateUp = navController::navigateUp) }
}

private fun NavGraphBuilder.comboScreen(
    navController: NavHostController, onSelectionModeChange: (Boolean) -> Unit
) {
    composable(
        route = Screen.Combo.route, arguments = listOf(navArgument(COMBO_SELECTION_MODE) {
            type = NavType.BoolType; defaultValue = false
        })
    ) {
        ComboScreen(
            navigateToComboDetailsScreen = navController::navigateToComboDetails,
            onSelectionModeChange = onSelectionModeChange
        )
    }
}

private fun NavGraphBuilder.comboDetailsScreen(
    navController: NavHostController, onSelectionModeChange: (Boolean) -> Unit
) {
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
}

private fun NavGraphBuilder.workoutScreen(
    navController: NavHostController, onSelectionModeChange: (Boolean) -> Unit
) {
    composable(route = Screen.Workout.route) {
        WorkoutScreen(
            navigateToWorkoutDetails = navController::navigateToWorkoutDetails,
            onSelectionModeChange = onSelectionModeChange
        )
    }
}

private fun NavGraphBuilder.workoutDetailsScreen(navController: NavHostController) {
    composable(route = Screen.WorkoutDetails.route) {
        WorkoutDetailsScreen(
            navigateUp = navController::navigateUp,
            navigateToComboScreen = navController::navigateFromWorkoutDetailsToComboScreen
        )
    }
}