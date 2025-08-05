package com.example.seijakulist.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun floatingButton() {
    FloatingActionButton(
        modifier = Modifier
            .padding(30.dp)
            .size(60.dp),
            /* .graphicsLayer {
                scaleX = scale
                scaleY = scale
            } */
        shape = FloatingActionButtonDefaults.shape,
        onClick = { },
        containerColor = Color.White,
        contentColor = contentColorFor(backgroundColor = Color.White)
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "Añadir anime",
            tint = Color.Black
        )
    }
}