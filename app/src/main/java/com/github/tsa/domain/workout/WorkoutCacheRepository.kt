package com.github.tsa.domain.workout

import com.github.tsa.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutCacheRepository {
    val workoutList: Flow<List<Workout>>

    suspend fun getWorkout(id: Long): Workout?
    suspend fun insert(workout: Workout, comboIdList: List<Long>)
    suspend fun update(workout: Workout, comboIdList: List<Long>)
    suspend fun delete(id: Long): Long
    suspend fun deleteAll(idList: List<Long>): Long
}