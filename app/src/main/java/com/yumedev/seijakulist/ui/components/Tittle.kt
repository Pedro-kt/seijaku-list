package com.yumedev.seijakulist.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.ui.theme.RobotoBold

@Composable
fun TitleScreen(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier,
        fontFamily = RobotoBold
    )
}