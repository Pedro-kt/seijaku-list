package com.example.seijakulist.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seijakulist.R

@Composable
fun SubTitleWithoutIcon(subTitle: String) {
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold)
    )

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = subTitle,
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = RobotoBold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
    }
}