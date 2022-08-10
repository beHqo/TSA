package com.example.android.strikingarts.database.repository

import com.example.android.strikingarts.database.dao.ComboDao
import javax.inject.Inject

class ComboRepository @Inject constructor(private val comboDao: ComboDao) {
    val comboList = comboDao.getComboList()

    suspend fun deleteCombo(id: Long) = comboDao.removeCombo(id)
}