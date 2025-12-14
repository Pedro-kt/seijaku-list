package com.yumedev.seijakulist.ui.components.confirm_dialog

import androidx.compose.runtime.Composable
import com.yumedev.seijakulist.ui.components.CustomDialog
import com.yumedev.seijakulist.ui.components.DialogType

@Composable
fun ConfirmResetEpisodesDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CustomDialog(
        onDismissRequest = onDismiss,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        title = "Reiniciar progreso",
        message = "Al cambiar de 'Completado' a otro estado, los episodios vistos se reiniciarán a 0.\n\n¿Deseas continuar?",
        confirmButtonText = "Sí",
        dismissButtonText = "No",
        type = DialogType.WARNING
    )
}
