package com.yumedev.seijakulist.ui.screens.profile

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.screens.auth_screen.AuthResult
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

@Composable
fun ProfileSetupView(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isVisible by remember { mutableStateOf(false) }

    val uiState by profileViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
        isVisible = true
    }

    LaunchedEffect(uiState.profileUpdateSuccess) {
        if (uiState.profileUpdateSuccess) {
            profileViewModel.resetUpdateSuccess()
            navController.navigate(AppDestinations.HOME) {
                popUpTo(AppDestinations.PROFILE_LOADER_ROUTE) { inclusive = true }
            }
        }
    }

    LaunchedEffect(uiState.userProfile) {
        uiState.userProfile?.let { profile ->
            if (profile.username != null && username.isEmpty()) {
                username = profile.username
            }
            if (profile.bio != null && bio.isEmpty()) {
                bio = profile.bio
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val isEditMode = uiState.userProfile?.username != null
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue   = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label         = "button_scale"
    )

    LazyColumn(
        modifier            = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding(),
        contentPadding      = PaddingValues(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600)) + slideInVertically(animationSpec = tween(600, easing = FastOutSlowInEasing), initialOffsetY = { -30 })
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text       = if (isEditMode) "Actualiza tu perfil" else "Crea tu perfil",
                        fontSize = 28.asp(),
                        fontFamily = PoppinsBold,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text       = "Cuéntanos un poco sobre ti",
                        fontSize = 15.asp(),
                        fontFamily = PoppinsRegular,
                        color      = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(50.adp()))
        }

        // ── Avatar ────────────────────────────────────────────────────────────
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(700, delayMillis = 200)) + scaleIn(initialScale = 0.8f, animationSpec = tween(700, delayMillis = 200, easing = FastOutSlowInEasing))
            ) {
                ProfileImagePicker(
                    imageUri    = selectedImageUri ?: uiState.userProfile?.profilePictureUrl,
                    onImagePick = { launcher.launch("image/*") }
                )
            }
            Spacer(modifier = Modifier.height(40.adp()))
        }

        // ── Username ──────────────────────────────────────────────────────────
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600, delayMillis = 400)) + slideInVertically(animationSpec = tween(600, delayMillis = 400), initialOffsetY = { 30 })
            ) {
                Column(
                    modifier            = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text       = "Nombre de usuario",
                        fontSize = 13.asp(),
                        fontFamily = PoppinsMedium,
                        color      = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier   = Modifier.padding(start = 4.dp)
                    )
                    OutlinedTextField(
                        value         = username,
                        onValueChange = { username = it },
                        placeholder   = { Text("Tu nombre", fontFamily = PoppinsRegular, fontSize = 15.asp()) },
                        leadingIcon   = { Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.primary) },
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(16.dp),
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            focusedContainerColor   = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        singleLine = true,
                        textStyle  = LocalTextStyle.current.copy(fontFamily = PoppinsMedium, fontSize = 15.asp())
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // ── Bio ───────────────────────────────────────────────────────────────
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600, delayMillis = 500)) + slideInVertically(animationSpec = tween(600, delayMillis = 500), initialOffsetY = { 30 })
            ) {
                Column(
                    modifier            = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            text       = "Biografía (opcional)",
                            fontSize = 13.asp(),
                            fontFamily = PoppinsMedium,
                            color      = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier   = Modifier.padding(start = 4.dp)
                        )
                        Text(
                            text       = "${bio.length}/150",
                            fontSize = 12.asp(),
                            fontFamily = PoppinsRegular,
                            color      = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                    OutlinedTextField(
                        value         = bio,
                        onValueChange = { if (it.length <= 150) bio = it },
                        placeholder   = { Text("¿Qué animes te gustan?", fontFamily = PoppinsRegular, fontSize = 15.asp()) },
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(16.dp),
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            focusedContainerColor   = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        maxLines  = 3,
                        minLines  = 3,
                        textStyle = LocalTextStyle.current.copy(fontFamily = PoppinsRegular, fontSize = 14.asp())
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.adp()))
        }

        // ── Botón guardar ─────────────────────────────────────────────────────
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600, delayMillis = 600)) + scaleIn(initialScale = 0.9f, animationSpec = tween(600, delayMillis = 600))
            ) {
                Button(
                    onClick = { profileViewModel.updateUserProfile(username, bio, selectedImageUri) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.adp())
                        .graphicsLayer { scaleX = buttonScale; scaleY = buttonScale },
                    shape    = RoundedCornerShape(16.dp),
                    enabled  = username.isNotBlank() && !uiState.isLoading,
                    colors   = ButtonDefaults.buttonColors(
                        containerColor         = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    interactionSource = interactionSource
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(22.adp()), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.5.dp)
                    } else {
                        Text(if (isEditMode) "Actualizar" else "Continuar", fontSize = 16.asp(), fontFamily = PoppinsBold)
                    }
                }
            }
        }

        // ── Mensajes de estado ────────────────────────────────────────────────
        item {
            AnimatedVisibility(visible = uiState.isLoading) {
                Text(
                    text       = if (uiState.isUploadingImage) "Subiendo imagen..." else "Guardando...",
                    fontSize = 13.asp(),
                    color      = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontFamily = PoppinsRegular,
                    modifier   = Modifier.padding(top = 16.dp)
                )
            }
            AnimatedVisibility(visible = uiState.error != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    color    = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                    shape    = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text       = uiState.error ?: "",
                        color      = MaterialTheme.colorScheme.error,
                        fontSize = 13.asp(),
                        modifier   = Modifier.padding(16.dp),
                        textAlign  = TextAlign.Center,
                        fontFamily = PoppinsRegular
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileImagePicker(
    imageUri: Any?,
    onImagePick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "image_scale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(140.adp())
    ) {
        // Círculo decorativo de fondo
        Box(
            modifier = Modifier
                .size(150.adp())
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Imagen principal
        Surface(
            modifier = Modifier
                .size(140.adp())
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onImagePick
                ),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 2.dp
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Agregar foto",
                        modifier = Modifier.size(80.adp()),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                }
            }
        }

        // Botón de editar
        Surface(
            modifier = Modifier
                .size(42.adp())
                .align(Alignment.BottomEnd)
                .offset(x = (-4).dp, y = (-4).dp)
                .clickable(onClick = onImagePick),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            tonalElevation = 6.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Cambiar foto",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.adp())
                )
            }
        }
    }
}