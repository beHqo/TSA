package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.local.room.dao.TechniqueDao
import com.example.android.strikingarts.data.mapper.toDataModelModel
import com.example.android.strikingarts.data.mapper.toDomainModel
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.common.logger.DataLogger
import com.example.android.strikingarts.domain.model.TechniqueListItem
import com.example.android.strikingarts.domain.repository.TechniqueCacheRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "TechniqueRepository"

@Singleton
class TechniqueRepository @Inject constructor(private val techniqueDao: TechniqueDao) :
    TechniqueCacheRepository {
    private val logger = DataLogger(TAG)

    override val techniqueList = techniqueDao.getTechniqueList().map { list ->
        ImmutableList(list.map { technique -> technique.toDomainModel() })
    }

    override suspend fun getTechnique(id: Long): TechniqueListItem {
        val technique = techniqueDao.getTechnique(id)

        return if (technique == null) {
            logger.logRetrieveOperation(id, "getTechnique")
            TechniqueListItem()
        } else technique.toDomainModel()
    }

    override suspend fun insert(techniqueListItem: TechniqueListItem) {
        val id = techniqueDao.insert(techniqueListItem.toDataModelModel())

        logger.logInsertOperation(id, techniqueListItem)
    }


    override suspend fun update(techniqueListItem: TechniqueListItem) {
        val affectedRows = techniqueDao.update(techniqueListItem.toDataModelModel())

        logger.logUpdateOperation(affectedRows, techniqueListItem.id, techniqueListItem)
    }

    override suspend fun delete(id: Long) {
        val affectedRows = techniqueDao.delete(id)

        logger.logDeleteOperation(affectedRows, id)
    }

    override suspend fun deleteAll(idList: List<Long>) {
        val affectedRows = techniqueDao.deleteAll(idList)

        logger.logDeleteAllOperation(affectedRows, idList)
    }
}