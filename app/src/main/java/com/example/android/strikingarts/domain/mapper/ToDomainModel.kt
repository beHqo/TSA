package com.example.android.strikingarts.domain.mapper

import com.example.android.strikingarts.domain.model.Combo
import com.example.android.strikingarts.domain.model.Workout
import com.example.android.strikingarts.domain.model.WorkoutDetails

fun Workout.toWorkoutDetails() = WorkoutDetails(
    rounds = this.rounds,
    roundLengthSeconds = this.roundLengthSeconds,
    restLengthSeconds = this.restLengthSeconds
)

fun Combo.getAudioStringList(): List<String> =
    this.techniqueList.map { it.audioAttributes.audioString }

fun Combo.getAudioDuration(): Long =
    this.techniqueList.sumOf { it.audioAttributes.durationMillis }