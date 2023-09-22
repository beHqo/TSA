package com.example.android.strikingarts.data.local.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "training_day_table", primaryKeys = ["epoch_date"])
data class TrainingDate(
    @ColumnInfo(name = "epoch_date") val epochDay: Long = 0L,
    @ColumnInfo(name = "workout_id_list") val workoutIdList: List<Long> = emptyList()
)