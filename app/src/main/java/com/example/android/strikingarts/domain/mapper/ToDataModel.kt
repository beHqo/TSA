package com.example.android.strikingarts.domain.mapper

import com.example.android.strikingarts.data.local.room.model.Combo
import com.example.android.strikingarts.data.local.room.model.ComboWithTechniques
import com.example.android.strikingarts.data.local.room.model.DataUriAudioAttributes
import com.example.android.strikingarts.data.local.room.model.Technique
import com.example.android.strikingarts.data.local.room.model.Workout
import com.example.android.strikingarts.data.local.room.model.WorkoutConclusion
import com.example.android.strikingarts.data.local.room.model.WorkoutWithCombos
import com.example.android.strikingarts.domain.model.AssetAudioAttributes
import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.domain.model.TechniqueListItem
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import com.example.android.strikingarts.domain.model.WorkoutListItem
import com.example.android.strikingarts.domain.model.WorkoutResult

fun AudioAttributes.toDataModel(): DataUriAudioAttributes? = when (this) {
    is UriAudioAttributes -> DataUriAudioAttributes(
        this.name, this.audioString, this.durationMillis, this.sizeByte
    )

    is AssetAudioAttributes -> DataUriAudioAttributes(
        this.name, this.audioString, this.durationMillis
    )

    else -> null
}

fun TechniqueListItem.toDataModel() = Technique(
    techniqueId = this.id,
    name = this.name,
    num = this.num,
    audioAttributes = this.audioAttributes.toDataModel(),
    color = this.color,
    movementType = this.movementType.name,
    techniqueType = this.techniqueType.name
)

fun ComboListItem.toDataModel() = ComboWithTechniques(combo = Combo(
    comboId = id, name = name, description = desc, delayMillis = delayMillis
), techniques = techniqueList.list.map { it.toDataModel() })

fun WorkoutListItem.toDataModel() = WorkoutWithCombos(workout = Workout(
    workoutId = this.id,
    name = this.name,
    rounds = this.rounds,
    roundLengthSeconds = this.roundLengthSeconds,
    subRounds = this.subRounds,
    restsLengthSeconds = this.restLengthSeconds
), combos = this.comboList.map { it.toDataModel() })

fun WorkoutResult.toDataModel() = WorkoutConclusion(
    workoutId = this.workoutId,
    workoutName = this.workoutName,
    isWorkoutAborted = this.isWorkoutAborted,
    trainingDateEpochDay = this.epochDay
)