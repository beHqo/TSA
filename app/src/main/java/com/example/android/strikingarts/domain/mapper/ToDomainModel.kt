package com.example.android.strikingarts.domain.mapper

import com.example.android.strikingarts.domain.model.Combo
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.Workout
import com.example.android.strikingarts.domain.model.WorkoutDetails
import com.example.android.strikingarts.domain.model.toImmutableList

fun Workout.toWorkoutDetails() = WorkoutDetails(
    rounds = this.rounds,
    roundLengthSeconds = this.roundLengthSeconds,
    restLengthSeconds = this.restLengthSeconds
)

fun Combo.getAudioStringList(): ImmutableList<String> =
    this.techniqueList.map { it.audioAttributes.audioString }.toImmutableList()

fun Combo.getAudioDuration(): Long =
    this.techniqueList.sumOf { it.audioAttributes.durationMillis }