package com.example.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun FilterTopAppBar() {
    var expanded by remember { mutableStateOf(false) }
    Box() {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Filtros",
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 0.dp, y = 16.dp),
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.6f))
        ) {
            DropdownMenuItem(
                text = { Text("Buscar titulo") },
                onClick = { /* Handle edit */ },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            )
            DropdownMenuItem(
                text = { Text("Filtrar por") },
                onClick = { /* Handle settings */ },
                leadingIcon = { Icon(Icons.Default.FilterList, contentDescription = "Filtrar por") },
            )
            DropdownMenuItem(
                text = { Text("Cambiar vista") },
                onClick = { /* Handle send feedback */ },
                leadingIcon = { Icon(Icons.Default.ViewModule, contentDescription = "Cambiar vista") },
            )
            DropdownMenuItem(
                text = { Text("Ordenar por titulo") },
                onClick = { /* Handle send feedback */ },
                leadingIcon = { Icon(Icons.Default.SortByAlpha, contentDescription = "Ordenar por titulo") },
            )
        }
    }
}

