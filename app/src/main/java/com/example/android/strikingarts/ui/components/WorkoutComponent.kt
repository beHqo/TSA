package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.database.entity.Workout
import com.example.android.strikingarts.utils.getWorkoutDetails

@Composable
fun WorkoutItem(workout: Workout) {
    ExpandableListItem(
        primaryText = workout.name,
        expandedText = getWorkoutDetails(workout)
    )
}

@Composable
fun WorkoutList(workoutList: List<Workout>) {
    val maxWidth = LocalConfiguration.current.screenWidthDp
    val dividerStartIndent = maxWidth.div(3.33)

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize()
    ) {
        items(workoutList) { workout ->
            WorkoutItem(workout)
            Divider(startIndent = dividerStartIndent.dp)
        }
    }
}

//
//@Preview
//@Composable
//fun PreviewWorkoutList() {
//    WorkoutList(workoutList = listOf(
//        Workout(name = "Kir1", numberOfRounds = 5, roundsDurationMilli = 3000, restsDurationMilli = 600),
//        Workout(name = "Kir2", numberOfRounds = 10, roundsDurationMilli = 3001, restsDurationMilli = 620),
//        Workout(name = "Kir3", numberOfRounds = 2, roundsDurationMilli = 1, restsDurationMilli = 1),
//        Workout(name = "Kir4", numberOfRounds = 1, roundsDurationMilli = 600, restsDurationMilli = 100)
//    ))
//}