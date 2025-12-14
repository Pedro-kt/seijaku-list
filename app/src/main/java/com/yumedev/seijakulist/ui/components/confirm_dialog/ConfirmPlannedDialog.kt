package com.yumedev.seijakulist.ui.components.confirm_dialog

import androidx.compose.runtime.Composable
import com.yumedev.seijakulist.ui.components.CustomDialog
import com.yumedev.seijakulist.ui.components.DialogType

@Composable
fun ConfirmPlannedDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    CustomDialog(
        onDismissRequest = onDismiss,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        title = "Cambiar a Planeado",
        message = "Esto reiniciará los episodios vistos a 0.\n\n¿Estás seguro?",
        confirmButtonText = "Sí, cambiar",
        dismissButtonText = "Cancelar",
        type = DialogType.WARNING
    )
}
