package com.example.seijakulist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.screens.detail.AnimeDetailScreen
import com.example.seijakulist.ui.screens.myAnimes.MyAnimesScreen
import com.example.seijakulist.ui.screens.profile.ProfileScreen
import com.example.seijakulist.ui.screens.search.SearchScreen
import com.example.seijakulist.ui.theme.SeijakuListTheme
import dagger.hilt.android.AndroidEntryPoint
//navegacion entre pantalla searchScreen y detallesAnime (para pasar el parametro del id)
import androidx.navigation.NavType
import androidx.navigation.navArgument

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
        startDestination = AppDestinations.MY_ANIMES_ROUTE,
    ) {
        composable(AppDestinations.MY_ANIMES_ROUTE) {
            MyAnimesScreen(navController)
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
                //manejo de errores para mas adelante

                //Log.e("NavError", "Anime ID is was null when navigating to detail screen.")
                //navController.popBackStack()
                Text("Error: anime no encontrado")
            }
        }
    }
}
