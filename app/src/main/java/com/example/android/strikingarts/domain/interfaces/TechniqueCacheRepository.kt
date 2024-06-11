package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.Technique
import kotlinx.coroutines.flow.Flow

interface TechniqueCacheRepository {
    val techniqueList: Flow<ImmutableList<Technique>>

    suspend fun getTechnique(id: Long): Technique
    suspend fun insert(techniqueListItem: Technique, audioAttributesId: Long?)
    suspend fun update(techniqueListItem: Technique, audioAttributesId: Long?)
    suspend fun delete(id: Long)
    suspend fun deleteAll(idList: List<Long>)
}