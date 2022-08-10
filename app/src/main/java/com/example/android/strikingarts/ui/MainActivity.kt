package com.example.android.strikingarts.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android.strikingarts.ui.NavigationKeys.Arg.TECHNIQUE_ID
import com.example.android.strikingarts.ui.combo.ComboList
import com.example.android.strikingarts.ui.technique.TechniqueListScreen
import com.example.android.strikingarts.ui.techniquedetails.TechniqueDetailsScreen
import com.example.android.strikingarts.ui.theme.StrikingArtsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            StrikingArtsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
//                    Test1()
//                    Test2()
//                    Test3()
                    NavTest()
                }
            }
        }
    }
}

@Composable
private fun Test1() {
    TechniqueListScreen { }
}

@Composable
private fun Test2() {
    TechniqueDetailsScreen {}
}

@Composable
private fun Test3() {
    ComboList {}
}

@Composable
fun NavTest() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationKeys.Route.TECHNIQUE_LIST
    ) {
        composable(route = NavigationKeys.Route.TECHNIQUE_LIST) {
            TechniqueListScreen { techniqueId ->
                navigateTo(navController, NavigationKeys.Route.TECHNIQUE_LIST, techniqueId)
            }
        }
        composable(
            route = NavigationKeys.Route.TECHNIQUE_DETAILS,
            arguments = listOf(navArgument(TECHNIQUE_ID) { type = NavType.LongType })
        ) {
            TechniqueDetailsScreen {
                navigateTo(
                    navController,
                    NavigationKeys.Route.TECHNIQUE_LIST
                )
            }
        }
    }
}

object NavigationKeys {

    object Arg {
        const val TECHNIQUE_ID = "techniqueId"
    }

    object Route {
        const val TECHNIQUE_LIST = "technique_list"
        const val TECHNIQUE_DETAILS = "$TECHNIQUE_LIST/{$TECHNIQUE_ID}"

        object BottomStuff {}
    }
}

private fun navigateTo(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id)
        launchSingleTop = true
    }
}

private fun navigateTo(navController: NavHostController, route: String, arg: Comparable<*>) {
    navController.navigate("$route/$arg") {}
}