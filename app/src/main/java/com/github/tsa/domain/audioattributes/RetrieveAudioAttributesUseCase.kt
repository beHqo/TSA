package com.github.tsa.domain.audioattributes

import com.github.tsa.domain.model.AudioAttributes
import javax.inject.Inject

class RetrieveAudioAttributesUseCase @Inject constructor(private val audioAttributesRetriever: AudioAttributesRetriever) {
    operator fun invoke(audioString: String): AudioAttributes =
        audioAttributesRetriever.getAudioAttributes(audioString)
}