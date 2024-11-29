package com.thestrikingarts.domain.technique

import com.thestrikingarts.domain.audioattributes.UpsertAudioAttributesUseCase
import com.thestrikingarts.domain.model.Technique
import javax.inject.Inject

class UpsertTechniqueUseCase @Inject constructor(
    private val repository: TechniqueCacheRepository,
    private val upsertAudioAttributesUseCase: UpsertAudioAttributesUseCase
) {
    suspend operator fun invoke(technique: Technique) {
        val audioAttributesId = upsertAudioAttributesUseCase(technique.audioAttributes)

        if (technique.id == 0L) repository.insert(technique, audioAttributesId)
        else repository.update(technique, audioAttributesId)
    }
}