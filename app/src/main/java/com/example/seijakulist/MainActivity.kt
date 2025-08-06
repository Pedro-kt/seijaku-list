package com.example.seijakulist

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.screens.detail.AnimeDetailScreen
import com.example.seijakulist.ui.screens.profile.ProfileScreen
import com.example.seijakulist.ui.screens.search.SearchScreen
import com.example.seijakulist.ui.theme.SeijakuListTheme
import dagger.hilt.android.AndroidEntryPoint
//navegacion entre pantalla searchScreen y detallesAnime (para pasar el parametro del id)
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.seijakulist.ui.components.WebViewScreen
import com.example.seijakulist.ui.screens.characters.CharacterDetailScreen
import com.example.seijakulist.ui.screens.home.HomeScreen
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
                    color = MaterialTheme.colorScheme.background
                    ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME,
    ) {
        composable(AppDestinations.HOME) {
            HomeScreen(navController)
        }
        composable(AppDestinations.SEARCH_ANIME_ROUTE) {
            SearchScreen(navController = navController)
        }
        composable(AppDestinations.MY_PROFILE_ROUTE) {
            ProfileScreen(navController)
        }
        composable(
            arguments = listOf(navArgument("animeId") {
                type = NavType.IntType
            }
            ),
            route = "${AppDestinations.ANIME_DETAIL_ROUTE}/{${AppDestinations.ANIME_ID_KEY}}"
            ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt(AppDestinations.ANIME_ID_KEY)

            if (animeId != null) {
                AnimeDetailScreen(navController, animeId = animeId)
            } else {
                Text("Error: anime no encontrado")
            }
        }
        composable(
            arguments = listOf(navArgument("characterId") {
                type = NavType.IntType
            }
            ),
            route = "${AppDestinations.CHARACTER_DETAIL_ROUTE}/{${AppDestinations.CHARACTER_ID_KEY}}"
            ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt(AppDestinations.CHARACTER_ID_KEY)

            if (characterId !=null) {
                CharacterDetailScreen(navController, characterId = characterId)
            } else {
                Text("Error: personaje no encontrado")
            }

        }
        composable(AppDestinations.MY_MANGAS_ROUTE) {
            MyMangasScreen(navController)
        }
        composable(AppDestinations.MY_ANIMES_ROUTE) {
            MyAnimeListScreen(navController)
        }
        composable(
            route = "${AppDestinations.WEB_VIEW}/{${AppDestinations.WEB_VIEW_URL}}",
            arguments = listOf(navArgument(AppDestinations.WEB_VIEW_URL) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString(AppDestinations.WEB_VIEW_URL) ?: ""
            WebViewScreen(url = Uri.decode(url), navController) // IMPORTANTE: decodearla para mostrar
        }
    }
}
