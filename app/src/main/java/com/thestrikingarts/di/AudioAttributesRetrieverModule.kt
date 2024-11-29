package com.thestrikingarts.di

import com.thestrikingarts.domain.audioattributes.AudioAttributesRetriever
import com.thestrikingarts.domainandroid.metadataretriever.AudioAttributesRetrieverImpl
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