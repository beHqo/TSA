package com.github.tsa.ui.components.detailsitem

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.github.tsa.ui.components.PrimaryText
import com.github.tsa.ui.theme.designsystemmanager.ColorManager
import com.github.tsa.ui.theme.designsystemmanager.PaddingManager
import com.github.tsa.ui.theme.designsystemmanager.SizeManager.InteractableDetailsItemMinHeight
import com.github.tsa.ui.theme.designsystemmanager.TypographyManager
import java.util.Locale

@Composable
fun BaseIntractableDetailsItem(modifier: Modifier, content: @Composable BoxScope.() -> Unit) = Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
        .heightIn(min = InteractableDetailsItemMinHeight)
        .fillMaxWidth()
        .padding(vertical = PaddingManager.Small, horizontal = PaddingManager.Medium),
    content = content
)

@Composable
fun SelectableDetailsItem(
    text: String, selected: Boolean, modifier: Modifier = Modifier, onSelectionChange: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (selected) ColorManager.primary else ColorManager.surface,
        label = "SelectableDetailsItemAnimation"
    )

    BaseIntractableDetailsItem(
        modifier = modifier
            .background(backgroundColor)
            .selectable(selected = selected, onClick = { onSelectionChange() })
    ) {
        PrimaryText(
            text = text,
            color = if (selected) ColorManager.onPrimary else ColorManager.onSurface,
            maxLines = Int.MAX_VALUE,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ClickableDetailsItem(
    text: String, modifier: Modifier = Modifier, onClick: () -> Unit
) = BaseIntractableDetailsItem(modifier = modifier.clickable { onClick() }) {
    Text(
        text = text.uppercase(Locale.getDefault()),
        color = ColorManager.primary,
        style = TypographyManager.labelLarge,
        textAlign = TextAlign.Center
    )
}