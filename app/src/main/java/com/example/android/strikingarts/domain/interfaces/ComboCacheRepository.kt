package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.Combo
import kotlinx.coroutines.flow.Flow

interface ComboCacheRepository {
    val comboList: Flow<List<Combo>>

    suspend fun getCombo(id: Long): Combo
    suspend fun insert(combo: Combo, techniqueIdList: List<Long>)
    suspend fun update(combo: Combo, techniqueIdList: List<Long>)
    suspend fun delete(id: Long): Long
    suspend fun deleteAll(idList: List<Long>): Long
}