package com.example.android.strikingarts.database.repository

import com.example.android.strikingarts.database.dao.ComboDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComboRepository @Inject constructor(private val comboDao: ComboDao) {
    val comboList = comboDao.getComboList()

    suspend fun getCombo(id: Long) = comboDao.getCombo(id)

    suspend fun deleteCombo(id: Long) = comboDao.deleteCombo(id)

    suspend fun deleteCombos(idList: List<Long>) = comboDao.deleteCombos(idList)
}