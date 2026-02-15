package com.yumedev.seijakulist.util.navigation_tools

import androidx.annotation.DrawableRes

data class BottomNavItem(
    val name: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val route: String
)