package com.yumedev.seijakulist

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.fadeOut
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.collectAsState
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.screens.detail.AnimeDetailScreen
import com.yumedev.seijakulist.ui.screens.search.SearchScreen
import com.yumedev.seijakulist.ui.theme.SeijakuListTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.yumedev.seijakulist.ui.screens.auth_screen.AuthScreen
import com.yumedev.seijakulist.ui.screens.auth_screen.LoginScreen
import com.yumedev.seijakulist.ui.screens.auth_screen.RegisterScreen
import com.yumedev.seijakulist.ui.screens.characters.CharacterDetailScreen
import com.yumedev.seijakulist.ui.screens.home.HomeScreen
import com.yumedev.seijakulist.ui.screens.local_anime_detail.AnimeDetailScreenLocal
import com.yumedev.seijakulist.ui.screens.my_animes.MyAnimeListScreen
import com.yumedev.seijakulist.ui.screens.my_mangas.MyMangasScreen
import com.yumedev.seijakulist.ui.components.AppScaffold
import com.yumedev.seijakulist.ui.components.BetaTestDialog
import com.yumedev.seijakulist.ui.screens.configuration.ConfigurationScreen
import com.yumedev.seijakulist.ui.screens.configuration.SettingsViewModel
import com.yumedev.seijakulist.ui.screens.profile.ProfileLoaderScreen
import com.yumedev.seijakulist.ui.screens.profile.ProfileSetupView
import com.yumedev.seijakulist.ui.screens.profile.ProfileView
import com.yumedev.seijakulist.ui.screens.profile.SelectTop5Screen
import com.yumedev.seijakulist.ui.screens.report.ReportErrorScreen
import com.yumedev.seijakulist.ui.screens.splash.SplashScreen
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsViewModel: com.yumedev.seijakulist.ui.screens.configuration.SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        // Instalar splash screen ANTES de super.onCreate()
       // val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Opcional: Controlar cuando se quita el splash nativo
       // var isAppReady = false
        //splashScreen.setKeepOnScreenCondition { !isAppReady }

        setContent {
            val themeMode by settingsViewModel.themeMode.collectAsState()

            android.util.Log.d("MainActivity", "Modo de tema activo: ${themeMode.name}")

            SeijakuListTheme(themeMode) {
                var showBetaDialog by remember { mutableStateOf(false) }

                Surface(modifier = Modifier.fillMaxSize()) {
                    AppScaffold(settingsViewModel = settingsViewModel)
                }

                // Mostrar diálogo de prueba beta
                if (showBetaDialog) {
                    BetaTestDialog(
                        onDismiss = { showBetaDialog = false }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AppNavigation(
    navController: NavHostController,
    isSearching: Boolean,
    onDismissSearch: () -> Unit,
    viewMode: com.yumedev.seijakulist.ui.components.ViewMode = com.yumedev.seijakulist.ui.components.ViewMode.LIST,
    sortOrder: com.yumedev.seijakulist.ui.components.SortOrder = com.yumedev.seijakulist.ui.components.SortOrder.NONE,
    settingsViewModel: com.yumedev.seijakulist.ui.screens.configuration.SettingsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.SPLASH
    ) {
        composable(
            route = AppDestinations.SPLASH
        ) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(AppDestinations.HOME) {
                        popUpTo(AppDestinations.SPLASH) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = AppDestinations.LOGIN_ROUTE,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
            }
        ) {
            LoginScreen(
                onSignInSuccess = {
                    navController.navigate(AppDestinations.PROFILE_SETUP_ROUTE) {
                        popUpTo(AppDestinations.LOGIN_ROUTE) {
                            inclusive = true
                        }
                    }
                },
                navController = navController
            )
        }
        composable(
            route = AppDestinations.REGISTER_ROUTE,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
            }
        ) {
            RegisterScreen(
                onSignInSuccess = {
                    navController.navigate(AppDestinations.PROFILE_SETUP_ROUTE) {
                        popUpTo(AppDestinations.REGISTER_ROUTE) {
                            inclusive = true
                        }
                    }
                },
                navController = navController
            )
        }
        composable(
            AppDestinations.PROFILE_LOADER_ROUTE
        ) {
            ProfileLoaderScreen(navController = navController)
        }
        composable(
            AppDestinations.PROFILE_SETUP_ROUTE
        ) {
            ProfileSetupView(navController = navController)
        }
        composable(
            AppDestinations.PROFILE_VIEW_ROUTE
        ) {
            ProfileView(navController = navController)
        }
        composable(
            AppDestinations.SELECT_TOP5_ROUTE
        ) {
            SelectTop5Screen(navController = navController)
        }
        composable(
            route = AppDestinations.AUTH_ROUTE,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
            }
        ) {
            AuthScreen(
                navController = navController,
                settingsViewModel = settingsViewModel
            )
        }
        composable(
            route = AppDestinations.CONFIGURATION_ROUTE
        ) {
            ConfigurationScreen(
                navController = navController,
                settingsViewModel = settingsViewModel
        )
        }
        composable(
            route = AppDestinations.NOVEDADES_ROUTE
        ) {
            com.yumedev.seijakulist.ui.screens.novedades.NovedadesScreen()
        }
        composable(
            route = AppDestinations.REPORT_ERROR_ROUTE
        ) {
            ReportErrorScreen(navController)
        }
        composable(
            route = AppDestinations.HOME
        ) {
            HomeScreen(navController)
        }
        composable(
            route = AppDestinations.SEARCH_ANIME_ROUTE
        ) {
            SearchScreen(navController)
        }
        composable(
            route = AppDestinations.MY_ANIMES_ROUTE
        ) {
            MyAnimeListScreen(
                navController = navController,
                isSearching = isSearching,
                onDismissSearch = onDismissSearch,
                viewMode = viewMode,
                sortOrder = sortOrder
            )
        }
        composable(
            route = AppDestinations.MY_MANGAS_ROUTE
        ) {
            MyMangasScreen(navController)
        }
        composable(
            arguments = listOf(
                navArgument("animeId") { type = NavType.IntType },
                navArgument("tab") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            ),
            route = "${AppDestinations.ANIME_DETAIL_ROUTE}/{${AppDestinations.ANIME_ID_KEY}}?tab={tab}"
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt(AppDestinations.ANIME_ID_KEY)
            val initialTab = backStackEntry.arguments?.getInt("tab") ?: 0
            if (animeId != null) {
                AnimeDetailScreen(navController, animeId = animeId, initialTab = initialTab)
            } else {
                Text("Error: anime no encontrado")
            }
        }
        composable(
            arguments = listOf(navArgument("characterId") { type = NavType.IntType }),
            route = "${AppDestinations.CHARACTER_DETAIL_ROUTE}/{${AppDestinations.CHARACTER_ID_KEY}}"
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
        composable(
            arguments = listOf(navArgument(AppDestinations.VIEW_MORE_SECTION_KEY) { type = NavType.StringType }),
            route = "${AppDestinations.VIEW_MORE_ROUTE}/{${AppDestinations.VIEW_MORE_SECTION_KEY}}"
        ) { backStackEntry ->
            val section = backStackEntry.arguments?.getString(AppDestinations.VIEW_MORE_SECTION_KEY)
            if (section != null) {
                com.yumedev.seijakulist.ui.screens.viewmore.ViewMoreScreen(
                    navController = navController,
                    section = section,
                    filter = null
                )
            } else {
                Text("Error: sección no encontrada")
            }
        }
    }
}

@Composable
fun WelcomeScreen(onFinish: () -> Unit) {
    var logoVisible by remember { mutableStateOf(false) }
    var titleVisible by remember { mutableStateOf(false) }
    var subtitleVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        logoVisible = true
        titleVisible = true
        subtitleVisible = true
        delay(5000)
        onFinish()
    }

    // Animación de pulso para el logo
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Animación de rotación sutil para el brillo
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4A0000), // Rojo oscuro
                        Color(0xFF3B003B), // Púrpura oscuro
                        Color(0xFF1A001A)  // Negro purpúreo
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Círculos decorativos de fondo
        Box(
            modifier = Modifier
                .size(400.dp)
                .graphicsLayer {
                    rotationZ = rotation
                    alpha = 0.15f
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFE91E63), // Rosa
                            Color.Transparent
                        ),
                        radius = 600f
                    ),
                    shape = CircleShape
                )
        )

        // Círculo decorativo secundario
        Box(
            modifier = Modifier
                .offset(x = (-80).dp, y = (-120).dp)
                .size(200.dp)
                .graphicsLayer { alpha = 0.1f }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF9C27B0), // Púrpura
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            // Logo con animación y efectos
            AnimatedVisibility(
                visible = logoVisible,
                enter = fadeIn(tween(800)) + scaleIn(
                    initialScale = 0.5f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // Glow effect detrás del logo
                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .graphicsLayer {
                                scaleX = pulse
                                scaleY = pulse
                                alpha = 0.3f
                            }
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFFE91E63).copy(alpha = 0.8f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    // Logo principal
                    Surface(
                        modifier = Modifier
                            .size(160.dp)
                            .graphicsLayer {
                                scaleX = pulse * 0.98f
                                scaleY = pulse * 0.98f
                            },
                        shape = RoundedCornerShape(36.dp),
                        color = Color.White,
                        shadowElevation = 24.dp,
                        tonalElevation = 8.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.logo),
                                contentDescription = "Logo SeijakuList",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(32.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Título con animación
            AnimatedVisibility(
                visible = titleVisible,
                enter = fadeIn(tween(700)) + slideInVertically(
                    initialOffsetY = { 40 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "SeijakuList",
                        fontFamily = PoppinsBold,
                        fontSize = 42.sp,
                        color = Color.White,
                        letterSpacing = 2.5.sp,
                        style = MaterialTheme.typography.displayMedium.copy(
                            shadow = Shadow(
                                color = Color(0xFFE91E63).copy(alpha = 0.5f),
                                offset = Offset(0f, 4f),
                                blurRadius = 12f
                            )
                        )
                    )

                    // Línea decorativa debajo del título
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(3.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent, // Extremo transparente
                                        Color(0xFFF44336), // Rojo
                                        Color(0xFF9C27B0), // Púrpura
                                        Color.Transparent
                                    )
                                ),
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }

            // Subtítulo con animación
            AnimatedVisibility(
                visible = subtitleVisible,
                enter = fadeIn(tween(600)) + slideInVertically(
                    initialOffsetY = { 30 },
                    animationSpec = tween(600, easing = FastOutSlowInEasing)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Tu colección de anime y manga",
                        fontFamily = PoppinsRegular,
                        fontSize = 15.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        letterSpacing = 0.8.sp,
                        textAlign = TextAlign.Center
                    )

                    // Loading indicator moderno
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(3) { index ->
                            val animatedAlpha by infiniteTransition.animateFloat(
                                initialValue = 0.3f,
                                targetValue = 1f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(
                                        durationMillis = 800,
                                        delayMillis = index * 200,
                                        easing = FastOutSlowInEasing
                                    ),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "dot_$index"
                            )

                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .graphicsLayer { alpha = animatedAlpha }
                                    .background(
                                        color = Color.White,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            }
        }

        // Versión info en la parte inferior
        AnimatedVisibility(
            visible = subtitleVisible,
            enter = fadeIn(tween(800, delayMillis = 400)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            Text(
                text = "v0.1.6 alpha",
                fontFamily = PoppinsRegular,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.5f),
                letterSpacing = 1.sp
            )
        }
    }
}