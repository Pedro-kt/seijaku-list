package com.example.seijakulist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.fadeOut
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.screens.detail.AnimeDetailScreen
import com.example.seijakulist.ui.screens.profile.ProfileScreen
import com.example.seijakulist.ui.screens.search.SearchScreen
import com.example.seijakulist.ui.theme.SeijakuListTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.seijakulist.ui.screens.characters.CharacterDetailScreen
import com.example.seijakulist.ui.screens.home.HomeScreen
import com.example.seijakulist.ui.screens.local_anime_detail.AnimeDetailScreenLocal
import com.example.seijakulist.ui.screens.my_animes.MyAnimeListScreen
import com.example.seijakulist.ui.screens.my_mangas.MyMangasScreen
import com.example.seijakulist.ui.components.AppScaffold

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }
            SeijakuListTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppScaffold(
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = { isDarkTheme = it }
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME,
    ) {
        composable(
            route = AppDestinations.HOME,
            enterTransition = {
                slideInVertically(animationSpec = tween(700), initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(animationSpec = tween(700), targetOffsetY = { it })
            },
        ) {
            HomeScreen(navController)
        }
        composable(
            enterTransition = {
                slideInVertically(animationSpec = tween(700), initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(animationSpec = tween(700), targetOffsetY = { it })
            },
            route = AppDestinations.SEARCH_ANIME_ROUTE
        ) {
            SearchScreen(navController)
        }
        composable(
            enterTransition = {
                slideInVertically(animationSpec = tween(700), initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(animationSpec = tween(700), targetOffsetY = { it })
            },
            route = AppDestinations.MY_PROFILE_ROUTE
        ) {
            ProfileScreen(navController)
        }
        composable(
            route = AppDestinations.MY_ANIMES_ROUTE,
            enterTransition = {
                slideInVertically(animationSpec = tween(700), initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(animationSpec = tween(700), targetOffsetY = { it })
            },
        ) {
            MyAnimeListScreen(navController)
        }
        composable(
            route = AppDestinations.MY_MANGAS_ROUTE,
            enterTransition = {
                slideInVertically(animationSpec = tween(700), initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(animationSpec = tween(700), targetOffsetY = { it })
            },
        ) {
            MyMangasScreen(navController)
        }
        composable(
            arguments = listOf(navArgument("animeId") { type = NavType.IntType }),
            route = "${AppDestinations.ANIME_DETAIL_ROUTE}/{${AppDestinations.ANIME_ID_KEY}}",
            enterTransition = {
                slideInVertically(animationSpec = tween(700), initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(animationSpec = tween(700), targetOffsetY = { it })
            },
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
            enterTransition = {
                slideInVertically(animationSpec = tween(700), initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(animationSpec = tween(700), targetOffsetY = { it })
            },
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