package com.yumedev.seijakulist.ui.screens.characters

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.yumedev.seijakulist.domain.models.CharacterDetail
import com.yumedev.seijakulist.domain.models.VoiceActorDomain
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.screens.detail.SectionHeader
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.util.CharacterDescriptionParser
import com.yumedev.seijakulist.util.CharacterInfo
import kotlinx.coroutines.delay
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

// ─────────────────────────────────────────────────────────────────────────────
//  CharacterDetailScreen
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CharacterDetailScreen(
    navController: NavController,
    characterId: Int,
    characterDetailViewModel: CharacterDetailViewModel = hiltViewModel(),
    characterPictureViewModel: CharacterPictureViewModel = hiltViewModel()
) {
    val characterDetail   by characterDetailViewModel.characterDetail.collectAsState()
    val characterLoading  by characterDetailViewModel.isLoading.collectAsState()
    val characterError    by characterDetailViewModel.errorMessage.collectAsState()
    val characterPictures by characterPictureViewModel.characterPictures.collectAsState()
    val picturesLoading   by characterPictureViewModel.isLoadingPicture.collectAsState()
    val picturesError     by characterPictureViewModel.errorMessagePicture.collectAsState()

    val isLoading  = characterLoading || picturesLoading
    val errorMsg   = characterError ?: picturesError

    var expanded         by rememberSaveable { mutableStateOf(false) }
    var showImageDialog  by remember { mutableStateOf(false) }
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }
    var contentVisible   by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(characterId) {
        characterDetailViewModel.loadCharacterDetail(characterId)
        characterPictureViewModel.loadCharacterPictures(characterId)
    }
    LaunchedEffect(isLoading) {
        if (!isLoading) { delay(120); contentVisible = true }
    }

    val parsedDescription = remember(characterDetail.descriptionCharacter) {
        CharacterDescriptionParser.parseDescription(characterDetail.descriptionCharacter)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> CharacterLoadingState()
            errorMsg != null -> CharacterErrorState(errorMsg)
            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        CharacterHeroHeader(
                            character     = characterDetail,
                        )
                    }

                    if (parsedDescription.keyValuePairs.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedSection(contentVisible, delayMs = 0L) {
                                CharacterInfoSection(parsedDescription.keyValuePairs)
                            }
                        }
                    }

                    if (characterDetail.nicknames.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedSection(contentVisible, delayMs = 60L) {
                                CharacterNicknamesSection(characterDetail.nicknames)
                            }
                        }
                    }

                    if (parsedDescription.cleanDescription.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedSection(contentVisible, delayMs = 120L) {
                                CharacterDescriptionSection(
                                    text     = parsedDescription.cleanDescription,
                                    expanded = expanded,
                                    onToggle = { expanded = !expanded }
                                )
                            }
                        }
                    }

                    if (characterPictures.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedSection(contentVisible, delayMs = 180L) {
                                CharacterGallerySection(
                                    pictures = characterPictures,
                                    onImageClick = { url ->
                                        selectedImageUrl = url
                                        showImageDialog = true
                                    }
                                )
                            }
                        }
                    }

                    if (characterDetail.animeRelations.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedSection(contentVisible, delayMs = 240L) {
                                CharacterAppearancesSection(
                                    title         = "Animes donde aparece",
                                    subtitle      = "${characterDetail.animeRelations.size} anime${if (characterDetail.animeRelations.size != 1) "s" else ""}",
                                    items         = characterDetail.animeRelations.map { AppearanceItem(it.title, it.imageUrl, it.role, it.malId) },
                                    navController = navController
                                )
                            }
                        }
                    }

                    if (characterDetail.mangaRelations.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedSection(contentVisible, delayMs = 300L) {
                                CharacterAppearancesSection(
                                    title         = "Mangas donde aparece",
                                    subtitle      = "${characterDetail.mangaRelations.size} manga${if (characterDetail.mangaRelations.size != 1) "s" else ""}",
                                    items         = characterDetail.mangaRelations.map { AppearanceItem(it.title, it.imageUrl, "Manga", malId = 0) },
                                    navController = navController
                                )
                            }
                        }
                    }

                    if (characterDetail.voiceActors.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedSection(contentVisible, delayMs = 360L) {
                                CharacterVoiceActorsSection(characterDetail.voiceActors)
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(48.adp())) }
                }
            }
        }

        if (showImageDialog && selectedImageUrl != null) {
            CharacterImageDialog(
                imageUrl  = selectedImageUrl!!,
                onDismiss = { showImageDialog = false }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Hero Header
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CharacterHeroHeader(
    character: CharacterDetail
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.adp())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(bottom = 28.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            // Avatar circular simple
            Surface(
                modifier        = Modifier.size(148.adp()),
                shape           = CircleShape,
                shadowElevation = 10.dp,
                border          = BorderStroke(2.dp, Color.White.copy(alpha = 0.15f))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character.images)
                        .size(Size.ORIGINAL)
                        .crossfade(true)
                        .build(),
                    contentDescription = character.nameCharacter,
                    modifier     = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(30.dp))

            // Nombre + kanji + favoritos
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text       = character.nameCharacter,
                    fontSize = 24.asp(),
                    fontFamily = PoppinsBold,
                    color      = Color.White,
                    textAlign  = TextAlign.Center,
                    lineHeight = 28.asp(),
                    modifier   = Modifier.padding(horizontal = 32.dp)
                )
                if (character.nameKanjiCharacter.isNotEmpty() && character.nameKanjiCharacter != "N/A") {
                    Text(
                        text       = character.nameKanjiCharacter,
                        fontSize = 14.asp(),
                        fontFamily = PoppinsRegular,
                        color      = Color.White.copy(alpha = 0.70f),
                        textAlign  = TextAlign.Center,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis,
                        modifier   = Modifier.padding(horizontal = 32.dp)
                    )
                }
                if (character.favorites > 0) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Surface(
                        shape  = RoundedCornerShape(20.dp),
                        color  = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.50f))
                    ) {
                        Text(
                            text       = "%,d favoritos".format(character.favorites),
                            modifier   = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                            fontSize = 11.asp(),
                            fontFamily = PoppinsBold,
                            color      = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  AnimatedSection — envuelve secciones con fade + slide-up escalonado
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun AnimatedSection(
    trigger: Boolean,
    delayMs: Long,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(trigger) {
        if (trigger) { delay(delayMs); visible = true }
    }
    val alpha by animateFloatAsState(if (visible) 1f else 0f, tween(380), label = "sec_a")
    val offsetY by animateDpAsState(
        targetValue  = if (visible) 0.dp else 16.dp,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow),
        label = "sec_y"
    )
    Box(modifier = Modifier.alpha(alpha).offset(y = offsetY)) {
        content()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Sección: Información (pares clave-valor del parser)
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CharacterInfoSection(pairs: List<CharacterInfo>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(title = "Información", subtitle = "${pairs.size} datos")
        FlowRow(
            modifier              = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement   = Arrangement.spacedBy(8.dp)
        ) {
            pairs.forEach { info -> CharacterInfoChip(info) }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Sección: Alias / Nicknames
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CharacterNicknamesSection(nicknames: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(title = "Alias", subtitle = "También conocido como")
        FlowRow(
            modifier              = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement   = Arrangement.spacedBy(8.dp)
        ) {
            nicknames.forEach { nickname ->
                Surface(
                    shape  = RoundedCornerShape(20.dp),
                    color  = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.20f))
                ) {
                    Text(
                        text       = nickname,
                        modifier   = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        fontSize = 12.asp(),
                        fontFamily = PoppinsMedium,
                        color      = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Sección: Descripción con fade + expand animado
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CharacterDescriptionSection(text: String, expanded: Boolean, onToggle: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(title = "Descripción", subtitle = "Sobre el personaje")
        Card(
            modifier  = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape     = RoundedCornerShape(16.dp),
            colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Texto con fade inferior cuando está colapsado
                Box {
                    Text(
                        text       = text,
                        color      = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.asp(),
                        lineHeight = 22.asp(),
                        fontFamily = PoppinsRegular,
                        textAlign  = TextAlign.Justify,
                        maxLines   = if (expanded) Int.MAX_VALUE else 7,
                        overflow   = TextOverflow.Ellipsis
                    )
                    // Gradiente fade-out inferior cuando colapsado
                    if (!expanded && text.length > 200) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        0.45f to Color.Transparent,
                                        1.00f to MaterialTheme.colorScheme.surfaceContainer
                                    )
                                )
                        )
                    }
                }

                if (text.length > 200) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        modifier  = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable(onClick = onToggle),
                        shape     = RoundedCornerShape(12.dp),
                        color     = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    ) {
                        Row(
                            modifier              = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Text(
                                text       = if (expanded) "Ver menos" else "Ver más",
                                fontSize = 12.asp(),
                                fontFamily = PoppinsBold,
                                color      = MaterialTheme.colorScheme.primary
                            )
                            Icon(
                                imageVector        = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint               = MaterialTheme.colorScheme.primary,
                                modifier           = Modifier.size(16.adp())
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Sección: Actores de Voz
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CharacterVoiceActorsSection(voiceActors: List<VoiceActorDomain>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(
            title    = "Actores de Voz",
            subtitle = "${voiceActors.size} actor${if (voiceActors.size != 1) "es" else ""}"
        ) {
            Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer.copy(0.4f)) {
                Icon(
                    Icons.Default.RecordVoiceOver,
                    contentDescription = null,
                    tint     = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(6.dp).size(14.dp)
                )
            }
        }
        LazyRow(
            modifier             = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding       = PaddingValues(horizontal = 20.dp)
        ) {
            items(voiceActors) { va -> VoiceActorCard(va) }
        }
    }
}

private data class AppearanceItem(
    val title: String,
    val imageUrl: String,
    val tag: String,
    val malId: Int = 0   // 0 = no navegable (manga)
)

// ─────────────────────────────────────────────────────────────────────────────
//  Sección: Apariciones (anime o manga)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CharacterAppearancesSection(
    title: String,
    subtitle: String,
    items: List<AppearanceItem>,
    navController: NavController
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(title = title, subtitle = subtitle)
        LazyRow(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding        = PaddingValues(horizontal = 20.dp)
        ) {
            items(items) { item ->
                CharacterAppearanceCard(item = item, navController = navController)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Sección: Galería
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CharacterGallerySection(
    pictures: List<com.yumedev.seijakulist.domain.models.CharacterPictures>,
    onImageClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(
            title    = "Galería",
            subtitle = "${pictures.size} imagen${if (pictures.size != 1) "es" else ""}"
        ) {
            Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer.copy(0.4f)) {
                Text(
                    text     = "${pictures.size}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    fontSize = 12.asp(),
                    fontFamily = PoppinsBold,
                    color    = MaterialTheme.colorScheme.primary
                )
            }
        }
        LazyRow(
            modifier             = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding       = PaddingValues(horizontal = 20.dp)
        ) {
            items(pictures) { picture ->
                ElevatedCard(
                    modifier  = Modifier
                        .width(140.adp())
                        .height(210.adp())
                        .clickable { onImageClick(picture.characterPictures) },
                    shape     = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(picture.characterPictures)
                            .size(Size.ORIGINAL)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier     = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  VoiceActorCard — foto circular + nombre + idioma
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun VoiceActorCard(voiceActor: VoiceActorDomain) {
    Column(
        modifier              = Modifier.width(82.adp()),
        horizontalAlignment   = Alignment.CenterHorizontally,
        verticalArrangement   = Arrangement.spacedBy(6.dp)
    ) {
        // Foto circular
        Surface(
            modifier        = Modifier.size(72.adp()),
            shape           = CircleShape,
            color           = MaterialTheme.colorScheme.surfaceContainerHigh,
            shadowElevation = 2.dp
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(voiceActor.imageUrl.ifEmpty { null })
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier     = Modifier.fillMaxSize().clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text       = voiceActor.name,
            fontSize = 10.asp(),
            fontFamily = PoppinsBold,
            color      = MaterialTheme.colorScheme.onSurface,
            textAlign  = TextAlign.Center,
            maxLines   = 2,
            overflow   = TextOverflow.Ellipsis,
            lineHeight = 13.asp()
        )
        if (voiceActor.language.isNotEmpty()) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
            ) {
                Text(
                    text       = voiceActor.language,
                    modifier   = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontSize = 9.asp(),
                    fontFamily = PoppinsMedium,
                    color      = MaterialTheme.colorScheme.onSecondaryContainer,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CharacterAppearanceCard — portada con overlay + título + tag de rol
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CharacterAppearanceCard(item: AppearanceItem, navController: NavController) {
    ElevatedCard(
        modifier  = Modifier
            .width(110.adp())
            .clickable(enabled = item.malId > 0) {
                navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${item.malId}")
            },
        shape     = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(155.adp())
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.imageUrl.ifEmpty { null })
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier     = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Overlay degradado para legibilidad del título
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(0.5f to Color.Transparent, 1.0f to Color.Black.copy(0.80f))
                )
            )
            // Tag del rol (top-right)
            if (item.tag.isNotEmpty()) {
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(6.dp),
                    shape    = RoundedCornerShape(6.dp),
                    color    = Color.Black.copy(alpha = 0.62f)
                ) {
                    Text(
                        text       = item.tag,
                        modifier   = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                        fontSize = 8.asp(),
                        fontFamily = PoppinsBold,
                        color      = Color.White,
                        maxLines   = 1
                    )
                }
            }
            // Título (bottom)
            Text(
                text       = item.title,
                fontSize = 10.asp(),
                fontFamily = PoppinsBold,
                color      = Color.White,
                maxLines   = 2,
                overflow   = TextOverflow.Ellipsis,
                lineHeight = 13.asp(),
                modifier   = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp, vertical = 7.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CharacterInfoChip — chip de información key • value
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CharacterInfoChip(info: CharacterInfo) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.8f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(0.3f))
    ) {
        Row(
            modifier              = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text       = info.key,
                fontSize = 11.asp(),
                fontFamily = PoppinsRegular,
                color      = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.75f),
                maxLines   = 1
            )
            Text(
                text     = "·",
                fontSize = 12.asp(),
                color    = MaterialTheme.colorScheme.outlineVariant
            )
            Text(
                text       = info.value,
                fontSize = 11.asp(),
                fontFamily = PoppinsBold,
                color      = MaterialTheme.colorScheme.onSurface,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Dialog de imagen en pantalla completa
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CharacterImageDialog(imageUrl: String, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties       = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier         = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier     = Modifier
                    .fillMaxWidth(0.95f)
                    .clickable(enabled = false) {}
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Fit
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(end = 16.dp, top = 8.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.18f)
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Estados de carga y error
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CharacterLoadingState() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier              = Modifier.align(Alignment.Center),
            horizontalAlignment   = Alignment.CenterHorizontally,
            verticalArrangement   = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier  = Modifier.size(48.adp()),
                color     = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp
            )
            Text(
                text       = "Cargando personaje...",
                fontSize = 14.asp(),
                fontFamily = PoppinsRegular,
                color      = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CharacterErrorState(message: String) {
    Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Column(
            modifier              = Modifier.align(Alignment.Center),
            horizontalAlignment   = Alignment.CenterHorizontally,
            verticalArrangement   = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text       = "Algo salió mal",
                fontSize = 20.asp(),
                fontFamily = PoppinsBold,
                color      = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text       = message,
                fontSize = 13.asp(),
                fontFamily = PoppinsRegular,
                color      = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign  = TextAlign.Center,
                lineHeight = 18.asp()
            )
        }
    }
}