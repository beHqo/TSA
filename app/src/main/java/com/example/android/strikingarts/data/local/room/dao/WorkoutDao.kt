package com.example.android.strikingarts.data.local.room.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.android.strikingarts.data.local.room.model.Workout
import com.example.android.strikingarts.data.local.room.model.WorkoutComboCrossRef
import com.example.android.strikingarts.data.local.room.model.WorkoutWithCombos
import kotlinx.coroutines.flow.Flow

private const val TAG = "WorkoutDao"

@Dao
interface WorkoutDao {
    @Transaction
    @Query("SELECT * FROM workout_table WHERE workoutId = :workoutId")
    suspend fun getWorkoutWithCombos(workoutId: Long): WorkoutWithCombos?

    @Transaction
    @Query("SELECT * FROM workout_table")
    fun getWorkoutList(): Flow<List<WorkoutWithCombos>>

    @Query("SELECT workout_name FROM WORKOUT_TABLE WHERE workoutId in (:idList)")
    suspend fun getWorkoutNames(idList: List<Long>): List<String>

    @Insert
    suspend fun insertWorkoutComboCrossRef(join: WorkoutComboCrossRef): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workout: Workout): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg workout: Workout): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(workoutList: List<Workout>): List<Long>

    @Transaction
    suspend fun insertWorkoutWithCombos(workout: Workout, comboIdList: List<Long>) {
        var count = 0

        val workoutId = insert(workout)

        if (workoutId == -1L) {
            Log.e(
                TAG,
                "insertWorkoutWithCombos: Failed to insert the workout, Stopping all of the remaining operations."
            )

            return
        } else Log.d(TAG, "insertWorkoutWithCombos: Workout was inserted successfully.")

        comboIdList.forEach { comboId ->
            count++

            val refId = insertWorkoutComboCrossRef(WorkoutComboCrossRef(workoutId, comboId))

            if (refId == -1L) Log.e(
                TAG,
                "insertWorkoutWithCombos: Operation failed. workoutId=$workoutId, comboId=$comboId, $count/${comboIdList.size}"
            )
            else Log.d(
                TAG,
                "insertWorkoutWithCombos: Operation succeeded. refId=$refId, $count/${comboIdList.size}"
            )
        }
    }

    @Transaction
    suspend fun updateWorkoutWithCombos(workout: Workout, comboIdList: List<Long>) {
        var count = 0

        val workoutId = workout.workoutId

        val updatedRows = update(workout)

        if (updatedRows == 0) {
            Log.e(
                TAG,
                "updateWorkoutWithCombos: Failed to update the workout, Stopping all of the remaining operations."
            )

            return
        } else Log.d(TAG, "updateWorkoutWithCombos: Workout was updated successfully.")

        val deletedRows = deleteWorkoutComboCrossRef(workoutId)

        Log.d(
            TAG, "updateWorkoutWithCombos: number of deleted cross-ref objects: $deletedRows"
        )

        comboIdList.forEach { comboId ->
            count++

            val refId = insertWorkoutComboCrossRef(WorkoutComboCrossRef(workoutId, comboId))

            if (refId == -1L) Log.e(
                TAG,
                "updateWorkoutWithCombos: Operation failed. workoutId=$workoutId, comboId=$comboId, $count/${comboIdList.size}"
            )
            else Log.d(
                TAG,
                "updateWorkoutWithCombos: Operation succeeded. refId=$refId, $count/${comboIdList.size}"
            )
        }
    }

    @Update
    suspend fun update(workout: Workout): Int

    @Query("SELECT * FROM workout_table WHERE workoutId = :workoutId")
    suspend fun getWorkout(workoutId: Long): Workout?

    @Query("DELETE FROM workout_table WHERE workoutId = :workoutId")
    suspend fun delete(workoutId: Long): Int

    @Query("DELETE FROM workout_table WHERE workoutId IN (:idList)")
    suspend fun deleteAll(idList: List<Long>): Int

    @Query("DELETE FROM workout_combo_ref WHERE workoutId = :workoutId")
    suspend fun deleteWorkoutComboCrossRef(workoutId: Long): Int
}