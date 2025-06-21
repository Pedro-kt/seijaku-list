package com.example.seijakulist.ui.screens.search

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun SearchScreen(

    viewModel: AnimeSearchViewModel = hiltViewModel()

){

    val searchQuery by viewModel.searchQuery.collectAsState()
    val animeList by viewModel.animeList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery,
            onValueChange = { newText -> viewModel.onSearchQueryChanged(newText) },
            label = {Text("Buscar anime...")},
        )

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = { viewModel.searchAnimes() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Buscar")
        }

        Spacer(Modifier.height(10.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.wrapContentSize())
        }

        errorMessage?.let { message ->
            Text(text = message, color = androidx.compose.ui.graphics.Color.Red)
        }
        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(animeList) { anime ->

                Text(text = anime.title)
                HorizontalDivider(Modifier.padding(top = 10.dp))

            }
        }|
    }

}

