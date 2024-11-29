package com.thestrikingarts.di

import com.thestrikingarts.domain.mediaplayer.EventPlayer
import com.thestrikingarts.domainandroid.audioplayers.soundpool.EventPlayerImpl
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