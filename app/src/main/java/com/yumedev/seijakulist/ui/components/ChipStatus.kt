package com.yumedev.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.MenuDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.ui.components.confirm_dialog.SimpleStatusChangeDialog
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.util.UserAction
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

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
    var showStatusChangeDialog by remember { mutableStateOf(false) }
    var pendingStatus by remember { mutableStateOf("") }
    var pendingAction: UserAction? by remember { mutableStateOf(null) }

    val statusAnime = listOf("Viendo", "Completado", "Pendiente", "Abandonado", "Planeado")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { expanded = true }
    ) {
        Surface(
            onClick = { expanded = true },
            shape = RoundedCornerShape(20.dp),
            color = statusColor.copy(alpha = 0.18f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(statusColor, CircleShape)
                )
                Text(
                    text = status,
                    color = Color.White,
                    fontFamily = PoppinsBold,
                    fontSize = 11.asp()
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 4.dp)) {
                Text(
                    text = "Cambiar estado",
                    fontFamily = PoppinsBold,
                    fontSize = 11.asp(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            statusAnime.forEach { newStatus ->
                val isCurrentStatus = status == newStatus
                val statusIcon = when (newStatus) {
                    "Viendo" -> Icons.Default.PlayArrow
                    "Completado" -> Icons.Default.CheckCircle
                    "Pendiente" -> Icons.Default.Pause
                    "Abandonado" -> Icons.Default.Stop
                    "Planeado" -> Icons.Default.EventAvailable
                    else -> null
                }
                val itemColor = when (newStatus) {
                    "Viendo" -> Color(0xFF66BB6A)
                    "Completado" -> Color(0xFF42A5F5)
                    "Pendiente" -> Color(0xFFFFCA28)
                    "Abandonado" -> Color(0xFFEF5350)
                    "Planeado" -> Color(0xFF78909C)
                    else -> MaterialTheme.colorScheme.onSurface
                }

                DropdownMenuItem(
                    text = {
                        Text(
                            text = newStatus,
                            fontFamily = PoppinsBold,
                            fontSize = 13.asp(),
                            color = itemColor
                        )
                    },
                    onClick = {
                        expanded = false
                        // No hacer nada si es el mismo estado
                        if (status == newStatus) return@DropdownMenuItem

                        val action = when (newStatus) {
                            "Viendo" -> UserAction.MarkAsWatching
                            "Completado" -> UserAction.MarkAsCompleted
                            "Abandonado" -> UserAction.MarkAsDropped
                            "Planeado" -> UserAction.MarkAsPlanned
                            "Pendiente" -> UserAction.MarkAsPending
                            else -> UserAction.None
                        }

                        // Mostrar diálogo de confirmación simple para cualquier cambio
                        pendingAction = action
                        pendingStatus = newStatus
                        showStatusChangeDialog = true
                    },
                    leadingIcon = {
                        if (statusIcon != null) {
                            Icon(
                                imageVector = statusIcon,
                                contentDescription = newStatus,
                                tint = itemColor,
                                modifier = Modifier.size(20.adp())
                            )
                        }
                    },
                    trailingIcon = {
                        if (isCurrentStatus) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = itemColor,
                                modifier = Modifier.size(16.adp())
                            )
                        }
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = itemColor,
                        leadingIconColor = itemColor,
                        trailingIconColor = itemColor
                    ),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isCurrentStatus) itemColor.copy(alpha = 0.12f)
                            else Color.Transparent
                        )
                )
            }
        }
    }

    // Diálogo simple genérico para cualquier cambio de estado
    if (showStatusChangeDialog && pendingAction != null) {
        SimpleStatusChangeDialog(
            newStatus = pendingStatus,
            onConfirm = {
                onStatusSelected(pendingAction!!)
                showStatusChangeDialog = false
                pendingAction = null
                pendingStatus = ""
            },
            onDismiss = {
                showStatusChangeDialog = false
                pendingAction = null
                pendingStatus = ""
            }
        )
    }
}

