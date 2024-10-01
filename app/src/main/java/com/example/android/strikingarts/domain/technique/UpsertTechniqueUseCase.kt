package com.example.android.strikingarts.domain.technique

import com.example.android.strikingarts.domain.audioattributes.UpsertAudioAttributesUseCase
import com.example.android.strikingarts.domain.model.Technique
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