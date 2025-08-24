package com.example.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.seijakulist.R
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.ui.navigation.AppDestinations

@Composable
fun CardAnimesHome(animeList: List<Anime>, navController: NavController) {

    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold)
    )
    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )


    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(animeList) { anime ->
            Column(
                modifier = Modifier
                    .width(130.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box() {

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(anime.image)
                            .size(Size.ORIGINAL)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Imagen de portada",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(130.dp)
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                            }
                    )
                    Row(
                        modifier = Modifier
                            .padding(start = 5.dp, top = 5.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(color = Color.Black.copy(alpha = 0.6f))
                            .height(24.dp)
                            .wrapContentWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Puntuacion",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(start = 6.dp)
                                .size(12.dp)
                        )
                        Text(
                            text = String.format("%.1f", anime.score),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.wrapContentWidth().padding(end = 6.dp),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontFamily = RobotoBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = anime.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = RobotoRegular
                )
            }
        }
    }
}