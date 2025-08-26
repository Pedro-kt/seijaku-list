package com.example.seijakulist.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seijakulist.ui.theme.RobotoBold

@Composable
fun TitleWithPadding(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
        fontFamily = RobotoBold
    )
}