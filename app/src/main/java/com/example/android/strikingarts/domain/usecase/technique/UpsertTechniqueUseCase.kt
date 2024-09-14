package com.example.android.strikingarts.domain.usecase.technique

import com.example.android.strikingarts.domain.interfaces.TechniqueCacheRepository
import com.example.android.strikingarts.domain.model.Technique
import com.example.android.strikingarts.domain.usecase.audioattributes.UpsertAudioAttributesUseCase
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