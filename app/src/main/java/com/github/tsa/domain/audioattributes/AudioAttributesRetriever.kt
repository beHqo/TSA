package com.github.tsa.domain.audioattributes

import com.github.tsa.domain.model.AudioAttributes

interface AudioAttributesRetriever {
    fun getAudioAttributes(audioString: String): AudioAttributes
}