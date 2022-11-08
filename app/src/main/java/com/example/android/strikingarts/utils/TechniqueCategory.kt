package com.example.android.strikingarts.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.android.strikingarts.R
import javax.annotation.concurrent.Immutable

@Immutable
data class TechniqueType(@StringRes val nameID: Int, @DrawableRes val imageId: Int)

@Immutable
object TechniqueCategory {

    const val DEFENSE_ID = (R.string.all_defense)
    const val OFFENSE_ID = R.string.all_offense
    const val DEFENSE = "Defense"
    const val OFFENSE = "Offense"

    private const val HAND_BLOCK = "Hand Block"
    private const val SHIN_BLOCK = "Shin Block"
    private const val FOOTWORK = "Footwork"
    private const val HEAD_MOVEMENT = "Head Movement"

    private const val PUNCH = "Punch"
    private const val ELBOW = "Elbow"
    private const val KICK = "Kick"
    private const val KNEE = "Knee"

    private val handBlock = TechniqueType(R.string.all_hand_block, R.drawable.hand_block_color)
    private val shinBlock = TechniqueType(R.string.all_shin_block, R.drawable.shin_block_color)
    private val footwork = TechniqueType(R.string.all_footwork, R.drawable.footwork_color)
    private val headMovement =
        TechniqueType(R.string.all_head_movement, R.drawable.head_movement_color)

    private val punch = TechniqueType(R.string.all_punch, R.drawable.punch_color)
    private val elbow = TechniqueType(R.string.all_elbow, R.drawable.elbow_color)
    private val kick = TechniqueType(R.string.all_kick, R.drawable.kick_color)
    private val knee = TechniqueType(R.string.all_knee, R.drawable.knee_color)

    val defenseTypes = mapOf(
        HAND_BLOCK to handBlock, SHIN_BLOCK to shinBlock,
        FOOTWORK to footwork, HEAD_MOVEMENT to headMovement,
    )

    val offenseTypes = mapOf(PUNCH to punch, ELBOW to elbow, KICK to kick, KNEE to knee)
}