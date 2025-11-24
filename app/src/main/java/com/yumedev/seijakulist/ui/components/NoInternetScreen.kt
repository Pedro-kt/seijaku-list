package com.yumedev.seijakulist.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yumedev.seijakulist.R
import kotlinx.coroutines.delay

//Composable de prueba para mostrar una animación de error
@Composable
fun NoInternetScreen(
    onRetryClick: () -> Unit // 🌟 Nuevo parámetro: un lambda
) {
    var mostrarContenido by remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.eight_bit_cat))

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LaunchedEffect(Unit) {
        delay(2000)
        mostrarContenido = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!mostrarContenido) {
            LoadingScreen()
        } else {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(300.dp)
            )
            Text(
                text = "Ups! Al parecer a ocurrido un error, verifique su conexion a internet y vuelva a intentarlo",
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            ElevatedButton(
                onClick = onRetryClick, // 🌟 Llama a la función que pasamos
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 16.dp
                )
            ) {
                Text(text = "Volver a intentar")
            }
        }
    }
}