package com.thestrikingarts.ui.components.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import com.thestrikingarts.ui.theme.designsystemmanager.ColorManager
import com.thestrikingarts.ui.theme.designsystemmanager.ElevationManager
import com.thestrikingarts.ui.theme.designsystemmanager.PaddingManager
import com.thestrikingarts.ui.theme.designsystemmanager.ShapeManager

@Composable
fun Modifier.highPriorityText() = fillMaxWidth()
    .padding(PaddingManager.Large)
    .shadow(elevation = ElevationManager.Level3, shape = ShapeManager.Medium)
    .clip(ShapeManager.Medium)
    .background(ColorManager.primaryContainer)
    .padding(PaddingManager.Large)