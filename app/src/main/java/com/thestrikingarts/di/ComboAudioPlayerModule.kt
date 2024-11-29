package com.thestrikingarts.di

import com.thestrikingarts.domain.mediaplayer.ComboAudioPlayer
import com.thestrikingarts.domainandroid.audioplayers.mediaplayer.ComboAudioPlayerImpl
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