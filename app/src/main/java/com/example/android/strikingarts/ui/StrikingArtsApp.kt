package com.example.android.strikingarts.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.navigation.*
import com.example.android.strikingarts.ui.scaffold.BottomNavigationBar
import com.example.android.strikingarts.utils.ImmutableList

@Composable
fun StrikingArtsApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Technique.route

    val bottomNavigationItems = ImmutableList(
        listOf(
            BottomNavigationItem(
                screenName = stringResource(R.string.all_technique),
                iconId = R.drawable.ic_workout,
                route = Screen.Technique.route,
                onClick = navController::navigateToTechniqueScreen
            ), BottomNavigationItem(
                screenName = stringResource(R.string.all_workout),
                iconId = R.drawable.ic_workout,
                route = Screen.Workout.route,
                onClick = navController::navigateToWorkoutScreen
            ), BottomNavigationItem(
                screenName = stringResource(R.string.all_combo),
                iconId = R.drawable.ic_workout,
                route = Screen.Combo.route,
                onClick = navController::navigateToComboScreen
            )
        )
    )

    Scaffold(bottomBar = {
        BottomNavigationBar(
            bottomNavigationItems = bottomNavigationItems, currentRoute = currentRoute
        )
    }) {
        NavGraph(navController = navController, modifier = Modifier.padding(it))
    }
}