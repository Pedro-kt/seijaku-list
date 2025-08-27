package com.example.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.seijakulist.data.local.entities.AnimeEntity

@Composable
fun FilterTopAppBar(
    onSearchClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "Filtros",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 0.dp, y = 16.dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            DropdownMenuItem(
                text = { Text("Buscar titulo") },
                onClick = {
                    onSearchClick()
                    expanded = false
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            DropdownMenuItem(
                text = { Text("Cambiar vista") },
                onClick = {
                    expanded = false
                },
                leadingIcon = { Icon(Icons.Default.ViewModule, contentDescription = "Cambiar vista") },
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            DropdownMenuItem(
                text = { Text("Ordenar por A-Z / Z-A") },
                onClick = {
                    expanded = false
                },
                leadingIcon = { Icon(Icons.Default.SortByAlpha, contentDescription = "Ordenar por titulo") },
            )
        }
    }
}