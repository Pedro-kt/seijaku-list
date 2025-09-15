import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArtTrack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.screens.auth_screen.AuthResult
import com.example.seijakulist.ui.screens.auth_screen.AuthViewModel
import com.example.seijakulist.ui.theme.RobotoBold
import com.example.seijakulist.ui.theme.RobotoRegular

data class FeatureCard(val title: String, val description: String, val icon: ImageVector)

val featureCards = listOf(
    FeatureCard(
        "Organiza tu lista",
        "Lleva un registro de los animes que has visto y los que planeas ver.",
        Icons.Default.CheckCircle
    ),
    FeatureCard(
        "Descubre nuevos animes y mangas",
        "Encuentra nuevas series o mangas basados en tus gustos y preferencias.",
        Icons.Default.Search
    ),
    FeatureCard(
        "Añade tus favoritos",
        "Guarda tus animes o mangas preferidos en un solo lugar y compártelos.",
        Icons.Default.Add
    ),
    FeatureCard(
        "Personaliza tu perfil",
        "Personaliza tu perfil con tus personajes, animes, y mangas favoritos.",
        Icons.Default.AccountCircle
    ),
    FeatureCard(
        "Obtén logros y estadísticas",
        "A medida que utilizas la aplicación, obtendrás logros y podrás ver las estadísticas de tu uso.",
        Icons.Default.ArtTrack
    )
)

@Composable
fun AuthScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val backgroundOption = 1
    val backgroundColors = when (backgroundOption) {
        1 -> listOf(Color(0xFF0A192F), Color(0xFF000000))
        2 -> listOf(Color(0xFF4A148C), Color(0xFF1A237E))
        3 -> listOf(Color(0xFF212121), Color(0xFF000000))
        else -> listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
    }

    var isContentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isContentVisible = true
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = backgroundColors
                    )
                )
        )

        AnimatedVisibility(
            visible = isContentVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = "Logo de la aplicación",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Bienvenido a Seijaku List",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = RobotoBold,
                    textAlign = TextAlign.Center,
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Organiza, descubre y sigue tus animes y mangas favoritos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    fontFamily = RobotoRegular,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(1f))

                val pagerState = rememberPagerState(pageCount = { featureCards.size })

                LaunchedEffect(pagerState) {
                    while (true) {
                        delay(5000)
                        val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
                        pagerState.animateScrollToPage(
                            page = nextPage,
                            animationSpec = tween(durationMillis = 1000)
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) { page ->
                    FeatureCardItem(featureCard = featureCards[page])
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Indicadores del Pager con animación
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val size by animateDpAsState(
                            targetValue = if (pagerState.currentPage == iteration) 12.dp else 8.dp,
                            animationSpec = tween(300)
                        )
                        val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(size)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { navController.navigate(AppDestinations.LOGIN_ROUTE) },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Iniciar sesión", fontFamily = RobotoBold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { navController.navigate(AppDestinations.REGISTER_ROUTE) },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text("Registrarse", fontFamily = RobotoBold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(32.dp))

                TextButton(
                    onClick = {
                        navController.navigate(AppDestinations.HOME) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                ) {
                    Text(
                        "Continuar como invitado",
                        fontFamily = RobotoRegular,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun FeatureCardItem(featureCard: FeatureCard) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = featureCard.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = featureCard.title,
                fontFamily = RobotoBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = featureCard.description,
                fontFamily = RobotoRegular,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 3
            )
        }
    }
}