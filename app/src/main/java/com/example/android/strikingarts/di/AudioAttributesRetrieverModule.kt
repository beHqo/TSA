package com.example.android.strikingarts.di

import com.example.android.strikingarts.domain.audioattributes.AudioAttributesRetriever
import com.example.android.strikingarts.domainandroid.metadataretriever.AudioAttributesRetrieverImpl
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