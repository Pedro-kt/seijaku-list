package com.example.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.seijakulist.domain.models.CharacterDetail
import com.example.seijakulist.ui.navigation.AppDestinations

@Composable
fun CharacterRandomCard(
    character: CharacterDetail,
    navController: NavController,
    onRefresh: () -> Unit
) {
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold)
    )
    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )

    val gradientColorsTopBar = listOf(
        Color(0xFF181818),
        Color(0xFF202020),
        Color(0xFF181818),
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColorsTopBar
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character.images)
                        .size(Size.ORIGINAL)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen de portada",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(140.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            navController.navigate("${AppDestinations.CHARACTER_DETAIL_ROUTE}/${character.characterId}")
                        }
                        .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
                )
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Top
                ) {
                    SubTitleWithoutIcon(character.nameCharacter)

                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .height(32.dp)
                            .wrapContentWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Kanji:",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(end = 8.dp),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = RobotoBold
                        )
                        Text(
                            text = character.nameKanjiCharacter,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(end = 16.dp),
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontFamily = RobotoRegular
                        )
                    }
                }
            }

            IconButton(
                onClick = onRefresh,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refrescar",
                    tint = Color.White,
                )
            }
        }
    }
}
