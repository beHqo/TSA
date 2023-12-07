package com.example.android.strikingarts.data.local.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.strikingarts.data.local.room.model.WorkoutConclusion.Companion.WORKOUT_CONCLUSION_TABLE_NAME

@Entity(tableName = WORKOUT_CONCLUSION_TABLE_NAME)
data class WorkoutConclusion(
    @PrimaryKey(autoGenerate = true) val workoutConclusionId: Long = 0,
    @ColumnInfo(WORKOUT_ID_COLUMN_NAME) val workoutId: Long = 0,
    @ColumnInfo(NAME_COLUMN_NAME) val workoutName: String = "",
    @ColumnInfo(IS_WORKOUT_ABORTED_COLUMN_NAME) val isWorkoutAborted: Boolean = false,
    @ColumnInfo(
        TRAINING_DATE_EPOCH_DATE_COLUMN_NAME,
        index = true
    ) val trainingDateEpochDay: Long = 0L
) {
    companion object {
        const val WORKOUT_CONCLUSION_TABLE_NAME = "workout_conclusion_table"
        const val WORKOUT_ID_COLUMN_NAME = "workout_id"
        const val NAME_COLUMN_NAME = "workout_name"
        const val IS_WORKOUT_ABORTED_COLUMN_NAME = "is_workout_aborted"
        const val TRAINING_DATE_EPOCH_DATE_COLUMN_NAME = "training_date_epoch_day"
    }
}