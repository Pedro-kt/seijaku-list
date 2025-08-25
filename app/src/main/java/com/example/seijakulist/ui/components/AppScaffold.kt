package com.example.seijakulist.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.seijakulist.AppNavigation
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.util.navigation_tools.bottomNavRoutes
import com.example.seijakulist.util.navigation_tools.navItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(isDarkTheme: Boolean, onThemeToggle: (Boolean) -> Unit) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                BottomNavigationBar(navController = navController, navItems = navItems)
            }
        },
        topBar = {
            if (navController.currentBackStackEntryAsState().value?.destination?.route != AppDestinations.SEARCH_ANIME_ROUTE) {
                TopAppBar(
                    title = {
                        Text(text = "SeijakuList", color = MaterialTheme.colorScheme.onSurface)
                    },
                    actions = {
                        val isDarkTheme = isDarkTheme
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = onThemeToggle,
                            colors = SwitchDefaults.colors(
                                uncheckedTrackColor = MaterialTheme.colorScheme.onSurface,
                                uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                                checkedTrackColor = MaterialTheme.colorScheme.onSurface,
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavigation(navController)
        }
    }
}