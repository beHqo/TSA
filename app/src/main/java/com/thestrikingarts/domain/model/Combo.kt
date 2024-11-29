package com.thestrikingarts.domain.model

data class Combo(
    val id: Long = 0L,
    val name: String = "",
    val desc: String = "",
    val delayMillis: Long = 3000,
    val techniqueList: List<Technique> = emptyList()
)