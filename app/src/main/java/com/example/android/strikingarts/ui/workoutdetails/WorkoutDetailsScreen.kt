package com.example.android.strikingarts.ui.workoutdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun WorkoutDetailsScreen(navigateUp: () -> Unit, navigateToComboScreen: () -> Unit) {
    Column {
        Text("WorkoutDetailsScreen is currently under development")
        Button(onClick = navigateUp) {
            Text("Navigate back to WorkoutScreen")
        }
    }
}