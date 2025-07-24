package com.example.seijakulist.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seijakulist.R
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.screens.home.BottomNavItem
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController
) {

    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    val navItems = listOf(
        BottomNavItem(name = "Mis Animes", icon = Icons.Default.Tv, route = AppDestinations.MY_ANIMES_ROUTE),
        BottomNavItem(name = "Mis Mangas", icon = Icons.AutoMirrored.Outlined.MenuBook, route = AppDestinations.MY_MANGAS_ROUTE),
        BottomNavItem(name = "Home", icon = Icons.Default.Home, route = AppDestinations.HOME),
        BottomNavItem(name = "Buscar", icon = Icons.Default.Search, route = AppDestinations.SEARCH_ANIME_ROUTE),
        BottomNavItem(name = "Perfil", icon = Icons.Default.AccountCircle, route = AppDestinations.MY_PROFILE_ROUTE)
    )

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = Color(0xFF121212))
            ) {
                Text(
                    text = "Perfil",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = RobotoBold,
                    modifier = Modifier.align(Alignment.Center)
                )

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver atras",
                        tint = Color.White
                    )
                }
                IconButton(
                    onClick = {  },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Configuracion",
                        tint = Color.White
                    )
                }
            }
        },
        containerColor = Color(0xFF050505),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF121212),

                modifier = Modifier.navigationBarsPadding().clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
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
                                tint = if (currentRoute == item.route) Color.White else Color.White.copy(alpha = 0.6f)
                            )
                        },
                        label = {
                            Text(
                                text = item.name,
                                color = if (currentRoute == item.route) Color.White else Color.White.copy(alpha = 0.6f),
                                fontFamily = RobotoRegular
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f),
                            indicatorColor = Color(0xFF050505)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = Color(0xFF050505))
        ) {

        }
    }
}
