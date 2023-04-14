package com.example.android.strikingarts.domain.model

import com.example.android.strikingarts.domain.common.ImmutableList

data class WorkoutListItem(
    val id: Long = 0,
    val name: String = "",
    val desc: String  = "",
    val rounds: Int = 0,
    val roundDurationMilli: Long = 0L,
    val breakpoints: Int = 0,
    val rest: Int = 0,
    val restDurationMilli: Long = 0L,
    val comboList: ImmutableList<ComboListItem> = ImmutableList()
)
