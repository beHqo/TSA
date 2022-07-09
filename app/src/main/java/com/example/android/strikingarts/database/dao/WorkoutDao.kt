package com.example.android.strikingarts.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.strikingarts.database.entity.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertSession(session: Workout) // maybe needs work

    @Update
    suspend fun updateSession(session: Workout) // maybe needs work

    @Transaction
    @Query("SELECT * FROM workout_table WHERE workoutId = :workoutId")
    suspend fun getWorkout(workoutId: Long) : Workout?

    @Transaction
    @Query("SELECT * FROM workout_table ORDER BY workoutId ASC")
    fun getAllWorkouts() : Flow<List<Workout>> // Didn't remove suspend keyword to see what happens!

    @Transaction
    @Query("SELECT * FROM workout_table ORDER BY workoutId DESC LIMIT 1")
    suspend fun getLastWorkout() : Workout // Maybe should change to Flow<Workout> also?

    @Query("DELETE FROM workout_table")
    suspend fun removeAllSessions()

    @Query("DELETE FROM workout_table WHERE workoutId = :workoutId")
    suspend fun removeSession(workoutId: Long)

}