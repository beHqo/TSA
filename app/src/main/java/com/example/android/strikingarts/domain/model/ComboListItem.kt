package com.example.android.strikingarts.domain.model

import com.example.android.strikingarts.domain.common.ImmutableList

data class ComboListItem(
    val id: Long = 0L,
    val name: String = "",
    val desc: String = "",
    val delay: Int = 3,
    val techniqueList: ImmutableList<TechniqueListItem> = ImmutableList()
)