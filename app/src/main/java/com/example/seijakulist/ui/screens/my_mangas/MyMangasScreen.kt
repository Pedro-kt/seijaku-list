package com.example.seijakulist.ui.screens.my_mangas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seijakulist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMangasScreen(
    navController: NavController
) {

    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "En este momento no hay mangas disponibles, pero pronto lo estará!",
            fontFamily = RobotoRegular,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
