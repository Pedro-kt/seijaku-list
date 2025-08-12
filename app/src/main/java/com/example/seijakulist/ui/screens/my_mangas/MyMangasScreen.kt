package com.example.seijakulist.ui.screens.my_mangas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seijakulist.R
import com.example.seijakulist.ui.components.ArrowBackTopAppBar
import com.example.seijakulist.ui.components.BottomNavItemScreen
import com.example.seijakulist.ui.components.FilterTopAppBar
import com.example.seijakulist.ui.components.FilterTopAppBarMangas

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

    Scaffold(
        containerColor = Color(0xFF121211),
        topBar = {
            Column(modifier = Modifier.background(color = Color(0xFF121211))) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .background(color = Color.Transparent)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArrowBackTopAppBar(navController)
                    Text(
                        text = "Mis mangas",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .weight(1f),
                        fontFamily = RobotoBold
                    )
                    FilterTopAppBarMangas(onSearchClick = {})
                }
                HorizontalDivider()
                /*
                if (savedMangas.isEmpty()) {
                    HorizontalDivider()
                }
                 */
            }
        },
        bottomBar = {
            BottomNavItemScreen(navController)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "En este momento no hay mangas disponibles, pero pronto lo estará!",
                fontFamily = RobotoRegular,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}
