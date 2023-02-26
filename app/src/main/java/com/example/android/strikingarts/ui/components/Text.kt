package com.example.android.strikingarts.ui.components

import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun PrimaryText(
    text: String,
    modifier: Modifier = Modifier,
    textAlpha: Float = 1F,
    color: Color? = null,
    maxLines: Int = 1
) = Text(
    text = text,
    style = MaterialTheme.typography.subtitle1,
    color = color ?: MaterialTheme.colors.onSurface.copy(textAlpha),
    maxLines = maxLines,
    overflow = if (maxLines == 1) TextOverflow.Ellipsis else TextOverflow.Clip,
    modifier = modifier
)

@Composable
fun SecondaryText(text: String, modifier: Modifier = Modifier) = Text(
    text = text,
    style = MaterialTheme.typography.caption,
    color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
    overflow = TextOverflow.Ellipsis,
    modifier = modifier
)