package com.example.android.strikingarts.ui.components

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.R
import com.example.android.strikingarts.utils.LockScreenOrientation
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker

@Composable
fun ColorPicker(
    controller: ColorPickerController,
    techniqueColor: String,
    onColorChange: (String) -> Unit,
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    controller.setWheelColor(MaterialTheme.colors.primary)
    controller.setWheelAlpha(0.8F)
    controller.setWheelRadius(16.dp)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
    ) {
        HsvColorPicker(
            modifier = Modifier
                .height(320.dp)
                .padding(bottom = 16.dp),
            controller = controller
        )
        ColorSample(controller, techniqueColor)
        BrightnessSlider(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .height(24.dp),
            controller = controller,
            wheelRadius = 16.dp,
            wheelColor = MaterialTheme.colors.primary,
            borderColor = Color.Transparent
        )
        TextButton(onClick = { onColorChange(controller.selectedColor.value.value.toString()) }) {
            Text(stringResource(R.string.all_save)) }
    }
}

@Composable
fun ColorSample(controller: ColorPickerController?, techniqueColor: String) {
    AlphaTile(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp)),
        controller = controller,
        selectedColor = Color(techniqueColor.toULong())
    )
}