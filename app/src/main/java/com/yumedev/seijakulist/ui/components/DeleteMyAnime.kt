package com.yumedev.seijakulist.ui.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme

@Composable
fun DeleteMyAnime(
    modifier: Modifier = Modifier,
    onDeleteConfirmed: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { isMenuExpanded = true },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Botón de opciones",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
            modifier = Modifier.background(Color.DarkGray)
        ) {
            DropdownMenuItem(
                text = { Text("Eliminar anime", color = Color.White) },
                onClick = {
                    onDeleteConfirmed()
                    isMenuExpanded = false
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red.copy(alpha = 0.9f)
                    )
                }
            )
        }
    }
}