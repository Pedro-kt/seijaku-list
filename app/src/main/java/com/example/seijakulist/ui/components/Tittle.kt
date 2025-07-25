package com.example.seijakulist.ui.components

import androidx.compose.foundation.layout.padding
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
import com.example.seijakulist.R

@Composable
fun TitleScreen(title: String) {
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold)
    )
        Text(
            text = title,
            color = Color.White,
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp),
            fontFamily = RobotoBold
        )
}