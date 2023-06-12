package com.example.android.strikingarts.domain.interfaces

import android.net.Uri
import com.example.android.strikingarts.ui.model.SoundAttributes

interface SoundAttributesRetriever {
    fun getSoundAttributes(uri: Uri): SoundAttributes
}