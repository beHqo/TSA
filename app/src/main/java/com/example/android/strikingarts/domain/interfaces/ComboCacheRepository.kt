package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.Combo
import com.example.android.strikingarts.domain.model.ImmutableList
import kotlinx.coroutines.flow.Flow

interface ComboCacheRepository {
    val comboList: Flow<ImmutableList<Combo>>

    suspend fun getCombo(id: Long): Combo
    suspend fun insert(comboListItem: Combo, techniqueIdList: List<Long>)
    suspend fun update(comboListItem: Combo, techniqueIdList: List<Long>)
    suspend fun delete(id: Long)
    suspend fun deleteAll(idList: List<Long>)
}