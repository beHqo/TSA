package com.example.android.strikingarts.ui.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun WorkoutScreen(
    navigateToWorkoutDetails: (Long) -> Unit, setSelectionModeValueGlobally: (Boolean) -> Unit
) {
    Column {
        Text("WorkoutScreen is currently under development!")
        Button(onClick = { navigateToWorkoutDetails(0L) }) {
            Text("navigate to WorkoutDetailsScreen")
        }
    }
}