package com.example.seijakulist.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.seijakulist.ui.navigation.AppDestinations

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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // 2. Accede a los datos del perfil directamente del uiState.
    val userProfile = uiState.userProfile
    val username = userProfile?.username ?: "Nombre de usuario"
    val profilePictureUrl = userProfile?.profilePictureUrl
    val userBio = "¡Hola! Estoy usando SeijakuList para seguir mis animes y mangas favoritos."

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(shape = CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = profilePictureUrl),
                            contentDescription = "Imagen de perfil",
                            modifier = Modifier.fillMaxSize(),
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(username, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = userBio,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .heightIn(min = 40.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { navController.navigate(AppDestinations.PROFILE_SETUP_ROUTE) },

                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = "Editar perfil",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle("Top 5 Animes")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(favoriteAnimes.size) { index ->
                        FavoriteCard(title = favoriteAnimes[index], position = index + 1)
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle("Top 5 Personajes")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    items(favoriteCharacters.size) { index ->
                        FavoriteCard(title = favoriteCharacters[index], position = index + 1)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            var expanded by rememberSaveable { mutableStateOf(false) }
            val achievementsToShow = if (expanded) achievements else achievements.take(5)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                SectionTitle("Logros")
                Spacer(modifier = Modifier.height(4.dp))
                achievementsToShow.forEach { achievement ->
                    AchievementCard(achievement = achievement)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (achievements.size > 10) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    TextButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = if (expanded) "Mostrar menos" else "Mostrar más")
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (expanded) "Contraer logros" else "Expandir logros"
                        )
                    }
                }
            }




            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun FavoriteCard(title: String, position: Int) {
    ElevatedCard(
        modifier = Modifier
            .width(160.dp)
            .fillMaxHeight(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            contentAlignment = Alignment.Center
        ) {
            if (position <= 3) {
                val (icon, color) = when (position) {
                    1 -> "🥇" to 0xFFFFD700 // Gold
                    2 -> "🥈" to 0xFFC0C0C0 // Silver
                    else -> "🥉" to 0xFFCD7F32 // Bronze
                }
                Text(
                    text = icon,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                )
            }
            Text(
                text = title,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


data class Achievement(val name: String, val description: String, val typeAchievement: String)

@Composable
private fun AchievementCard(achievement: Achievement) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icono del logro
            val color = when (achievement.typeAchievement) {
                "Normal" -> MaterialTheme.colorScheme.primaryContainer
                "Rare" -> Color(0xFF67A8D4) // Un color azulado
                "Epic" -> Color(0xFF9E67D4) // Un color morado
                "Legendary" -> Color(0xFFD4B167) // Un color dorado/amarillo
                else -> MaterialTheme.colorScheme.primaryContainer
            }


            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🏆", fontSize = 24.sp)
            }

            // Detalles del logro
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = achievement.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
            // Tipo de logro como una "píldora"
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(color)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = achievement.typeAchievement,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}