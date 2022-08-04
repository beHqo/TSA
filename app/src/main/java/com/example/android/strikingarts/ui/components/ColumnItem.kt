package com.example.android.strikingarts.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SingleLineItem() {

}

@Composable
fun DoubleLineItem() {

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
        modifier = modifier.clickable { onItemClick() }
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = imageContentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(56.dp)
                .padding(end = 16.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = primaryText,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1
            )
            Text(
                text = secondaryText,
                style = MaterialTheme.typography.caption,
                color = Color.Black.copy(alpha = 0.5f)
            )
        }
        MoreVertIconButton(modifier = Modifier.offset(x = 16.dp), onMoreVertClick)
    }
}

@Composable
fun TripleLineItem() {

}

