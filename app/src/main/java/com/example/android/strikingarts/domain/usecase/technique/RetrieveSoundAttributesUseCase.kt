package com.example.android.strikingarts.domain.usecase.technique

import android.net.Uri
import com.example.android.strikingarts.domain.interfaces.SoundAttributesRetriever
import com.example.android.strikingarts.ui.model.SoundAttributes
import javax.inject.Inject

class RetrieveSoundAttributesUseCase @Inject constructor(private val soundAttributesRetriever: SoundAttributesRetriever) {
    operator fun invoke(uri: Uri): SoundAttributes =
        soundAttributesRetriever.getSoundAttributes(uri)
}