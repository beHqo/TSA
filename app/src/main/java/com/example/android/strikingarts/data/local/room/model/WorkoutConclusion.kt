package com.example.android.strikingarts.data.local.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.android.strikingarts.data.local.room.model.WorkoutConclusion.Companion.PARENT_PRIMARY_KEY_REFERENCE
import com.example.android.strikingarts.data.local.room.model.WorkoutConclusion.Companion.WORKOUT_CONCLUSION_TABLE_NAME

@Entity(
    tableName = WORKOUT_CONCLUSION_TABLE_NAME, foreignKeys = [ForeignKey(
        entity = TrainingDate::class,
        parentColumns = [TrainingDate.PRIMARY_KEY_COLUMN_NAME],
        childColumns = [PARENT_PRIMARY_KEY_REFERENCE],
        onDelete = ForeignKey.NO_ACTION
    )]
)
data class WorkoutConclusion(
    @PrimaryKey(autoGenerate = true) val workoutConclusionId: Long = 0,
    @ColumnInfo(WORKOUT_ID_COLUMN_NAME) val workoutId: Long = 0,
    @ColumnInfo(NAME_COLUMN_NAME) val workoutName: String = "",
    @ColumnInfo(IS_WORKOUT_ABORTED_COLUMN_NAME) val isWorkoutAborted: Boolean = false,
    @ColumnInfo(PARENT_PRIMARY_KEY_REFERENCE, index = true) val trainingDateEpochDay: Long = 0L
) {
    companion object {
        const val WORKOUT_CONCLUSION_TABLE_NAME = "workout_conclusion_table"
        const val PRIMARY_KEY_COLUMN_NAME = "workoutConclusionId"
        const val WORKOUT_ID_COLUMN_NAME = "workout_id"
        const val NAME_COLUMN_NAME = "workout_name"
        const val IS_WORKOUT_ABORTED_COLUMN_NAME = "is_workout_aborted"
        const val PARENT_PRIMARY_KEY_REFERENCE = "training_date_epoch_day"
    }
}