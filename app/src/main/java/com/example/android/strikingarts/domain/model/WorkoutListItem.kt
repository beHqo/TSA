package com.example.android.strikingarts.domain.model

import com.example.android.strikingarts.domain.common.ImmutableList

data class WorkoutListItem(
    val id: Long = 0,
    val name: String = "",
    val rounds: String = "",
    val roundLengthMilli: Long = 0L,
    val restLengthMilli: Long = 0L,
    val notificationIntervals: String = "",
    val comboList: ImmutableList<ComboListItem> = ImmutableList()
)
