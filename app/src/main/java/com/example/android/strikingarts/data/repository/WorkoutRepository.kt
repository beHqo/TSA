package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.local.dao.WorkoutDao
import com.example.android.strikingarts.domain.logger.DataLogger
import com.example.android.strikingarts.domain.model.Workout
import com.example.android.strikingarts.domain.workout.WorkoutCacheRepository
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "WorkoutRepository"

@Singleton
class WorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao
) : WorkoutCacheRepository {
    private val logger = DataLogger(TAG)

    override val workoutList = workoutDao.workoutList

    override suspend fun getWorkout(id: Long): Workout? {
        val workout = workoutDao.getWorkout(id)

        return workout.also { if (it == null) logger.logRetrieveOperation(id, "getWorkout") }
    }

    override suspend fun insert(workout: Workout, comboIdList: List<Long>) {
        val id = workoutDao.insert(workout, comboIdList)

        logger.logInsertOperation(id, workout)
    }

    override suspend fun update(workout: Workout, comboIdList: List<Long>) {
        val affectedRows = workoutDao.update(workout, comboIdList)

        logger.logUpdateOperation(affectedRows, workout.id, workout)
    }

    override suspend fun delete(id: Long): Long {
        val affectedRows = workoutDao.delete(id)

        logger.logDeleteOperation(affectedRows, id)

        return affectedRows
    }

    override suspend fun deleteAll(idList: List<Long>): Long {
        val affectedRows = workoutDao.deleteAll(idList)

        logger.logDeleteAllOperation(affectedRows, idList)

        return affectedRows
    }
}