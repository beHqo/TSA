package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.listOfTechniques
import com.example.android.strikingarts.domain.interfaces.TechniqueCacheRepository
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.SilenceAudioAttributes
import com.example.android.strikingarts.domain.model.Technique
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTechniqueRepository : TechniqueCacheRepository {
    private val data = listOfTechniques.toMutableList()

    override val techniqueList: Flow<ImmutableList<Technique>> = flowOf(ImmutableList(data))

    override suspend fun getTechnique(id: Long): Technique =
        data.firstOrNull { it.id == id } ?: Technique()

    override suspend fun insert(techniqueListItem: Technique, audioAttributesId: Long?) {
        data += techniqueListItem.copy(
            audioAttributes = if (audioAttributesId != null) UriAudioAttributes(id = audioAttributesId)
            else SilenceAudioAttributes
        )
    }

    override suspend fun update(techniqueListItem: Technique, audioAttributesId: Long?) {
        val retrieved = data.firstOrNull { it.id == techniqueListItem.id } ?: return

        data -= retrieved

        data += techniqueListItem.copy(
            audioAttributes = if (audioAttributesId != null) UriAudioAttributes(id = audioAttributesId)
            else SilenceAudioAttributes
        )
    }

    override suspend fun delete(id: Long) {
        data.removeIf { it.id == id }
    }

    override suspend fun deleteAll(idList: List<Long>) {
        data.removeIf { it.id in idList }
    }

    fun doesDatabaseContainTechniqueWithIdOf(id: Long): Boolean = data.any { it.id == id }
}