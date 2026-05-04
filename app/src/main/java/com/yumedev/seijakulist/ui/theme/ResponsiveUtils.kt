package com.yumedev.seijakulist.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val REFERENCE_WIDTH_DP = 420f
private const val SCALE_MIN = 0.75f
private const val SCALE_MAX = 1.0f

@Composable
private fun screenScale(): Float {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.toFloat()
    return (screenWidthDp / REFERENCE_WIDTH_DP).coerceIn(SCALE_MIN, SCALE_MAX)
}

@Composable
fun Int.adp(): Dp = (this * screenScale()).dp

@Composable
fun Float.adp(): Dp = (this * screenScale()).dp

@Composable
fun Double.adp(): Dp = (this * screenScale()).dp

@Composable
fun Int.asp(): TextUnit = (this * screenScale()).sp

@Composable
fun Float.asp(): TextUnit = (this * screenScale()).sp