package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.model.TechniqueListItem
import kotlinx.coroutines.flow.Flow

interface TechniqueCacheRepository {
    val techniqueList: Flow<ImmutableList<TechniqueListItem>>

    suspend fun getTechnique(id: Long): TechniqueListItem
    suspend fun insert(techniqueListItem: TechniqueListItem)
    suspend fun update(techniqueListItem: TechniqueListItem)
    suspend fun delete(id: Long)
    suspend fun deleteAll(idList: List<Long>)
}