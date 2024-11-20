package com.github.tsa.ui.components.detailsitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.tsa.R
import com.github.tsa.ui.components.ColorSample
import com.github.tsa.ui.components.PrimaryText
import com.github.tsa.ui.components.SelectableButton
import com.github.tsa.ui.theme.designsystemmanager.ColorManager
import com.github.tsa.ui.theme.designsystemmanager.ContentAlphaManager
import com.github.tsa.ui.theme.designsystemmanager.PaddingManager

private fun Constraints.hasMinWidth(): Boolean = hasBoundedWidth && minWidth in 1..<maxWidth
private fun Constraints.hasMinHeight(): Boolean = hasBoundedHeight && minHeight in 1..<maxHeight

@Composable
private fun DetailsItemLayout(
    modifier: Modifier = Modifier, startingText: String, endingText: String
) {
    val firstComposable = @Composable { StartingText(startingText) }
    val secondComposable = @Composable { EndingText(endingText) }

    Layout(
        modifier = modifier,
        content = { firstComposable(); secondComposable() }) { measurables, constraints ->
        val placeables =
            measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }

        val minWidth = placeables.maxOf { it.width }
        val sumWidth = placeables.sumOf { it.width }
        val minHeight = placeables.maxOf { it.height }
        val sumHeight = placeables.sumOf { it.height }

        val cantFitHorizontally = sumWidth > constraints.maxWidth

        val height = when {
            (constraints.hasBoundedHeight && constraints.hasFixedHeight) -> constraints.maxHeight
            (constraints.hasBoundedHeight && constraints.hasMinHeight()) -> maxOf(
                constraints.minHeight, minHeight
            )

            cantFitHorizontally -> sumHeight
            else -> minHeight
        }

        val width = when {
            (constraints.hasBoundedWidth && constraints.hasFixedWidth) -> constraints.maxWidth
            (constraints.hasBoundedWidth && constraints.hasMinWidth()) -> maxOf(
                constraints.minWidth, minWidth
            )

            cantFitHorizontally -> minWidth
            else -> sumWidth
        }

        layout(width, height) {
            val first = placeables.first()
            val last = placeables.last()

            val coordinatesSize = coordinates?.size ?: IntSize.Zero
            val coordinatesHeight = coordinatesSize.height
            val yPos: Int

            if (cantFitHorizontally) {
                yPos = (coordinatesHeight - sumHeight) / 2

                first.placeRelative(0, yPos)
                last.placeRelative(0, yPos + first.height)
            } else {
                yPos = (coordinatesHeight - minHeight) / 2

                first.placeRelative(0, yPos)
                last.placeRelative(coordinatesSize.width - last.width, yPos)
            }
        }
    }
}

@Composable
fun DetailsItem(
    modifier: Modifier = Modifier, startText: String, endText: String, onClick: () -> Unit = {}
) = DetailsItemLayout(
    modifier = modifier
        .clickable(onClick = onClick)
        .fillMaxWidth()
        .heightIn(min = 48.dp)
        .padding(vertical = PaddingManager.Medium, horizontal = PaddingManager.Large),
    startingText = startText,
    endingText = endText
)

@Composable
fun DetailsItem(
    modifier: Modifier = Modifier,
    startText: String,
    onClick: () -> Unit,
    endSideSlot: @Composable () -> Unit
) = Box(
    contentAlignment = Alignment.CenterEnd,
    modifier = modifier
        .clickable(onClick = onClick)
        .fillMaxWidth()
        .heightIn(min = 48.dp)
        .padding(vertical = PaddingManager.Medium, horizontal = PaddingManager.Large)
) {
    PrimaryText(
        text = startText,
        textAlpha = ContentAlphaManager.medium,
        maxLines = 1,
        modifier = Modifier.align(Alignment.CenterStart)
    )
    endSideSlot()
}

@Composable
fun DetailsItem(
    modifier: Modifier = Modifier, startText: String, color: Color, onClick: () -> Unit
) = DetailsItem(modifier = modifier, startText = startText, onClick = onClick) {
    if (color == Color.Transparent) PrimaryText(
        stringResource(R.string.all_tap_to_set), color = ColorManager.primary
    ) else ColorSample(color)
}

@Composable
fun DetailsItem(
    modifier: Modifier = Modifier,
    startText: String,
    selected: Boolean,
    onSelectionChange: (Boolean) -> Unit
) = DetailsItem(
    modifier = modifier,
    startText = startText,
    onClick = { onSelectionChange(!selected) }) {
    SelectableButton(selected = selected, onSelectionChange = { onSelectionChange(it) })
}

@Composable
private fun StartingText(text: String) = PrimaryText(
    text = text, textAlpha = ContentAlphaManager.medium, textAlign = TextAlign.Center, maxLines = 1
)

@Composable
private fun EndingText(text: String) = PrimaryText(
    text = text.ifEmpty { stringResource(R.string.all_tap_to_set) },
    textAlign = TextAlign.Center,
    maxLines = 1,
    color = if (text.isEmpty()) ColorManager.primary else null,
)