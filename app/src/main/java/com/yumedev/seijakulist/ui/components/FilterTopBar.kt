package com.yumedev.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Dehaze
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.data.local.entities.AnimeEntity

@Composable
fun FilterTopAppBar(
    onSearchClick: () -> Unit,
    onViewModeChange: () -> Unit = {},
    viewMode: ViewMode = ViewMode.LIST,
    onSortClick: () -> Unit = {},
    sortOrder: com.yumedev.seijakulist.ui.components.SortOrder = com.yumedev.seijakulist.ui.components.SortOrder.NONE
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        // Botón mejorado con superficie
        Surface(
            onClick = { expanded = true },
            shape = RoundedCornerShape(12.dp),
            color = if (expanded) MaterialTheme.colorScheme.primaryContainer
                   else MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Dehaze,
                    contentDescription = "Opciones",
                    tint = if (expanded) MaterialTheme.colorScheme.onPrimaryContainer
                           else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Menú desplegable mejorado
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = (-8).dp, y = 8.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(240.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            // Header del menú
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Opciones",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Personaliza tu lista",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Opción: Buscar título
            DropdownMenuItem(
                text = {
                    Column {
                        Text(
                            text = "Buscar título",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Encuentra animes rápidamente",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                },
                onClick = {
                    onSearchClick()
                    expanded = false
                },
                leadingIcon = {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                modifier = Modifier.padding(horizontal = 8.dp),
                colors = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Opción: Cambiar vista
            DropdownMenuItem(
                text = {
                    Column {
                        Text(
                            text = when (viewMode) {
                                ViewMode.LIST -> "Vista Grid"
                                ViewMode.GRID -> "Vista Card"
                                ViewMode.CARD -> "Vista Lista"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = when (viewMode) {
                                ViewMode.LIST -> "Cambiar a vista cuadrícula (3 columnas)"
                                ViewMode.GRID -> "Cambiar a vista card (2 columnas)"
                                ViewMode.CARD -> "Cambiar a vista lista detallada"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                },
                onClick = {
                    onViewModeChange()
                    expanded = false
                },
                leadingIcon = {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = when (viewMode) {
                                    ViewMode.LIST -> Icons.Default.GridView
                                    ViewMode.GRID -> Icons.Default.ViewModule
                                    ViewMode.CARD -> Icons.Default.ViewAgenda
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                modifier = Modifier.padding(horizontal = 8.dp),
                colors = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Opción: Ordenar
            DropdownMenuItem(
                text = {
                    Column {
                        Text(
                            text = when (sortOrder) {
                                com.yumedev.seijakulist.ui.components.SortOrder.NONE -> "Ordenar A-Z"
                                com.yumedev.seijakulist.ui.components.SortOrder.A_TO_Z -> "Ordenar Z-A"
                                com.yumedev.seijakulist.ui.components.SortOrder.Z_TO_A -> "Sin ordenar"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = when (sortOrder) {
                                com.yumedev.seijakulist.ui.components.SortOrder.NONE -> "Ordena alfabéticamente A-Z"
                                com.yumedev.seijakulist.ui.components.SortOrder.A_TO_Z -> "Ordena alfabéticamente Z-A"
                                com.yumedev.seijakulist.ui.components.SortOrder.Z_TO_A -> "Volver al orden original"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                },
                onClick = {
                    onSortClick()
                    expanded = false
                },
                leadingIcon = {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (sortOrder != com.yumedev.seijakulist.ui.components.SortOrder.NONE)
                                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.8f)
                                else MaterialTheme.colorScheme.tertiaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.SortByAlpha,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                modifier = Modifier.padding(horizontal = 8.dp),
                colors = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}