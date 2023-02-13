package com.example.android.strikingarts.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ComboTechniqueCrossRef(
    @PrimaryKey(autoGenerate = true) val comboTechniqueId: Long = 0,
    @ColumnInfo(index = true) val comboId: Long,
    @ColumnInfo(index = true) val techniqueId: Long
)

@Entity
data class WorkoutComboCrossRef(
    @PrimaryKey(autoGenerate = true) val workoutComboId: Long = 0,
    @ColumnInfo(index = true) val workoutId: Long,
    @ColumnInfo(index = true) val comboId: Long
)
