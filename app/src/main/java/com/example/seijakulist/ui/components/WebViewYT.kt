package com.example.seijakulist.ui.components

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(url: String, navController: NavController) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {ArrowBackTopAppBar(navController)},
                title = {
                    TitleScreen("Seijaku List")
                },
                colors = TopAppBarColors(
                    containerColor = Color(0xFF050505),
                    scrolledContainerColor = Color(0xFF050505),
                    navigationIconContentColor = Color(0xFF050505),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color(0xFF050505),
                )
            )
        },
        containerColor = Color(0xFF050505)
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                AndroidView(factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = WebViewClient()
                        loadUrl(url)
                    }
                })
            }
        }
    }
}
