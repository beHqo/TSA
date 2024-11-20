package com.github.tsa.domain.combo

import com.github.tsa.domain.model.Combo
import kotlinx.coroutines.flow.Flow

interface ComboCacheRepository {
    val comboList: Flow<List<Combo>>

    suspend fun getCombo(id: Long): Combo
    suspend fun insert(combo: Combo, techniqueIdList: List<Long>)
    suspend fun update(combo: Combo, techniqueIdList: List<Long>)
    suspend fun delete(id: Long): Long
    suspend fun deleteAll(idList: List<Long>): Long
}