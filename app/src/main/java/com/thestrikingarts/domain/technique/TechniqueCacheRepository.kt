package com.thestrikingarts.domain.technique

import com.thestrikingarts.domain.model.Technique
import kotlinx.coroutines.flow.Flow

interface TechniqueCacheRepository {
    val techniqueList: Flow<List<Technique>>

    suspend fun getTechnique(id: Long): Technique
    suspend fun insert(technique: Technique, audioAttributesId: Long?)
    suspend fun update(technique: Technique, audioAttributesId: Long?)
    suspend fun delete(id: Long): Long
    suspend fun deleteAll(idList: List<Long>): Long
}