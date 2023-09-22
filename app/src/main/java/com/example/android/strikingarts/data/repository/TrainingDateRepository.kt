package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.local.room.dao.TrainingDateDao
import com.example.android.strikingarts.domain.common.logger.DataLogger
import com.example.android.strikingarts.domain.interfaces.TrainingDateCacheRepository
import com.example.android.strikingarts.domain.mapper.toDomainModel
import com.example.android.strikingarts.domain.mapper.toTrainingDate
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.toImmutableList
import com.example.android.strikingarts.domain.model.toImmutableMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "TrainingDateRepository"

@Singleton
class TrainingDateRepository @Inject constructor(private val trainingDateDao: TrainingDateDao) :
    TrainingDateCacheRepository {
    private val logger = DataLogger(TAG)

    private val monthBounds = MutableStateFlow(Pair(0L, 0L))

    @OptIn(ExperimentalCoroutinesApi::class)
    override val trainingDateByMonthMap = monthBounds.flatMapLatest { monthBounds ->
        trainingDateDao.getTrainingDateByMonth(monthBounds.first, monthBounds.second).map { list ->
            list.associate { it.epochDay to it.workoutIdList.toImmutableList() }.toImmutableMap()
        }
    }

    override fun setMonthBounds(monthBounds: Pair<Long, Long>) {
        this.monthBounds.update { monthBounds }
    }

    suspend fun getTrainingDay(epochDay: Long): Pair<Long, ImmutableList<Long>> {
        val trainingDate = trainingDateDao.getTrainingDate(epochDay)

        return if (trainingDate == null) {
            logger.logRetrieveOperation(epochDay, "getTrainingDay")
            Pair(0L, ImmutableList())
        } else trainingDate.toDomainModel()
    }

    override suspend fun insert(trainingDatePair: Pair<Long, ImmutableList<Long>>) {
        val id = trainingDateDao.insert(trainingDatePair.toTrainingDate())

        logger.logInsertOperation(id, trainingDatePair)
    }

    override suspend fun update(trainingDatePair: Pair<Long, ImmutableList<Long>>) {
        val affectedRows = trainingDateDao.update(trainingDatePair.toTrainingDate())

        logger.logUpdateOperation(affectedRows, trainingDatePair.first, trainingDatePair)
    }

    suspend fun delete(epochDay: Long) {
        val affectedRows = trainingDateDao.delete(epochDay)

        logger.logDeleteOperation(affectedRows, epochDay)
    }

    suspend fun deleteAll(epochDayList: ImmutableList<Long>) {
        val affectedRows = trainingDateDao.deleteAll(epochDayList.list)

        logger.logDeleteAllOperation(affectedRows, epochDayList)
    }
}