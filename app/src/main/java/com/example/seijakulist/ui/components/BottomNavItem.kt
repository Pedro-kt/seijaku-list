package com.example.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seijakulist.R
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.screens.home.BottomNavItem

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomNavItemScreen(navController: NavController) {

    val navItems = listOf(
        BottomNavItem(
            name = "Mis Animes",
            icon = Icons.Default.Tv,
            route = AppDestinations.MY_ANIMES_ROUTE
        ),
        BottomNavItem(
            name = "Mis Mangas",
            icon = Icons.AutoMirrored.Outlined.MenuBook,
            route = AppDestinations.MY_MANGAS_ROUTE
        ),
        BottomNavItem(name = "Home", icon = Icons.Default.Home, route = AppDestinations.HOME),
        BottomNavItem(
            name = "Buscar",
            icon = Icons.Default.Search,
            route = AppDestinations.SEARCH_ANIME_ROUTE
        ),
        BottomNavItem(
            name = "Perfil",
            icon = Icons.Default.AccountCircle,
            route = AppDestinations.MY_PROFILE_ROUTE
        )
    )
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold)
    )

    Column() {
        HorizontalDivider()

        NavigationBar(
            containerColor = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            navItems.forEach { item ->
                val isSelected = currentRoute == item.route
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name,
                        )
                    },
                    label = {
                        Text(text = item.name, fontFamily = RobotoBold)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f),
                        indicatorColor = Color(0xFF673AB7)
                    )
                )
            }
        }
    }
}
