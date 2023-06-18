package com.example.android.strikingarts.ui.mapper

import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.ui.model.ComboPlaylist

fun ComboListItem.toComboPlaylist() =
    ComboPlaylist(this.delay, ImmutableList(this.techniqueList.map {
        it.audioUriString.ifEmpty { it.audioAssetFileName.ifEmpty { "Silence.wav" } }
    }))