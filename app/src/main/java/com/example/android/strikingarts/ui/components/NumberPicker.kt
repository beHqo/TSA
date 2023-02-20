package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.shapes.HexagonShape

@Composable
fun NumberPicker(
    quantity: Int,
    setQuantity: (Int) -> Unit,
    deSelectItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { setQuantity(quantity + 1) }) {
            Icon(Icons.Sharp.KeyboardArrowUp, stringResource(R.string.all_number_picker_increase))
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(LocalViewConfiguration.current.minimumTouchTargetSize)
                .shadow(1.dp, HexagonShape)
        ) { CounterAnimation(quantity = quantity) }

        IconButton(onClick = {
            if (quantity > 1) setQuantity(quantity - 1) else if (quantity == 1) {
                deSelectItem(); setQuantity(0)
            }
        }) {
            Icon(Icons.Sharp.KeyboardArrowDown, stringResource(R.string.all_number_picker_decrease))
        }
    }
}

@Preview
@Composable
fun PreviewNumberPicker() {
    val (quantity, setQuantity) = rememberSaveable { mutableStateOf(0) }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
            NumberPicker(quantity, setQuantity, {}, Modifier.align(Alignment.CenterHorizontally))
            NumberPicker(quantity, setQuantity, {}, Modifier.align(Alignment.CenterHorizontally))
            NumberPicker(quantity, setQuantity, {}, Modifier.align(Alignment.CenterHorizontally))
            NumberPicker(quantity, setQuantity, {}, Modifier.align(Alignment.CenterHorizontally))
            NumberPicker(quantity, setQuantity, {}, Modifier.align(Alignment.CenterHorizontally))
            NumberPicker(quantity, setQuantity, {}, Modifier.align(Alignment.CenterHorizontally))
            NumberPicker(quantity, setQuantity, {}, Modifier.align(Alignment.CenterHorizontally))
        }
    }
}
