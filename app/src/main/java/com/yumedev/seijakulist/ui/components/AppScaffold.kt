package com.yumedev.seijakulist.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.navigation.NavController

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yumedev.seijakulist.ui.theme.asp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.screens.my_animes.MyAnimeListViewModel
import com.yumedev.seijakulist.util.navigation_tools.bottomNavRoutes
import com.yumedev.seijakulist.util.navigation_tools.navItems
import com.yumedev.seijakulist.AppNavigation
import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular

enum class SortOrder {
    NONE,
    A_TO_Z,
    Z_TO_A
}

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AppScaffold(
    viewModel: MyAnimeListViewModel = hiltViewModel(),
    settingsViewModel: com.yumedev.seijakulist.ui.screens.configuration.SettingsViewModel
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var isSearching by remember { mutableStateOf(false) }
    val viewMode by settingsViewModel.viewMode.collectAsState()
    var sortOrder by remember { mutableStateOf(SortOrder.NONE) }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var homeScrolled by remember { mutableStateOf(false) }

    // Pantallas fullscreen que deben dibujarse detrás de la status bar (sin Scaffold)
    if (currentRoute == AppDestinations.AUTH_ROUTE ||
        currentRoute == AppDestinations.LOGIN_ROUTE ||
        currentRoute == AppDestinations.REGISTER_ROUTE || currentRoute == AppDestinations.SELECT_TOP5_ROUTE
    ) {
        AppNavigation(
            navController,
            isSearching = isSearching,
            onDismissSearch = { isSearching = false },
            viewMode = viewMode,
            sortOrder = sortOrder,
            settingsViewModel = settingsViewModel,
            onSearchExpandedChange = { isSearchExpanded = it },
            onHomeScrollChanged = { homeScrolled = it }
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
                                    val newMode = when (viewMode) {
                                        ViewMode.LIST -> ViewMode.GRID
                                        ViewMode.GRID -> ViewMode.CARD
                                        ViewMode.CARD -> ViewMode.LIST
                                    }
                                    settingsViewModel.setViewMode(newMode)
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

                    TopAppBar(
                        navigationIcon = { ArrowBackTopAppBar(navController, tint = MaterialTheme.colorScheme.onSurface) },
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
                    var titleVisible by remember { mutableStateOf(false) }

                    // Lista de placeholders que van iterando
                    val searchPlaceholders = remember {
                        listOf(
                            "Buscar anime...",
                            "Buscar manga...",
                            "Buscar personajes...",
                            "Buscar estudios...",
                            "Buscar géneros de anime...",
                            "Buscar géneros de manga...",
                            "Buscar staffs..."
                        )
                    }
                    var currentPlaceholderIndex by remember { mutableStateOf(0) }

                    androidx.compose.runtime.LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(100)
                        titleVisible = true
                    }

                    // Iteración del placeholder cada 3 segundos
                    androidx.compose.runtime.LaunchedEffect(homeScrolled) {
                        if (homeScrolled) {
                            while (true) {
                                kotlinx.coroutines.delay(3000)
                                currentPlaceholderIndex = (currentPlaceholderIndex + 1) % searchPlaceholders.size
                            }
                        }
                    }

                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        title = {
                            AnimatedContent(
                                targetState = homeScrolled,
                                transitionSpec = {
                                    if (targetState) {
                                        // Scrolled: searchbar entra desde abajo
                                        (androidx.compose.animation.slideInVertically(
                                            animationSpec = androidx.compose.animation.core.tween(250),
                                            initialOffsetY = { it }
                                        ) + androidx.compose.animation.fadeIn(
                                            animationSpec = androidx.compose.animation.core.tween(250)
                                        )).togetherWith(
                                            androidx.compose.animation.slideOutVertically(
                                                animationSpec = androidx.compose.animation.core.tween(250),
                                                targetOffsetY = { -it }
                                            ) + androidx.compose.animation.fadeOut(
                                                animationSpec = androidx.compose.animation.core.tween(250)
                                            )
                                        )
                                    } else {
                                        // No scrolled: título entra desde arriba
                                        (androidx.compose.animation.slideInVertically(
                                            animationSpec = androidx.compose.animation.core.tween(250),
                                            initialOffsetY = { -it }
                                        ) + androidx.compose.animation.fadeIn(
                                            animationSpec = androidx.compose.animation.core.tween(250)
                                        )).togetherWith(
                                            androidx.compose.animation.slideOutVertically(
                                                animationSpec = androidx.compose.animation.core.tween(250),
                                                targetOffsetY = { it }
                                            ) + androidx.compose.animation.fadeOut(
                                                animationSpec = androidx.compose.animation.core.tween(250)
                                            )
                                        )
                                    }
                                },
                                label = "home_topbar_content"
                            ) { scrolled ->
                                if (scrolled) {
                                    // Searchbar compacto
                                    androidx.compose.material3.Surface(
                                        onClick = { navController.navigate("${AppDestinations.SEARCH_ANIME_ROUTE}?${AppDestinations.SEARCH_AUTO_EXPAND_KEY}=true") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 16.dp)
                                            .height(40.dp),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                                        color = MaterialTheme.colorScheme.surfaceContainerHigh
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 16.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = "Buscar",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(12.dp))

                                            // Placeholder animado que itera
                                            AnimatedContent(
                                                targetState = searchPlaceholders[currentPlaceholderIndex],
                                                transitionSpec = {
                                                    androidx.compose.animation.slideInVertically(
                                                        animationSpec = androidx.compose.animation.core.tween(300),
                                                        initialOffsetY = { it / 2 }
                                                    ) + androidx.compose.animation.fadeIn(
                                                        animationSpec = androidx.compose.animation.core.tween(300)
                                                    ) togetherWith androidx.compose.animation.slideOutVertically(
                                                        animationSpec = androidx.compose.animation.core.tween(300),
                                                        targetOffsetY = { -it / 2 }
                                                    ) + androidx.compose.animation.fadeOut(
                                                        animationSpec = androidx.compose.animation.core.tween(300)
                                                    )
                                                },
                                                label = "placeholder_animation"
                                            ) { placeholder ->
                                                Text(
                                                    text = placeholder,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    fontSize = 14.sp,
                                                    fontFamily = PoppinsRegular
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    // Título normal
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
                                            fontSize = 26.asp(),
                                            fontStyle = FontStyle.Italic,
                                            modifier = Modifier.padding(end = 4.dp)
                                        )
                                    }
                                }
                            }
                        },
                        actions = {
                            // Los iconos solo se muestran cuando NO está scrolleado
                            AnimatedVisibility(
                                visible = !homeScrolled,
                                enter = androidx.compose.animation.fadeIn(
                                    animationSpec = androidx.compose.animation.core.tween(200)
                                ),
                                exit = androidx.compose.animation.fadeOut(
                                    animationSpec = androidx.compose.animation.core.tween(200)
                                )
                            ) {
                                Row {
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

                AppDestinations.POLICY_PRIVACY_ROUTE -> {
                    TopAppBar(
                        navigationIcon = { ArrowBackTopAppBar(navController) },
                        title = {
                            Text(
                                text = "Política de privacidad",
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
                    when {
                        currentRoute?.startsWith("${AppDestinations.CHARACTER_DETAIL_ROUTE}/") == true -> {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Detalle del Personaje",
                                        fontFamily = PoppinsBold,
                                        fontSize = 20.asp(),
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
                settingsViewModel = settingsViewModel,
                onSearchExpandedChange = { isSearchExpanded = it },
                onHomeScrollChanged = { homeScrolled = it }
            )

            // Bottom Navigation flotante
            AnimatedVisibility(
                visible = (currentRoute in bottomNavRoutes ||
                          currentRoute?.startsWith(AppDestinations.SEARCH_ANIME_ROUTE) == true)
                          && !isSearchExpanded,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                BottomNavigationBar(navController = navController, navItems = navItems)
            }
        }
    }
}