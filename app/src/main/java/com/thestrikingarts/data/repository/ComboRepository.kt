package com.thestrikingarts.data.repository

import com.thestrikingarts.data.local.dao.ComboDao
import com.thestrikingarts.domain.combo.ComboCacheRepository
import com.thestrikingarts.domain.logger.DataLogger
import com.thestrikingarts.domain.model.Combo
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

    override suspend fun insert(combo: Combo, techniqueIdList: List<Long>) {
        val id = comboDao.insert(combo, techniqueIdList)

        logger.logInsertOperation(id, combo)
    }

    override suspend fun update(combo: Combo, techniqueIdList: List<Long>) {
        val affectedRows = comboDao.update(combo, techniqueIdList)

        logger.logUpdateOperation(affectedRows, combo.id, combo)
    }

    override suspend fun delete(id: Long): Long {
        val affectedRows = comboDao.delete(id)

        logger.logDeleteOperation(affectedRows, id)

        return affectedRows
    }

    override suspend fun deleteAll(idList: List<Long>): Long {
        val affectedRows = comboDao.deleteAll(idList)

        logger.logDeleteAllOperation(affectedRows, idList)

        return affectedRows
    }
}
