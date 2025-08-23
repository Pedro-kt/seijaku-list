package com.example.seijakulist

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.screens.detail.AnimeDetailScreen
import com.example.seijakulist.ui.screens.profile.ProfileScreen
import com.example.seijakulist.ui.screens.search.SearchScreen
import com.example.seijakulist.ui.theme.SeijakuListTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.seijakulist.ui.screens.characters.CharacterDetailScreen
import com.example.seijakulist.ui.screens.home.HomeScreen
import com.example.seijakulist.ui.screens.local_anime_detail.AnimeDetailScreenLocal
import com.example.seijakulist.ui.screens.my_animes.MyAnimeListScreen
import com.example.seijakulist.ui.screens.my_mangas.MyMangasScreen
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeijakuListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    AppScaffold()
                }
            }
        }
    }
}

data class BottomNavItem(
    val name: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold() {
    val navController = rememberNavController()

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
            unselectedIcon = Icons.AutoMirrored.Filled.MenuBook,
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

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, navItems = navItems)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "SeijakuList")
                },
            )
        },
        containerColor = Color(0xFF121211)
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavigation(navController)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, navItems: List<BottomNavItem>) {
    // Para observar los cambios en la ruta actual de manera segura
    val currentRoute by navController.currentBackStackEntryAsState()
    val route = currentRoute?.destination?.route

    val RobotoBold = FontFamily(Font(R.font.roboto_bold))

    Column {
        HorizontalDivider()
        NavigationBar(
            containerColor = Color(0xFF121211),
            modifier = Modifier.fillMaxWidth()
        ) {
            navItems.forEach { item ->
                val isSelected = route == item.route
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        // ✨ Lógica para evitar la navegación si ya estamos en la pantalla
                        if (route != item.route) {
                            navController.navigate(item.route) {
                                // Elimina las pantallas hasta la pantalla de inicio de la barra de navegación
                                // para evitar una pila de navegación profunda
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true // Guarda el estado de la pantalla
                                }
                                // Evita crear múltiples copias de la misma pantalla
                                launchSingleTop = true
                                // Restaura el estado de la pantalla si ya existe
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.name
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
                        indicatorColor = Color(0xff7226ff)
                    )
                )
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val navRoutes = listOf(
        AppDestinations.MY_ANIMES_ROUTE,
        AppDestinations.MY_MANGAS_ROUTE,
        AppDestinations.HOME,
        AppDestinations.SEARCH_ANIME_ROUTE,
        AppDestinations.MY_PROFILE_ROUTE
    )

    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME,
        modifier = Modifier,
    ) {
        composable(
            route = AppDestinations.HOME,
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            HomeScreen(navController)
        }
        composable(
            route = AppDestinations.SEARCH_ANIME_ROUTE,
            // Transiciones de deslizamiento para el resto de la BottomBar
            enterTransition = {
                val enterIndex = navRoutes.indexOf(this.targetState.destination.route)
                val exitIndex = navRoutes.indexOf(this.initialState.destination.route)
                if (enterIndex > exitIndex) {
                    slideInHorizontally(initialOffsetX = { it })
                } else {
                    slideInHorizontally(initialOffsetX = { -it })
                }
            },
            exitTransition = {
                val enterIndex = navRoutes.indexOf(this.targetState.destination.route)
                val exitIndex = navRoutes.indexOf(this.initialState.destination.route)
                if (enterIndex > exitIndex) {
                    slideOutHorizontally(targetOffsetX = { -it })
                } else {
                    slideOutHorizontally(targetOffsetX = { it })
                }
            }
        ) {
            SearchScreen(navController)
        }
        composable(
            route = AppDestinations.MY_PROFILE_ROUTE,
            enterTransition = {
                val enterIndex = navRoutes.indexOf(this.targetState.destination.route)
                val exitIndex = navRoutes.indexOf(this.initialState.destination.route)
                if (enterIndex > exitIndex) {
                    slideInHorizontally(initialOffsetX = { it })
                } else {
                    slideInHorizontally(initialOffsetX = { -it })
                }
            },
            exitTransition = {
                val enterIndex = navRoutes.indexOf(this.targetState.destination.route)
                val exitIndex = navRoutes.indexOf(this.initialState.destination.route)
                if (enterIndex > exitIndex) {
                    slideOutHorizontally(targetOffsetX = { -it })
                } else {
                    slideOutHorizontally(targetOffsetX = { it })
                }
            }
        ) {
            ProfileScreen(navController)
        }
        composable(
            route = AppDestinations.MY_ANIMES_ROUTE,
            enterTransition = {
                val enterIndex = navRoutes.indexOf(this.targetState.destination.route)
                val exitIndex = navRoutes.indexOf(this.initialState.destination.route)
                if (enterIndex > exitIndex) {
                    slideInHorizontally(initialOffsetX = { it })
                } else {
                    slideInHorizontally(initialOffsetX = { -it })
                }
            },
            exitTransition = {
                val enterIndex = navRoutes.indexOf(this.targetState.destination.route)
                val exitIndex = navRoutes.indexOf(this.initialState.destination.route)
                if (enterIndex > exitIndex) {
                    slideOutHorizontally(targetOffsetX = { -it })
                } else {
                    slideOutHorizontally(targetOffsetX = { it })
                }
            }
        ) {
            MyAnimeListScreen(navController)
        }
        composable(
            route = AppDestinations.MY_MANGAS_ROUTE,
            enterTransition = {
                val enterIndex = navRoutes.indexOf(this.targetState.destination.route)
                val exitIndex = navRoutes.indexOf(this.initialState.destination.route)
                if (enterIndex > exitIndex) {
                    slideInHorizontally(initialOffsetX = { it })
                } else {
                    slideInHorizontally(initialOffsetX = { -it })
                }
            },
            exitTransition = {
                val enterIndex = navRoutes.indexOf(this.targetState.destination.route)
                val exitIndex = navRoutes.indexOf(this.initialState.destination.route)
                if (enterIndex > exitIndex) {
                    slideOutHorizontally(targetOffsetX = { -it })
                } else {
                    slideOutHorizontally(targetOffsetX = { it })
                }
            }
        ) {
            MyMangasScreen(navController)
        }
        composable(
            arguments = listOf(navArgument("animeId") { type = NavType.IntType }),
            route = "${AppDestinations.ANIME_DETAIL_ROUTE}/{${AppDestinations.ANIME_ID_KEY}}",
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt(AppDestinations.ANIME_ID_KEY)
            if (animeId != null) {
                AnimeDetailScreen(navController, animeId = animeId)
            } else {
                Text("Error: anime no encontrado")
            }
        }
        composable(
            arguments = listOf(navArgument("characterId") { type = NavType.IntType }),
            route = "${AppDestinations.CHARACTER_DETAIL_ROUTE}/{${AppDestinations.CHARACTER_ID_KEY}}",
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt(AppDestinations.CHARACTER_ID_KEY)
            if (characterId != null) {
                CharacterDetailScreen(navController, characterId = characterId)
            } else {
                Text("Error: personaje no encontrado")
            }
        }
        composable(
            arguments = listOf(navArgument("animeId") { type = NavType.IntType }),
            route = "${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/{${AppDestinations.ANIME_ID_KEY}}",
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt(AppDestinations.ANIME_ID_KEY)
            if (animeId != null) {
                AnimeDetailScreenLocal(navController, animeId = animeId)
            } else {
                Text("Error: anime no encontrado")
            }
        }
    }
}