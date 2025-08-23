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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import com.example.seijakulist.R
import com.example.seijakulist.ui.navigation.AppDestinations

data class BottomNavItem(
    val name: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomNavItemScreen(navController: NavController) {

    var isIconSelected by remember { mutableStateOf(false) }

    val navItems = listOf(
        // Define ambos íconos en el objeto
        BottomNavItem(
            name = "Mis Animes",
            selectedIcon = Icons.Filled.Tv,
            unselectedIcon = Icons.Outlined.Tv,
            route = AppDestinations.MY_ANIMES_ROUTE
        ),
        // Define ambos íconos en el objeto
        BottomNavItem(
            name = "Mis Mangas",
            selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
            unselectedIcon = Icons.Default.MenuBook,
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
            containerColor = Color(0xFF121211),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            navItems.forEach { item ->
                val isSelected = currentRoute == item.route
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != item.route) {
                            //Limpiar el historial antes de navegar
                            navController.popBackStack(
                                route = AppDestinations.HOME,
                                inclusive = false
                            )
                            // Navegar al destino deseado
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
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
                        indicatorColor = Color(0xff7226ff),
                    )
                )
            }
        }
    }
}
