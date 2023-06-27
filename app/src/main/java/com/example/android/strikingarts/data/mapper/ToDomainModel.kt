package com.example.android.strikingarts.data.mapper

import com.example.android.strikingarts.R
import com.example.android.strikingarts.data.local.room.model.ComboWithTechniques
import com.example.android.strikingarts.data.local.room.model.DataAssetAudioAttributes
import com.example.android.strikingarts.data.local.room.model.DataAudioAttributes
import com.example.android.strikingarts.data.local.room.model.DataUriAudioAttributes
import com.example.android.strikingarts.data.local.room.model.Technique
import com.example.android.strikingarts.data.local.room.model.WorkoutWithCombos
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.model.AssetAudioAttributes
import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.domain.model.SilenceAudioAttributes
import com.example.android.strikingarts.domain.model.TechniqueCategory
import com.example.android.strikingarts.domain.model.TechniqueListItem
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import com.example.android.strikingarts.domain.model.WorkoutListItem

fun DataAudioAttributes?.toDomainModel(): AudioAttributes = when (this) {
    is DataUriAudioAttributes -> UriAudioAttributes(
        this.name, this.audioString, this.durationMilli, this.sizeByte
    )

    is DataAssetAudioAttributes -> AssetAudioAttributes(
        this.name, this.audioString, this.durationMilli
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
    delay = this.combo.delay,
    techniqueList = ImmutableList(this.techniques.map { it.toDomainModel() })
)

fun WorkoutWithCombos.toDomainModel() = WorkoutListItem(
    id = this.workout.workoutId,
    name = this.workout.name,
    rounds = this.workout.rounds,
    roundLengthMilli = this.workout.roundLengthMilli,
    notificationIntervals = this.workout.notificationIntervals,
    restLengthMilli = this.workout.restsLengthMilli,
    comboList = ImmutableList(this.combos.map { it.toDomainModel() })
)