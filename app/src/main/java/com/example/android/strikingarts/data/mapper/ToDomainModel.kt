package com.example.android.strikingarts.data.mapper

import com.example.android.strikingarts.R
import com.example.android.strikingarts.data.local.room.model.ComboWithTechniques
import com.example.android.strikingarts.data.local.room.model.Technique
import com.example.android.strikingarts.data.local.room.model.WorkoutWithCombos
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.domain.model.TechniqueCategory
import com.example.android.strikingarts.domain.model.TechniqueListItem
import com.example.android.strikingarts.domain.model.WorkoutListItem

fun Technique.toDomainModel() = TechniqueListItem(
    id = this.techniqueId,
    name = this.name,
    num = this.num,
    canBeFaint = this.canBeFaint,
    canBeBodyshot = this.canBeBodyshot,
    sound = this.sound,
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
    roundDurationMilli = this.workout.roundDurationMilli,
    breakpoints = this.workout.breakpoints,
    rest = this.workout.rests,
    restDurationMilli = this.workout.restsDurationMilli,
    comboList = ImmutableList(this.combos.map { it.toDomainModel() })
)