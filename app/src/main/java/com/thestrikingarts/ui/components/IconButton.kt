package com.thestrikingarts.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.thestrikingarts.R
import com.thestrikingarts.ui.theme.designsystemmanager.SizeManager.MoreVertIconSize

@Composable
fun MoreVertIconButton(modifier: Modifier = Modifier, onClick: () -> Unit) = IconButton(
    modifier = modifier.size(MoreVertIconSize), onClick = onClick
) {
    Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
}


@Composable
fun PlayButton(itemName: String, modifier: Modifier = Modifier, onClick: () -> Unit) =
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.Rounded.PlayArrow,
            contentDescription = stringResource(R.string.all_play, itemName)
        )
    }

@Composable
fun DropdownIcon(expanded: Boolean, modifier: Modifier = Modifier) = Icon(
    imageVector = if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
    contentDescription = null,
    modifier = modifier
)


//@Composable
//fun CountingIconButton(
//    imageVector: ImageVector,
//    contentDescription: String,
//    enabled: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    IconButton(enabled = enabled, onClick = onClick, modifier = modifier) {
//        Icon(
//            imageVector = imageVector,
//            contentDescription = contentDescription,
//            modifier = Modifier
//                .size(32.dp)
//                .clip(CutCornerShape(8.dp))
//                .background(MaterialTheme.colors.primarySurface.copy(alpha = ContentAlphaManager.medium))
//        )
//    }
//}