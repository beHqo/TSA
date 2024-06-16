package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.listOfWorkouts
import com.example.android.strikingarts.domain.interfaces.WorkoutCacheRepository
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.Workout
import com.example.android.strikingarts.domain.model.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWorkoutRepository : WorkoutCacheRepository {
    private var lastAvailableIndex = -1L
    private val data = listOfWorkouts.toMutableList()

    init {
        lastAvailableIndex = (data.maxOfOrNull { it.id } ?: 0) + 1
    }

    override val workoutList: Flow<ImmutableList<Workout>> = flowOf(data.toImmutableList())

    override suspend fun getWorkout(id: Long): Workout =
        data.firstOrNull { it.id == id } ?: Workout()

    override suspend fun insert(workoutListItem: Workout, comboIdList: List<Long>) {
        data += workoutListItem.copy(id = lastAvailableIndex++)
    }

    override suspend fun update(workoutListItem: Workout, comboIdList: List<Long>) {
        val retrieved = data.firstOrNull { it.id == workoutListItem.id } ?: return

        data -= retrieved

        data += workoutListItem
    }

    override suspend fun delete(id: Long) {
        data.removeIf { it.id == id }
    }

    override suspend fun deleteAll(idList: List<Long>) {
        data.removeIf { it.id in idList }
    }

    fun doesDatabaseContainWorkoutWithIdOf(id: Long): Boolean = data.any { it.id == id }

    fun getLastInsertedWorkout(): Workout = data.last()
}