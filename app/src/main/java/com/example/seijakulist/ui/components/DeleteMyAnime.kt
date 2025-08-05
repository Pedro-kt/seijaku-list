package com.example.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.seijakulist.ui.screens.my_animes.MyAnimeListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DeleteMyAnime(
    modifier: Modifier = Modifier,
    viewModel: MyAnimeListViewModel,
    animeId: Int,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    var click by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { click = true },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.White
            )

            DropdownMenu(
                expanded = click,
                onDismissRequest = { click = false },
                modifier = Modifier.background(Color.Black)
            ) {
                DropdownMenuItem(
                    text = { Text("Eliminar anime") },
                    onClick = {
                        viewModel.deleteAnimeToList(animeId)
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "Anime eliminado de tu lista",
                                actionLabel = "Deshacer",
                                duration = SnackbarDuration.Long
                            )
                            //volver a agregar el anime si se deshace
                            if (result == SnackbarResult.ActionPerformed) {

                            }
                        }
                        click = false
                    },
                    leadingIcon = { Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red.copy(alpha = 0.8f)) }
                )
            }
        }
    }
}
