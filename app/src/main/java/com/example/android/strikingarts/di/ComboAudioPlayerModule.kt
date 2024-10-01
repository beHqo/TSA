package com.example.android.strikingarts.di

import com.example.android.strikingarts.domain.mediaplayer.ComboAudioPlayer
import com.example.android.strikingarts.domainandroid.audioplayers.mediaplayer.ComboAudioPlayerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ComboAudioPlayerModule {
    @Binds
    fun bindsComboAudioPlayerImp(comboAudioPlayerIpl: ComboAudioPlayerImpl): ComboAudioPlayer
}