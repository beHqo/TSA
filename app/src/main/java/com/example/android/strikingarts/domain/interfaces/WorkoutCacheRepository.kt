package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutCacheRepository {
    val workoutList: Flow<List<Workout>>

    suspend fun getWorkout(id: Long): Workout
    suspend fun insert(workoutListItem: Workout, comboIdList: List<Long>)
    suspend fun update(workoutListItem: Workout, comboIdList: List<Long>)
    suspend fun delete(id: Long): Long
    suspend fun deleteAll(idList: List<Long>): Long
}