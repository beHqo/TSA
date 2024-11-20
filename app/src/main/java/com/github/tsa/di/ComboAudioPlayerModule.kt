package com.github.tsa.di

import com.github.tsa.domain.mediaplayer.ComboAudioPlayer
import com.github.tsa.domainandroid.audioplayers.mediaplayer.ComboAudioPlayerImpl
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