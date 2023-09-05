package com.example.android.strikingarts.hilt.module

import com.example.android.strikingarts.domain.interfaces.ComboAudioPlayer
import com.example.android.strikingarts.ui.audioplayers.mediaplayer.ComboAudioPlayerImpl
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