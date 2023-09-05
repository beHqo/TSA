package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material.icons.sharp.MoreVert
import androidx.compose.material.icons.sharp.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.R

@Composable
fun MoreVertIconButton(modifier: Modifier = Modifier, onClick: () -> Unit) = IconButton(
    modifier = modifier.size(24.dp), onClick = onClick
) {
    Icon(imageVector = Icons.Sharp.MoreVert, contentDescription = null)
}


@Composable
fun PlayButton(itemName: String, modifier: Modifier = Modifier, onClick: () -> Unit) =
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.Sharp.PlayArrow,
            contentDescription = stringResource(R.string.all_play, itemName)
        )
    }

@Composable
fun DropdownIcon(expanded: Boolean, modifier: Modifier = Modifier) = Icon(
    imageVector = if (expanded) Icons.Sharp.KeyboardArrowUp else Icons.Sharp.KeyboardArrowDown,
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