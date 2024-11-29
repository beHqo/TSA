package com.thestrikingarts.domain.model

data class Workout(
    val id: Long = 0,
    val name: String = "",
    val rounds: Int = 1,
    val roundLengthSeconds: Int = 180,
    val restLengthSeconds: Int = 60,
    val subRounds: Int = 0,
    val comboList: List<Combo> = emptyList()
)
