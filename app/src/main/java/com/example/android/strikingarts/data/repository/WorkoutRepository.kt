package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.local.dao.WorkoutDao
import com.example.android.strikingarts.domain.common.logger.DataLogger
import com.example.android.strikingarts.domain.interfaces.WorkoutCacheRepository
import com.example.android.strikingarts.domain.model.Workout
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "WorkoutRepository"

@Singleton
class WorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao
) : WorkoutCacheRepository {
    private val logger = DataLogger(TAG)

    override val workoutList = workoutDao.workoutList

    override suspend fun getWorkout(id: Long): Workout {
        val workoutWithCombos = workoutDao.getWorkoutListItem(id)

        return if (workoutWithCombos == null) {
            logger.logRetrieveOperation(id, "getWorkout")
            Workout()
        } else workoutWithCombos
    }

    override suspend fun insert(workoutListItem: Workout, comboIdList: List<Long>) {
        val id = workoutDao.insert(workoutListItem, comboIdList)

        logger.logInsertOperation(id, workoutListItem)
    }

    override suspend fun update(workoutListItem: Workout, comboIdList: List<Long>) {
        val affectedRows = workoutDao.update(workoutListItem, comboIdList)

        logger.logUpdateOperation(affectedRows, workoutListItem.id, workoutListItem)
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