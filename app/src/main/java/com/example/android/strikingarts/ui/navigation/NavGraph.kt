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
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_ID
import com.example.android.strikingarts.ui.technique.TechniqueListScreen
import com.example.android.strikingarts.ui.techniquedetails.TechniqueDetailsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier, startDestination: String = Screen.Technique.route
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable(route = Screen.Technique.route) {
            TechniqueListScreen(onNavigateToTechniqueDetails = navController::navigateToTechniqueDetails)
        }
        composable(
            route = Screen.TechniqueDetails.route,
            arguments = listOf(navArgument(TECHNIQUE_ID) { type = NavType.LongType })
        ) { TechniqueDetailsScreen(navigateUp = navController::navigateUp) }
        composable(route = Screen.Combo.route) {
            ComboScreen(navigateToComboDetailsScreen = navController::navigateToComboDetails)
        }
        composable(route = Screen.ComboDetails.route) {
            ComboDetailsScreen(
                onNavigateToTechniqueScreen = navController::navigateFromComboToTechniqueScreen
            )
        }
    }
}