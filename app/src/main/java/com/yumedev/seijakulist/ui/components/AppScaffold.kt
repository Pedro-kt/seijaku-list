package com.yumedev.seijakulist.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.ui.res.painterResource
import com.yumedev.seijakulist.R
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.screens.my_animes.MyAnimeListViewModel
import com.yumedev.seijakulist.util.navigation_tools.bottomNavRoutes
import com.yumedev.seijakulist.util.navigation_tools.navItems
import com.yumedev.seijakulist.AppNavigation
import com.yumedev.seijakulist.ui.theme.PoppinsBold

enum class SortOrder {
    NONE,
    A_TO_Z,
    Z_TO_A
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    viewModel: MyAnimeListViewModel = hiltViewModel(),
    settingsViewModel: com.yumedev.seijakulist.ui.screens.configuration.SettingsViewModel
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var isSearching by remember { mutableStateOf(false) }
    var viewMode by remember { mutableStateOf(ViewMode.LIST) }
    var sortOrder by remember { mutableStateOf(SortOrder.NONE) }

    // Pantallas fullscreen que deben dibujarse detrás de la status bar (sin Scaffold)
    if (currentRoute == AppDestinations.SPLASH ||
        currentRoute == AppDestinations.AUTH_ROUTE ||
        currentRoute == AppDestinations.LOGIN_ROUTE ||
        currentRoute == AppDestinations.REGISTER_ROUTE) {
        AppNavigation(
            navController,
            isSearching = isSearching,
            onDismissSearch = { isSearching = false },
            viewMode = viewMode,
            sortOrder = sortOrder,
            settingsViewModel = settingsViewModel
        )
        return
    }

    Scaffold(
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
                            FilterTopAppBar(
                                onSearchClick = { isSearching = true },
                                onViewModeChange = {
                                    viewMode = when (viewMode) {
                                        ViewMode.LIST -> ViewMode.GRID
                                        ViewMode.GRID -> ViewMode.CARD
                                        ViewMode.CARD -> ViewMode.LIST
                                    }
                                },
                                viewMode = viewMode,
                                onSortClick = {
                                    sortOrder = when (sortOrder) {
                                        SortOrder.NONE -> SortOrder.A_TO_Z
                                        SortOrder.A_TO_Z -> SortOrder.Z_TO_A
                                        SortOrder.Z_TO_A -> SortOrder.NONE
                                    }
                                },
                                sortOrder = sortOrder
                            )
                        }
                    )
                }

                AppDestinations.PROFILE_LOADER_ROUTE,
                AppDestinations.PROFILE_VIEW_ROUTE -> {
                    // Obtener el ProfileViewModel del NavBackStackEntry para compartir la misma instancia
                    val backStackEntry = navController.currentBackStackEntry
                    val profileViewModel: com.yumedev.seijakulist.ui.screens.profile.ProfileViewModel =
                        hiltViewModel(backStackEntry!!)
                    val profileUiState by profileViewModel.uiState.collectAsState()

                    // Si no hay perfil, siempre usar background
                    val hasProfile = profileUiState.userProfile != null

                    // Animar el color del TopAppBar basado en el scroll
                    val topAppBarColor by androidx.compose.animation.animateColorAsState(
                        targetValue = if (hasProfile && profileUiState.isAtTop) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.background
                        },
                        animationSpec = androidx.compose.animation.core.tween(200),
                        label = "topAppBarColor"
                    )

                    val topAppBarContentColor by androidx.compose.animation.animateColorAsState(
                        targetValue = if (hasProfile && profileUiState.isAtTop) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        },
                        animationSpec = androidx.compose.animation.core.tween(200),
                        label = "topAppBarContentColor"
                    )

                    TopAppBar(
                        navigationIcon = { ArrowBackTopAppBar(navController, tint = topAppBarContentColor) },
                        title = {
                            Text(
                                text = "Mi perfil",
                                color = topAppBarContentColor
                            )
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate(AppDestinations.CONFIGURATION_ROUTE) }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Configuración",
                                    tint = topAppBarContentColor
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = topAppBarColor
                        )
                    )
                }

                AppDestinations.HOME -> {
                    var titleVisible by remember { mutableStateOf(false) }

                    androidx.compose.runtime.LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(100)
                        titleVisible = true
                    }

                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        title = {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = titleVisible,
                                enter = androidx.compose.animation.fadeIn(
                                    animationSpec = androidx.compose.animation.core.tween(
                                        durationMillis = 800,
                                        easing = androidx.compose.animation.core.FastOutSlowInEasing
                                    )
                                ) + androidx.compose.animation.slideInHorizontally(
                                    initialOffsetX = { -40 },
                                    animationSpec = androidx.compose.animation.core.tween(
                                        durationMillis = 800,
                                        easing = androidx.compose.animation.core.FastOutSlowInEasing
                                    )
                                ) + androidx.compose.animation.scaleIn(
                                    initialScale = 0.9f,
                                    animationSpec = androidx.compose.animation.core.spring(
                                        dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                                        stiffness = androidx.compose.animation.core.Spring.StiffnessLow
                                    )
                                )
                            ) {
                                Text(
                                    text = "Seijaku List",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 26.sp,
                                    fontStyle = FontStyle.Italic,
                                )
                            }
                        },
                        actions = {
                            // Notificaciones
                            IconButton(onClick = {
                                navController.navigate(AppDestinations.NOVEDADES_ROUTE)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notificaciones",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            // Perfil
                            IconButton(onClick = {
                                navController.navigate(AppDestinations.PROFILE_VIEW_ROUTE)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Perfil",
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

                AppDestinations.LOGIN_ROUTE,
                AppDestinations.REGISTER_ROUTE -> {
                    // No TopAppBar: estas pantallas son fullscreen con back button propio
                }

                AppDestinations.NOVEDADES_ROUTE -> {
                    TopAppBar(
                        navigationIcon = { ArrowBackTopAppBar(navController) },
                        title = {
                            Text(
                                text = "Novedades",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )
                }

                else -> {
                    // Detectar rutas con parámetros
                    when {
                        currentRoute?.startsWith("${AppDestinations.CHARACTER_DETAIL_ROUTE}/") == true -> {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Detalle del Personaje",
                                        fontFamily = PoppinsBold,
                                        fontSize = 20.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_arrow_left_line),
                                            contentDescription = "Volver"
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.background,
                                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                                )
                            )
                        }

                        currentRoute?.startsWith(AppDestinations.VIEW_MORE_ROUTE) == true -> {
                            val section = navController.currentBackStackEntry?.arguments?.getString("section")
                            val title = when (section) {
                                "season_now" -> "En emisión"
                                "top_anime" -> "Top puntuación"
                                "season_upcoming" -> "Próxima temporada"
                                else -> "Ver más"
                            }

                            TopAppBar(
                                navigationIcon = { ArrowBackTopAppBar(navController) },
                                title = {
                                    Text(
                                        text = title,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.background
                                )
                            )
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AppNavigation(
                navController,
                isSearching = isSearching,
                onDismissSearch = { isSearching = false },
                viewMode = viewMode,
                sortOrder = sortOrder,
                settingsViewModel = settingsViewModel
            )

            // Bottom Navigation flotante
            AnimatedVisibility(
                visible = currentRoute in bottomNavRoutes,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                BottomNavigationBar(navController = navController, navItems = navItems)
            }
        }
    }
}