package com.example.android.strikingarts.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.components.VerticalExpandAnimatedVisibility
import com.example.android.strikingarts.ui.navigation.BottomNavigationItem
import com.example.android.strikingarts.ui.navigation.NavGraph
import com.example.android.strikingarts.ui.navigation.Screen
import com.example.android.strikingarts.ui.navigation.navigateToComboScreen
import com.example.android.strikingarts.ui.navigation.navigateToTechniqueScreen
import com.example.android.strikingarts.ui.navigation.navigateToWorkoutScreen
import com.example.android.strikingarts.ui.scaffold.BottomNavigationBar
import com.example.android.strikingarts.domain.common.ImmutableList

@Composable
fun StrikingArtsApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Combo.route

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

    val (selectionMode, setSelectionModeValueGlobally) = rememberSaveable { mutableStateOf(false) }

    val bottomNavBarVisible by remember(currentRoute, selectionMode) {
        derivedStateOf { !selectionMode && bottomNavigationItems.any { currentRoute == it.route } }
    }

    Scaffold(bottomBar = {
        VerticalExpandAnimatedVisibility(visible = bottomNavBarVisible) {
            BottomNavigationBar(
                bottomNavigationItems = bottomNavigationItems, currentRoute = currentRoute
            )
        }
    }) {
        NavGraph(
            navController = navController,
            setSelectionModeValueGlobally = setSelectionModeValueGlobally,
            modifier = Modifier.padding(it)
        )
    }
}