package com.example.seijakulist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.screens.myAnimes.MyAnimesScreen
import com.example.seijakulist.ui.screens.profile.ProfileScreen
import com.example.seijakulist.ui.screens.search.SearchScreen
import com.example.seijakulist.ui.theme.SeijakuListTheme
import dagger.hilt.android.AndroidEntryPoint

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
            SearchScreen()
        }
        composable(AppDestinations.MY_PROFILE_ROUTE) {
            ProfileScreen(navController)
        }
    }
}
