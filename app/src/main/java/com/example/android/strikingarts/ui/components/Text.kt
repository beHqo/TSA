package com.example.android.strikingarts.ui.components

import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PrimaryText(
    text: String,
    modifier: Modifier = Modifier,
    textAlpha: Float = 1F,
    color: Color? = null,
    maxLines: Int = 1
) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle1,
        color = color ?: MaterialTheme.colors.onSurface.copy(textAlpha),
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun SecondaryText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
        modifier = modifier
    )
}

@Composable
fun TertiaryText(tertiaryText: String, modifier: Modifier = Modifier) =
    SecondaryText(limitTextByMaxChars(tertiaryText, TEXTFIELD_DESC_MAX_CHARS), modifier)

private fun limitTextByMaxChars(text: String, maxChars: Int): String =
    if (text.length < maxChars) text else (text.substring(0..maxChars) + "...")