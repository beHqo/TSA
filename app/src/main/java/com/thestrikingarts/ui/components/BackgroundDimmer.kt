package com.thestrikingarts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.thestrikingarts.ui.components.modifier.clickableWithNoIndication
import com.thestrikingarts.ui.theme.designsystemmanager.ContentAlphaManager

@Composable
fun BackgroundDimmer(
    modifier: Modifier = Modifier, visible: Boolean, setVisibility: (Boolean) -> Unit
) = FadingAnimatedVisibility(visible = visible) {
    Box(
        modifier
            .fillMaxSize()
            .background(Color.Transparent.copy(ContentAlphaManager.disabled))
            .clickableWithNoIndication { setVisibility(false) })
}