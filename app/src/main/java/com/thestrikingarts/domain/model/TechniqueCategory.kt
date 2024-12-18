package com.thestrikingarts.domain.model

enum class MovementType { OFFENSE, DEFENSE }

enum class TechniqueType {
    UNSPECIFIED, SPECIAL, PUNCH, KICK, ELBOW, KNEE, HAND_BLOCK, SHIN_BLOCK, HEAD_MOVEMENT, FOOTWORK;

    companion object {
        val offenseTypes = listOf(PUNCH, KICK, ELBOW, KNEE, SPECIAL)
        val defenseTypes = listOf(HAND_BLOCK, SHIN_BLOCK, HEAD_MOVEMENT, FOOTWORK)
    }
}