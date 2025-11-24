package com.yumedev.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.ui.components.confirm_dialog.ConfirmCompleteDialog
import com.yumedev.seijakulist.ui.components.confirm_dialog.ConfirmPlannedDialog
import com.yumedev.seijakulist.ui.components.confirm_dialog.ConfirmResetEpisodesDialog
import com.yumedev.seijakulist.util.UserAction

@Composable
fun AnimeStatusChip(
    status: String,
    statusColor: Color,
    episodesWatched: Int,
    totalEpisodes: Int,
    animeTitle: String,
    onStatusSelected: (UserAction) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showPlannedDialog by remember { mutableStateOf(false) }
    var showCompleteDialog by remember { mutableStateOf(false) }
    var showConfirmCompleteDialog by remember { mutableStateOf(false) }
    var pendingAction: UserAction? by remember { mutableStateOf(null) }

    val RobotoRegular = FontFamily(Font(R.font.roboto_regular))
    val statusAnime = listOf("Viendo", "Completado", "Pendiente", "Abandonado", "Planeado")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { expanded = true }
    ) {
        ElevatedFilterChip(
            label = { Text(text = status) },
            selected = false,
            onClick = { expanded = true },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = statusColor,
                labelColor = Color.White
            ),
            elevation = FilterChipDefaults.filterChipElevation(
                elevation = 16.dp
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.DarkGray,
        ) {
            statusAnime.forEach { newStatus ->
                DropdownMenuItem(
                    text = { Text(newStatus, fontFamily = RobotoRegular, color = Color.White) },
                    onClick = {
                        expanded = false
                        if (status == "Completado" && newStatus == "Completado") return@DropdownMenuItem

                        val action = when (newStatus) {
                            "Viendo" -> UserAction.MarkAsWatching
                            "Completado" -> UserAction.MarkAsCompleted
                            "Abandonado" -> UserAction.MarkAsDropped
                            "Planeado" -> UserAction.MarkAsPlanned
                            "Pendiente" -> UserAction.MarkAsPending
                            else -> UserAction.None
                        }

                        when (newStatus) {
                            "Planeado" -> {
                                if (episodesWatched > 0) {
                                    pendingAction = action
                                    showPlannedDialog = true
                                } else onStatusSelected(action)
                            }
                            "Completado" -> {
                                if (episodesWatched >= 0 && episodesWatched < totalEpisodes || episodesWatched == totalEpisodes) {
                                    pendingAction = action
                                    showConfirmCompleteDialog = true
                                } else {
                                    onStatusSelected(action)
                                }
                            }
                            else -> {
                                if (episodesWatched == totalEpisodes && newStatus != "Completado") {
                                    pendingAction = action
                                    showCompleteDialog = true
                                } else onStatusSelected(action)
                            }
                        }
                    }
                )
            }
        }
    }

    if (showPlannedDialog && pendingAction != null) {
        ConfirmPlannedDialog(
            onConfirm = {
                onStatusSelected(pendingAction!!)
                showPlannedDialog = false
                pendingAction = null
            },
            onDismiss = {
                showPlannedDialog = false
                pendingAction = null
            }
        )
    }

    if (showCompleteDialog && pendingAction != null) {
        ConfirmResetEpisodesDialog(
            onConfirm = {
                onStatusSelected(pendingAction!!)
                showCompleteDialog = false
                pendingAction = null
            },
            onDismiss = {
                showCompleteDialog = false
                pendingAction = null
            }
        )
    }

    if (showConfirmCompleteDialog && pendingAction != null) {
        ConfirmCompleteDialog(
            animeTitle = animeTitle,
            watched = episodesWatched,
            total = totalEpisodes,
            onConfirm = {
                onStatusSelected(pendingAction!!)
                showConfirmCompleteDialog = false
                pendingAction = null
            },
            onDismiss = {
                showConfirmCompleteDialog = false
                pendingAction = null
            }
        )
    }
}

