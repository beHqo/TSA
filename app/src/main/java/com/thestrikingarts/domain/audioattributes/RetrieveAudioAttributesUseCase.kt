package com.thestrikingarts.domain.audioattributes

import com.thestrikingarts.domain.model.AudioAttributes
import javax.inject.Inject

class RetrieveAudioAttributesUseCase @Inject constructor(private val audioAttributesRetriever: AudioAttributesRetriever) {
    operator fun invoke(audioString: String): AudioAttributes =
        audioAttributesRetriever.getAudioAttributes(audioString)
}