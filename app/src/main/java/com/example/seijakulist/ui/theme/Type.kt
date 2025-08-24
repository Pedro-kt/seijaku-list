package com.example.seijakulist.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.seijakulist.R

// Set of Material typography styles to start with
val RobotoRegular = FontFamily(
    Font(R.font.roboto_regular)
)

val RobotoBold = FontFamily(
    Font(R.font.roboto_bold)
)

val Typography = Typography(
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = RobotoRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = RobotoBold,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
)