package com.example.seijakulist.ui.components.confirm_dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmResetEpisodesDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reiniciar progreso") },
        text = { Text("Al cambiar de 'Completado' a otro estado, los episodios vistos se reiniciarán a 0. ¿Deseas continuar?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Sí")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}
