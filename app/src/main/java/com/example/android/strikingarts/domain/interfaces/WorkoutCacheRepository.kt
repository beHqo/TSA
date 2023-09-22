package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.WorkoutListItem
import kotlinx.coroutines.flow.Flow

interface WorkoutCacheRepository {
    val workoutList: Flow<ImmutableList<WorkoutListItem>>

    suspend fun getWorkout(id: Long): WorkoutListItem
    suspend fun getWorkoutNames(idList: List<Long>): ImmutableList<String>
    suspend fun insert(workoutListItem: WorkoutListItem)
    suspend fun update(workoutListItem: WorkoutListItem)
    suspend fun delete(id: Long)
    suspend fun deleteAll(idList: List<Long>)
    suspend fun insertComboTechniqueCrossRef(
        workoutListItem: WorkoutListItem, comboIdList: List<Long>
    )

    suspend fun updateComboTechniqueCrossRef(
        workoutListItem: WorkoutListItem, comboIdList: List<Long>
    )

}