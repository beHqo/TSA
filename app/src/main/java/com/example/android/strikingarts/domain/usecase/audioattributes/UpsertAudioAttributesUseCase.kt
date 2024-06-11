package com.example.android.strikingarts.domain.usecase.audioattributes

import com.example.android.strikingarts.domain.interfaces.AudioAttributesCacheRepository
import com.example.android.strikingarts.domain.model.AssetAudioAttributes
import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import javax.inject.Inject

class UpsertAudioAttributesUseCase
@Inject constructor(private val repository: AudioAttributesCacheRepository) {
    suspend operator fun invoke(audioAttributes: AudioAttributes): Long? {
        return when (audioAttributes) {
            is AssetAudioAttributes -> audioAttributes.id
            is UriAudioAttributes -> {
                val id: Long

                val alreadyInserted =
                    repository.getAudioAttributesByPath(audioAttributes.audioString)

                if (alreadyInserted == null) id = repository.insert(audioAttributes)
                else {
                    id = alreadyInserted.id!!
                    repository.update(audioAttributes.copy(id = id))
                }

                return id
            }

            else -> null
        }
    }
}