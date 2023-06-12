package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.local.room.dao.ComboDao
import com.example.android.strikingarts.data.mapper.toDataModel
import com.example.android.strikingarts.data.mapper.toDomainModel
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.common.logger.DataLogger
import com.example.android.strikingarts.domain.interfaces.ComboCacheRepository
import com.example.android.strikingarts.domain.model.ComboListItem
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ComboRepository"

@Singleton
class ComboRepository @Inject constructor(private val comboDao: ComboDao) : ComboCacheRepository {
    private val logger = DataLogger(TAG)

    override val comboList = comboDao.getComboList().map { list ->
        ImmutableList(list.map { comboWithTechniques -> comboWithTechniques.toDomainModel() })
    }

    override suspend fun getCombo(id: Long): ComboListItem {
        val comboWithTechniques = comboDao.getCombo(id)

        return if (comboWithTechniques == null) {
            logger.logRetrieveOperation(id, "getCombo")
            ComboListItem()
        } else comboWithTechniques.toDomainModel()
    }

    override suspend fun insert(comboListItem: ComboListItem) {
        val id = comboDao.insert(comboListItem.toDataModel().combo)

        logger.logInsertOperation(id, comboListItem)
    }

    override suspend fun update(comboListItem: ComboListItem) {
        val affectedRows = comboDao.update(comboListItem.toDataModel().combo)

        logger.logUpdateOperation(affectedRows, comboListItem.id, comboListItem)
    }

    override suspend fun delete(id: Long) {
        val affectedRows = comboDao.delete(id)

        logger.logDeleteOperation(affectedRows, id)
    }

    override suspend fun deleteAll(idList: List<Long>) {
        val affectedRows = comboDao.deleteAll(idList)

        logger.logDeleteAllOperation(affectedRows, idList)
    }

    override suspend fun insertComboTechniqueCrossRef(
        comboListItem: ComboListItem, techniqueIdList: List<Long>
    ) = comboDao.insertComboWithTechniques(comboListItem.toDataModel().combo, techniqueIdList)

    override suspend fun updateComboTechniqueCrossRef(
        comboListItem: ComboListItem, techniqueIdList: List<Long>
    ) = comboDao.updateComboWithTechniques(comboListItem.toDataModel().combo, techniqueIdList)
}
