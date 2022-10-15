package com.example.android.strikingarts.ui.navigation

import androidx.annotation.DrawableRes

data class BottomNavigationItem(
    val screenName: String,
    @DrawableRes val iconId: Int,
    val route: String,
    val onClick: () -> Unit,
)