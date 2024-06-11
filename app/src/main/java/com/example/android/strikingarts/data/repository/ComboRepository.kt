package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.local.dao.ComboDao
import com.example.android.strikingarts.domain.common.logger.DataLogger
import com.example.android.strikingarts.domain.interfaces.ComboCacheRepository
import com.example.android.strikingarts.domain.model.Combo
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ComboRepository"

@Singleton
class ComboRepository @Inject constructor(private val comboDao: ComboDao) : ComboCacheRepository {
    private val logger = DataLogger(TAG)

    override val comboList = comboDao.comboList

    override suspend fun getCombo(id: Long): Combo {
        val comboWithTechniques = comboDao.getComboWithTechniques(id)

        return if (comboWithTechniques == null) {
            logger.logRetrieveOperation(id, "getCombo")
            Combo()
        } else comboWithTechniques
    }

    override suspend fun insert(comboListItem: Combo, techniqueIdList: List<Long>) {
        val id = comboDao.insert(comboListItem, techniqueIdList)

        logger.logInsertOperation(id, comboListItem)
    }

    override suspend fun update(comboListItem: Combo, techniqueIdList: List<Long>) {
        val affectedRows = comboDao.update(comboListItem, techniqueIdList)

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
}
