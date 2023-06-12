package com.example.android.strikingarts.hilt.di

import com.example.android.strikingarts.data.local.resolver.SoundAttributesRetrieverImpl
import com.example.android.strikingarts.domain.interfaces.SoundAttributesRetriever
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
interface SoundAttributesRetrieverModule {

    @Binds
    fun bindsSoundAttributesRetriever(soundAttributesRetrieverImpl: SoundAttributesRetrieverImpl): SoundAttributesRetriever
}