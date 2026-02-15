package com.yumedev.seijakulist.util.navigation_tools

import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.ui.navigation.AppDestinations

val navItems = listOf(
    BottomNavItem(
        name = "Mis Animes",
        selectedIcon = R.drawable.ic_device_tv_old_filled,
        unselectedIcon = R.drawable.ic_device_tv_old,
        route = AppDestinations.MY_ANIMES_ROUTE
    ),
    BottomNavItem(
        name = "Mis Mangas",
        selectedIcon = R.drawable.ic_book_open,
        unselectedIcon = R.drawable.ic_book_open_line,
        route = AppDestinations.MY_MANGAS_ROUTE
    ),
    BottomNavItem(
        name = "Home",
        selectedIcon = R.drawable.ic_home,
        unselectedIcon = R.drawable.ic_home_line,
        route = AppDestinations.HOME
    ),
    BottomNavItem(
        name = "Buscar",
        selectedIcon = R.drawable.ic_search_fill,
        unselectedIcon = R.drawable.ic_search_line,
        route = AppDestinations.SEARCH_ANIME_ROUTE
    ),
    BottomNavItem(
        name = "Perfil",
        selectedIcon = R.drawable.ic_user_solid,
        unselectedIcon = R.drawable.ic_user_outline,
        route = AppDestinations.PROFILE_VIEW_ROUTE
    ),
)