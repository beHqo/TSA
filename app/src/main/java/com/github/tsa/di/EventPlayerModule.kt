package com.github.tsa.di

import com.github.tsa.domain.mediaplayer.EventPlayer
import com.github.tsa.domainandroid.audioplayers.soundpool.EventPlayerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface EventPlayerModule {
    @Binds
    fun bindsEventPlayerImpl(eventPlayerImpl: EventPlayerImpl): EventPlayer
}