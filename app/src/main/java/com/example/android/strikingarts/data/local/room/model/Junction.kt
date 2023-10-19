package com.example.android.strikingarts.data.local.room.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ComboWithTechniques(
    @Embedded val combo: Combo, @Relation(
        parentColumn = "comboId",
        entityColumn = "techniqueId",
        associateBy = Junction(ComboTechniqueCrossRef::class)
    ) val techniques: List<Technique>
)

data class WorkoutWithCombos(
    @Embedded val workout: Workout, @Relation(
        parentColumn = "workoutId",
        entityColumn = "comboId",
        entity = Combo::class,
        associateBy = Junction(WorkoutComboCrossRef::class)
    ) val combos: List<ComboWithTechniques>
)

data class TrainingDateWithWorkoutConclusions(
    @Embedded val date: TrainingDate, @Relation(
        parentColumn = TrainingDate.PRIMARY_KEY_COLUMN_NAME,
        entityColumn = WorkoutConclusion.PARENT_PRIMARY_KEY_REFERENCE
    ) val workoutConclusions: List<WorkoutConclusion>
)