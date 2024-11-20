package com.github.tsa.domain.technique

import com.github.tsa.domain.audioattributes.UpsertAudioAttributesUseCase
import com.github.tsa.domain.model.Technique
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