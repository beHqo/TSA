package com.example.android.strikingarts.hilt.module

import com.example.android.strikingarts.data.local.resolver.AudioAttributesRetrieverImpl
import com.example.android.strikingarts.domain.interfaces.AudioAttributesRetriever
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
interface AudioAttributesRetrieverModule {

    @Binds
    fun bindsAudioAttributesRetriever(audioAttributesRetrieverImpl: AudioAttributesRetrieverImpl): AudioAttributesRetriever
}