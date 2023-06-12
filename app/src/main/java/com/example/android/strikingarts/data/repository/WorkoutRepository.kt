package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.local.room.dao.WorkoutDao
import com.example.android.strikingarts.data.mapper.toDataModel
import com.example.android.strikingarts.data.mapper.toDomainModel
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.common.logger.DataLogger
import com.example.android.strikingarts.domain.interfaces.WorkoutCacheRepository
import com.example.android.strikingarts.domain.model.WorkoutListItem
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "WorkoutRepository"

class WorkoutRepository
@Inject constructor(private val workoutDao: WorkoutDao) : WorkoutCacheRepository {
    private val logger = DataLogger(TAG)

    override val workoutList = workoutDao.getWorkoutList().map { list ->
        ImmutableList(list.map { workoutWithCombos -> workoutWithCombos.toDomainModel() })
    }

    override suspend fun getWorkout(id: Long): WorkoutListItem {
        val workoutWithCombos = workoutDao.getWorkoutWithCombos(id)

        return if (workoutWithCombos == null) {
            logger.logRetrieveOperation(id, "getWorkout")
            WorkoutListItem()
        } else workoutWithCombos.toDomainModel()
    }

    override suspend fun insert(workoutListItem: WorkoutListItem) {
        val id = workoutDao.insert(workoutListItem.toDataModel().workout)

        logger.logInsertOperation(id, workoutListItem)
    }

    override suspend fun update(workoutListItem: WorkoutListItem) {
        val affectedRows = workoutDao.update(workoutListItem.toDataModel().workout)

        logger.logUpdateOperation(affectedRows, workoutListItem.id, workoutListItem)
    }

    override suspend fun delete(id: Long) {
        val affectedRows = workoutDao.delete(id)

        logger.logDeleteOperation(affectedRows, id)
    }

    override suspend fun deleteAll(idList: List<Long>) {
        val affectedRows = workoutDao.deleteAll(idList)

        logger.logDeleteAllOperation(affectedRows, idList)
    }

    override suspend fun insertComboTechniqueCrossRef(
        workoutListItem: WorkoutListItem, comboIdList: List<Long>
    ) {
        workoutDao.insertWorkoutWithCombos(workoutListItem.toDataModel().workout, comboIdList)
    }

    override suspend fun updateComboTechniqueCrossRef(
        workoutListItem: WorkoutListItem, comboIdList: List<Long>
    ) {
        workoutDao.updateWorkoutWithCombos(workoutListItem.toDataModel().workout, comboIdList)
    }
}