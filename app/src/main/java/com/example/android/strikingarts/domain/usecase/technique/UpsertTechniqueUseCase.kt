package com.example.android.strikingarts.domain.usecase.technique

import com.example.android.strikingarts.domain.interfaces.TechniqueCacheRepository
import com.example.android.strikingarts.domain.model.Technique
import com.example.android.strikingarts.domain.usecase.audioattributes.UpsertAudioAttributesUseCase
import javax.inject.Inject

class UpsertTechniqueUseCase @Inject constructor(
    private val repository: TechniqueCacheRepository,
    private val upsertAudioAttributesUseCase: UpsertAudioAttributesUseCase
) {
    suspend operator fun invoke(techniqueListItem: Technique) {
        val audioAttributesId = upsertAudioAttributesUseCase(techniqueListItem.audioAttributes)

        if (techniqueListItem.id == 0L) repository.insert(techniqueListItem, audioAttributesId)
        else repository.update(techniqueListItem, audioAttributesId)
    }
}