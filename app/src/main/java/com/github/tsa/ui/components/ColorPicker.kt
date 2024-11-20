package com.github.tsa.ui.components

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.tsa.ui.theme.designsystemmanager.ContentAlphaManager
import com.github.tsa.ui.theme.designsystemmanager.PaddingManager
import com.github.tsa.ui.theme.designsystemmanager.ShapeManager
import com.github.tsa.ui.theme.designsystemmanager.SizeManager.BrightnessSliderHeight
import com.github.tsa.ui.theme.designsystemmanager.SizeManager.ColorPickerSampleSize
import com.github.tsa.ui.theme.designsystemmanager.SizeManager.ColorPickerSize
import com.github.tsa.ui.theme.designsystemmanager.SizeManager.ColorSampleSize
import com.github.tsa.ui.theme.designsystemmanager.SizeManager.WheelRadiosSize
import com.github.tsa.ui.util.LockScreenOrientation

@Composable
fun ColorPicker(colorPickerController: ColorPickerController) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    colorPickerController.wheelColor = Color.Black
    colorPickerController.wheelAlpha = ContentAlphaManager.medium
    colorPickerController.wheelRadius = WheelRadiosSize

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(PaddingManager.Large)
    ) {
        HsvColorPicker(
            modifier = Modifier
                .height(ColorPickerSize)
                .padding(bottom = PaddingManager.Large),
            controller = colorPickerController
        )
        ColorSample(colorPickerController, ColorSampleSize)

        BrightnessSlider(
            modifier = Modifier
                .padding(top = PaddingManager.Large)
                .fillMaxWidth()
                .height(BrightnessSliderHeight)
                .clip(ShapeManager.Small),
            controller = colorPickerController,
            wheelRadius = WheelRadiosSize,
            wheelColor = Color.Black,
            borderColor = Color.Transparent
        )
    }
}

@Composable
fun ColorSample(colorPickerController: ColorPickerController, size: Dp) {
    AlphaTile(
        modifier = Modifier
            .size(size)
            .clip(ShapeManager.Small),
        controller = colorPickerController
    )
}

@Composable
fun ColorSample(color: Color) {
    AlphaTile(
        modifier = Modifier
            .size(ColorPickerSampleSize)
            .clip(ShapeManager.Small), selectedColor = color
    )
}