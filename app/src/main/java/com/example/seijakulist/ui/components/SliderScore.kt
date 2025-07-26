package com.example.seijakulist.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SliderSelectScore(score: MutableFloatState) {
    Slider(
        modifier = Modifier.padding(horizontal = 40.dp),
        value = score.value,
        onValueChange = { score.value = it },
        valueRange = 1f..10f,
        steps = 8,
    )
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(text = "%.0f".format(score.value))
    }
}