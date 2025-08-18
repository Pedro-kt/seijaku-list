package com.example.seijakulist.ui.components.confirm_dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.seijakulist.util.UserAction

@Composable
fun ConfirmPlannedDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Sí, cambiar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Cambiar a Planeado") },
        text = { Text("Esto reiniciará los episodios vistos a 0. ¿Estás seguro?") }
    )
}
