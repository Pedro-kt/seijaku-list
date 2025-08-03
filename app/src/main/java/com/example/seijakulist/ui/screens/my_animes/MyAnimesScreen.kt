package com.example.seijakulist.ui.screens.my_animes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.seijakulist.R
import com.example.seijakulist.ui.components.ArrowBackTopAppBar
import com.example.seijakulist.ui.components.BottomNavItemScreen
import com.example.seijakulist.ui.components.DeleteMyAnime
import com.example.seijakulist.ui.components.FilterTopAppBar
import com.example.seijakulist.ui.navigation.AppDestinations

@Composable
fun MyAnimeListScreen(
    navController: NavController,
    viewModel: MyAnimeListViewModel = hiltViewModel()
) {
    val savedAnimes by viewModel.savedAnimes.collectAsState()

    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = Color(0xFF050505),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = Color(0xFF121212))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ArrowBackTopAppBar(navController)
                Text(
                    text = "Mis animes",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    fontFamily = RobotoBold
                )
                FilterTopAppBar()
            }
        },
        bottomBar = {
            BottomNavItemScreen(navController)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    actionContentColor = Color.Black,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(16.dp)
                )
        }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF050505))
                .padding(innerPadding),
        ) {
            if (savedAnimes.isEmpty()) {
                Text(
                    text = "No tienes animes agregados a tu lista",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    fontFamily = RobotoRegular,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
            } else {
                LazyColumn(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)) {
                    items(savedAnimes) { anime ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Row(modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(anime.imageUrl),
                                        contentDescription = anime.title,
                                        modifier = Modifier.height(100.dp).width(70.dp).clip(RoundedCornerShape(4.dp)),
                                        contentScale = ContentScale.Crop
                                    )

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text =  anime.title,
                                            fontFamily = RobotoBold,
                                            color = Color.White,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(end = 16.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Score: ${anime.score}",
                                            fontFamily = RobotoRegular,
                                            color = Color.White
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                DeleteMyAnime(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(16.dp),
                                    viewModel,
                                    anime.malId,
                                    snackbarHostState = snackbarHostState,
                                    scope = scope
                                )
                            }
                        }

                    }
                }
            }

        }
    }
}
