package com.example.android.strikingarts.ui.components.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ElevationManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ShapeManager

fun Modifier.highPriorityText() = composed {
    then(
        fillMaxWidth()
            .padding(PaddingManager.Large)
            .shadow(elevation = ElevationManager.Level3, shape = ShapeManager.Medium)
            .clip(ShapeManager.Medium)
            .background(ColorManager.primaryContainer)
            .padding(PaddingManager.Large)
    )
}