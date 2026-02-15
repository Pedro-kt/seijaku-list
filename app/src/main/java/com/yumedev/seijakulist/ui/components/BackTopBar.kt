package com.yumedev.seijakulist.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yumedev.seijakulist.R

@Composable
fun ArrowBackTopAppBar(
    navController: NavController,
    tint: Color? = null
) {
    IconButton(
        onClick = { navController.popBackStack() },
        modifier = Modifier.size(48.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_left_line),
            contentDescription = "Volver atras",
            tint = tint ?: MaterialTheme.colorScheme.onSurface
        )
    }
}