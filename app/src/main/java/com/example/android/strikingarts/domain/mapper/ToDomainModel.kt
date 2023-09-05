package com.example.android.strikingarts.domain.mapper

import com.example.android.strikingarts.R
import com.example.android.strikingarts.data.local.room.model.ComboWithTechniques
import com.example.android.strikingarts.data.local.room.model.DataAssetAudioAttributes
import com.example.android.strikingarts.data.local.room.model.DataAudioAttributes
import com.example.android.strikingarts.data.local.room.model.DataUriAudioAttributes
import com.example.android.strikingarts.data.local.room.model.Technique
import com.example.android.strikingarts.data.local.room.model.WorkoutWithCombos
import com.example.android.strikingarts.domain.model.AssetAudioAttributes
import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.SilenceAudioAttributes
import com.example.android.strikingarts.domain.model.TechniqueCategory
import com.example.android.strikingarts.domain.model.TechniqueListItem
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import com.example.android.strikingarts.domain.model.WorkoutDetails
import com.example.android.strikingarts.domain.model.WorkoutListItem

fun DataAudioAttributes?.toDomainModel(): AudioAttributes = when (this) {
    is DataUriAudioAttributes -> UriAudioAttributes(
        this.name, this.audioString, this.durationMillis, this.sizeByte
    )

    is DataAssetAudioAttributes -> AssetAudioAttributes(
        this.name, this.audioString, this.durationMillis
    )

    else -> SilenceAudioAttributes
}

fun Technique.toDomainModel() = TechniqueListItem(
    id = this.techniqueId,
    name = this.name,
    num = this.num,
    canBeFaint = this.canBeFaint,
    canBeBodyshot = this.canBeBodyshot,
    audioAttributes = this.audioAttributes.toDomainModel(),
    color = this.color,
    techniqueType = this.techniqueType,
    movementType = this.movementType,
    imageRes = if (this.movementType == TechniqueCategory.OFFENSE) TechniqueCategory.offenseTypes[this.techniqueType]?.imageId
        ?: R.drawable.none_color else TechniqueCategory.defenseTypes[this.techniqueType]?.imageId
        ?: R.drawable.none_color
)

fun ComboWithTechniques.toDomainModel() = ComboListItem(
    id = this.combo.comboId,
    name = this.combo.name,
    desc = this.combo.description,
    delayMillis = this.combo.delayMillis,
    techniqueList = ImmutableList(this.techniques.map { it.toDomainModel() })
)

fun WorkoutWithCombos.toDomainModel() = WorkoutListItem(
    id = this.workout.workoutId,
    name = this.workout.name,
    rounds = this.workout.rounds,
    roundLengthSeconds = this.workout.roundLengthSeconds,
    subRounds = this.workout.subRounds,
    restLengthSeconds = this.workout.restsLengthSeconds,
    comboList = ImmutableList(this.combos.map { it.toDomainModel() })
)

fun WorkoutListItem.toWorkoutDetails(): WorkoutDetails = WorkoutDetails(
    rounds = this.rounds,
    roundLengthSeconds = this.roundLengthSeconds,
    restLengthSeconds = this.restLengthSeconds
)