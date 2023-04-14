package com.example.android.strikingarts.data.local.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "combo_technique_ref", foreignKeys = [ForeignKey(
        entity = Combo::class,
        parentColumns = ["comboId"],
        childColumns = ["comboId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Technique::class,
        parentColumns = ["techniqueId"],
        childColumns = ["techniqueId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class ComboTechniqueCrossRef(
    @ColumnInfo("comboId", index = true) val comboId: Long,
    @ColumnInfo("techniqueId", index = true) val techniqueId: Long,
    @PrimaryKey(autoGenerate = true) val comboTechniqueId: Long = 0
)

@Entity(
    tableName = "workout_combo_ref", foreignKeys = [ForeignKey(
        entity = Workout::class,
        parentColumns = ["workoutId"],
        childColumns = ["workoutId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Combo::class,
        parentColumns = ["comboId"],
        childColumns = ["comboId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class WorkoutComboCrossRef(
    @ColumnInfo("workoutId", index = true) val workoutId: Long,
    @ColumnInfo("comboId", index = true) val comboId: Long,
    @PrimaryKey(autoGenerate = true) val workoutComboId: Long = 0
)
