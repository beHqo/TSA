package com.example.android.strikingarts.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SingleLineItem(
    primaryText: String,
    onItemClick: () -> Unit,
    onMoreVertClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .heightIn(min = 48.dp)
            .padding(top = 12.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
            .clickable { onItemClick() }
    ) {
        PrimaryText(primaryText, Modifier.weight(1F))
        MoreVertIconButton(modifier = Modifier.size(24.dp), onMoreVertClick)
    }

}

@Composable
fun DoubleLineItem(
    primaryText: String,
    secondaryText: String,
    onItemClick: () -> Unit,
    onMoreVertClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = 64.dp)
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { onItemClick() }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1F)
        ) {
            PrimaryText(primaryText)
            SecondaryText(secondaryText)
        }
        MoreVertIconButton(modifier = Modifier.size(24.dp), onMoreVertClick)
    }
}

@Composable
fun DoubleLineItemWithImage(
    primaryText: String,
    secondaryText: String,
    @DrawableRes image: Int,
    imageContentDescription: String,
    onItemClick: () -> Unit,
    onMoreVertClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = 72.dp)
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { onItemClick() }
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = imageContentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(end = 16.dp)
                .height(56.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1F)
        ) {
            PrimaryText(primaryText)
            SecondaryText(secondaryText)
        }
        MoreVertIconButton(modifier = Modifier.size(24.dp), onMoreVertClick)
    }
}

@Composable
fun TripleLineItem(
    primaryText: String,
    secondaryText: String,
    tertiaryText: String,
    onItemClick: () -> Unit,
    onMoreVertClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = 88.dp)
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .clickable { onItemClick() }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1F)
        ) {
            PrimaryText(primaryText)
            SecondaryText(secondaryText)
            TertiaryText(tertiaryText)
        }
        MoreVertIconButton(modifier = Modifier.size(24.dp), onMoreVertClick)
    }
}

@Composable
private fun PrimaryText(primaryText: String, modifier: Modifier = Modifier) {
    Text(
        text = primaryText,
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.subtitle1,
        maxLines = 1,
        modifier = modifier
    )
}

@Composable
private fun SecondaryText(secondaryText: String, modifier: Modifier = Modifier) {
    Text(
        text = secondaryText,
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5F),
        maxLines = 1,
        modifier = modifier
    )
}

@Composable
private fun TertiaryText(tertiaryText: String, modifier: Modifier = Modifier) =
    SecondaryText(secondaryText = tertiaryText, modifier = modifier)