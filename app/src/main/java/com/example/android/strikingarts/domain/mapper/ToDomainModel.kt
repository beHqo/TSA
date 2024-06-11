package com.example.android.strikingarts.domain.mapper

import com.example.android.strikingarts.domain.model.Workout
import com.example.android.strikingarts.domain.model.WorkoutDetails

fun Workout.toWorkoutDetails() = WorkoutDetails(
    rounds = this.rounds,
    roundLengthSeconds = this.roundLengthSeconds,
    restLengthSeconds = this.restLengthSeconds
)