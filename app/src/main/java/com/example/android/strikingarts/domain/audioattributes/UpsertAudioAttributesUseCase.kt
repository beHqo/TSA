package com.example.android.strikingarts.domain.audioattributes

import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.SilenceAudioAttributes
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import javax.inject.Inject

class UpsertAudioAttributesUseCase
@Inject constructor(private val repository: AudioAttributesCacheRepository) {
    suspend operator fun invoke(audioAttributes: AudioAttributes): Long? {
        return if (audioAttributes is SilenceAudioAttributes) null
        else {
            val alreadyInserted = repository.getAudioAttributesByPath(audioAttributes.audioString)

            val id =
                if (alreadyInserted == null) repository.insert(audioAttributes) else alreadyInserted.id!!

            if (audioAttributes is UriAudioAttributes) repository.update(audioAttributes.copy(id = id))

            return id
        }
    }
}