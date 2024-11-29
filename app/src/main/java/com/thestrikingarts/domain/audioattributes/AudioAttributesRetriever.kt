package com.thestrikingarts.domain.audioattributes

import com.thestrikingarts.domain.model.AudioAttributes

interface AudioAttributesRetriever {
    fun getAudioAttributes(audioString: String): AudioAttributes
}