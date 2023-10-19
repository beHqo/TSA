package com.example.android.strikingarts.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.strikingarts.data.local.room.model.WorkoutConclusion
import com.example.android.strikingarts.data.local.room.model.WorkoutConclusion.Companion.IS_WORKOUT_ABORTED_COLUMN_NAME
import com.example.android.strikingarts.data.local.room.model.WorkoutConclusion.Companion.PARENT_PRIMARY_KEY_REFERENCE
import com.example.android.strikingarts.data.local.room.model.WorkoutConclusion.Companion.WORKOUT_CONCLUSION_TABLE_NAME

@Dao
interface WorkoutConclusionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workoutConclusion: WorkoutConclusion): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg workoutConclusion: WorkoutConclusion): List<Long>

    @Query("SELECT * FROM $WORKOUT_CONCLUSION_TABLE_NAME WHERE $IS_WORKOUT_ABORTED_COLUMN_NAME = 0 ORDER BY $PARENT_PRIMARY_KEY_REFERENCE DESC LIMIT 1")
    suspend fun getLastSuccessfulWorkoutConclusion(): WorkoutConclusion?

    @Query("SELECT * FROM $WORKOUT_CONCLUSION_TABLE_NAME WHERE $IS_WORKOUT_ABORTED_COLUMN_NAME = 1 ORDER BY $PARENT_PRIMARY_KEY_REFERENCE DESC LIMIT 1")
    suspend fun getLastFailedWorkoutConclusion(): WorkoutConclusion?
}