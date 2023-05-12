package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.ui.model.Time
import kotlin.math.abs

@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    label: (Int) -> String = { it.toString() },
    value: Int,
    onValueChange: (Int) -> Unit,
    dividersColor: Color = MaterialTheme.colors.primary,
    range: ImmutableList<Int>,
    textStyle: TextStyle = LocalTextStyle.current,
) = ListItemPicker(
    modifier = modifier,
    label = label,
    value = value,
    onValueChange = onValueChange,
    dividersColor = dividersColor,
    list = ImmutableList(range.toList()),
    textStyle = textStyle
)

@Composable
private fun BaseTimePicker(
    modifier: Modifier = Modifier,
    listPickerName: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    dividersColor: Color = MaterialTheme.colors.primary,
    textStyle: TextStyle = LocalTextStyle.current,
) = Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

    Text(listPickerName, modifier = Modifier.padding(end = 4.dp))

    NumberPicker(
        label = { "${if (abs(it) < 10) "0" else ""}$it" },
        value = value,
        onValueChange = onValueChange,
        dividersColor = dividersColor,
        range = ImmutableList((0..59).toList()),
        textStyle = textStyle
    )
}

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    value: Time,
    onValueChange: (Time) -> Unit,
    dividersColor: Color = MaterialTheme.colors.primary,
    textStyle: TextStyle = LocalTextStyle.current,
) = Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
) {
    BaseTimePicker(
        listPickerName = "Minutes",
        value = value.minutes,
        onValueChange = { onValueChange(value.copy(minutes = it)) },
        dividersColor = dividersColor,
        textStyle = textStyle,
    )

    Text(
        text = ":",
        style = textStyle,
        modifier = Modifier
            .offset(y = 8.dp)
            .padding(horizontal = 12.dp)
    )

    BaseTimePicker(
        listPickerName = "Seconds",
        value = value.seconds,
        onValueChange = { onValueChange(value.copy(seconds = it)) },
        dividersColor = dividersColor,
        textStyle = textStyle,
    )
}