package com.example.android.strikingarts.ui.model

import com.example.android.strikingarts.domain.common.ImmutableList

data class ComboPlaylist(
    val delay: Int = 3000, val audioStringList: ImmutableList<String> = ImmutableList()
)
