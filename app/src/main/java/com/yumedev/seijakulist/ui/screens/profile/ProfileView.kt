package com.yumedev.seijakulist.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.RobotoBold
import com.yumedev.seijakulist.ui.theme.RobotoRegular

// Datos de prueba
val favoriteAnimes = listOf("Shingeki no Kyojin", "Steins;Gate", "Fullmetal Alchemist: Brotherhood", "Hunter x Hunter", "Vinland Saga")
val favoriteCharacters = listOf("Levi Ackerman", "Rintarou Okabe", "Edward Elric", "Killua Zoldyck", "Askeladd")
val achievements = listOf(
    Achievement("Pionero", "Te uniste en el primer mes de SeijakuList", "Legendary"),
    Achievement("Analista", "Escribiste tu primera reseña", "Normal"),
    Achievement("Crítico", "Escribiste 10 reseñas", "Rare"),
    Achievement("Veterano", "Llevas 1 año en SeijakuList", "Epic"),
    Achievement("Coleccionista Principiante", "Añadiste 10 animes a tu lista", "Normal"),
    Achievement("Maratonista", "Marcaste 50 episodios como vistos en una semana", "Epic"),
    Achievement("Coleccionista Avanzado", "Añadiste 50 animes a tu lista", "Rare"),
    Achievement("Coleccionista Experto", "Añadiste 100 animes a tu lista", "Legendary"),
    Achievement("Tomamos un Té luego de las clases?", "Añadiste el anime favorito del desarrollador", "Legendary"),
    Achievement("Maratonista Avanzado", "Marcaste 100 episodios como vistos en una semana", "Legendary"),
    Achievement("Coleccionista Master", "Añadiste 200 animes a tu lista", "Legendary")
)

@Composable
fun ProfileView(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by profileViewModel.uiState.collectAsState()

    // 1. Muestra una carga si el perfil aún es nulo.
    if (uiState.userProfile == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Icono de perfil",
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Tu perfil te espera",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Inicia sesión o regístrate para ver tu perfil, tus listas y logros.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { navController.navigate(AppDestinations.AUTH_ROUTE) },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Iniciar sesión o Registrarse")
            }
        }
    } else if (uiState.isLoading) {
        CircularProgressIndicator()
    }

    // 2. Accede a los datos del perfil directamente del uiState.
    val userProfile = uiState.userProfile
    val username = userProfile?.username ?: "Nombre de usuario"
    val profilePictureUrl = userProfile?.profilePictureUrl
    val userBio = "¡Hola! Estoy usando SeijakuList para seguir mis animes y mangas favoritos."

    if (userProfile != null) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header con imagen de fondo y perfil
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                ) {
                    // Imagen de fondo con blur
                    AsyncImage(
                        model = profilePictureUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(25.dp),
                        contentScale = ContentScale.Crop
                    )

                    // Gradiente overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.4f),
                                        Color.Black.copy(alpha = 0.7f),
                                        MaterialTheme.colorScheme.background
                                    )
                                )
                            )
                    )

                    // Contenido del header
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Foto de perfil
                        Surface(
                            modifier = Modifier.size(140.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface,
                            shadowElevation = 12.dp
                        ) {
                            AsyncImage(
                                model = profilePictureUrl,
                                contentDescription = "Foto de perfil",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        width = 4.dp,
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = CircleShape
                                    ),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Nombre de usuario
                        Text(
                            text = username,
                            fontSize = 28.sp,
                            fontFamily = RobotoBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Bio
                        Text(
                            text = userBio,
                            fontSize = 15.sp,
                            fontFamily = RobotoRegular,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botón de editar perfil
                        FilledTonalButton(
                            onClick = { navController.navigate(AppDestinations.PROFILE_SETUP_ROUTE) },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Editar perfil",
                                fontFamily = RobotoBold
                            )
                        }
                    }
                }
            }

            // Estadísticas
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.PlayArrow,
                        value = "127",
                        label = "Animes"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Star,
                        value = "2.5K",
                        label = "Episodios"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.EmojiEvents,
                        value = "${achievements.size}",
                        label = "Logros"
                    )
                }
            }

            // Información adicional
            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoRow(
                            icon = Icons.Default.LocationOn,
                            label = "Ubicación",
                            value = "Argentina"
                        )
                        HorizontalDivider()
                        InfoRow(
                            icon = Icons.Default.CalendarToday,
                            label = "Miembro desde",
                            value = "Mayo 2025"
                        )
                    }
                }
            }
            // Top 5 Animes
            item {
                Spacer(modifier = Modifier.height(8.dp))
                var showTop5Dialog by rememberSaveable { mutableStateOf(false) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Top 5 Animes",
                        fontSize = 24.sp,
                        fontFamily = RobotoBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    IconButton(onClick = { showTop5Dialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar Top 5",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (uiState.top5Animes.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        items(uiState.top5Animes.size) { index ->
                            AnimeTop5Card(
                                anime = uiState.top5Animes[index],
                                position = index + 1,
                                navController = navController
                            )
                        }
                    }
                } else {
                    // Placeholder cuando no hay top 5
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                            Text(
                                text = "Selecciona tus 5 animes favoritos",
                                fontSize = 16.sp,
                                fontFamily = RobotoRegular,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            FilledTonalButton(onClick = { showTop5Dialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Elegir Top 5", fontFamily = RobotoBold)
                            }
                        }
                    }
                }

                if (showTop5Dialog) {
                    SelectTop5Dialog(
                        allAnimes = uiState.allSavedAnimes,
                        currentTop5Ids = uiState.top5Animes.map { it.malId },
                        isSaving = uiState.isSavingTop5,
                        onDismiss = {
                            showTop5Dialog = false
                            profileViewModel.resetTop5UpdateSuccess()
                        },
                        onConfirm = { selectedIds ->
                            profileViewModel.updateTop5Animes(selectedIds)
                        },
                        onSuccess = {
                            showTop5Dialog = false
                            profileViewModel.resetTop5UpdateSuccess()
                        },
                        updateSuccess = uiState.top5UpdateSuccess
                    )
                }
            }

            // Top 5 Personajes
            item {
                Spacer(modifier = Modifier.height(24.dp))
                SectionTitle("Top 5 Personajes")
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    items(favoriteCharacters.size) { index ->
                        FavoriteCard(title = favoriteCharacters[index], position = index + 1)
                    }
                }
            }

            // Logros
            item {
                Spacer(modifier = Modifier.height(24.dp))
                var expanded by rememberSaveable { mutableStateOf(false) }
                val achievementsToShow = if (expanded) achievements else achievements.take(5)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Logros",
                            fontSize = 24.sp,
                            fontFamily = RobotoBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${achievements.size} desbloqueados",
                            fontSize = 14.sp,
                            fontFamily = RobotoRegular,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    achievementsToShow.forEach { achievement ->
                        AchievementCard(achievement = achievement)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (achievements.size > 5) {
                        FilledTonalButton(
                            onClick = { expanded = !expanded },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = if (expanded) "Mostrar menos" else "Mostrar más logros",
                                fontFamily = RobotoBold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontFamily = RobotoBold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    )
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String
) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = value,
                fontSize = 22.sp,
                fontFamily = RobotoBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = label,
                fontSize = 13.sp,
                fontFamily = RobotoRegular,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontFamily = RobotoRegular,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontFamily = RobotoBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun FavoriteCard(title: String, position: Int) {
    ElevatedCard(
        modifier = Modifier
            .width(180.dp)
            .height(240.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceContainerHigh,
                            MaterialTheme.colorScheme.surfaceContainer
                        )
                    )
                )
        ) {
            // Badge de posición
            if (position <= 3) {
                val (icon, bgColor) = when (position) {
                    1 -> "🥇" to Color(0xFFFFD700)
                    2 -> "🥈" to Color(0xFFC0C0C0)
                    else -> "🥉" to Color(0xFFCD7F32)
                }
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(40.dp),
                    shape = CircleShape,
                    color = bgColor.copy(alpha = 0.2f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = icon,
                            fontSize = 20.sp
                        )
                    }
                }
            } else {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "#$position",
                        fontSize = 14.sp,
                        fontFamily = RobotoBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Título centrado
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontFamily = RobotoBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 22.sp
                )
            }
        }
    }
}


@Composable
private fun AnimeTop5Card(anime: AnimeEntity, position: Int, navController: NavController) {
    ElevatedCard(
        modifier = Modifier
            .width(180.dp)
            .height(240.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = {
            navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/${anime.malId}")
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen del animebien, ahora
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradiente overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    )
            )

            // Badge de posición
            val (bgColor, textColor) = when (position) {
                1 -> Color(0xFFFFD700) to Color.Black  // Oro
                2 -> Color(0xFFC0C0C0) to Color.Black  // Plata
                3 -> Color(0xFFCD7F32) to Color.White  // Bronce
                4 -> Color(0xFF6366F1) to Color.White  // Índigo
                5 -> Color(0xFFEC4899) to Color.White  // Rosa
                else -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                shape = RoundedCornerShape(10.dp),
                color = bgColor,
                shadowElevation = 4.dp
            ) {
                Text(
                    text = "#$position",
                    fontSize = 16.sp,
                    fontFamily = RobotoBold,
                    color = textColor,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }

            // Título en la parte inferior
            Text(
                text = anime.title,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = RobotoBold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(12.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectTop5Dialog(
    allAnimes: List<AnimeEntity>,
    currentTop5Ids: List<Int>,
    isSaving: Boolean,
    updateSuccess: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (List<Int>) -> Unit,
    onSuccess: () -> Unit
) {
    // Usar remember en lugar de rememberSaveable para que se reinicie con los valores actuales
    var orderedSelectedIds by remember { mutableStateOf(currentTop5Ids) }

    // Cerrar el diálogo automáticamente cuando se complete el guardado
    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            kotlinx.coroutines.delay(300) // Pequeño delay para que el usuario vea el cambio
            onSuccess()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 700.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Selecciona tus Top 5 Animes",
                        fontSize = 24.sp,
                        fontFamily = RobotoBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${orderedSelectedIds.size}/5 seleccionados",
                        fontSize = 14.sp,
                        fontFamily = RobotoRegular,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar"
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Sección de animes seleccionados (ordenables)
            if (orderedSelectedIds.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 250.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Tu Top 5 (usa las flechas para reordenar)",
                        fontSize = 18.sp,
                        fontFamily = RobotoBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(orderedSelectedIds.size) { index ->
                            val animeId = orderedSelectedIds[index]
                            val anime = allAnimes.find { it.malId == animeId }
                            if (anime != null) {
                                SelectedAnimeItem(
                                    anime = anime,
                                    position = index + 1,
                                    canMoveUp = index > 0,
                                    canMoveDown = index < orderedSelectedIds.size - 1,
                                    onMoveUp = {
                                        if (index > 0) {
                                            val newList = orderedSelectedIds.toMutableList()
                                            val temp = newList[index]
                                            newList[index] = newList[index - 1]
                                            newList[index - 1] = temp
                                            orderedSelectedIds = newList
                                        }
                                    },
                                    onMoveDown = {
                                        if (index < orderedSelectedIds.size - 1) {
                                            val newList = orderedSelectedIds.toMutableList()
                                            val temp = newList[index]
                                            newList[index] = newList[index + 1]
                                            newList[index + 1] = temp
                                            orderedSelectedIds = newList
                                        }
                                    },
                                    onRemove = {
                                        orderedSelectedIds = orderedSelectedIds - animeId
                                    }
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }

            // Título para la lista de selección
            Text(
                text = if (orderedSelectedIds.isEmpty()) "Tus animes guardados" else "Agregar más animes",
                fontSize = 18.sp,
                fontFamily = RobotoBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Lista de animes
            if (allAnimes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                        Text(
                            text = "No tienes animes guardados",
                            fontSize = 16.sp,
                            fontFamily = RobotoBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Agrega animes a tu lista para poder seleccionar tus favoritos",
                            fontSize = 14.sp,
                            fontFamily = RobotoRegular,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .heightIn(max = 300.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(allAnimes.size) { index ->
                        val anime = allAnimes[index]
                        val isSelected = orderedSelectedIds.contains(anime.malId)

                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected)
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                else
                                    MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                            onClick = {
                                if (!isSelected && orderedSelectedIds.size < 5) {
                                    orderedSelectedIds = orderedSelectedIds + anime.malId
                                }
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Imagen
                                AsyncImage(
                                    model = anime.imageUrl,
                                    contentDescription = anime.title,
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                // Información
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = anime.title,
                                        fontSize = 16.sp,
                                        fontFamily = RobotoBold,
                                        color = if (isSelected)
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        else
                                            MaterialTheme.colorScheme.onSurface,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFFFD700),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = anime.userScore.toString(),
                                            fontSize = 13.sp,
                                            fontFamily = RobotoRegular,
                                            color = if (isSelected)
                                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                            else
                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )
                                    }
                                }

                                // Checkbox/Icono de selección
                                if (isSelected) {
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primary
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Seleccionado",
                                            tint = Color.White,
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .size(20.dp)
                                        )
                                    }
                                } else {
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.surface,
                                        border = BorderStroke(
                                            2.dp,
                                            MaterialTheme.colorScheme.outline
                                        ),
                                        modifier = Modifier.size(28.dp)
                                    ) {}
                                }
                            }
                        }
                    }
                }
            }

            // Botones de acción
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    enabled = !isSaving,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar", fontFamily = RobotoBold)
                }
                Button(
                    onClick = { onConfirm(orderedSelectedIds) },
                    enabled = orderedSelectedIds.size == 5 && !isSaving,
                    modifier = Modifier.weight(1f)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardando...", fontFamily = RobotoBold)
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar", fontFamily = RobotoBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectedAnimeItem(
    anime: AnimeEntity,
    position: Int,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onRemove: () -> Unit
) {
    val positionColors = mapOf(
        1 to Color(0xFFFFD700),  // Oro
        2 to Color(0xFFC0C0C0),  // Plata
        3 to Color(0xFFCD7F32),  // Bronce
        4 to Color(0xFF6366F1),  // Índigo
        5 to Color(0xFFEC4899)   // Rosa
    )

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Badge de posición
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = positionColors[position] ?: MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#$position",
                        fontSize = 18.sp,
                        fontFamily = RobotoBold,
                        color = if (position <= 2) Color.Black else Color.White
                    )
                }
            }

            // Imagen
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                modifier = Modifier
                    .width(50.dp)
                    .height(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // Información
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = anime.title,
                    fontSize = 15.sp,
                    fontFamily = RobotoBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = anime.userScore.toString(),
                        fontSize = 13.sp,
                        fontFamily = RobotoRegular,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            // Botones de reordenar y quitar
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botón subir
                IconButton(
                    onClick = onMoveUp,
                    enabled = canMoveUp,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Subir",
                        tint = if (canMoveUp)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }

                // Botón bajar
                IconButton(
                    onClick = onMoveDown,
                    enabled = canMoveDown,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Bajar",
                        tint = if (canMoveDown)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
            }

            // Botón eliminar
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Quitar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

data class Achievement(val name: String, val description: String, val typeAchievement: String)

@Composable
private fun AchievementCard(achievement: Achievement) {
    val (color, emoji) = when (achievement.typeAchievement) {
        "Normal" -> Color(0xFF4CAF50) to "🏆"
        "Rare" -> Color(0xFF2196F3) to "💎"
        "Epic" -> Color(0xFF9C27B0) to "⭐"
        "Legendary" -> Color(0xFFFF9800) to "👑"
        else -> MaterialTheme.colorScheme.primary to "🏆"
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icono con gradiente
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                color.copy(alpha = 0.8f),
                                color.copy(alpha = 0.4f)
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        color = color,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 28.sp
                )
            }

            // Detalles del logro
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = achievement.name,
                        fontSize = 16.sp,
                        fontFamily = RobotoBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    // Badge de rareza
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = color.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = achievement.typeAchievement,
                            fontSize = 11.sp,
                            fontFamily = RobotoBold,
                            color = color,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Text(
                    text = achievement.description,
                    fontSize = 14.sp,
                    fontFamily = RobotoRegular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }
        }
    }
}