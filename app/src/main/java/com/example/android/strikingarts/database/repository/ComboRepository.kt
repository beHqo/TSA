package com.example.android.strikingarts.database.repository

import com.example.android.strikingarts.database.dao.ComboDao
import com.example.android.strikingarts.database.entity.Combo
import com.example.android.strikingarts.database.entity.ComboTechniqueCrossRef
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComboRepository @Inject constructor(private val comboDao: ComboDao) {
    val comboList = comboDao.getComboList()

    suspend fun getCombo(id: Long) = comboDao.getCombo(id)

    suspend fun deleteCombo(id: Long) = comboDao.deleteCombo(id)

    suspend fun deleteCombos(idList: List<Long>) = comboDao.deleteCombos(idList)

    suspend fun insertOrUpdateComboWithTechniques(combo: Combo, techniqueIdList: List<Long>) {
        val comboId = if (combo.comboId == 0L) comboDao.insertCombo(combo) else {
            combo.comboId
        }.also { comboDao.updateCombo(combo); comboDao.deleteComboTechniqueCrossRef(it) }

        techniqueIdList.forEach { id ->
            comboDao.insertComboTechniqueCrossRef(
                ComboTechniqueCrossRef(comboId = comboId, techniqueId = id)
            )
        }
    }
}