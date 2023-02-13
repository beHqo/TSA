package com.example.android.strikingarts.database.dao

import androidx.room.*
import com.example.android.strikingarts.database.entity.Workout
import com.example.android.strikingarts.database.entity.WorkoutComboCrossRef
import com.example.android.strikingarts.database.entity.WorkoutWithCombos
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertSession(session: Workout): Long

    @Insert
    suspend fun insertSession(vararg session: Workout): List<Long>

    @Update
    suspend fun updateSession(session: Workout)

    @Query("SELECT * FROM workout_table WHERE workoutId = :workoutId")
    suspend fun getWorkout(workoutId: Long): Workout?

    @Query("SELECT * FROM workout_table ORDER BY workoutId ASC")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workout_table ORDER BY workoutId DESC LIMIT 1")
    suspend fun getLastWorkout(): Workout

    @Query("DELETE FROM workout_table")
    suspend fun removeAllSessions()

    @Query("DELETE FROM workout_table WHERE workoutId = :workoutId")
    suspend fun removeSession(workoutId: Long)

    @Transaction
    @Query("SELECT * FROM workout_table WHERE workoutId = :workoutId")
    suspend fun getWorkoutWithCombos(workoutId: Long): WorkoutWithCombos?

    @Transaction
    @Query("SELECT * FROM workout_table")
    fun getWorkoutList(): Flow<List<WorkoutWithCombos>>

    @Transaction
    @Insert
    suspend fun insertWorkoutComboCrossRef(join: WorkoutComboCrossRef): Long
}