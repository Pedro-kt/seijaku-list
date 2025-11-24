package com.yumedev.seijakulist.util.navigation_tools

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)