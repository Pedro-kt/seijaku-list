package com.example.seijakulist.util.navigation_tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tv
import com.example.seijakulist.ui.navigation.AppDestinations

val navItems = listOf(
    BottomNavItem(
        name = "Mis Animes",
        selectedIcon = Icons.Filled.Tv,
        unselectedIcon = Icons.Outlined.Tv,
        route = AppDestinations.MY_ANIMES_ROUTE
    ),
    BottomNavItem(
        name = "Mis Mangas",
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook,
        route = AppDestinations.MY_MANGAS_ROUTE
    ),
    BottomNavItem(
        name = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = AppDestinations.HOME
    ),
    BottomNavItem(
        name = "Buscar",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        route = AppDestinations.SEARCH_ANIME_ROUTE
    ),
    BottomNavItem(
        name = "Perfil",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        route = AppDestinations.PROFILE_VIEW_ROUTE
    ),
)