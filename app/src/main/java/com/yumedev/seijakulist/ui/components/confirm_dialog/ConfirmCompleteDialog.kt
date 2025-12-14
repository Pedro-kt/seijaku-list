package com.yumedev.seijakulist.ui.components.confirm_dialog

import androidx.compose.runtime.Composable
import com.yumedev.seijakulist.ui.components.CustomDialog
import com.yumedev.seijakulist.ui.components.DialogType

@Composable
fun ConfirmCompleteDialog(
    animeTitle: String,
    watched: Int,
    total: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CustomDialog(
        onDismissRequest = onDismiss,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        title = "Marcar como completado",
        message = "Actualmente llevas $watched de $total episodios vistos.\n\n¿Quieres marcar \"$animeTitle\" como Completado?\n\nEsto ajustará los episodios vistos a $total.",
        confirmButtonText = "Sí, completar",
        dismissButtonText = "Cancelar",
        type = DialogType.SUCCESS
    )
}