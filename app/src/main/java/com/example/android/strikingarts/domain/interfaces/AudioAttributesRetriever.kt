package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.AudioAttributes

interface AudioAttributesRetriever {
    fun getAudioAttributes(audioString: String): AudioAttributes
}