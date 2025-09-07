package com.example.seijakulist.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.seijakulist.AppNavigation
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.screens.my_animes.MyAnimeListViewModel
import com.example.seijakulist.util.navigation_tools.bottomNavRoutes
import com.example.seijakulist.util.navigation_tools.navItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    viewModel: MyAnimeListViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var isSearching by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = currentRoute in bottomNavRoutes,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                BottomNavigationBar(navController = navController, navItems = navItems)
            }
        },
        topBar = {
            when (currentRoute) {
                AppDestinations.MY_ANIMES_ROUTE -> {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Mis animes",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        navigationIcon = { ArrowBackTopAppBar(navController) },
                        actions = {
                            FilterTopAppBar(onSearchClick = { isSearching = true })
                        }
                    )
                }

                AppDestinations.PROFILE_LOADER_ROUTE,
                AppDestinations.PROFILE_VIEW_ROUTE -> {
                    TopAppBar(
                        navigationIcon = { ArrowBackTopAppBar(navController) },
                        title = {
                            Text(
                                text = "Mi perfil",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate(AppDestinations.CONFIGURATION_ROUTE) }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Configuración",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )
                }

                AppDestinations.HOME -> {

                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        title = {
                            Text(
                                text = "SeijakuList",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 24.sp
                            )
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    navController.navigate(AppDestinations.SEARCH_ANIME_ROUTE)
                                },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Configuración",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    )

                }

                AppDestinations.MY_MANGAS_ROUTE -> {
                    TopAppBar(
                        navigationIcon = { ArrowBackTopAppBar(navController) },
                        title = {
                            Text(
                                text = "Mis mangas",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavigation(
                navController,
                isSearching = isSearching,
                onDismissSearch = { isSearching = false })
        }
    }
}