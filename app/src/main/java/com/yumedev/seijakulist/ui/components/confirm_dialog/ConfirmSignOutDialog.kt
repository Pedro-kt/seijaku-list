package com.yumedev.seijakulist.ui.components.confirm_dialog

import androidx.compose.runtime.Composable
import com.yumedev.seijakulist.ui.components.CustomDialog
import com.yumedev.seijakulist.ui.components.DialogType

@Composable
fun ConfirmSignOutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CustomDialog(
        onDismissRequest = onDismiss,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        title = "Cerrar sesión",
        message = "¿Estás seguro de que quieres cerrar sesión de tu cuenta?",
        confirmButtonText = "Sí",
        dismissButtonText = "No",
        type = DialogType.WARNING
    )
}