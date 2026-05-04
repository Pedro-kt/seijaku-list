package com.yumedev.seijakulist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.asp

@Composable
fun SubTitleWithoutIcon(subTitle: String) {

    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = subTitle,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 22.asp(),
            fontFamily = PoppinsBold,
            textAlign = TextAlign.Start,
        )
        TextButton(
            onClick = {}
        ) {
            Text(
                text = "Ver más",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.asp(),
                fontFamily = PoppinsRegular,
            )
        }
    }
}