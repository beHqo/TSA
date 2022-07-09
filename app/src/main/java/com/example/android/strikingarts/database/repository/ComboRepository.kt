package com.example.android.strikingarts.database.repository

import com.example.android.strikingarts.database.dao.ComboDao
import javax.inject.Inject

class ComboRepository @Inject constructor(comboDao: ComboDao) {
    val comboList = comboDao.getComboList()
}