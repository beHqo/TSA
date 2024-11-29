package com.thestrikingarts.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.thestrikingarts.R
import com.thestrikingarts.ui.theme.designsystemmanager.BorderStrokeWidthManager
import com.thestrikingarts.ui.theme.designsystemmanager.ColorManager
import com.thestrikingarts.ui.theme.designsystemmanager.PaddingManager
import com.thestrikingarts.ui.theme.designsystemmanager.ShapeManager
import com.thestrikingarts.ui.theme.designsystemmanager.SizeManager.SelectionSize

@Composable
fun NumberPicker(
    quantity: Int, setQuantity: (Int) -> Unit, modifier: Modifier = Modifier
) = Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    IconButton(onClick = { setQuantity(1) }) {
        Icon(Icons.Rounded.KeyboardArrowUp, stringResource(R.string.all_number_picker_increase))
    }

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .size(SelectionSize)
            .border(
                border = BorderStroke(BorderStrokeWidthManager.Level1, ColorManager.onSurface),
                shape = ShapeManager.ExtraSmall
            )
            .padding(PaddingManager.ExtraSmall)
    ) { CounterAnimation(quantity = quantity) }

    IconButton(onClick = { setQuantity(-1) }) {
        Icon(Icons.Rounded.KeyboardArrowDown, stringResource(R.string.all_number_picker_decrease))
    }
}