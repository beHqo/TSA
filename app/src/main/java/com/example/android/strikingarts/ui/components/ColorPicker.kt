package com.example.android.strikingarts.ui.components

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ContentAlphaManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.util.LockScreenOrientation
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker

@Composable
fun ColorPicker(colorPickerController: ColorPickerController) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    colorPickerController.setWheelColor(ColorManager.primary)
    colorPickerController.setWheelAlpha(ContentAlphaManager.medium)
    colorPickerController.setWheelRadius(16.dp)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(PaddingManager.Large)
    ) {
        HsvColorPicker(
            modifier = Modifier
                .height(280.dp)
                .padding(bottom = PaddingManager.Large),
            controller = colorPickerController
        )
        ColorSample(colorPickerController, 80.dp)

        BrightnessSlider(
            modifier = Modifier
                .padding(top = PaddingManager.Large)
                .fillMaxWidth()
                .height(24.dp)
                .clip(CutCornerShape(16.dp)),
            controller = colorPickerController,
            wheelRadius = 16.dp,
            wheelColor = ColorManager.primary,
            borderColor = Color.Transparent
        )
    }
}

@Composable
fun ColorSample(colorPickerController: ColorPickerController, size: Dp) {
    AlphaTile(
        modifier = Modifier
            .size(size)
            .clip(CutCornerShape(20.dp)),
        controller = colorPickerController
    )
}

@Composable
fun ColorSample(color: Color) {
    AlphaTile(
        modifier = Modifier
            .size(32.dp)
            .clip(CutCornerShape(8.dp)), selectedColor = color
    )
}