package com.example.android.strikingarts.database.entity

//import com.example.android.strikingarts.R
//
//enum class MovementType {
//    Offense,
//    Defense,
//    NONE
//}
//
//enum class TechniqueType(val techniqueName: String, val id: Int) {
//    PUNCH("Punch", R.drawable.punch_color),
//    ELBOW("Elbow", R.drawable.elbow_color),
//    KICK("Kick", R.drawable.kick_color),
//    KNEE("Knee", R.drawable.knee_color),
//    NONE("", R.drawable.none_color), // Changed from "None" to "",
//    HAND_BLOCK("Hand Block", R.drawable.hand_block_color),
//    SHIN_BLOCK("Shin Block", R.drawable.shin_block_color),
//    HEAD_MOVEMENT("Head Movement", R.drawable.head_movement_color),
//    FOOTWORK("Footwork", R.drawable.footwork_color);
//}
//
//fun getOffenseTypes(): List<TechniqueType> {
//    return TechniqueType.values().dropLast(5)
//}
//
//fun getDefenseTypes(): List<TechniqueType> {
//    return TechniqueType.values().drop(5)
//}
//
//fun getTechniqueType(name: String) : TechniqueType {
//    TechniqueType.values().forEach { if (it.techniqueName == name) return it }
//
//    throw Exception("Invalid techniqueName")
//}