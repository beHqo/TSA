package com.example.android.strikingarts.di

import com.example.android.strikingarts.domain.mediaplayer.EventPlayer
import com.example.android.strikingarts.domainandroid.audioplayers.soundpool.EventPlayerImpl
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