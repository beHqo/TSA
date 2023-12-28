package com.example.android.strikingarts.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.TechniqueType

@Composable
fun TechniqueType.getLocalizedName(): String = when (this) {
    TechniqueType.PUNCH -> stringResource(R.string.all_punch)
    TechniqueType.KICK -> stringResource(R.string.all_kick)
    TechniqueType.ELBOW -> stringResource(R.string.all_elbow)
    TechniqueType.KNEE -> stringResource(R.string.all_knee)
    TechniqueType.HAND_BLOCK -> stringResource(R.string.all_hand_block)
    TechniqueType.SHIN_BLOCK -> stringResource(R.string.all_shin_block)
    TechniqueType.HEAD_MOVEMENT -> stringResource(R.string.all_head_movement)
    TechniqueType.FOOTWORK -> stringResource(R.string.all_footwork)
    else -> stringResource(R.string.all_special)
}