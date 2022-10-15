package com.example.android.strikingarts.ui.scaffold

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.android.strikingarts.ui.navigation.BottomNavigationItem
import com.example.android.strikingarts.utils.ImmutableList

@Composable
fun BottomNavigationBar(
    bottomNavigationItems: ImmutableList<BottomNavigationItem>,
    currentRoute: String,
    modifier: Modifier = Modifier,
) {
    BottomNavigation(modifier = modifier) {
        bottomNavigationItems.forEach { bottomNavigationItem ->
            BottomNavigationItem(
                selected = currentRoute == bottomNavigationItem.route,
                onClick = bottomNavigationItem.onClick,
                label = { Text(bottomNavigationItem.screenName) },
                icon = { Icon(painterResource(bottomNavigationItem.iconId), null) })
        }
    }
}
