package com.example.android.strikingarts.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ContentAlphaManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.TypographyManager

@Composable
fun PrimaryText(
    text: String,
    modifier: Modifier = Modifier,
    textAlpha: Float = 1F,
    color: Color? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE
) = Text(
    text = text,
    style = TypographyManager.titleSmall,
    color = color ?: ColorManager.onSurface.copy(textAlpha),
    textAlign = textAlign,
    maxLines = maxLines,
    overflow = if (maxLines == 1) TextOverflow.Ellipsis else TextOverflow.Clip,
    modifier = modifier
)

@Composable
fun PrimaryText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    textAlpha: Float = 1F,
    color: Color? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE
) = Text(
    text = text,
    style = TypographyManager.titleSmall,
    color = color ?: ColorManager.onSurface.copy(textAlpha),
    textAlign = textAlign,
    maxLines = maxLines,
    overflow = if (maxLines == 1) TextOverflow.Ellipsis else TextOverflow.Clip,
    modifier = modifier
)

@Composable
fun SecondaryText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = ColorManager.onSurface.copy(ContentAlphaManager.medium),
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE
) = Text(
    text = text,
    style = TypographyManager.bodySmall,
    color = color,
    overflow = TextOverflow.Ellipsis,
    textAlign = textAlign,
    maxLines = maxLines,
    modifier = modifier
)