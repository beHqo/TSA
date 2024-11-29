package com.thestrikingarts.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.thestrikingarts.ui.losersscreen.LosersScreen
import com.thestrikingarts.ui.training.TrainingScreen
import com.thestrikingarts.ui.winnersscreen.WinnersScreen
import com.thestrikingarts.ui.workoutpreview.WorkoutPreviewScreen

fun NavGraphBuilder.sessionNavGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.WorkoutPreview.route,
        route = Screen.NavGraphNames.SESSION_NAV_GRAPH
    ) {
        composable(
            route = Screen.WorkoutPreview.route,
            arguments = listOf(navArgument(Screen.Arguments.WORKOUT_PREVIEW_WORKOUT_ID) {
                type = NavType.LongType; defaultValue = 0L
            })
        ) {
            WorkoutPreviewScreen(
                navigateUp = navController::navigateUp,
                navigateToTrainingScreen = navController::navigateToTrainingScreen,
                navigateToWorkoutDetails = navController::navigateToWorkoutDetails
            )
        }

        composable(
            route = Screen.Training.route,
            arguments = listOf(navArgument(Screen.Arguments.TRAINING_WORKOUT_ID) {
                type = NavType.LongType; defaultValue = 0L
            })
        ) {
            TrainingScreen(
                navigateToWinnersScreen = navController::navigateToWinnersScreen,
                navigateToLosersScreen = navController::navigateToLosersScreen
            )
        }

        composable(
            route = Screen.Winners.route,
            arguments = listOf(navArgument(Screen.Arguments.WINNERS_WORKOUT_ID) {
                type = NavType.LongType; defaultValue = 0L
            })
        ) {
            WinnersScreen(
                navigateToHomeScreen = navController::navigateToHomeScreen,
                navigateToWorkoutPreview = navController::navigateToWorkoutPreviewScreen
            )
        }

        composable(
            route = Screen.Losers.route,
            arguments = listOf(navArgument(Screen.Arguments.LOSERS_WORKOUT_ID) {
                type = NavType.LongType; defaultValue = 0L
            })
        ) {
            LosersScreen(navigateToHomeScreen = navController::navigateToHomeScreen)
        }
    }
}