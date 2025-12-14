package com.yumedev.seijakulist.ui.screens.auth_screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yumedev.seijakulist.ui.components.custom.AnimeButton
import com.yumedev.seijakulist.ui.components.custom.AnimeButtonVariant
import com.yumedev.seijakulist.ui.components.custom.AnimeGradientBackground
import com.yumedev.seijakulist.ui.components.custom.AnimeStar
import com.yumedev.seijakulist.ui.components.custom.AnimeTextField
import com.yumedev.seijakulist.ui.components.custom.SakuraFlower
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.RobotoBold
import com.yumedev.seijakulist.ui.theme.RobotoRegular
import kotlinx.coroutines.delay

@Composable
fun AnimeLoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onSignInSuccess: () -> Unit,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val authResult by viewModel.authResult.collectAsState()
    val focusManager = LocalFocusManager.current

    // Animación de entrada
    val titleAlpha = remember { Animatable(0f) }
    val titleOffset = remember { Animatable(50f) }

    LaunchedEffect(Unit) {
        titleAlpha.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
        titleOffset.animateTo(
            targetValue = 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    LaunchedEffect(authResult) {
        if (authResult is AuthResult.Success) {
            onSignInSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo con gradiente anime
        AnimeGradientBackground()

        // Elementos decorativos
        SakuraFlower(
            modifier = Modifier
                .offset(x = 30.dp, y = 80.dp)
                .alpha(0.3f),
            size = 60.dp
        )

        SakuraFlower(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-20).dp, y = 150.dp)
                .alpha(0.2f),
            size = 80.dp
        )

        AnimeStar(
            modifier = Modifier
                .offset(x = 50.dp, y = 200.dp)
                .alpha(0.5f),
            size = 20.dp
        )

        AnimeStar(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-100).dp, y = 100.dp)
                .alpha(0.6f),
            size = 16.dp
        )

        AnimeStar(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 80.dp, y = (-200).dp)
                .alpha(0.4f),
            size = 18.dp
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo/Título con animación
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .alpha(titleAlpha.value)
                    .offset(y = titleOffset.value.dp)
            ) {
                // Título principal con gradiente
                Text(
                    text = "SeijakuList",
                    fontSize = 52.sp,
                    fontFamily = RobotoBold,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.displayLarge.copy(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                            )
                        )
                    ),
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtítulo decorativo
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 30.dp, height = 2.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    )

                    Text(
                        text = "アニメリスト",
                        fontSize = 14.sp,
                        fontFamily = RobotoRegular,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        letterSpacing = 2.sp
                    )

                    Box(
                        modifier = Modifier
                            .size(width = 30.dp, height = 2.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Mensaje de bienvenida
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¡Bienvenido de nuevo!",
                    fontFamily = RobotoBold,
                    fontSize = 26.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Ingresa a tu cuenta para continuar",
                    fontFamily = RobotoRegular,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Formulario
            AnimeTextField(
                value = email,
                onValueChange = { email = it },
                label = "Correo electrónico",
                placeholder = "tu@correo.com",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            AnimeTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                placeholder = "Ingresa tu contraseña",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Olvidaste contraseña
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { /* TODO */ }) {
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        fontFamily = RobotoRegular,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de login
            AnimeButton(
                text = "Iniciar Sesión",
                icon = Icons.Default.Login,
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.signIn(email, password)
                    }
                },
                enabled = email.isNotEmpty() && password.isNotEmpty(),
                variant = AnimeButtonVariant.Primary
            )

            // Mensaje de error
            if (authResult is AuthResult.Error) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "⚠️ ${(authResult as AuthResult.Error).message}",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        fontFamily = RobotoRegular
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Registro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿No tienes cuenta?",
                    fontFamily = RobotoRegular,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 15.sp
                )

                TextButton(
                    onClick = { navController.navigate(AppDestinations.REGISTER_ROUTE) }
                ) {
                    Text(
                        text = "Regístrate",
                        fontFamily = RobotoBold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
