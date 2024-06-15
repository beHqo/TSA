package com.example.android.strikingarts.domain.usecase.audioattributes

import com.example.android.strikingarts.domain.interfaces.AudioAttributesRetriever
import com.example.android.strikingarts.domain.model.AudioAttributes
import javax.inject.Inject

class RetrieveAudioAttributesUseCase @Inject constructor(private val audioAttributesRetriever: AudioAttributesRetriever) {
    operator fun invoke(audioString: String): AudioAttributes =
        audioAttributesRetriever.getAudioAttributes(audioString)
}