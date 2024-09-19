package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.listOfWorkouts
import com.example.android.strikingarts.domain.interfaces.WorkoutCacheRepository
import com.example.android.strikingarts.domain.model.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWorkoutRepository : WorkoutCacheRepository {
    private var lastAvailableIndex = -1L
    private val data = listOfWorkouts.toMutableList()

    init {
        lastAvailableIndex = (data.maxOfOrNull { it.id } ?: 0) + 1
    }

    override val workoutList: Flow<List<Workout>> = flowOf(data)

    override suspend fun getWorkout(id: Long): Workout? = data.firstOrNull { it.id == id }

    override suspend fun insert(workout: Workout, comboIdList: List<Long>) {
        data += workout.copy(id = lastAvailableIndex++)
    }

    override suspend fun update(workout: Workout, comboIdList: List<Long>) {
        val retrieved = data.firstOrNull { it.id == workout.id } ?: return

        data -= retrieved

        data += workout
    }

    override suspend fun delete(id: Long): Long = if (data.removeIf { it.id == id }) 1 else 0

    override suspend fun deleteAll(idList: List<Long>): Long {
        val tobeDeleted = data.filter { it.id in idList }

        val idListSize = idList.size
        return if (tobeDeleted.size == idListSize) {
            data.removeAll(tobeDeleted)

            idListSize.toLong()
        } else 0L
    }

    fun doesDatabaseContainWorkoutWithIdOf(id: Long): Boolean = data.any { it.id == id }

    fun getLastInsertedWorkout(): Workout = data.last()
}