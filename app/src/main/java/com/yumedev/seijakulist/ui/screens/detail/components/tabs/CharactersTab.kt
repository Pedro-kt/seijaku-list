package com.yumedev.seijakulist.ui.screens.detail.components.tabs

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.yumedev.seijakulist.domain.models.AnimeCharactersDetail
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.screens.detail.components.shared.SectionHeader
import com.yumedev.seijakulist.ui.screens.detail.components.shared.StateMessage
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

/**
 * Tab de personajes del anime
 */
@Composable
fun CharactersTab(
    characters: List<AnimeCharactersDetail>,
    isLoading: Boolean,
    errorMessage: String?,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        // Header con búsqueda
        AnimatedContent(
            targetState = isSearching,
            transitionSpec = {
                (fadeIn(animationSpec = tween(250)) + expandVertically()) togetherWith
                        (fadeOut(animationSpec = tween(200)) + shrinkVertically())
            },
            label = "CharacterHeader"
        ) { searching ->
            if (searching) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                "Buscar por nombre...",
                                style = TextStyle(
                                    fontFamily = PoppinsRegular,
                                    fontSize = 15.asp(),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.Search,
                                null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.adp())
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                if (searchQuery.isEmpty()) isSearching = false
                                else searchQuery = ""
                            }) {
                                Icon(
                                    Icons.Rounded.Close,
                                    null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.adp())
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(
                                alpha = 0.5f
                            ),
                        ),
                        textStyle = TextStyle(
                            fontFamily = PoppinsMedium,
                            fontSize = 15.asp(),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            } else {
                SectionHeader(
                    title = "Personajes",
                    subtitle = "Reparto de la obra",
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    IconButton(
                        onClick = { isSearching = true },
                        modifier = Modifier.size(44.adp())
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Buscar",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            modifier = Modifier.size(26.adp())
                        )
                    }
                }
            }
        }

        // Contenido
        AnimatedContent(
            targetState = isLoading,
            label = "CharacterContent"
        ) { loading ->
            when {
                loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    }
                }

                errorMessage != null -> {
                    StateMessage(
                        icon = Icons.Default.CloudOff,
                        title = "¡Ups! Algo salió mal",
                        description = errorMessage
                    )
                }

                characters.isEmpty() -> {
                    StateMessage(
                        icon = Icons.Default.PersonOff,
                        title = "Sin personajes",
                        description = "Este anime aún no tiene personajes registrados."
                    )
                }

                else -> {
                    val filteredCharacters = if (searchQuery.isBlank()) {
                        characters
                    } else {
                        characters.filter { character ->
                            character.nameCharacter?.contains(
                                searchQuery,
                                ignoreCase = true
                            ) == true
                        }
                    }

                    if (filteredCharacters.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No se encontraron personajes",
                                style = MaterialTheme.typography.bodyLarge,
                                fontFamily = PoppinsRegular
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            filteredCharacters.chunked(3).forEach { rowItems ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                ) {
                                    rowItems.forEach { character ->
                                        CharacterCard(
                                            character = character,
                                            onClick = {
                                                navController.navigate(
                                                    "${AppDestinations.CHARACTER_DETAIL_ROUTE}/${character.idCharacter}"
                                                )
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    // Fill remaining space if row is incomplete
                                    repeat(3 - rowItems.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterCard(
    character: AnimeCharactersDetail,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val translatedRole = when (character.role) {
        "Main" -> "Principal"
        "Supporting" -> "Secundario"
        else -> character.role
    }

    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            pressedElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.75f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(character.imageCharacter?.jpg?.imageUrl.orEmpty())
                        .size(Size.ORIGINAL)
                        .crossfade(true)
                        .build(),
                    contentDescription = character.nameCharacter,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                // Role badge
                if (translatedRole != null) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = if (character.role == "Main")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    ) {
                        Text(
                            text = translatedRole,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = if (character.role == "Main")
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSecondary,
                            fontSize = 10.asp(),
                            fontFamily = PoppinsBold
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = character.nameCharacter ?: "Unknown",
                    fontSize = 13.asp(),
                    fontFamily = PoppinsBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
