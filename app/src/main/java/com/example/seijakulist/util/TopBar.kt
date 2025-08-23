package com.example.seijakulist.util

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seijakulist.ui.components.ArrowBackTopAppBar
import com.example.seijakulist.ui.components.FilterTopAppBar

/*
@Composable
fun topBarMyAnimeScreen() {
    topBar = {
        AnimatedContent(
            targetState = collapsedSearch,
            label = "search bar animation",
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF121211))
                .statusBarsPadding()
        ) { isSearchExpanded ->
            if (isSearchExpanded) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Buscar...") },
                    singleLine = true,
                    shape = RoundedCornerShape(50.dp),
                    leadingIcon = {
                        IconButton(onClick = {
                            collapsedSearch = false
                            searchQuery = ""
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Cerrar búsqueda"
                            )
                        }
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpiar"
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.White.copy(alpha = 0.5f),
                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.3f),
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White.copy(alpha = 0.7f),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                    ),
                )
            } else {
                Column(modifier = Modifier.background(color = Color(0xFF121211))) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .background(color = Color(0xFF121211))
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ArrowBackTopAppBar(navController)
                        Text(
                            text = "Mis animes",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .weight(1f),
                            fontFamily = RobotoBold
                        )
                        FilterTopAppBar(
                            onSearchClick = {
                                collapsedSearch = !collapsedSearch
                            },
                            onSortClick = {
                                isSortedAscending = !isSortedAscending
                            },
                            savedAnimes = savedAnimes
                        )
                    }
                    if (savedAnimes.isEmpty()) {
                        HorizontalDivider()
                    }
                }
            }
        }

    },
}

 */