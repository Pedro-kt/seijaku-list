package com.yumedev.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.models.CharacterDetail
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.RobotoBold
import com.yumedev.seijakulist.ui.theme.RobotoRegular

@Composable
fun CharacterRandomCard(
    character: CharacterDetail,
    navController: NavController,
    onRefresh: () -> Unit
) {

    var isLiked by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                )
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    navController.navigate("${AppDestinations.CHARACTER_DETAIL_ROUTE}/${character.characterId}")
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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
                        .height(210.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                )
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = character.nameCharacter,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, end = 40.dp)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontFamily = RobotoBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
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
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp,
                            fontFamily = RobotoRegular
                        )
                        Text(
                            text = character.nameKanjiCharacter,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(end = 16.dp),
                            color = MaterialTheme.colorScheme.onSurface,
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
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
            IconButton(
                onClick = { isLiked = !isLiked },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Añadir a favoritos",
                    tint = if (isLiked) Color.Red else MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
