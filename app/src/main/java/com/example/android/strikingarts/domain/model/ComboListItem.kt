package com.example.android.strikingarts.domain.model

data class ComboListItem(
    val id: Long = 0L,
    val name: String = "",
    val desc: String = "",
    val delayMillis: Long = 3000,
    val techniqueList: ImmutableList<TechniqueListItem> = ImmutableList()
)