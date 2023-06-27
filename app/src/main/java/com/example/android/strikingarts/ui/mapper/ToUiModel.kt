package com.example.android.strikingarts.ui.mapper

import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.domain.model.TechniqueListItem
import com.example.android.strikingarts.ui.model.ComboPlaylist
import com.example.android.strikingarts.ui.model.PlayableTechnique

fun ComboListItem.toComboPlaylist(): ComboPlaylist = ComboPlaylist(
    delay = this.delay,
    playableTechniqueList = ImmutableList(this.techniqueList.map { it.toPlayableTechnique() })
)

fun TechniqueListItem.toPlayableTechnique(): PlayableTechnique =
    PlayableTechnique(color = this.color, audioAttributes = this.audioAttributes)