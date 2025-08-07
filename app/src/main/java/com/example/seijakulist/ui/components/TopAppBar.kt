package com.example.seijakulist.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seijakulist.R
import com.example.seijakulist.ui.navigation.AppDestinations

@Composable
fun TopHomeScreen(navController: NavController) {
    Column() {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TitleScreen("Explora Animes!")

            Box(
                modifier = Modifier
                    .padding(
                        bottom = 2.dp,
                        end = 16.dp)
                    .size(55.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate(
                            AppDestinations.MY_PROFILE_ROUTE
                        )
                    }
                    .background(color = Color.White)
                    .border(width = 2.dp, color = Color(0xFF673AB7), shape = CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_profile),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        HorizontalDivider()
    }

}