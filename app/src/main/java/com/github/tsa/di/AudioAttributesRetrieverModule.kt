package com.github.tsa.di

import com.github.tsa.domain.audioattributes.AudioAttributesRetriever
import com.github.tsa.domainandroid.metadataretriever.AudioAttributesRetrieverImpl
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